package domain.repository

import data.model.PlatedVehicle
import domain.ktor.Result

interface PlatedVehicleRepository {
    suspend fun getPlatedVehicle(licensePlate: String): Result<PlatedVehicle>
    suspend fun getRecentPlatedVehicles(): List<PlatedVehicle>
}