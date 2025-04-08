package ai.nextbillion.compose.example.kotlin.model
class TrafficModel constructor(
    private val name: String,
    private val icon: Int,
    private val traffic: String,
) {

    fun getName(): String = name
    fun getIcon(): Int = icon
    fun getTraffic(): String = traffic
}