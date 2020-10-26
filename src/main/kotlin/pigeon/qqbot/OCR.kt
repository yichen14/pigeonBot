package pigeon.qqbot
import com.beust.klaxon.JsonValue
import okhttp3.*
import java.io.IOException

class OCR {
    private val apiKey = "140b4b8ee688957"
    private val url = "https://api.ocr.space/parse/image"
    val request = Request.Builder()
        .url(url)
        .header("apikey", apiKey)
        .header("language","chs")
        .build()

    val client = OkHttpClient()


    private fun fetchJson(){
        client.newCall(request)
    }
}