package di.modules

import org.koin.dsl.module
import presentation.PlatedVehicleViewModel

actual val platformModules = module {
    factory {
        PlatedVehicleViewModel(get())
    }
}