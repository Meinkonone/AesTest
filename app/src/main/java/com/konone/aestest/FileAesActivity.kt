package com.konone.aestest

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.konone.aestest.databinding.ActivityFileBinding
import com.konone.aestest.vm.AesFileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

/**
 * Created by konone on 2/1/21.
 */
class FileAesActivity : AppCompatActivity() {

    val mActivityFileBinding: ActivityFileBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_file)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 10000
                )
            }
        }

        mActivityFileBinding.fileAesViewModel =
            ViewModelProvider(this).get(AesFileViewModel::class.java)
        mActivityFileBinding.fileAesViewModel!!.setContext(this)
        mActivityFileBinding.lifecycleOwner = this
        subscribeAesFileVM()
    }

    private fun subscribeAesFileVM() {
        val encryptObserver = Observer<Boolean> {
            //encrypt success
            Log.i(TAG, "subscribeAesFileVM: encrypt value change")
            GlobalScope.launch {
                val toastText =
                    if (it) {
                        "Encrypt success, file save in ${filesDir.path + File.separator + ENCRYPT_FILE_NAME}"
                    } else {
                        "Encrypt failed, please check the assets file and code!"
                    }
                runOnUiThread {
                    Toast.makeText(applicationContext, toastText, Toast.LENGTH_SHORT).show()
                }
            }
        }
        mActivityFileBinding.fileAesViewModel!!.mAesEncryptResult.observe(this, encryptObserver)

        val decryptObserver = Observer<ByteArray?> {
            //decrypt success
            Log.i(TAG, "subscribeAesFileVM: decrypt value change")
            if (it != null) {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                val imageView: ImageView = mActivityFileBinding.decryptShowIv
                imageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Decrypt failed, please confirm you has encrypt file in " +
                            "${filesDir.path + File.separator + ENCRYPT_FILE_NAME}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        mActivityFileBinding.fileAesViewModel!!.mAesDecryptImage.observe(this, decryptObserver)
    }

    companion object {
        const val TAG = "AesTest_File"
        const val ENCRYPT_FILE_NAME = "beauty.aes"
    }
}