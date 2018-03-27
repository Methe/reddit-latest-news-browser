package br.com.matheus.reddit

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout

class TestActivity : AppCompatActivity() {

    private lateinit var content: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = FrameLayout(this)
        content.setBackgroundColor(Color.WHITE)
        content.id = android.R.id.content
        setContentView(content)
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, "TAG").commit()
    }

    fun setView(view: View) = runOnUiThread {
        content.addView(view)
    }
}
