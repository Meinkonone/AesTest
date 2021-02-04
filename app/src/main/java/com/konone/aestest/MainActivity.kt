package com.konone.aestest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.konone.aestest.databinding.ActivityMainBinding
import com.konone.aestest.vm.AesStringViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    /**
     * dataBinding
     */
    private val mActivityMainBinding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val mInputText by lazy {
        mActivityMainBinding.dataInputEt
    }

    private val mEncryptResultText by lazy {
        mActivityMainBinding.encryptResult
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //liveData
        mActivityMainBinding!!.aesViewModel =
            ViewModelProvider(this).get(AesStringViewModel::class.java)
        mActivityMainBinding!!.lifecycleOwner = this
        Log.i(TAG, "aesKey is ${AESUtil.secretKey}")
    }

    fun onEncryptClick(view: View) {
        val inputText = mInputText.text.toString().toByteArray(AESUtil.CHARSET_ISO)
        Log.i(TAG, "onEncryptClick: inputText = $inputText")
        GlobalScope.launch {
            val encryptResult = encrypt(inputText)
            mActivityMainBinding!!.aesViewModel?.setEncryptValue(
                String(
                    encryptResult,
                    AESUtil.CHARSET_ISO
                )
            )
        }
    }

    fun onDecryptClick(view: View) {
        val text = mEncryptResultText.text.toString().toByteArray(AESUtil.CHARSET_ISO)
        Log.i(TAG, "onDecryptClick: text = $text")
        if (text != null) {
            GlobalScope.launch {
                val decryptResult = decrypt(text)
                mActivityMainBinding!!.aesViewModel?.setDecryptValue(
                    String(
                        decryptResult,
                        AESUtil.CHARSET_ISO
                    )
                )
            }
        }
    }

    fun onFileAesJumpClick(view: View) {
        val intent = Intent(this, FileAesActivity::class.java)
        startActivity(intent)
    }

    private suspend fun encrypt(inputText: ByteArray): ByteArray {
        return withContext(Dispatchers.IO) {
            AESUtil.aesEncrypt(AESUtil.secretKey, inputText)
        }
    }

    private suspend fun decrypt(inputText: ByteArray): ByteArray {
        return withContext(Dispatchers.IO) {
            AESUtil.aesDecrypt(AESUtil.secretKey, inputText)
        }
    }

    companion object {
        const val TAG = "AesTest_Main"
    }
}