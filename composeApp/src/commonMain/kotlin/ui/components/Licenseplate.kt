package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import toSp
import ui.StringDefaults
import ui.extensions.WidthFraction
import ui.extensions.fillMaxWidthByFraction

@Composable
fun LicensePlate(
    modifier: Modifier = Modifier,
    text: String = StringDefaults.placeholderLicensePlateText,
    widthFraction: WidthFraction = WidthFraction.LARGE,
    onClick: ((String) -> Unit)? = null
) {
    BoxWithConstraints(
        modifier
            .fillMaxWidthByFraction(widthFraction)
            .aspectRatio(4.73f)
            .clip(MaterialTheme.shapes.large)
            .background(color = MaterialTheme.colors.primary)
            .clickable(onClick != null, onClick = { onClick?.invoke(text) })
    ) {
        val boxWidth = this.maxWidth
        val europeBoxWidth = boxWidth * 0.175f
        val fontSize = (this@BoxWithConstraints.maxHeight * 0.5f).toSp()

        Row {
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(europeBoxWidth)
                    .background(MaterialTheme.colors.secondary)
            )

            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text, fontSize = fontSize, fontWeight = FontWeight.W500)
            }
        }
    }
}