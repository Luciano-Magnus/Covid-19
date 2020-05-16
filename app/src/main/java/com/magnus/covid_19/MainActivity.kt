package com.magnus.covid_19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_mundo.setOnClickListener({
            val enviarPara = Intent(this, BoletimMundoActivity::class.java)
            startActivity(enviarPara)
        })
        btn_brasil.setOnClickListener({
            val enviarPara = Intent(this, BoletimBrasilActivity::class.java)
            startActivity(enviarPara)
        })

    }
}
