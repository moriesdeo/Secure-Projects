package id.secure.secureprefs

import id.secure.secureprefs.strategy.aes.AESEncryptionStrategy
import id.secure.secureprefs.strategy.chacha20.ChaCha20EncryptionStrategy
import id.secure.secureprefs.strategy.rsa.RSAEncryptionStrategy

class EncryptionFactory {

    companion object {
        // Method to get an instance of EncryptionStrategy based on the algorithm type
        fun getEncryptionStrategy(algorithm: String): EncryptionStrategy {
            return when (algorithm) {
                "AES" -> AESEncryptionStrategy()
                "RSA" -> RSAEncryptionStrategy()
                "ChaCha20" -> ChaCha20EncryptionStrategy()
                else -> throw IllegalArgumentException("Unknown encryption algorithm: $algorithm")
            }
        }
    }
}
