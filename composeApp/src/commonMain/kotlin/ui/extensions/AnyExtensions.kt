package ui.extensions

import androidx.compose.runtime.Composable

/**
 * Or else is a method that can be added to a nullable object.
 * It will launch a block of code if the object is null.
 *
 * @param function
 * @receiver
 */
fun Any?.orElse(function: () -> Unit) {
    if (this == null) {
        function.invoke()
    }
}

fun String?.orElse(string: String) = this ?: string


@Composable
fun Any?.orElseComposable(composable: @Composable () -> Unit) {
    if (this == null) {
        composable.invoke()
    }
}