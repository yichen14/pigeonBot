package pigeon.qqbot

import com.google.gson.Gson
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.queryUrl
import okhttp3.*

data class OCRdata(
        val ParsedResults: PR,
        val OCRExitCode: Int,
        val IsErroredOnProcessing: Boolean,
        var SearchablePDFURL:String
)
{
    data class PR(
            val TextOverlay:String,
            val TextOrientation: String,
            val FileParseExitCode: Int,
            val ParsedText: String,
            val ErrorMessage: String? = null,
            val ErrorDetails:String
    )
}
const val apiKey = "140b4b8ee688957"
const val url = "https://api.ocr.space/parse/image"
const val lang = "chs"

fun Bot.ocr(){
    this.subscribeMessages{
        startsWith("#OCR"){
            if(message[Image] != null) {
                val img = message[Image]
                val md5 = saveImg(img?.queryUrl(),"114514")
                val text = fetchJson("src/img/114514/$md5.jpg")
                reply(text)
            }
            else{
                reply("ERROR")
            }
        }
    }
}

fun fetchJson(imageUrl: String):String{
    val request = Request.Builder()
        .url(url)
        .header("apikey", apiKey)
        .header("language",lang)
        .header("url",imageUrl)
        .build()

    val client = OkHttpClient()

    val response = client.newCall(request).execute().body().toString()
    val gson = Gson()
    val json = gson.fromJson<OCRdata>(response,OCRdata::class.java)

    if (json.ParsedResults.ErrorMessage!=null){
        return json.ParsedResults.ErrorMessage
    }
    else{
        return json.ParsedResults.ParsedText
    }

}
