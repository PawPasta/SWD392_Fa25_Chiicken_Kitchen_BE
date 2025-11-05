package com.ChickenKitchen.app.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


@Configuration
class MomoConfig(

    @param:Value("\${momo.partnerCode}")
    val partnerCode: String,

    @param:Value("\${momo.accessKey}")
    val accessKey: String,

    @param:Value("\${momo.secretKey}")
    val secretKey: String,

    @param:Value("\${momo.endpoint}")
    val endpoint: String,

    @param:Value("\${momo.ipnUrl}")
    val ipnUrl: String,

    @param:Value("\${momo.redirectUrl}")
    val redirectUrl: String,

    @param:Value("\${momo.redirectAppUrl}")
    val appRedirectUrl: String,

    @param:Value("\${momo.lang:vi}")
    val lang: String

) {

    companion object {
        fun hmacSHA256(secretKey: String, data: String): String {
            val algorithm = "HmacSHA256"
            val mac = Mac.getInstance(algorithm)
            val secretKeySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), algorithm)
            mac.init(secretKeySpec)
            val rawHmac = mac.doFinal(data.toByteArray(Charsets.UTF_8))
            return rawHmac.joinToString("") { "%02x".format(it) }
        }
    }

    fun sign(data: String): String = hmacSHA256(secretKey, data)
}