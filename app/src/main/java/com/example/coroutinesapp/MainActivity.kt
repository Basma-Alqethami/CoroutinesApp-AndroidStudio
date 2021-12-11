package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var butt: Button
    lateinit var textAdvice : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        butt = findViewById(R.id.butt)
        textAdvice = findViewById(R.id.textAdvice)

        butt.setOnClickListener(){
            requestData()
        }
    }

    private fun requestData()
    {
        CoroutineScope(Dispatchers.IO).launch {

            val data = async { fetchData() }.await()

            if (data.isNotEmpty())
            {
                withContext(Dispatchers.Main)
                {

                    var jsonOfAdvice = JSONObject(data)
                    var holdJson = jsonOfAdvice.getJSONObject("slip").getString("advice")
                    withContext(Main){
                        textAdvice.text = holdJson
                    }
                }
            }
        }
    }

    fun fetchData():String {
        var response = ""

        try {
            response = URL("https://api.adviceslip.com/advice").readText()
        }catch (e: Exception)
        {
            Log.d("advice","$e")

        }

        return response
    }
}