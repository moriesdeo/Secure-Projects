package id.secure.secureprefs

interface EncryptionStrategy {
    fun encrypt(plainText: String): String
    fun decrypt(cipherText: String): String
}
