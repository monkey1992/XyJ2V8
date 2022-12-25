package com.xy.j2v8

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.V8Object
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var v8: V8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        v8 = V8.createV8Runtime()

        addVariables()
        registerConsole()
        registerToast()

        btnConsoleLog.setOnClickListener {
            Thread {
                v8.executeScript("console.log('myConsole', [key1, key2, key3]);")
            }.start()
        }

        btnToast.setOnClickListener {
//            v8.executeScript("toast('Hello, I am a toast!')")
            v8.executeJSFunction("toast", "Hello, I am a toast!")
        }
    }

    /**
     * 注入js变量
     */
    private fun addVariables() {
        v8.add("key1", "value1")
        v8.add("key2", "value2")
        v8.add("key3", "value3")
    }

    /**
     * 注册Console
     */
    private fun registerConsole() {
        val console = Console()
        val consoleObject = V8Object(v8)
        v8.add("console", consoleObject)
        consoleObject.registerJavaMethod(
            console, "log", "log", arrayOf(
                String::class.java, V8Array::class.java
            )
        )
        consoleObject.close()
    }

    /**
     * 注册Toast
     */
    private fun registerToast() {
        v8.registerJavaMethod({ v8Object, v8Array ->
            Toast.makeText(this, "$v8Object, $v8Array", Toast.LENGTH_SHORT).show()
        }, "toast")
    }

    override fun onDestroy() {
        super.onDestroy()
        v8.close()
    }
}