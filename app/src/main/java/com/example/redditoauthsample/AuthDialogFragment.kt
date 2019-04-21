package com.example.redditoauthsample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_fragment_auth.*
import okhttp3.Credentials


class AuthDialogFragment : DialogFragment() {

    companion object {
        private const val OAUTH_SCOPE = "read"
        private val OAUTH_FULL_URL = "${BuildConfig.REDDIT_BASE_ENDPOINT}" +
                "${BuildConfig.REDDIT_AUTH_URL}?client_id=${BuildConfig.REDDIT_CLIENT_ID}&response_type=code&state=TEST&" +
                "redirect_uri=${BuildConfig.REDDIT_REDIRECT_URI}&scope=$OAUTH_SCOPE"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_fragment_auth, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        web_view_auth.loadUrl(OAUTH_FULL_URL)
        web_view_auth.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                val uri = Uri.parse(url)
                val error = uri.getQueryParameter("error")
                val authCode = uri.getQueryParameter("code")
                val state = uri.getQueryParameter("state")

                val resultIntent = Intent()
                if (error != null) {
                    resultIntent.putExtra(AuthFragment.KEY_AUTH_ERROR, error)
                    targetFragment!!.onActivityResult(AuthFragment.REQUEST_TARGET, Activity.RESULT_CANCELED, resultIntent)
                    dismiss()
                } else if (authCode != null && state != null) {
                    if (state.equals("TEST")) {
                        resultIntent.putExtra(AuthFragment.KEY_AUTH_SUCCESS, authCode)
                        targetFragment!!.onActivityResult(AuthFragment.REQUEST_TARGET, Activity.RESULT_OK, resultIntent)
                        dismiss()
                    }
                }

            }
        }
    }
}