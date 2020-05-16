package com.magnus.covid_19

import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_boletim_brasil.*

class BoletimBrasilActivity : AppCompatActivity() {
    private var asyncTask: BoletimTask? = null
    private var boletinsList = mutableListOf<BoletimBrasil>()
    private var adapter = AdapterBrasil(boletinsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boletim_brasil)
        carregaDados()
        initRecyclerView()
        val actionbar = supportActionBar

        actionbar!!.title = "Lista do Brasil"

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun carregaDados() {
        if (boletinsList.isNotEmpty()) {
            showProgress(false)
        } else {
            if (asyncTask == null) {
                if (BoletimBrasilHTTP.hasConnection(this)) {
                    starDonwload()
                } else {
                    progressBar.visibility = View.GONE
                    txtMsg.text = "Erro"
                }
            } else if (asyncTask?.status == AsyncTask.Status.RUNNING) {
                showProgress(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_refresh -> {

            Toast.makeText(this, "Carregando...", Toast.LENGTH_LONG).show()
            carregaDados()

            true
        }


        else -> {

            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initRecyclerView() {
        rvDados.adapter = adapter
        // val layoutManager = GridLayoutManager(this,1)
        val layoutManager = LinearLayoutManager(this)
        rvDados.layoutManager = layoutManager
    }


    private fun starDonwload() {
        if (asyncTask?.status != AsyncTask.Status.RUNNING) {
            asyncTask = BoletimTask()
            asyncTask?.execute()
        }
    }

    fun data(boletins: List<BoletimBrasil>?) {
        if (boletins != null) {
            text_data.text = boletins.get(0).data + " às " + "" + boletins.get(0).hora
        }
    }


    fun somar(boletins: List<BoletimBrasil>?): Int {
        var casos = 0
        if (boletins != null) {
            for (i in 0..boletins.size - 1) {
                casos += boletins.get(i).cases
            }
        }
        return casos
    }


    private fun updateBoletns(result: List<BoletimBrasil>?) {
        if (result != null) {
            boletinsList.clear()
            boletinsList.addAll(result)
            data(result).toString()
            txt_casos.text = somar(result).toString()
        } else {
            txtMsg.text = "Erro ao Carregar"
        }
        adapter.notifyDataSetChanged()
        asyncTask = null
    }

    fun showProgress(show: Boolean) {
        if (show) {
            txtMsg.text = "Carregando...."
        }
        txtMsg.visibility = if (show) View.VISIBLE else View.GONE
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    inner class BoletimTask : AsyncTask<Void, Void, List<BoletimBrasil>?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgress(true)

        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg p0: Void?): List<BoletimBrasil>? {
            return BoletimBrasilHTTP.loadBoletim()

        }

        override fun onPostExecute(bo: List<BoletimBrasil>?) {
            super.onPostExecute(bo)
            showProgress(false)
            updateBoletns(bo)

        }

    }
}
