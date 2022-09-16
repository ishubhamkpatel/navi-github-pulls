#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_navi_secrets_NativeLib_githubAccessToken(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "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";
    return env->NewStringUTF(hello.c_str());
}