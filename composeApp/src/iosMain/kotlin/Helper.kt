import di.modules.appModule
import di.modules.platformModules
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule() + platformModules)
    }
}