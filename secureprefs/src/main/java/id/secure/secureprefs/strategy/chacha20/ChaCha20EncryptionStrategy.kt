package id.secure.secureprefs.strategy.chacha20

import android.util.Base64
import id.secure.secureprefs.EncryptionStrategy
import org.bouncycastle.crypto.engines.ChaChaEngine
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.crypto.params.ParametersWithIV
import java.security.SecureRandom

class ChaCha20EncryptionStrategy : EncryptionStrategy {

    companion object {
        private const val KEY_SIZE = 32 // 256-bit key for ChaCha20 (32 bytes)
        private const val NONCE_SIZE = 12 // Recommended nonce size for ChaCha20 is 12 bytes
    }

    // Generate a new key for ChaCha20
    private fun generateKey(): ByteArray {
        val key = ByteArray(KEY_SIZE)
        SecureRandom().nextBytes(key)
        return key
    }

    // Method to encrypt the plain text
    override fun encrypt(plainText: String): String {
        val key = generateKey()

        // Generate a new nonce for each encryption
        val nonce = ByteArray(NONCE_SIZE)
        SecureRandom().nextBytes(nonce)

        val chacha = ChaChaEngine(20) // ChaCha20
        chacha.init(true, ParametersWithIV(KeyParameter(key), nonce))

        val plainTextBytes = plainText.toByteArray(Charsets.UTF_8)
        val cipherText = ByteArray(plainTextBytes.size)
        chacha.processBytes(plainTextBytes, 0, plainTextBytes.size, cipherText, 0)

        // Combine nonce and cipherText and return as a Base64 encoded string
        val encryptedData = nonce + cipherText
        val keyEncoded = Base64.encodeToString(key, Base64.DEFAULT)
        return Base64.encodeToString(encryptedData, Base64.DEFAULT) + ":" + keyEncoded
    }

    // Method to decrypt the cipher text
    override fun decrypt(cipherText: String): String {
        val parts = cipherText.split(":")
        val encryptedData = Base64.decode(parts[0], Base64.DEFAULT)
        val decodedKey = Base64.decode(parts[1], Base64.DEFAULT)

        // Extract the nonce and actual cipherText
        val nonce = encryptedData.copyOfRange(0, NONCE_SIZE)
        val cipherTextBytes = encryptedData.copyOfRange(NONCE_SIZE, encryptedData.size)

        val chacha = ChaChaEngine(20)
        chacha.init(false, ParametersWithIV(KeyParameter(decodedKey), nonce))

        val plainTextBytes = ByteArray(cipherTextBytes.size)
        chacha.processBytes(cipherTextBytes, 0, cipherTextBytes.size, plainTextBytes, 0)

        return String(plainTextBytes, Charsets.UTF_8)
    }
}
