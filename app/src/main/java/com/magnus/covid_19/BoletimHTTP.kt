package com.magnus.covid_19

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object BoletimHttp {
    val Json_URL = "https://covid19-brazil-api.now.sh/api/report/v1/countries"
    private fun connect(urlAdrress: String): HttpURLConnection {
        val second = 1000
        val url = URL(urlAdrress)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            readTimeout = 10 * second
            connectTimeout = 15 * second
            requestMethod = "GET"
            doInput = true
            doOutput = false
        }
        connection.connect()
        return connection
    }

    fun hasConnection(ctx: Context): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun readBoletins(json: JSONArray): List<Boletim> {
        val boletins = mutableListOf<Boletim>()
        try {
           // var jsonArray = JSONArray(json)
            for (i in 0..json.length() - 1) {
                var js = json.getJSONObject(i)
                val dia = formatarData(js.getString("updated_at").substring(0, 10))
                val hora = js.getString("updated_at").substring(11, 16)
                val boletim = Boletim(
                    js.getString("country"),
                    js.getInt("cases"),
                    js.getInt("confirmed"),
                    js.getInt("deaths"),
                    js.getInt("recovered"), dia , hora

                )

                boletins.add(boletim)

            }
        } catch (e: IOException) {
            Log.e("Erro", "Impossivel ler JSON")
        }
        return boletins
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatarData(data: String): String {
        val diaString = data
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var date = LocalDate.parse(diaString)
        var formattedDate = date.format(formatter)
        return formattedDate
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadBoletim(): List<Boletim>? {
        try {
            val connection = connect(Json_URL)
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val jsonString = streamToString(inputStream)
                val jsonO = JSONObject(jsonString)
                val json = jsonO.getJSONArray("data")
                return readBoletins(json)
            }
        } catch (e: Exception) {
            Log.e("ERRO", e.message)
            e.printStackTrace()
        }
        return null
    }

    private fun streamToString(inputStream: InputStream): String {
        val buffer = ByteArray(1024)
        val bigBuffer = ByteArrayOutputStream()
        var bytesRead: Int
        while (true) {
            bytesRead = inputStream.read(buffer)
            if (bytesRead == -1) break
            bigBuffer.write(buffer, 0, bytesRead)
        }
        return String(bigBuffer.toByteArray(), Charset.forName("UTF-8"))
    }
}