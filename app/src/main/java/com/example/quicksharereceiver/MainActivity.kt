package com.example.quicksharereceiver

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import coil.load
import com.example.quicksharereceiver.contentReceiver.CustomContentReceiver

class MainActivity : AppCompatActivity() {

    lateinit var uriOfficial: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val deleteImage = findViewById<ImageView>(R.id.deleteImage)
        val shareImage = findViewById<ImageView>(R.id.shareImage)

        /**
         * verification of Android OS version
         */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            findViewById<TextView>(R.id.serviceNA).visibility = View.GONE
        } else {
            // if the version isn't the API 31 or later
            // there a possibility to the application will not use AndroidX 1.5 or later
            showNonAvailable()
            findViewById<TextView>(R.id.serviceNA).visibility = View.VISIBLE
        }

        val contentReceiver = CustomContentReceiver { uri ->
            // load image using provided URI
            val mimeType = contentResolver.getType(uri)
            uriOfficial = uri
            deleteImage.visibility = View.VISIBLE
            shareImage.visibility = View.VISIBLE

            // in case of an image resource
            when {
                mimeType?.contains("image/") == true -> {
                    val imageView = findViewById<ImageView>(R.id.imageView)
                    imageView.apply {
                        isVisible = true
                        load(uri)
                        sendWhatsapp(uri)
                        deleteImage.visibility = View.VISIBLE
                        shareImage.visibility = View.VISIBLE
                    }
                }
            }
        }

        ViewCompat.setOnReceiveContentListener(
            findViewById(R.id.editTextTextPersonName),
            arrayOf("image/*", "video/*"), // types that can be supported
            contentReceiver
        )

        shareImage.setOnClickListener {
            sendWhatsapp(uriOfficial)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun sendWhatsapp(uri: Uri) {
        val imgUri = Uri.parse(uri.toString())
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share")
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri)
        whatsappIntent.type = "image/jpeg"
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            Log.d("NOTT", "Not allowed")
        }
    }

    private fun showNonAvailable() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.error_popup_dialog)
        var textView = dialog.findViewById<TextView>(R.id.error_description)
        var textViewTitle = dialog.findViewById<TextView>(R.id.title)
        var okTV = dialog.findViewById<TextView>(R.id.okBtn)
        okTV.setText("OK")
        okTV.setOnClickListener {
            dialog.dismiss()
        }
        val width = resources.displayMetrics.widthPixels * 0.90
        val height = resources.displayMetrics.heightPixels * 0.40
        textView.text = "Ce service est compatible avec AndroidX Core 1.5.0-beta1 ou plus et Appcompat 1.3.0-beta-01 ou plus"
        textViewTitle.text = "Service incompatible"
        dialog.window?.setLayout(width.toInt(), height.toInt())
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}
