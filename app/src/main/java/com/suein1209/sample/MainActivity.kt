package com.suein1209.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.suein1209.common.kotlinsupportlib.ext.isNotNullEmpty

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tt = ""
        tt.isNotNullEmpty() // 테스트 추가

    }
}