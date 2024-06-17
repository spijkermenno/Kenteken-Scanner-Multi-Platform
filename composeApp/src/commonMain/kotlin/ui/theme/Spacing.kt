package ui.theme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class Spacing(val dp: Dp) {
    ExtraSmall(4.dp),
    Small(8.dp),
    Medium(16.dp),
    Large(24.dp),
    ExtraLarge(32.dp)
}

@Composable
fun Spacer(modifier: Modifier = Modifier, spacing: Spacing) {
    Spacer(modifier = modifier.size(spacing.dp))
}

@Composable
fun SpacerXS(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.size(Spacing.ExtraSmall.dp))
}

@Composable
fun SpacerS(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.size(Spacing.Small.dp))
}

@Composable
fun SpacerM(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.size(Spacing.Medium.dp))
}

@Composable
fun SpacerL(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.size(Spacing.Large.dp))
}

@Composable
fun SpacerXL(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.size(Spacing.ExtraLarge.dp))
}