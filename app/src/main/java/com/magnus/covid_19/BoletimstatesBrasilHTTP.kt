package com.magnus.covid_19

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.magnus.covid_19.BoletimBrasil
import com.magnus.covid_19.BoletimStates
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
import java.util.concurrent.TimeUnit

object BoletimstatesBrasilHTTP {

    val url = "https://covid19-brazil-api.now.sh/api/report/v1/brazil/uf/"

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
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadState(uf: String): BoletimStates? {
        try {
            val connection = connect(url + uf)
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val jsonString = streamToString(inputStream)
                val jsonO = JSONObject(jsonString)
                return readBoletins(jsonO)
            }
        } catch (e: Exception) {
            Log.e("ERRO", e.message)
            e.printStackTrace()
        }
        return null


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun readBoletins(json: JSONObject): BoletimStates? {
        try {
            // var jsonArray = JSONArray(json)
            val dia = formatarData(json.getString("datetime").substring(0, 10))
            val hora = json.getString("datetime").substring(11, 16)
            val boletim = BoletimStates(
                json.getString("state"),
                json.getInt("cases"),
                json.getInt("deaths"),
                json.getInt("suspects"),
                json.getInt("refuses"), dia, hora,
                json.getString("uf")
            )
            return boletim
        } catch (e: IOException) {
            Log.e("Erro", "Impossivel ler JSON")
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatarData(data: String): String {
        val diaString = data
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var date = LocalDate.parse(diaString)
        var formattedDate = date.format(formatter)
        return formattedDate
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
