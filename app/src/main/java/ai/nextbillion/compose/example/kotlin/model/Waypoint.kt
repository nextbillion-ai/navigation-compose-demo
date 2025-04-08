package ai.nextbillion.compose.example.kotlin.model

import ai.nextbillion.kits.geojson.Point
import android.text.TextUtils
import java.io.Serializable
class Waypoint(
    private var name: String = "",
    private var point: Point
) : Cloneable, Serializable {

    init {
        this.name =
            if (TextUtils.isEmpty(this.name))
                "${this.point.latitude()},${this.point.longitude()}"
            else
                this.name
    }

    fun getName(): String {
        return name
    }

    fun getPoint(): Point {
        return point
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setPoint(point: Point) {
        this.point = point
    }

    fun isAvailablePoint(): Boolean {
        return this.point.latitude() != 0.0 && this.point.longitude() != 0.0
    }

    override fun clone(): Any {
        return super.clone()
    }
}