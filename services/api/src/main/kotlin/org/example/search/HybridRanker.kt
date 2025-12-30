package org.example.search

data class SemanticHit(
    val id: String,
    val score: Double
)

object HybridRanker {

    // Weights you can tune later
    private const val W_SEMANTIC = 0.60
    private const val W_LEXICAL = 0.40

    fun fuse(
        lexical: List<SearchResult>,
        semantic: List<SemanticHit>
    ): List<SearchResult> {

        // Map semantic scores by note id
        val semanticMap = semantic.associateBy({ it.id }, { it.score })

        return lexical
            .map { lex ->
                val sem = semanticMap[lex.note.id] ?: 0.0

                // lexical score is Int currently; normalize to Double
                val lexScore = lex.score.toDouble()

                val fusedScore = (W_SEMANTIC * sem) + (W_LEXICAL * lexScore)

                val reason = buildString {
                    append(lex.reason.ifBlank { "lexical: no explicit keyword match" })
                    append(" | semantic similarity: ")
                    append(String.format("%.4f", sem))
                    append(" | fused: ")
                    append(String.format("%.4f", fusedScore))
                }

                lex.copy(
                    score = fusedScore.toInt(), // temporary: keep schema compatible if score is Int
                    reason = reason
                )
            }
            .sortedByDescending { it.score }
    }
}
