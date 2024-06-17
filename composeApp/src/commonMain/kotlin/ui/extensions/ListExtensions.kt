package ui.extensions

fun List<Any>.hasMultiple(unit: (() -> Unit)? = null): Boolean {
    return if (this.size > 1) {
        unit?.invoke()
        true
    } else false
}