package pigeon.qqbot

import com.beust.klaxon.Klaxon
import kotlinx.coroutines.delay
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.queryUrl
import okhttp3.*
import java.io.File
import java.util.Base64

data class OCRdata(
    val ParsedResults: List<PR>,
    val OCRExitCode: Int,
    val IsErroredOnProcessing: Boolean,
    val SearchablePDFURL:String,

    val ProcessingTimeInMilliseconds: String
){
    data class PR(
        //var TextOverlay:String = "",
        val ParsedText: String,
        val TextOrientation: String,
        val FileParseExitCode: Int,
        val ErrorMessage: String,
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
                val md5 = saveImg(img?.queryUrl(),"mythquotes")
                delay(1000L)
                val base64ImageString = encoder("src/img/mythquotes/$md5.jpg")
                val text = fetchJson(base64ImageString)
                reply(text)
            }
            else{
                reply("ERROR")
            }
        }
    }
}

/**
 * return OCR result for a given image.
 */
fun ocr(img: File): String{
    val base64ImageString = encoder(img)
    return fetchJson(base64ImageString)
}

fun encoder(filePath: String): String{
    val bytes = File(filePath).readBytes()
    val base64 = Base64.getEncoder().encodeToString(bytes)
    return base64
}

fun encoder(file: File): String{
    val bytes = file.readBytes()
    val base64 = Base64.getEncoder().encodeToString(bytes)
    return base64
}

/**
 * return parsed OCR result of a given image encoded by base64
 */
fun fetchJson(imageBaseString: String):String{
    val postBody = FormBody.Builder()
        .add("apikey", apiKey)
        .add("base64Image","data:image/jpg;base64,$imageBaseString")
        .add("language",lang)
        .build()

    val request = Request.Builder()
        .url(url)
        .post(postBody)
        .build()

    val client = OkHttpClient()
    val response = client.newCall(request).execute().body()!!.string()
    val result = Klaxon().parse<OCRdata>(response)
    return result!!.ParsedResults[0].ParsedText
}
