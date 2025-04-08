package ai.nextbillion.compose.example.kotlin

data class DemoItem(
    val title: String,
    val description: String,
    val action: () -> Unit
)
