package ai.nextbillion.compose.example.kotlin.repo

import ai.nextbillion.maps.extension.compose.settings.LocationComponentSettings
import ai.nextbillion.maps.location.LocationComponentOptions
import ai.nextbillion.maps.location.modes.CameraMode
import ai.nextbillion.maps.location.modes.RenderMode
import android.content.Context

object LocationTrackingRepository {

    fun initMapLocationComponentSettings(context: Context) : LocationComponentSettings{
        return LocationComponentSettings {
            enable = true
            renderMode = RenderMode.NORMAL
            cameraMode = CameraMode.TRACKING
            options = LocationComponentOptions.builder(context).build()
        }
    }
}