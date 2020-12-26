package com.example.hackernewsapp.core.webview

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hackernewsapp.R
import com.example.hackernewsapp.core.WEBVIEW_URL_EXTRA
import kotlinx.android.synthetic.main.activity_webview.*

class WebviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val url = retrieveUrl()
        if (url.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.url_error), Toast.LENGTH_LONG).show()
            onBackPressed()
        } else {
            webView.loadUrl(url)

        }
    }

    private fun retrieveUrl(): String? {
        return intent.getStringExtra(WEBVIEW_URL_EXTRA)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}