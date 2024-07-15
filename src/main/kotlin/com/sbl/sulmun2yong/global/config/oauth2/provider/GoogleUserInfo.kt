package com.sbl.sulmun2yong.global.config.oauth2.provider

class GoogleUserInfo(
    private val attributes: Map<String, Any>,
) : OAuth2UserInfo {
    override fun getProvider(): String = "google"

    override fun getProviderId(): String = attributes["sub"] as String

    override fun getNickname(): String = attributes["name"] as String
}
