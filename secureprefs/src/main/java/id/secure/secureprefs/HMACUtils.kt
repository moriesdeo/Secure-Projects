package id.secure.secureprefs

import android.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HMACUtils {

    private const val HMAC_ALGORITHM = "HmacSHA256"
    private const val SECRET_KEY = "your-secret-key-here" // Replace with a securely generated key

    // Method to generate HMAC for the given data
    fun generateHMAC(data: String): String {
        val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(), HMAC_ALGORITHM)
        val mac = Mac.getInstance(HMAC_ALGORITHM)
        mac.init(secretKeySpec)

        val hmacBytes = mac.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(hmacBytes, Base64.DEFAULT)
    }

    // Method to verify the HMAC of the given data
    fun verifyHMAC(data: String, hmac: String): Boolean {
        val generatedHMAC = generateHMAC(data)
        return generatedHMAC == hmac
    }
}
