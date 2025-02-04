package no.uio.ifi.in2000.johastoe.blackjackapp.ui.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.johastoe.blackjackapp.R
import no.uio.ifi.in2000.johastoe.blackjackapp.ui.models.Card


@Composable
    fun CardComponent(cardResId: Int, modifier: Modifier = Modifier) {
        val cardShape = RoundedCornerShape(8.dp)

        Image(
            painter = painterResource(id = cardResId),
            contentDescription = null,
            modifier = modifier
                .padding(16.dp)
                .size(width=80.dp, height = 110.dp)
        )
    }

fun getCardDrawableResId(card: Card): Int {
    return when (card.suit) {
        "clubs" -> when (card.rank) {
            "2" -> R.drawable.c_2
            "3" -> R.drawable.c_3
            "4" -> R.drawable.c_4
            "5" -> R.drawable.c_5
            "6" -> R.drawable.c_6
            "7" -> R.drawable.c_7
            "8" -> R.drawable.c_8
            "9" -> R.drawable.c_9
            "10" -> R.drawable.c_10
            "J" -> R.drawable.c_j
            "Q" -> R.drawable.c_q
            "K" -> R.drawable.c_k
            "A" -> R.drawable.c_a
            else -> R.drawable.back
        }
        "diamonds" -> when (card.rank) {
            "2" -> R.drawable.d_2
            "3" -> R.drawable.d_3
            "4" -> R.drawable.d_4
            "5" -> R.drawable.d_5
            "6" -> R.drawable.d_6
            "7" -> R.drawable.d_7
            "8" -> R.drawable.d_8
            "9" -> R.drawable.d_9
            "10" -> R.drawable.d_10
            "J" -> R.drawable.d_j
            "Q" -> R.drawable.d_q
            "K" -> R.drawable.d_k
            "A" -> R.drawable.d_a
            else -> R.drawable.back
        }
        "hearts" -> when (card.rank) {
            "2" -> R.drawable.h_2
            "3" -> R.drawable.h_3
            "4" -> R.drawable.h_4
            "5" -> R.drawable.h_5
            "6" -> R.drawable.h_6
            "7" -> R.drawable.h_7
            "8" -> R.drawable.h_8
            "9" -> R.drawable.h_9
            "10" -> R.drawable.h_10
            "J" -> R.drawable.h_j
            "Q" -> R.drawable.h_h
            "K" -> R.drawable.h_k
            "A" -> R.drawable.h_a
            else -> R.drawable.back
        }
        "spades" -> when (card.rank) {
            "2" -> R.drawable.s_2
            "3" -> R.drawable.s_3
            "4" -> R.drawable.s_4
            "5" -> R.drawable.s_5
            "6" -> R.drawable.s_6
            "7" -> R.drawable.s_7
            "8" -> R.drawable.s_8
            "9" -> R.drawable.s_9
            "10" -> R.drawable.s_10
            "J" -> R.drawable.s_j
            "Q" -> R.drawable.s_q
            "K" -> R.drawable.s_k
            "A" -> R.drawable.s_a
            else -> R.drawable.back
        }
        else -> R.drawable.back
    }
}
@Preview
@Composable
fun PreviewCardComponent() {
    CardComponent(cardResId = R.drawable.c_10)
}