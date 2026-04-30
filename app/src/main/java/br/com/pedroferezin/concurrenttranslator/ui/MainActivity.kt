package br.com.pedroferezin.concurrenttranslator.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.pedroferezin.concurrenttranslator.R
import br.com.pedroferezin.concurrenttranslator.databinding.ActivityMainBinding
import br.com.pedroferezin.concurrenttranslator.ui.viewmodels.ConcurrentTranslatorViewModel
import br.com.pedroferezin.concurrenttranslator.ui.viewmodels.states.FetchLanguagesState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val concurrentTranslatorViewModel: ConcurrentTranslatorViewModel by viewModels()

    private var selectedOriginLanguage: String = ""
    private var selectedDestinyLanguage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.mainTb.apply { title = getString(R.string.app_name) })

        val languagesAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())

        listenFetchState(languagesAdapter)
        configureViewListeners(languagesAdapter)

        concurrentTranslatorViewModel.fetchLanguagues()
    }

    private fun configureViewListeners(adapter: ArrayAdapter<String>) {
        with(amb) {
            originLanguageAc.apply {
                setAdapter(adapter)
                setOnItemClickListener { _, _, _, _ ->
                    selectedOriginLanguage = text.toString()
                }
            }

            destinyLanguageAc.apply {
                setAdapter(adapter)
                setOnItemClickListener { _, _, _, _ ->
                    selectedDestinyLanguage = text.toString()
                }
            }

            translateBt.setOnClickListener {
                concurrentTranslatorViewModel.translate(
                    originLanguageEt.text.toString(),
                    selectedOriginLanguage,
                    selectedDestinyLanguage
                )
            }
        }
    }

    fun listenFetchState(adapter: ArrayAdapter<String>) {
        lifecycleScope.launch {
            concurrentTranslatorViewModel.fetchLanguagesState.collect { state ->
                when (state) {
                    is FetchLanguagesState.Empty -> {
                        adapter.clear()
                    }

                    is FetchLanguagesState.Success -> {
                        adapter.clear()
                        adapter.addAll(state.languages.languages.map { language -> language.language }
                            .sorted())

                        adapter.getItem(0)?.also { language ->
                            amb.originLanguageAc.setText(language, false)
                            selectedOriginLanguage = language
                        }

                        adapter.getItem(adapter.count - 1)?.also { language ->
                            amb.destinyLanguageAc.setText(language, false)
                            selectedDestinyLanguage = language
                        }
                    }

                    is FetchLanguagesState.Error -> {
                        adapter.clear()
                        Snackbar.make(
                            amb.root,
                            state.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}