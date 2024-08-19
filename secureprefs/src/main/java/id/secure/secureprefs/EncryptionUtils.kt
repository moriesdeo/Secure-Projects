package id.secure.secureprefs

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object EncryptionUtils {

    private const val KEYSTORE_ALIAS = "MyKeyAlias"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }

    private fun getSecretKey(): SecretKey {
        return if (keyStore.containsAlias(KEYSTORE_ALIAS)) {
            keyStore.getKey(KEYSTORE_ALIAS, null) as SecretKey
        } else {
            createSecretKey()
        }
    }

    private fun createSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEYSTORE_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build()
        )
        return keyGenerator.generateKey()
    }

    fun encryptData(plainText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

        val iv = cipher.iv
        val encryption = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        return Base64.encodeToString(iv + encryption, Base64.DEFAULT)
    }

    fun decryptData(encryptedData: String): String {
        val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)
        val iv = decodedData.copyOfRange(0, 12)
        val cipherText = decodedData.copyOfRange(12, decodedData.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)

        return String(cipher.doFinal(cipherText), Charsets.UTF_8)
    }

    // Example utility function to perform Base64 encoding
    fun base64Encode(input: ByteArray): String {
        return Base64.encodeToString(input, Base64.DEFAULT)
    }

    // Example utility function to perform Base64 decoding
    fun base64Decode(input: String): ByteArray {
        return Base64.decode(input, Base64.DEFAULT)
    }

    // Example utility function to convert string to byte array
    fun stringToBytes(input: String): ByteArray {
        return input.toByteArray(Charsets.UTF_8)
    }

    // Example utility function to convert byte array to string
    fun bytesToString(input: ByteArray): String {
        return String(input, Charsets.UTF_8)
    }

    // Example utility function to generate random bytes for IV or nonce
    fun generateRandomBytes(size: Int): ByteArray {
        val randomBytes = ByteArray(size)
        SecureRandom().nextBytes(randomBytes)
        return randomBytes
    }
}
