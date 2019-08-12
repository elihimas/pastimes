package com.elihimas.games.pastimes.model

enum class SuggestionType {
    MISSED_A_VICTORY, MISSED_A_DEFEAT, NOTHING_TO_SAY
}

data class Suggestion(
    val suggestionType: SuggestionType,
    val originalPick: TicTacToeCell,
    val textResId: Int = -1,
    val suggestionShownCallback: (() -> Unit)? = null,
    val betterPicks: List<TicTacToeCell> = listOf()
) {
    fun isSuggestionNeeded() = suggestionType != SuggestionType.NOTHING_TO_SAY
}