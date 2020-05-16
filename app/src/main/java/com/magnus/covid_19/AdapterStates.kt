package com.magnus.covid_19

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*

    class AdapterStates (private val dados: MutableList<BoletimStates>) :
    RecyclerView.Adapter<AdapterStates.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterStates.VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        val vh = VH(v)
        v.setOnClickListener {
            var boletim = dados[vh.adapterPosition]
            var dados = dados[vh.adapterPosition]
            val enviarDados =
                Intent(v.context, BoletimStatesActivity::class.java)
            enviarDados.putExtra("Uf", dados.uf)
            v.context.startActivity(enviarDados)
        }
        return vh
    }

    override fun getItemCount(): Int {
        return dados.size
    }

    override fun onBindViewHolder(holder: AdapterStates.VH, position: Int) {

        var dados = dados[position]
        holder.txtConfirmados.text = dados.cases.toString()
        holder.txtMortos.text = dados.deaths.toString()
        holder.txtRecuperados.text = dados.refuses.toString()
        holder.txtPais.text = dados.state
    }


    ///INNERCLASS
    class VH(itenView: View) : RecyclerView.ViewHolder(itenView) {
        var txtConfirmados: TextView = itenView.text_n_corfirmados
        var txtRecuperados: TextView = itenView.text_n_recuperados
        var txtMortos: TextView = itenView.text_n_mortos
        var txtPais: TextView = itemView.txtpais
    }

    }