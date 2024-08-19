package id.secure.secureprefs

import android.content.Context
import android.content.SharedPreferences

class SecurePreferences(context: Context, algorithm: String = "AES") {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)

    private val encryptionStrategy: EncryptionStrategy = EncryptionFactory.getEncryptionStrategy(algorithm)

    // Save data securely
    fun saveSecureData(key: String, value: String) {
        // Encrypt the value using the selected encryption strategy
        val encryptedValue = encryptionStrategy.encrypt(value)

        // Generate HMAC for the encrypted value
        val hmac = HMACUtils.generateHMAC(encryptedValue)

        // Save the encrypted value and its HMAC in SharedPreferences
        sharedPreferences.edit().putString(key, encryptedValue).putString("$key:hmac", hmac).apply()
    }

    // Retrieve data securely
    fun getSecureData(key: String): String? {
        // Retrieve the encrypted value and its HMAC from SharedPreferences
        val encryptedValue = sharedPreferences.getString(key, null) ?: return null
        val storedHMAC = sharedPreferences.getString("$key:hmac", null)

        // Verify the HMAC to ensure the data hasn't been tampered with
        if (storedHMAC != null && HMACUtils.verifyHMAC(encryptedValue, storedHMAC)) {
            // Decrypt the value if HMAC verification is successful
            return encryptionStrategy.decrypt(encryptedValue)
        } else {
            throw SecurityException("Data integrity check failed for key: $key")
        }
    }

    // Clear specific data securely
    fun clearData(key: String) {
        sharedPreferences.edit().remove(key).remove("$key:hmac").apply()
    }

    // Clear all secure data
    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }
}
