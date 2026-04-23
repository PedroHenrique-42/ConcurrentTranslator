package br.com.pedroferezin.concurrenttranslator.ui.viewmodels.states

import br.com.pedroferezin.concurrenttranslator.domain.LanguagesList

sealed class FetchLanguagesState {
    data class Success(val languages: LanguagesList) : FetchLanguagesState()
    data class Error(val message: String) : FetchLanguagesState()
    data object Empty : FetchLanguagesState()
}