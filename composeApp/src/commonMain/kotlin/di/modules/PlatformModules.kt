package di.modules

import data.repository.AppPlatedVehicleRepository
import domain.repository.PlatedVehicleRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModules: Module

val commonModule = module {
    factoryOf(::AppPlatedVehicleRepository) bind PlatedVehicleRepository::class
}

fun appModule() = listOf(commonModule)