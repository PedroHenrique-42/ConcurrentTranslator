package br.com.pedroferezin.concurrenttranslator.domain

data class TranslationRequest(
    val q: String,
    val source: String,
    val target: String
)