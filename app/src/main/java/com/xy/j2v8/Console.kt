package com.xy.j2v8

import android.util.Log
import com.eclipsesource.v8.V8Array

/**
 * 输出日志的类
 */
class Console {

    fun log(tag: String, message: V8Array) {
        Log.d(tag, message.toString())
    }
}