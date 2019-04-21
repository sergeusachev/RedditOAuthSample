package com.example.redditoauthsample

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class RedditWebViewClient(
    val processResponse: (String?, String?, String?) -> Unit
) : WebViewClient() {


}