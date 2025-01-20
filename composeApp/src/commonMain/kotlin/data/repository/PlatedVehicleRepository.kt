package data.repository

import data.model.PlatedVehicle
import domain.ktor.Result
import domain.ktor.client
import domain.ktor.validate
import domain.repository.PlatedVehicleRepository
import io.ktor.client.request.get
import io.ktor.client.request.url

class AppPlatedVehicleRepository : PlatedVehicleRepository {
    override suspend fun getPlatedVehicle(licensePlate: String): Result<PlatedVehicle> {
        try {
            val response = client.get {
                url("api-endpoint/$licensePlate")
            }.validate<PlatedVehicle>()

            return response
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun getRecentPlatedVehicles(): List<PlatedVehicle> {
        TODO("Not yet implemented")
    }
}