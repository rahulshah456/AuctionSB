package com.droid2developers.auction.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.droid2developers.auction.R
import com.droid2developers.auction.models.AuctionAccount
import com.droid2developers.auction.models.PhoneRegistrations
import com.droid2developers.auction.utils.Constants.ACCOUNT_TYPES
import com.droid2developers.auction.utils.Constants.DATABASE_LOCATION_ACCOUNTS
import com.droid2developers.auction.utils.Constants.DATABASE_REGISTRATIONS
import com.droid2developers.auction.utils.Constants.EXTRA_ACCOUNT_DATA
import com.droid2developers.auction.utils.Constants.EXTRA_ACCOUNT_TYPE
import com.droid2developers.auction.utils.Constants.EXTRA_LOGIN_PROGRESS
import com.droid2developers.auction.utils.Constants.INDIAN_PHONE_CODE
import com.droid2developers.auction.utils.PrefixEditText
import com.droid2developers.auction.utils.SmartPreferences
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class PhoneActivity : AppCompatActivity() {

    private var codeEditText: TextInputEditText? = null
    private var phoneEditText: PrefixEditText? = null

    private var contButton: MaterialCardView? = null
    private var verificationContent: RelativeLayout? = null
    private var mainContent: RelativeLayout? = null
    private var resendCode: TextView? = null
    private var codeCountdown: TextView? = null
    private var closeButton: ImageView? = null

    private var database: FirebaseFirestore? = null
    private var mAuth: FirebaseAuth? = null

    private var mRegistrations: PhoneRegistrations? = null
    private var phoneNumberData: String? = null
    private var mNumber: String? = null

    private var mVerificationId: String? = null
    private var mResendToken: ForceResendingToken? = null
    private var preferences: SmartPreferences? = null

    private var isVerified: Boolean = false
    private var accountType = -1
    private var mVerificationInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

        phoneEditText = findViewById(R.id.phoneEditTextID)
        codeEditText = findViewById(R.id.confirmEditTextID)
        contButton = findViewById(R.id.mcv_continue)
        closeButton = findViewById(R.id.closeId)
        verificationContent = findViewById(R.id.confirmLayoutId)
        mainContent = findViewById(R.id.mainLayoutId)
        resendCode = findViewById(R.id.resendTextID)
        codeCountdown = findViewById(R.id.countdownTextID)

        database = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        preferences = SmartPreferences.getInstance(applicationContext)

        closeButton?.setOnClickListener { onBackPressed() }
        contButton?.setOnClickListener { launchLogin() }

        phoneEditText?.addTextChangedListener(onTextChanged = {
                text: CharSequence?, _: Int, _: Int, _: Int ->  Logger.d(text)
        })

    }


    private fun launchLogin() {

        phoneNumberData = INDIAN_PHONE_CODE + phoneEditText?.text.toString().trim()

        if (!isVerified) {
            if (!TextUtils.isEmpty(phoneEditText?.text.toString().trim())) {
                searchPhoneRegistration()
                isVerified = true
                mainContent?.visibility = View.GONE
                verificationContent?.visibility = View.VISIBLE
                contButton?.visibility = View.VISIBLE
                phoneEditText?.isFocusable = false
                phoneEditText?.isEnabled = false


                //verification code sending
                startPhoneNumberVerification(phoneNumberData, mResendToken)
                startCountDown()
            } else {
                phoneEditText?.error = "Phone number can't be empty!"
            }
        } else {
            if (!TextUtils.isEmpty(codeEditText?.text)) {
                val code = codeEditText?.text.toString().trim { it <= ' ' }
                verifyPhoneNumberWithCode(mVerificationId, code)
            } else {
                Toast.makeText(
                    this@PhoneActivity,
                    "Please provide correct Verification Code!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String?, token: ForceResendingToken?) {
        Logger.d("phoneNumber: $phoneNumber")
        phoneNumber?.let {
            Logger.d("phoneNumber: $phoneNumber")
            val options = PhoneAuthOptions.newBuilder(mAuth!!)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
            if (token != null) options.setForceResendingToken(token)
            PhoneAuthProvider.verifyPhoneNumber(options.build())
            mVerificationInProgress = true
        }
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String?) {
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId!!, code!!)
            signInWithPhoneAuthCredential(credential)
        }catch (e: Exception){
            Toast.makeText(this, "Verification Code is wrong!", Toast.LENGTH_SHORT).show()
            Logger.d("Exception $e")
        }
    }

    private fun searchPhoneRegistration(): Single<Boolean> {
        return Single.create {

            database?.collection(DATABASE_REGISTRATIONS)?.get()
                ?.addOnSuccessListener { result ->
                    var isRegistered = false
                    if (result != null && result.isEmpty) {
                        for (document in result) {
                            Logger.d("${document.id} => ${document.data}")
                            mRegistrations = document?.toObject(PhoneRegistrations::class.java)
                            if (mRegistrations != null) {
                                if (phoneNumberData == mRegistrations?.number
                                    && ACCOUNT_TYPES.contains(mRegistrations?.accountType)){
                                    accountType = mRegistrations?.accountType!!
                                    isRegistered = true
                                    break
                                }
                            }
                        }
                    }
                    it.onSuccess(isRegistered)
                }
                ?.addOnFailureListener { exception ->
                    Logger.e(exception, "Exception")
                    it.onError(exception)
                }
        }
    }

    private fun startCountDown() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Logger.d(millisUntilFinished)
                if (millisUntilFinished / 1000 >= 10) {
                    codeCountdown?.text = String.format(Locale.ENGLISH, "00:%d",
                        millisUntilFinished / 1000)
                } else {
                    codeCountdown?.text = String.format(Locale.ENGLISH, "00:0%d" ,
                        millisUntilFinished / 1000)
                }
            }

            override fun onFinish() {
                Logger.d("CountDown Finished")
                codeCountdown?.text = getString(R.string.init_time)
                resendCode?.setTextColor(ContextCompat.getColor(applicationContext,
                    R.color.colorPrimary))
            }
        }.start()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        //TODO - progress show
        mAuth?.let {
            it.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Logger.d("signInWithCredential:success" + task.result)
                        val user = task.result?.user
                        mNumber = user?.phoneNumber.toString()
                        checkRegistration(mNumber)
                    } else {
                        //TODO progress hide
                        Toast.makeText(
                            this@PhoneActivity,
                            "Verification Code is wrong!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Logger.w("signInWithCredential:failure", task.exception)
                    }
                }
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Logger.d("onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Logger.w("onVerificationFailed", e)
            if (e is FirebaseAuthInvalidCredentialsException) {
                Logger.w("Invalid credentials request")
            } else if (e is FirebaseTooManyRequestsException) {
                Logger.w("The SMS quota for the project has been exceeded")
            }
        }

        override fun onCodeSent(verificationId: String, token: ForceResendingToken) {
            Logger.w("onCodeSent:$verificationId")
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId
            mResendToken = token
        }
    }



    private fun checkRegistration(number: String?) {
        searchPhoneRegistration()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { isRegistered: Boolean? ->
                    run {
                        if (checkNotNull(isRegistered)) {
                            getRegistrationDetails(number)
                        } else {
                            sendToRegistration(number)
                        }
                    }
                },
                { throwable: Throwable ->
                    run {
                        Logger.e(throwable, "Check Registration Exception")
                        sendToRegistration(number)
                    }
                }
            )
    }

    private fun sendToRegistration(number: String?) {
        //TODO - progress hide
        preferences?.saveValue(EXTRA_LOGIN_PROGRESS, 0)
        val intent = Intent(this@PhoneActivity, CompleteActivity::class.java)
        intent.putExtra("phone", number)
        startActivity(intent)
        finish()
    }

    private fun getRegistrationDetails(number: String?) {

        database?.collection(DATABASE_LOCATION_ACCOUNTS)?.document(mAuth?.uid!!)?.get()
            ?.addOnSuccessListener { document->
                val userData = document?.toObject(AuctionAccount::class.java)
                if (userData != null) {
                    //TODO - close progress
                    val gson = Gson()
                    val accountJson: String = gson.toJson(userData)
                    preferences?.saveValue(EXTRA_ACCOUNT_DATA, accountJson)
                    preferences?.saveValue(EXTRA_ACCOUNT_TYPE, userData.accountType)
                    preferences?.saveValue(EXTRA_LOGIN_PROGRESS, 1)
                    sendToMainActivity(userData.accountType)
                } else {
                    sendToRegistration(number!!)
                }
            }
            ?.addOnFailureListener { exception->
                //TODO - close progress
                Logger.e(exception, "Exception")
            }
    }

    private fun sendToMainActivity(accountType: Int?) {
        val intent = Intent(this@PhoneActivity, MainActivity::class.java)
        intent.putExtra(EXTRA_ACCOUNT_TYPE, accountType!!)
        startActivity(intent)
        finish()
    }

}