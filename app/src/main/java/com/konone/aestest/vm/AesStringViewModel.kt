package com.konone.aestest.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by konone on 1/29/21.
 */
class AesStringViewModel : ViewModel() {
    var mEncryptResult: MutableLiveData<String> = MutableLiveData()
    var mDecryptResult: MutableLiveData<String> = MutableLiveData()

    fun setEncryptValue(string: String) {
        mEncryptResult.postValue(string)
    }

    fun setDecryptValue(string: String) {
        mDecryptResult.postValue(string)
    }

}