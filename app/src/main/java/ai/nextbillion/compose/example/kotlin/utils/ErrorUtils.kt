package ai.nextbillion.compose.example.kotlin.utils

import org.json.JSONException
import org.json.JSONObject

object ErrorUtils {

    fun getErrorMessage(errorBody: String?): String? {
        var jsonObj: JSONObject? = null
        return try {
            jsonObj = JSONObject(errorBody)
            val errorMsg = jsonObj.getString("msg")
            val errorCode = jsonObj.getInt("status")
            "$errorCode:$errorMsg"
        } catch (e: JSONException) {
            ""
        }
    }
}