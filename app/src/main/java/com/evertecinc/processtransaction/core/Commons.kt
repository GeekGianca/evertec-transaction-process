package com.evertecinc.processtransaction.core

import android.os.Build
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Commons {
    val formatDecimal = DecimalFormat("###,###.00")
    fun getRandom(): String {
        return BigInteger(130, SecureRandom()).toString()
    }

    fun getCurrentDateFormat(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.getDefault()).format(Date())
    }

    fun getRefFormat(): String {
        return SimpleDateFormat("yyyyMMdd'_'HHmm", Locale.getDefault()).format(Date())
    }

    fun getBase64(input: ByteArray): String {
        val encodedBytes: ByteArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encode(input)
        } else {
            android.util.Base64.encode(input, android.util.Base64.NO_WRAP)
        }
        return String(encodedBytes)
    }

    fun getSHA256(input: String): ByteArray {
        val mDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
        return mDigest.digest(input.toByteArray())
    }
}