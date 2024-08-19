SecurePrefs: Secure Data Storage Library for Android

SecurePrefs is a library designed for secure data storage in Android applications. This library supports multiple encryption algorithms such as AES, RSA, and ChaCha20, and provides mechanisms to ensure data integrity using HMAC.

Key Features
Data Encryption: Supports AES, RSA, and ChaCha20 for encrypting stored data.
Integrity Verification: Uses HMAC to ensure that the data has not been tampered with.
Flexible Configuration: Users can choose the desired encryption algorithm.
Compatibility: Supports Android API level 21 and above.

Installation
Add the following dependency to your module-level build.gradle file:
dependencies {
    implementation 'com.github.moriesdeo:SecurePrefs:1.0.0'
}

If using JitPack, add the JitPack repository to your project-level settings.gradle or build.gradle:
repositories {
    maven { url 'https://jitpack.io' }
}

Usage
Initialize SecurePrefs
val securePrefs = SecurePreferences(context, "AES")

// Save data securely
securePrefs.saveSecureData("user_token", "my_secure_token")

// Retrieve secure data
val token = securePrefs.getSecureData("user_token")

// Clear specific data
securePrefs.clearData("user_token")

Choosing an Encryption Algorithm
// Use RSA encryption
val securePrefs = SecurePreferences(context, "RSA")

HMAC Verification
// Verification is automatically performed when retrieving data
val token = securePrefs.getSecureData("user_token")

Contributing
Contributions are welcome! Please open a pull request for bug fixes, new features, or documentation improvements.

Support
If you have any questions or issues, please open an issue on GitHub or contact me at moriesdeo574@gmail.com.
"Buy Me a Coffee"
buymeacoffee.com/nasio
