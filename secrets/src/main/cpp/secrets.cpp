#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_navi_secrets_NativeLib_githubAccessToken(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "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";
    return env->NewStringUTF(hello.c_str());
}