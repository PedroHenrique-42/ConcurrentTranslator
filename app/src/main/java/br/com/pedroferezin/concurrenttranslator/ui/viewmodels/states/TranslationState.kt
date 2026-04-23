package br.com.pedroferezin.concurrenttranslator.ui.viewmodels.states

import br.com.pedroferezin.concurrenttranslator.domain.TranslationResult

sealed class TranslationState {
    data class Success(val translation: TranslationResult) : TranslationState()
    data class Error(val message: String) : TranslationState()
    data object Empty : TranslationState()
}