package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import ui.extensions.optional
import ui.theme.Spacing

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    bottomSheetNavigator: BottomSheetNavigator,
    showDragHandle: Boolean = false,
    hasContentPadding: Boolean = true,
    header: (@Composable (Modifier) -> Unit)? = null,
    content: @Composable (Modifier) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight(0.9f)
    ) {
        if (showDragHandle) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp) // Material 3 default padding for bottom sheet drag handle.
            ) {
                Divider(
                    modifier = Modifier
                        .width(35.dp)
                        .height(4.dp)
                        .background(Color.DarkGray.copy(alpha = 0.33f))
                        .align(Alignment.Center)
                        .clickable { bottomSheetNavigator.hide() }
                )
            }
        } else {
            Spacer(modifier = Modifier.size(Spacing.Small.dp))
        }

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(Spacing.Small.dp)
        ) {
            header?.invoke(Modifier.padding(horizontal = Spacing.Medium.dp))

            content(
                Modifier.optional(
                    hasContentPadding,
                    Modifier.padding(horizontal = Spacing.Medium.dp)
                )
            )
        }
    }
}