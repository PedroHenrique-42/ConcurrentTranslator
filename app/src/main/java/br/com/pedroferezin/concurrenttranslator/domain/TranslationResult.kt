package br.com.pedroferezin.concurrenttranslator.domain

data class TranslationResult(
    val `data`: Data
) {
    data class Data(
        val translations: Translations
    ) {
        data class Translations(
            val translatedText: List<String>
        )
    }
}