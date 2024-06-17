package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import data.model.PlatedVehicle
import presentation.HomeScreenEvent
import presentation.PlatedVehicleViewModel
import ui.components.DefaultButton
import ui.components.LicensePlate

class TestScreen(private val platedVehicleViewModel: PlatedVehicleViewModel) : Screen {

    @Composable
    override fun Content() {
        val uiState by platedVehicleViewModel.event.collectAsState()

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    DefaultButton(
                        modifier = Modifier,
                        onClick = { platedVehicleViewModel.getPlatedVehicle("3tkh03") }
                    ) {
                        Text("Fetch Vehicle")
                    }

                    DefaultButton(
                        modifier = Modifier,
                        onClick = { platedVehicleViewModel.getPlatedVehicle("00000000") }
                    ) {
                        Text("Test error state")
                    }
                }
            }

            when (uiState) {
                is HomeScreenEvent.Empty -> Unit
                is HomeScreenEvent.OpenLicensePlateDetails -> {
                    item {
                        LicensePlate(
                            text =
                            PlatedVehicle.getFormattedLicensePlate((uiState as HomeScreenEvent.OpenLicensePlateDetails).platedVehicle.kenteken)
                        )
                    }
                }

                is HomeScreenEvent.Error -> {
                    item {
                        Snackbar() {
                            Text((uiState as HomeScreenEvent.Error).message)
                        }
                    }
                }

                HomeScreenEvent.Loading -> {
                    item {
                        Box(Modifier.fillMaxSize()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }
    }
}