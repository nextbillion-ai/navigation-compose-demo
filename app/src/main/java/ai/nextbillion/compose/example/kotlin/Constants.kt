package ai.nextbillion.compose.example.kotlin

object Constants {
    // Shared Preference Keys
    const val KEY_SHARE_PREFERENCE = "KEY_SHARE_PREFERENCE"

    // Route Avoid Options
    object AvoidOptions {
        const val HIGHWAY = "AVOID_HIGHWAY"
        const val TOLLS = "AVOID_TOLLS"
        const val FERRIES = "AVOID_FERRIES"
        const val U_TURN = "AVOID_U_TURN"
        const val SHARP_TURN = "AVOID_SHARP_TURN"
        const val COUNTRY_BOARDER = "AVOID_COUNTRY_BOARDER"
        const val SERVICE_ROAD = "AVOID_SERVICE_ROAD"
        const val HAZMAT_TYPE = "AVOID_HAZMAT_TYPE"
    }

    // Navigation Settings
    object NavigationSettings {
        const val SIMULATION = "KEY_SIMULATION"
        const val FLEXIBLE = "KEY_FLEXIBLE"
        const val DISTANCE_UNIT = "KEY_DISTANCE_UNIT"
        const val LANGUAGE = "KEY_LANGUAGE"

        object DistanceUnit {
            const val AUTO = "Auto"
            const val METRIC = "Metric"
            const val IMPERIAL = "Imperial"
        }
    }
} 