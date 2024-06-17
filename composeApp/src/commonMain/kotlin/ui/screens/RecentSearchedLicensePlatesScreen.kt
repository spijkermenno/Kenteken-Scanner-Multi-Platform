package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import ui.components.BottomSheetContent
import ui.components.LicensePlate
import ui.theme.Spacing

class RecentSearchedLicensePlatesScreen(private val onLicensePlateClick: (String) -> Unit) :
    Screen {
    private val licensePlateList = listOf(
        "3-TKH-03",
        "79-RKN-7",
        "31-SL-DL",
        "X-TKH-03",
    )

    @Composable
    override fun Content() {
        val navigator = LocalBottomSheetNavigator.current

        BottomSheetContent(
            modifier = Modifier,
            showDragHandle = true,
            bottomSheetNavigator = navigator,
            content = { DetailsContent(modifier = it, navigator = navigator) }
        )
    }

    @Composable
    private fun DetailsContent(modifier: Modifier, navigator: BottomSheetNavigator) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(Spacing.Small.dp)
        ) {
            items(licensePlateList) { licensePlate ->
                LicensePlate(text = licensePlate) {
                    navigator.hide()
                    onLicensePlateClick.invoke(licensePlate)
                }
            }
        }
    }
}