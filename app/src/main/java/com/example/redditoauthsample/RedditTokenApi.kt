package com.example.redditoauthsample

import io.reactivex.Single
import retrofit2.http.*

interface RedditTokenApi {

    @FormUrlEncoded
    @POST("access_token")
    fun getAccessToken(
        @Header("Authorization") authString: String,
        @Field("grant_type") grantType: String,
        @Field("code") authCode: String,
        @Field("redirect_uri") redirectUri: String
    ): Single<AccessTokenResponse>
}