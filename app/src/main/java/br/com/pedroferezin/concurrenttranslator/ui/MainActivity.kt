package br.com.pedroferezin.concurrenttranslator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.pedroferezin.concurrenttranslator.R
import br.com.pedroferezin.concurrenttranslator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.mainTb.apply { title = getString(R.string.app_name) })
    }
}