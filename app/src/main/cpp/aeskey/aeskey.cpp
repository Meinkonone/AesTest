//
// Created by konone on 1/28/21.
//
#include <jni.h>
#include <string.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_konone_aestest_AESUtil_getSecretKey(JNIEnv *env, jobject instance) {
    char *AUTH_KEY = "konone$#|AesKey@";
    return env->NewStringUTF(AUTH_KEY);
}