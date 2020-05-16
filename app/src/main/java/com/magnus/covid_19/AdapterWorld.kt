package com.magnus.covid_19

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*

class AdapterWorld(private val dados: List<Boletim>) : RecyclerView.Adapter<AdapterWorld.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        val vh = VH(v)
        v.setOnClickListener {
            var boletim = dados[vh.adapterPosition]
            var dados = dados[vh.adapterPosition]
            val enviarDados =
                Intent(v.context, BoletimMundoActivity::class.java)
            enviarDados.putExtra("Suspeitos", dados.cases.toString())
            enviarDados.putExtra("Confirmados", dados.confirmed.toString())
            enviarDados.putExtra("Data", dados.data)
            enviarDados.putExtra("Hora", dados.hora)
            enviarDados.putExtra("Curados", dados.recovered.toString())
            enviarDados.putExtra("mortes", dados.deaths.toString())
            v.context.startActivity(enviarDados)
        }
        return vh
    }

    override fun getItemCount(): Int {
        return dados.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        var dados = dados[position]
        holder.txtConfirmados.text = dados.cases.toString()
        holder.txtMortos.text = dados.deaths.toString()
        holder.txtRecuperados.text = dados.recovered.toString()
        holder.txtPais.text= dados.country
    }


    ///INNERCLASS
    class VH(itenView: View) : RecyclerView.ViewHolder(itenView) {
        var txtConfirmados: TextView = itenView.text_n_corfirmados
        var txtRecuperados: TextView = itenView.text_n_recuperados
        var txtMortos: TextView = itenView.text_n_mortos
        var txtPais: TextView = itemView.txtpais
    }
}
