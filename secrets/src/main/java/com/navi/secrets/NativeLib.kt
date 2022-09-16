package com.navi.secrets

internal class NativeLib {

    /**
     * A native method that is implemented by the 'secrets' native library,
     * which is packaged with this application.
     */
    external fun githubAccessToken(): String
}