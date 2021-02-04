package com.konone.aestest

import java.io.Closeable
import java.io.IOException
import kotlin.experimental.and

/**
 * Created by konone on 1/29/21.
 */
class Utils {
    companion object {
        fun closeSilently(stream: Closeable?) {
            try {
                stream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        /**
         * 字符串转hex格式(写入并能被单片机执行的十六进制数据格式)的byte数据
         */
        open fun parseHexStr2Byte(hexStr: String): ByteArray {
            val result = ByteArray(hexStr.length / 2)
            for (i in 0 until hexStr.length / 2) {
                val high = hexStr.substring(i * 2, i * 2 + 1).toInt(16)
                val low = hexStr.substring(i * 2 + 1, i * 2 + 2).toInt(16)
                result[i] = (high * 16 + low).toByte()
            }
            return result
        }

        /**
         * byte数据转hex格式的字符串
         */
        open fun parseByte2HexStr(buf: ByteArray): String {
            val sb = StringBuilder()
            for (b in buf) {
                var hex = Integer.toHexString((b and 0xFF.toByte()).toInt())
                if (hex.length == 1) {
                    hex = "0$hex"
                }
                sb.append(hex.toUpperCase())
            }
            return String(sb.toString().toByteArray(AESUtil.CHARSET_ISO), AESUtil.CHARSET_ISO)
        }
    }
}