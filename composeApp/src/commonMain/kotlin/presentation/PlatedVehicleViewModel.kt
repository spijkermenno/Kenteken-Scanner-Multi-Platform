package presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.model.PlatedVehicle
import data.repository.AppPlatedVehicleRepository
import domain.ktor.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ui.extensions.remove

class PlatedVehicleViewModel(
    private val platedVehicleRepository: AppPlatedVehicleRepository
) : ScreenModel {
    private val _event: MutableStateFlow<HomeScreenEvent> =
        MutableStateFlow(HomeScreenEvent.Empty)
    val event by lazy {
        _event.asStateFlow()
    }

    fun getPlatedVehicle(licensePlate: String) {
        _event.value = HomeScreenEvent.Loading

        screenModelScope.launch {
            when (val result = platedVehicleRepository.getPlatedVehicle(
                licensePlate.remove("-").lowercase()
            )) {
                is Result.Error -> {
                    result.exception.printStackTrace()

                    _event.value =
                        HomeScreenEvent.Error(message = "There was a problem while fetch the vehicle.")
                }

                is Result.Success -> {
                    _event.value =
                        HomeScreenEvent.OpenLicensePlateDetails(platedVehicle = result.data)
                }
            }
        }
    }
}

sealed class HomeScreenEvent {
    data class OpenLicensePlateDetails(val platedVehicle: PlatedVehicle) : HomeScreenEvent()
    data object Empty : HomeScreenEvent()
    data object Loading : HomeScreenEvent()
    data class Error(val message: String, val exception: Throwable? = null) : HomeScreenEvent()
}