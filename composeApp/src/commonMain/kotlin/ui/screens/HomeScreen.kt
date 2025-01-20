package ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import domain.ktor.client
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import presentation.HomeScreenEvent
import presentation.PlatedVehicleViewModel
import ui.StringDefaults
import ui.components.LicensePlateTextField
import ui.theme.Corner
import ui.theme.Spacing

class HomeScreen(private val platedVehicleViewModel: PlatedVehicleViewModel) : Screen {

    init {
        //platedVehicleViewModel.getPlatedVehicle("3tkh03")
    }

    private suspend fun testInternetConnection(): Exception? {
        try {
            client.get("https://google.com/")
            return null
        } catch (e: Exception) {
            return Exception("There is something wrong with your internet connection.")
        }
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val event = platedVehicleViewModel.event.collectAsState()
        val focusManager = LocalFocusManager.current
        val scope = rememberCoroutineScope()

        val snackBarHostState = remember { SnackbarHostState() }

        scope.launch {
            testInternetConnection()?.let {
                snackBarHostState.showSnackbar(it.message ?: "Something went wrong.")
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) {
            BottomSheetNavigator(
                sheetShape = Corner.Large,
                hideOnBackPress = true
            ) { navigator ->
                when (event.value) {
                    HomeScreenEvent.Empty -> Unit // Empty state is default.
                    is HomeScreenEvent.Error -> {
                        scope.launch {
                            snackBarHostState.showSnackbar((event.value as HomeScreenEvent.Error).message)
                        }
                    }

                    HomeScreenEvent.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is HomeScreenEvent.OpenLicensePlateDetails -> {
                        navigator.show(LicensePlateDetailsScreen((event.value as HomeScreenEvent.OpenLicensePlateDetails).platedVehicle))
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = Spacing.Small.dp)
                        .padding(horizontal = Spacing.Large.dp)
                        .combinedClickable(
                            onClick = { focusManager.clearFocus(true) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Small.dp)
                ) {
                    Spacer(Modifier.height(Spacing.Small.dp))

                    LicensePlateSearchField {
                        focusManager.clearFocus(true)
                        platedVehicleViewModel.getPlatedVehicle(it)
                    }

                    Spacer(Modifier.height(Spacing.Small.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp).clip(CircleShape).clickable {
                                navigator.show(
                                    RecentSearchedLicensePlatesScreen {
                                        platedVehicleViewModel.getPlatedVehicle(it)
                                    }
                                )
                            },
                            imageVector = Icons.Default.History,
                            contentDescription = "Search history",
                            tint = Color(0xff5555ff)
                        )

                        Icon(
                            modifier = Modifier.size(32.dp).clip(CircleShape).clickable { },
                            imageVector = Icons.Outlined.StarOutline,
                            contentDescription = "Favorites",
                            tint = Color(0xffFFD700)
                        )

                        Icon(
                            modifier = Modifier.size(32.dp).clip(CircleShape).clickable { },
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "License plates with a notification",
                            tint = Color(0xffff5555)
                        )

                        Icon(
                            modifier = Modifier.size(32.dp).clip(CircleShape).clickable { },
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = "Scan License plate with camera",
                            tint = Color(0xff5555ff)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LicensePlateSearchField(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onValidLicensePlate: (String) -> Unit
) {
    Box(modifier = modifier) {
        LicensePlateTextField(
            placeholderText = StringDefaults.placeholderLicensePlateText,
            isEnabled = isEnabled,
            onValidLicensePlate = onValidLicensePlate
        )
    }
}