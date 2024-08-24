# SecurePrefs: Secure Data Storage Library for Android

SecurePrefs is a library designed for secure data storage in Android applications. This library supports multiple encryption algorithms such as AES, RSA, and ChaCha20, and provides mechanisms to ensure data integrity using HMAC.

## Key Features
- **Data Encryption**: Supports AES, RSA, and ChaCha20 for encrypting stored data.
- **Integrity Verification**: Uses HMAC to ensure that the data has not been tampered with.
- **Flexible Configuration**: Users can choose the desired encryption algorithm.
- **Compatibility**: Supports Android API level 21 and above.

## Installation
Add the following dependency to your module-level `build.gradle` file:

```groovy
dependencies {
    implementation 'com.github.moriesdeo:SecurePrefs:1.0.8'
}

```
## If using JitPack, add the JitPack repository to your project-level settings.gradle or build.gradle:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

```
## Usage
Initialize SecurePrefs
```kotlin
val securePrefs = SecurePreferences(context, "AES")

```
## Save data securely
```kotlin
securePrefs.saveSecureData("user_token", "my_secure_token")

```
## Retrieve secure data
```kotlin
val token = securePrefs.getSecureData("user_token")

```
## Clear specific data
```kotlin
securePrefs.clearData("user_token")

```
## Choosing an Encryption Algorithm

```kotlin
// Use RSA encryption
val securePrefs = SecurePreferences(context, "RSA")

```
## HMAC Verification
```kotlin
// Verification is automatically performed when retrieving data
val token = securePrefs.getSecureData("user_token")

```
## Contributing
Contributions are welcome! Please open a pull request for bug fixes, new features, or documentation improvements.

## Support
If you find this project useful, consider buying me a coffee:
buymeacoffee.com/nasio

[![](https://jitpack.io/v/moriesdeo/Secure-Projects.svg)](https://jitpack.io/#moriesdeo/Secure-Projects)
