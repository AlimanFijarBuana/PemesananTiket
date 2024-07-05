package com.azhar.pemesanantiket.view.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azhar.pemesanantiket.R
import com.azhar.pemesanantiket.model.ModelDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail.*
import com.azhar.pemesanantiket.utils.FunctionHelper.rupiahFormat

class DetailActivity : AppCompatActivity() {

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    private var isImageUploaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Mengambil data dari intent
        val modelDatabase = intent.getParcelableExtra<ModelDatabase>("MODEL_DATABASE")

        // Menampilkan data ke layout
        modelDatabase?.let {
            tvKode1.text = getKodeBandara(it.keberangkatan)
            tvKode2.text = getKodeBandara(it.tujuan)
            tvKelas.text = it.kelas
            tvDate.text = it.tanggal
            tvNama.text = it.namaPenumpang
            tvKeberangkatan.text = it.keberangkatan
            tvTujuan.text = it.tujuan
            tvHargaTiket.text = rupiahFormat(it.hargaTiket)
            tvAnakAnak.text = it.anakAnak.toString()
            tvDewasa.text = it.dewasa.toString()
            tvNomorTelepon.text = it.nomorTelepon
            tvstatus.text = "Penumpang "+ it.status
        }

        // Inisialisasi tombol untuk upload bukti bayar
        val btnUploadBuktiBayar: Button = findViewById(R.id.btnUploadBuktiBayar)
        btnUploadBuktiBayar.setOnClickListener {
            if (!isImageUploaded) {
                // Panggil metode untuk melakukan upload bukti bayar ke Firebase atau proses lainnya
                openFileChooser()
            } else {
                Toast.makeText(this, "Anda sudah mengunggah gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getKodeBandara(namaBandara: String): String {
        return when (namaBandara) {
            "Jakarta" -> "JKT"
            "Semarang" -> "SRG"
            "Surabaya" -> "SUB"
            "Bali" -> "DPS"
            else -> ""
        }
    }

    private fun openFileChooser() {
        // Implementasi untuk membuka file chooser atau galeri foto
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            // Panggil metode untuk upload gambar ke Firebase atau tempat penyimpanan lainnya
            uploadImageToFirebase(imageUri)
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        if (imageUri == null) {
            Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val modelDatabase = intent.getParcelableExtra<ModelDatabase>("MODEL_DATABASE")
        val namaPenumpang = modelDatabase?.namaPenumpang ?: "unknown"

        val fileName = "${namaPenumpang}_${System.currentTimeMillis()}.${getFileExtension(imageUri)}"
        val fileReference = FirebaseStorage.getInstance().getReference("uploads")
            .child(fileName)

        fileReference.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Mendapatkan URL download setelah berhasil diunggah
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    Toast.makeText(this, "Upload berhasil", Toast.LENGTH_SHORT).show()
                    // Lanjutkan dengan penggunaan URL download jika diperlukan

                    // Set status unggah gambar ke true
                    isImageUploaded = true

                    // Atur tombol unggah gambar menjadi tidak aktif
                    findViewById<Button>(R.id.btnUploadBuktiBayar).isEnabled = false
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Upload gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    // Metode lain yang mungkin Anda perlukan dalam aktivitas ini
}
