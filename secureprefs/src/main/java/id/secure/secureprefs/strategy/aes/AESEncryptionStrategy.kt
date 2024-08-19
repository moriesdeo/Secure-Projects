package id.secure.secureprefs.strategy.aes

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import id.secure.secureprefs.EncryptionStrategy
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class AESEncryptionStrategy : EncryptionStrategy {

    companion object {
        private const val KEY_ALIAS = "SecurePrefsAESKey"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val IV_SIZE = 12 // GCM recommended IV size is 12 bytes
        private const val TAG_SIZE = 128 // GCM authentication tag size in bits
    }

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }

    // Method to retrieve or generate the secret key
    private fun getSecretKey(): SecretKey {
        return if (keyStore.containsAlias(KEY_ALIAS)) {
            keyStore.getKey(KEY_ALIAS, null) as SecretKey
        } else {
            createSecretKey()
        }
    }

    // Method to create and store the secret key in the Android Keystore
    private fun createSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true).build()
        )
        return keyGenerator.generateKey()
    }

    // Method to encrypt the plain text
    override fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = getSecretKey()

        // Generate a new IV for each encryption
        val iv = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(iv)
        val gcmSpec = GCMParameterSpec(TAG_SIZE, iv)

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)
        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // Concatenate IV and cipherText and return as a Base64 encoded string
        val encryptedData = iv + cipherText
        return Base64.encodeToString(encryptedData, Base64.DEFAULT)
    }

    // Method to decrypt the cipher text
    override fun decrypt(cipherText: String): String {
        val decodedData = Base64.decode(cipherText, Base64.DEFAULT)

        // Extract the IV and the actual cipherText
        val iv = decodedData.copyOfRange(0, IV_SIZE)
        val cipherTextBytes = decodedData.copyOfRange(IV_SIZE, decodedData.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val gcmSpec = GCMParameterSpec(TAG_SIZE, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), gcmSpec)

        val plainTextBytes = cipher.doFinal(cipherTextBytes)
        return String(plainTextBytes, Charsets.UTF_8)
    }
}
