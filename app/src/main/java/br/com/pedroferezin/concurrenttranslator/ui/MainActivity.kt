package br.com.pedroferezin.concurrenttranslator.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.pedroferezin.concurrenttranslator.R
import br.com.pedroferezin.concurrenttranslator.databinding.ActivityMainBinding
import br.com.pedroferezin.concurrenttranslator.domain.LanguagesList
import br.com.pedroferezin.concurrenttranslator.ui.viewmodels.ConcurrentTranslatorViewModel
import br.com.pedroferezin.concurrenttranslator.ui.viewmodels.states.FetchLanguagesState
import br.com.pedroferezin.concurrenttranslator.ui.viewmodels.states.TranslationState
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
            ArrayAdapter<LanguagesList.Language>(
                this,
                android.R.layout.simple_list_item_1,
                mutableListOf()
            )

        listenFetchState(languagesAdapter)
        listenTranslateState()
        configureViewListeners(languagesAdapter)

        concurrentTranslatorViewModel.fetchLanguages()
    }

    private fun configureViewListeners(adapter: ArrayAdapter<LanguagesList.Language>) {
        with(amb) {
            originLanguageAc.apply {
                setAdapter(adapter)
                setOnItemClickListener { _, _, position, _ ->
                    val language = adapter.getItem(position)
                    selectedOriginLanguage = language?.language ?: ""

                    originLanguageAcTil.error = null
                    originLanguageAcTil.isErrorEnabled = false
                }
            }

            destinyLanguageAc.apply {
                setAdapter(adapter)
                setOnItemClickListener { _, _, position, _ ->
                    val language = adapter.getItem(position)
                    selectedDestinyLanguage = language?.language ?: ""

                    destinyLanguageAcTil.error = null
                    destinyLanguageAcTil.isErrorEnabled = false
                }
            }

            translateBt.setOnClickListener {
                if (validateOriginLanguageText() && validateOriginLanguage() && validateDestinyLanguage()) {
                    concurrentTranslatorViewModel.translate(
                        originLanguageEt.text.toString(),
                        selectedOriginLanguage,
                        selectedDestinyLanguage
                    )
                }
            }
        }
    }

    fun listenFetchState(adapter: ArrayAdapter<LanguagesList.Language>) {
        lifecycleScope.launch {
            concurrentTranslatorViewModel.fetchLanguagesState.collect { state ->
                when (state) {
                    is FetchLanguagesState.Empty -> {
                        adapter.clear()
                    }

                    is FetchLanguagesState.Success -> {
                        adapter.clear()
                        val sortedLanguages = state.languages.languages.sortedBy { it.name }
                        adapter.addAll(sortedLanguages)

                        sortedLanguages.find { language -> language.language == "en" }
                            ?.also { language ->
                                amb.originLanguageAc.setText(language.name, false)
                                selectedOriginLanguage = language.language
                            }

                        sortedLanguages.find { language -> language.language == "pt" }
                            ?.also { language ->
                                amb.destinyLanguageAc.setText(language.name, false)
                                selectedDestinyLanguage = language.language
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

    fun listenTranslateState() {
        lifecycleScope.launch {
            concurrentTranslatorViewModel.translationState.collect { state ->
                when (state) {
                    is TranslationState.Empty -> {
                        amb.loadingPb.visibility = View.GONE
                    }

                    is TranslationState.Loading -> {
                        amb.loadingPb.visibility = View.VISIBLE
                    }

                    is TranslationState.Success -> {
                        amb.loadingPb.visibility = View.GONE

                        amb.translationTiet.setText(
                            state.translation.data.translations.translatedText.firstOrNull() ?: ""
                        )
                    }

                    is TranslationState.Error -> {
                        amb.loadingPb.visibility = View.GONE

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

    private fun validateOriginLanguageText(): Boolean = with(amb) {
        if (originLanguageEt.text.toString().isEmpty()) {
            originLanguageEtTil.error = getString(R.string.type_text_error)
            originLanguageEtTil.requestFocus()
            false
        } else {
            originLanguageEtTil.isErrorEnabled = false
            true
        }
    }

    private fun validateOriginLanguage(): Boolean = with(amb) {
        if (originLanguageAc.text.toString().isEmpty()) {
            originLanguageAcTil.error = getString(R.string.error_select_language)
            originLanguageAcTil.requestFocus()
            false
        } else {
            originLanguageAcTil.isErrorEnabled = false
            true
        }
    }

    private fun validateDestinyLanguage(): Boolean = with(amb) {
        if (destinyLanguageAc.text.toString().isEmpty()) {
            destinyLanguageAcTil.error = getString(R.string.error_select_language)
            destinyLanguageAcTil.requestFocus()
            false
        } else {
            destinyLanguageAcTil.isErrorEnabled = false
            true
        }
    }
}
