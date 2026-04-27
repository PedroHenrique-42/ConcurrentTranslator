package br.com.pedroferezin.concurrenttranslator.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pedroferezin.concurrenttranslator.api.ConcurrentTranslatorApiClient
import br.com.pedroferezin.concurrenttranslator.ui.viewmodels.states.FetchLanguagesState
import br.com.pedroferezin.concurrenttranslator.ui.viewmodels.states.TranslationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class ConcurrentTranslatorViewModel : ViewModel() {
    private val _translationState = MutableStateFlow<TranslationState>(TranslationState.Empty)
    val translationState = _translationState.asStateFlow()

    private val _fetchLanguagesState =
        MutableStateFlow<FetchLanguagesState>(FetchLanguagesState.Empty)
    val fetchLanguagesState = _fetchLanguagesState.asStateFlow()

    fun translate(text: String, originLanguague: String, destinyLanguague: String) =
        viewModelScope.launch {
            ConcurrentTranslatorApiClient.service.translate(
                text = text,
                originLanguague = originLanguague,
                destinyLanguague = destinyLanguague,
            ).execute().also { response ->
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    _translationState.emit(TranslationState.Error("Ocorreu um erro ao traduzir o texto"))
                    return@launch
                }

                response.body()?.also { translationResult ->
                    _translationState.emit(TranslationState.Success(translationResult))
                }
            }
        }

    fun fetchLanguagues() {
        viewModelScope.launch(Dispatchers.IO) {
            ConcurrentTranslatorApiClient.service.fetchLanguagues().execute().also { response ->
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    _fetchLanguagesState.emit(FetchLanguagesState.Error("Ocorreu um erro ao recuperar os idiomas"))
                    return@launch
                }

                response.body()?.also { languagesList ->
                    if (languagesList.languages.isEmpty()) {
                        _fetchLanguagesState.emit(FetchLanguagesState.Empty)
                    } else {
                        _fetchLanguagesState.emit(FetchLanguagesState.Success(languagesList))
                    }
                }
            }
        }
    }

}