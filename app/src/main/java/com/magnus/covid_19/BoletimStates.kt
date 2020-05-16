package com.magnus.covid_19

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BoletimStates (
    var state: String?,
    var cases: Int = 0,
    var deaths: Int = 0,
    var suspects: Int = 0,
    var refuses: Int = 0,
    var data: String,
    var hora: String,
    var uf: String
) {
    override fun toString(): String {
        return data
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getData(data: String): String {
        val diaString = data
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var date = LocalDate.parse(diaString)
        var formattedDate = date.format(formatter)
        return formattedDate
    }

}