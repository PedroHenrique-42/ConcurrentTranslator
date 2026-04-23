package br.com.pedroferezin.concurrenttranslator.domain

data class LanguagesList(
    val languages: List<Language>
) {
    data class Language(
        val language: String,
        val name: String
    )
}