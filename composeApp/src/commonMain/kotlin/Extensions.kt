import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

// Extension functions for Dp
fun Dp.toInt(): Int = value.toInt()
fun Dp.toFloat(): Float = value

// Extension functions for TextUnit (Sp)
fun TextUnit.toInt(): Int = value.toInt()
fun TextUnit.toFloat(): Float = value

// Extension functions for Float
fun Float.toDp(): Dp = this.dp
fun Float.toSp(): TextUnit = this.sp

// Extension functions for Int
fun Int.toDp(): Dp = this.dp
fun Int.toSp(): TextUnit = this.sp

// Helpers to convert Dp to Sp and vice versa using Density
@Composable
fun Dp.toSp(): TextUnit {
    val density = LocalDensity.current
    return with(density) { this@toSp.toSp() }
}

@Composable
fun TextUnit.toDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@toDp.toDp() }
}

fun Color.Companion.random(): Color {
    val red = Random.nextFloat()
    val green = Random.nextFloat()
    val blue = Random.nextFloat()
    return Color(red, green, blue)
}