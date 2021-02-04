package com.konone.aestest.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.konone.aestest.AESUtil
import com.konone.aestest.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.Exception

/**
 * Created by konone on 2/1/21.
 */
class AesFileViewModel : ViewModel() {
    private var mContext: Context? = null
    var mAesDecryptImage: MutableLiveData<ByteArray?> = MutableLiveData()
    var mAesEncryptResult: MutableLiveData<Boolean> = MutableLiveData()

    fun setContext(context: Context) {
        mContext = context
    }

    fun doEncryptClick() {
        GlobalScope.launch(Dispatchers.IO) {
            var inputStream: InputStream? = null
            try {
                inputStream = mContext!!.assets.open("beauty.jpg")
                mAesEncryptResult.postValue(AESUtil.aesEncrypt(inputStream, AESUtil.secretKey, mContext!!, "beauty"))
            } catch (e: Exception) {
                e.printStackTrace()
                mAesEncryptResult.postValue(false)
            } finally {
                Utils.closeSilently(inputStream)
            }
        }
    }

    fun doDecryptClick() {
        GlobalScope.launch(Dispatchers.IO) {
            var inputStream: FileInputStream? = null
            try {
                inputStream =
                    FileInputStream(mContext!!.filesDir.path + File.separator + "beauty.aes")
                mAesDecryptImage.postValue(AESUtil.aesDecrypt(inputStream, AESUtil.secretKey))
            } catch (e: Exception) {
                e.printStackTrace()
                mAesDecryptImage.postValue(null)
            } finally {
                Utils.closeSilently(inputStream)
            }
        }
    }
}