package com.azhar.pemesanantiket.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azhar.pemesanantiket.R
import com.azhar.pemesanantiket.model.ModelDatabase
import com.azhar.pemesanantiket.utils.FunctionHelper.rupiahFormat
import kotlinx.android.synthetic.main.list_item_history.view.*

class HistoryAdapter(private var modelDatabaseList: MutableList<ModelDatabase>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    // Listener untuk menangkap klik pada item transaksi
    private var onItemClickListener: ((ModelDatabase) -> Unit)? = null

    fun setOnItemClickListener(listener: (ModelDatabase) -> Unit) {
        onItemClickListener = listener
    }

    fun setDataAdapter(items: List<ModelDatabase>) {
        modelDatabaseList.clear()
        modelDatabaseList.addAll(items)
        notifyDataSetChanged()
    }

    fun setSwipeRemove(position: Int): ModelDatabase {
        return modelDatabaseList.removeAt(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = modelDatabaseList[position]


        holder.bind(data)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(data)
        }
    }

    override fun getItemCount(): Int {
        return modelDatabaseList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ModelDatabase) {
            with(itemView) {
                tvKelas.text = item.kelas
                tvDate.text = item.tanggal
                tvNama.text = item.namaPenumpang
                tvKeberangkatan.text = item.keberangkatan
                tvTujuan.text = item.tujuan
                tvHargaTiket.text = rupiahFormat(item.hargaTiket)
                tvKode1.text = getKodeBandara(item.keberangkatan)
                tvKode2.text = getKodeBandara(item.tujuan)
                tvstatus.text = item.status
            }
        }

        private fun getKodeBandara(kota: String): String {
            return when (kota) {
                "Jakarta" -> "JKT"
                "Semarang" -> "SRG"
                "Surabaya" -> "SUB"
                "Bali" -> "DPS"
                else -> ""
            }
        }
    }

    // Antarmuka untuk menangani klik pada item
    interface HistoryItemClickListener {
        fun onItemClick(modelDatabase: ModelDatabase)
    }
}
