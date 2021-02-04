package com.konone.aestest

import android.content.Context
import android.util.Log
import com.konone.aestest.Utils.Companion.closeSilently
import java.io.*
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by konone on 1/28/21.
 */
object AESUtil {
    private const val KEY_ALGORITHM = "AES"

    private const val CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding"

    val CHARSET_ISO = Charsets.ISO_8859_1

    private const val TAG = "AesTest_AESUtil"

    /**
     * 密钥是16位的ivBytes
     */
    private var ivBytes: ByteArray = byteArrayOf(
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    )

    /**
     * 文件流aes加密 (图片/文件)
     */
    fun aesEncrypt(ips: InputStream, key: String, context: Context, title: String): Boolean {
        var bops = FileOutputStream(context.filesDir.toString() + File.separator + "$title.aes")
        var cops: CipherOutputStream? = null
        Log.i(TAG, "aesEncrypt: key = $key")
        try {
            val keySpec =
                SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), KEY_ALGORITHM)
            val cipher = Cipher.getInstance(KEY_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            cops = CipherOutputStream(bops, cipher)
            val buffer = ByteArray(1024)
            var length: Int
            while (ips.read(buffer).also { length = it } != -1) {
                cops.write(buffer, 0, length)
            }
            cops.flush()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            closeSilently(cops)
        }
        return true
    }

    /**
     * 文件流aes解密 (图片/文件)
     */
    fun aesDecrypt(ips: InputStream, key: String): ByteArray? {
        var bytes: ByteArray? = null
        var bops: ByteArrayOutputStream? = null
        Log.i(TAG, "aesDecrypt: key = $key")
        val keySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), KEY_ALGORITHM)
        var cips: CipherInputStream? = null
        try {
            bops = ByteArrayOutputStream()
            val cipher = Cipher.getInstance(KEY_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            cips = CipherInputStream(ips, cipher)
            var length: Int
            val buffer = ByteArray(1024)
            while (cips.read(buffer).also { length = it } != -1) {
                bops.write(buffer, 0, length)
            }
            bops.flush()
            bytes = bops.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeSilently(bops)
            closeSilently(cips)
        }
        return bytes
    }


    /**
     * byte数据的解密 (字符串数据)
     */
    fun aesDecrypt(key: String, byteArray: ByteArray): ByteArray {
        Log.i(TAG, "aesDecrypt: key = $key, secretKey = $secretKey")
        val keySpec = SecretKeySpec(key.toByteArray(CHARSET_ISO), KEY_ALGORITHM)
        val cipher = Cipher.getInstance(CBC_PKCS5_PADDING)
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(ivBytes))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cipher.doFinal(byteArray)
    }

    /**
     * byte数据的加密 (字符串数据)
     */
    fun aesEncrypt(key: String, byteArray: ByteArray): ByteArray {
        Log.i(TAG, "aesEncrypt: key = $key, secretKey = $secretKey")
        val keySpec = SecretKeySpec(key.toByteArray(CHARSET_ISO), KEY_ALGORITHM)
        val cipher = Cipher.getInstance(CBC_PKCS5_PADDING)
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(ivBytes))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cipher.doFinal(byteArray)
    }

    val secretKey: String
        external get

    init {
        System.loadLibrary("aeskey")
    }
}