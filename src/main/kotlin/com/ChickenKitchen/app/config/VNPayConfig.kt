package com.ChickenKitchen.app.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

@Configuration
class VNPayConfig(
    @param:Value("\${vnpay.pay-url}") val vnpPayUrl: String,
    @param:Value("\${vnpay.return-url}") val vnpReturnUrl: String,
    @param:Value("\${vnpay.app-return-url}") val vnpAppReturnUrl: String,
    @param:Value("\${vnpay.tmn-code}") val vnpTmnCode: String,
    @param:Value("\${vnpay.hash-secret}") val vnpHashSecret: String,
    @param:Value("\${vnpay.api-url}") val vnpApiUrl: String,
    @param:Value("\${vnpay.version}") val vnpVersion: String,
    @param:Value("\${vnpay.command}") val vnpCommand: String,
    @param:Value("\${vnpay.order-type}") val vnpOrderType: String,
    @param:Value("\${vnpay.IpAddr}") val vnpIpAddr: String

) {
    companion object {
        fun md5(message: String): String {
            return try {
                val md = MessageDigest.getInstance("MD5")
                val hash = md.digest(message.toByteArray(Charsets.UTF_8))
                hash.joinToString("") { String.format("%02x", it.toInt() and 0xff) }
            } catch (e: Exception) {
                ""
            }
        }

        fun sha256(message: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-256")
                val hash = md.digest(message.toByteArray(Charsets.UTF_8))
                hash.joinToString("") { String.format("%02x", it.toInt() and 0xff) }
            } catch (e: Exception) {
                ""
            }
        }

        fun hmacSHA512(key: String, data: String): String {
            return try {
                require(key.isNotBlank() && data.isNotBlank()) { throw NullPointerException() }
                val hmac512 = Mac.getInstance("HmacSHA512")
                val hmacKeyBytes = key.toByteArray()
                val secretKey = SecretKeySpec(hmacKeyBytes, "HmacSHA512")
                hmac512.init(secretKey)
                val dataBytes = data.toByteArray(StandardCharsets.UTF_8)
                val result = hmac512.doFinal(dataBytes)
                result.joinToString("") { String.format("%02x", it.toInt() and 0xff) }
            } catch (e: Exception) {
                ""
            }
        }

        fun getIpAddress(request: HttpServletRequest): String {
            return try {
                request.getHeader("X-FORWARDED-FOR") ?: request.localAddr
            } catch (e: Exception) {
                "Invalid IP: ${e.message}"
            }
        }

        fun getRandomNumber(len: Int): String {
            val chars = "0123456789"
            return buildString {
                repeat(len) {
                    append(chars[Random.nextInt(chars.length)])
                }
            }
        }
    }

    fun hashAllFields(fields: Map<String, String?>): String {
        val fieldNames = fields.keys.sorted()
        val sb = StringBuilder()
        fieldNames.forEachIndexed { index, fieldName ->
            val fieldValue = fields[fieldName]
            if (!fieldValue.isNullOrEmpty()) {
                sb.append(fieldName)
                sb.append("=")
                sb.append(fieldValue)
            }
            if (index < fieldNames.size - 1) {
                sb.append("&")
            }
        }
        return hmacSHA512(vnpHashSecret, sb.toString())
    }
}
