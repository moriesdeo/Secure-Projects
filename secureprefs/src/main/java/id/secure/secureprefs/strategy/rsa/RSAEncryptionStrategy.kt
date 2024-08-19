package id.secure.secureprefs.strategy.rsa

import android.util.Base64
import id.secure.secureprefs.EncryptionStrategy
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class RSAEncryptionStrategy : EncryptionStrategy {

    companion object {
        private const val ALGORITHM = "RSA"
        private const val KEY_SIZE = 2048 // RSA key size
        private const val TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
    }

    // Generate a new RSA key pair
    private fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM)
        keyPairGenerator.initialize(KEY_SIZE)
        return keyPairGenerator.genKeyPair()
    }

    // Method to get PublicKey from a Base64 encoded string
    private fun getPublicKey(publicKeyStr: String): PublicKey {
        val keyBytes = Base64.decode(publicKeyStr, Base64.DEFAULT)
        val spec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        return keyFactory.generatePublic(spec)
    }

    // Method to get PrivateKey from a Base64 encoded string
    private fun getPrivateKey(privateKeyStr: String): PrivateKey {
        val keyBytes = Base64.decode(privateKeyStr, Base64.DEFAULT)
        val spec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        return keyFactory.generatePrivate(spec)
    }

    // Method to encrypt the plain text
    override fun encrypt(plainText: String): String {
        val keyPair = generateKeyPair()
        val publicKey = keyPair.public
        val privateKey = keyPair.private

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // Encode the cipherText and keys to Base64 and concatenate them
        val cipherTextEncoded = Base64.encodeToString(cipherText, Base64.DEFAULT)
        val publicKeyEncoded = Base64.encodeToString(publicKey.encoded, Base64.DEFAULT)
        val privateKeyEncoded = Base64.encodeToString(privateKey.encoded, Base64.DEFAULT)

        return "$cipherTextEncoded:$publicKeyEncoded:$privateKeyEncoded"
    }

    // Method to decrypt the cipher text
    override fun decrypt(cipherText: String): String {
        val parts = cipherText.split(":")
        val cipherTextBytes = Base64.decode(parts[0], Base64.DEFAULT)
        val privateKey = getPrivateKey(parts[2])

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        val plainTextBytes = cipher.doFinal(cipherTextBytes)
        return String(plainTextBytes, Charsets.UTF_8)
    }
}
