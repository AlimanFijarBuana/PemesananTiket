package com.azhar.pemesanantiket.view.history

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azhar.pemesanantiket.R
import com.azhar.pemesanantiket.model.ModelDatabase
import com.azhar.pemesanantiket.viewmodel.HistoryViewModel
import com.azhar.pemesanantiket.view.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity(), HistoryAdapter.HistoryItemClickListener {

    private lateinit var modelDatabaseList: MutableList<ModelDatabase>
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setStatusBar()
        setToolbar()
        setRecyclerView()
        initViewModel()
        setUpItemTouchHelper()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setRecyclerView() {
        modelDatabaseList = mutableListOf()
        historyAdapter = HistoryAdapter(modelDatabaseList)

        rvHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
        }

        historyAdapter.setOnItemClickListener { modelDatabase ->
            // Menavigasi ke DetailActivity dengan membawa data ModelDatabase
            val intent = Intent(this@HistoryActivity, DetailActivity::class.java)
            intent.putExtra("MODEL_DATABASE", modelDatabase)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        historyViewModel.dataList.observe(this, { modelDatabases ->
            if (modelDatabases.isEmpty()) {
                tvNotFound.visibility = View.VISIBLE
                rvHistory.visibility = View.GONE
            } else {
                tvNotFound.visibility = View.GONE
                rvHistory.visibility = View.VISIBLE
            }
            historyAdapter.setDataAdapter(modelDatabases)
        })
    }

    private fun setUpItemTouchHelper() {
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.START or ItemTouchHelper.END
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val swipedPosition = viewHolder.adapterPosition
                val modelDatabase = historyAdapter.setSwipeRemove(swipedPosition)
                val uid = modelDatabase.uid
                historyViewModel.deleteDataById(uid)
                Toast.makeText(
                    this@HistoryActivity,
                    "Data yang dipilih sudah dihapus",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rvHistory)
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val window = window
        val layoutParams = window.attributes
        if (on) {
            layoutParams.flags = layoutParams.flags or bits
        } else {
            layoutParams.flags = layoutParams.flags and bits.inv()
        }
        window.attributes = layoutParams
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(modelDatabase: ModelDatabase) {
        // Penanganan klik item sekarang dilakukan di adapter
    }
}
