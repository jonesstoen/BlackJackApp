package no.uio.ifi.in2000.johastoe.blackjackapp.Utils



import android.content.Context
import android.content.SharedPreferences

class HighScoreManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)

    fun getHighScore(): Pair<Int, String?> {
        val score = prefs.getInt("high_score", 0)
        val name = prefs.getString("high_score_name", null)
        return Pair(score, name)
    }

    fun setHighScore(score: Int, name: String) {
        val editor = prefs.edit()
        editor.putInt("high_score", score)
        editor.putString("high_score_name", name)
        val previousScores = getPreviousHighScores().toMutableList()
        previousScores.add(Pair(score, name))
        editor.putStringSet("previous_high_scores", previousScores.map { "${it.first}:${it.second}" }.toSet())
        editor.apply()
    }

    fun getPreviousHighScores(): List<Pair<Int, String?>> {
        val scores = prefs.getStringSet("previous_high_scores", emptySet()) ?: emptySet()
        return scores.map {
            val parts = it.split(":")
            Pair(parts[0].toInt(), parts.getOrNull(1))
        }
    }

    fun resetHighScores() {
        val editor = prefs.edit()
        editor.putInt("high_score", 0)
        editor.putString("high_score_name", null)
        editor.putStringSet("previous_high_scores", emptySet())
        editor.apply()
    }
}