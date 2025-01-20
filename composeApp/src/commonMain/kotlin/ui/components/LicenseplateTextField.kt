package ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import data.model.PlatedVehicle
import kentekenscanner.composeapp.generated.resources.European_stars
import kentekenscanner.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import toSp
import ui.extensions.transparent

@Composable
fun LicensePlateTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    placeholderText: String = "",
    isEnabled: Boolean = true,
    onValidLicensePlate: (String) -> Unit
) {
    BoxWithConstraints(
        Modifier
            .fillMaxWidth()
            .aspectRatio(4.73f)
            .clip(MaterialTheme.shapes.large)
            .background(color = MaterialTheme.colors.onPrimary)
    ) {
        var licensePlateText by remember { mutableStateOf(value) }
        val boxWidth = this.maxWidth
        val europeBoxWidth = boxWidth * 0.175f

        Row {
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(europeBoxWidth)
                    .background(MaterialTheme.colors.secondary),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.European_stars),
                    contentDescription = "Europe stars",
                    modifier = Modifier.fillMaxSize(0.8f)
                )
            }

            val fontSize = (this@BoxWithConstraints.maxHeight * 0.5f).toSp()
            val lineHeight = (this@BoxWithConstraints.maxHeight).toSp()

            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = licensePlateText,
                    enabled = isEnabled,
                    onValueChange = {
                        if (PlatedVehicle.getSideCode(it) != -2) {
                            licensePlateText = PlatedVehicle.getFormattedLicensePlate(it)
                            onValidLicensePlate(licensePlateText)
                        } else {
                            licensePlateText = it.uppercase()
                        }
                    },
                    placeholder = {
                        Text(
                            text = placeholderText,
                            modifier = Modifier.fillMaxSize(),
                            fontSize = fontSize,
                            textAlign = TextAlign.Center,
                            lineHeight = 2.sp,
                            color = MaterialTheme.colors.primaryVariant
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = fontSize,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center,
                        lineHeight = lineHeight,
                        letterSpacing = 2.sp
                    ),
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.transparent()
                )
            }
        }
    }
}