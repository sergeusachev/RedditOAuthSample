package com.example.redditoauthsample

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(

    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("expires_in")
    val expiresInUnix: Long,

    @SerializedName("scope")
    val scope: String,

    @SerializedName("refresh_token")
    val refreshToken: String
)