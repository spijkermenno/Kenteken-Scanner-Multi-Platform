
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import ui.screens.HomeScreen
import ui.theme.AppTheme

@Composable
@Preview
fun App() {

    KoinContext {
        AppTheme {
            Navigator(HomeScreen(koinInject()))
        }
    }
}