package ui.extensions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier

enum class WidthFraction(val widthFraction: Float) {
    SMALL(0.5f), MEDIUM(0.8f), LARGE(1f)
}

fun Modifier.fillMaxWidthByFraction(widthFraction: WidthFraction) =
    this.fillMaxWidth(widthFraction.widthFraction)

fun Modifier.optional(condition: Boolean, conditionalModifier: Modifier): Modifier {
    return if (!condition) {
        this
    } else {
        this.then(conditionalModifier)
    }
}