package com.example.redditoauthsample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_auth.*
import okhttp3.Credentials

class AuthFragment : Fragment() {

    companion object {
        const val KEY_AUTH_ERROR = "AUTH_CODE_ERROR"
        const val KEY_AUTH_SUCCESS = "AUTH_CODE_SUCCESS"
        const val REQUEST_TARGET = 1122
    }

    private val compositeDisposable = CompositeDisposable()
    private val redditTokenApi = RedditService.getRedditTokenApi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_auth.setOnClickListener {
            val authDialog = AuthDialogFragment()
            authDialog.setTargetFragment(this, REQUEST_TARGET)
            authDialog.show(activity!!.supportFragmentManager, "AuthDialog")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Check request code
        if (resultCode == Activity.RESULT_OK) {
            val authCode = data!!.getStringExtra(KEY_AUTH_SUCCESS)
            getAccessToken(authCode)
        } else {
            val intentData = data!!.getStringExtra(KEY_AUTH_ERROR)
            Toast.makeText(activity!!, "Error authCode", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    private fun getAccessToken(authCode: String) {
        redditTokenApi.getAccessToken(getEncodedAuthString(), "authorization_code", authCode, BuildConfig.REDDIT_REDIRECT_URI)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Toast.makeText(activity!!, "Token received: \n${it.accessToken}", Toast.LENGTH_LONG).show()
                        },
                        { Toast.makeText(activity!!, "Error token", Toast.LENGTH_LONG).show() }
                ).addTo(compositeDisposable)
    }

    private fun getEncodedAuthString(): String {
        return Credentials.basic(BuildConfig.REDDIT_CLIENT_ID, "")
    }
}