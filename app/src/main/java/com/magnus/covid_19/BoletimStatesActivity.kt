package com.magnus.covid_19

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_boletim_states.*


class BoletimStatesActivity : AppCompatActivity() {
    var uf: String = "rs"
    private var asyncTask: StatesTask? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boletim_states)
        val actionbar = supportActionBar

        uf = intent.getStringExtra("Uf")
        CarregaDados()
        actionbar!!.title = resources.getString(R.string.estados)

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun CarregaDados() {
        if (asyncTask == null) {
            if (BoletimstatesBrasilHTTP.hasConnection(this)) {
                if (asyncTask?.status != AsyncTask.Status.RUNNING) {
                    asyncTask = StatesTask()
                    asyncTask?.execute()
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class StatesTask : AsyncTask<Void, Void, BoletimStates?>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }


        @SuppressLint("WrongThread")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg params: Void?): BoletimStates? {
            return BoletimstatesBrasilHTTP.loadState(uf)
        }

        private fun update(result: BoletimStates?) {

            if (result != null) {

                txt_pais.text = result.state
                txt_casos.text =
                    resources.getString(R.string.casos_confirmados) + " " + "${result.cases}"
                txt_suspeitos.text =
                    resources.getString(R.string.suspeitos) + " " + "${result.suspects}"
                txt_refuses.text =
                    resources.getString(R.string.casos_recuperados) + " " + "${result.refuses}"
                txt_mortos.text = resources.getString(R.string.mortes) + " " + "${result.deaths}"

            }

            asyncTask = null
        }

        override fun onPostExecute(result: BoletimStates?) {
            super.onPostExecute(result)
            update(result as BoletimStates?)
        }

    }
}