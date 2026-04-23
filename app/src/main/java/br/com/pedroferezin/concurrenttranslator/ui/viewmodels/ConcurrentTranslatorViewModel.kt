package br.com.pedroferezin.concurrenttranslator.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.pedroferezin.concurrenttranslator.ui.viewmodels.states.FetchLanguagesState
import br.com.pedroferezin.concurrenttranslator.ui.viewmodels.states.TranslationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConcurrentTranslatorViewModel : ViewModel() {
    private val _translateState = MutableStateFlow<TranslationState>(TranslationState.Empty)
    val translateState = _translateState.asStateFlow()

    private val _fetchLanguagesState =
        MutableStateFlow<FetchLanguagesState>(FetchLanguagesState.Empty)
    val fetchLanguagesState = _fetchLanguagesState.asStateFlow()
}