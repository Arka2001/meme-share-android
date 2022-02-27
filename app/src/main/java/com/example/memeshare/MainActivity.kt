package com.example.memeshare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shareMemeButton : Button = findViewById(R.id.shareButton)
        val nextMemeButton : Button = findViewById(R.id.nextButton)

        loadMeme()

        shareMemeButton.setOnClickListener {
            shareMeme(it)
        }

        nextMemeButton.setOnClickListener {
            nextMeme(it)
        }
    }

    private fun loadMeme() {
        val progressBar : ProgressBar = findViewById(R.id.memeImageProgressBar)
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        val stringRequest = JsonObjectRequest(
            url,
            { response ->
                //DO Something
                imageUrl = response.getString("url")
                Glide.with(this).load(imageUrl).into(findViewById(R.id.memeImageView))
                progressBar.visibility = View.INVISIBLE
            },
            {
                Log.d("Error Message", "Unexpected Error Occurred")
            }

        )

// Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    private fun nextMeme(view: View) {
        loadMeme()
    }

    private fun shareMeme(view: View) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hey, check out this cool meme I got fron Reddit: $imageUrl")
            type="text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share this meme using...")
        startActivity(shareIntent)
    }
}