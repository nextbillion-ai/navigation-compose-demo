package ai.nextbillion.compose.example.kotlin

import androidx.compose.ui.graphics.Color

object AppColor {
    // Primary Colors
    object Primary {
        val Main = Color(0xFF7588E9)
        val Dark = Color(0xFF5861AD)
        val Light = Color(0xFFBB86FC)
    }

    // Secondary Colors
    object Secondary {
        val Main = Color(0xFF03DAC5)
        val Dark = Color(0xFF018786)
        val Light = Color(0xFF03DAC5)
    }

    // Background Colors
    object Background {
        val Primary = Color(0xFFFFFFFF)
        val Secondary = Color(0xFFF5F5F5)
        val Dark = Color(0xFF121212)
    }

    // Text Colors
    object Text {
        val Primary = Color(0xFF000000)
        val Secondary = Color(0xFF666666)
        val Light = Color(0xFFFFFFFF)
    }

    // Status Colors
    object Status {
        val Success = Color(0xFF4CAF50)
        val Error = Color(0xFFB00020)
        val Warning = Color(0xFFFFC107)
        val Info = Color(0xFF2196F3)
    }

    // Navigation Colors
    object Navigation {
        val Route = Color(0xFF2196F3)
        val RouteAlternate = Color(0xFF4CAF50)
        val CurrentLocation = Color(0xFFE91E63)
        val Destination = Color(0xFFF44336)
    }
} 