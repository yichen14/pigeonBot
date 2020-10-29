package pigeon.qqbot

import com.beust.klaxon.Json
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import com.google.gson.JsonArray
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.queryUrl
import okhttp3.*
import java.io.File
import java.io.StringReader
import java.util.Base64
import kotlin.js.*
import kotlinx.serialization.*
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
                val md5 = saveImg(img?.queryUrl(),"114514")
                val base64ImageString = encoder("src/img/114514/$md5.jpg")
                //reply("check image base64 string: $base64ImageString")
                val text = fetchJson(base64ImageString)
                reply(text)
            }
            else{
                reply("ERROR")
            }
        }
    }
}
fun encoder(filePath: String): String{
    val bytes = File(filePath).readBytes()
    val base64 = Base64.getEncoder().encodeToString(bytes)
    return base64
}

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

    println(response)
    val result = Klaxon().parse<OCRdata>(response)
    return result!!.ParsedResults[0].ParsedText
   /* val klaxon  = Klaxon()
    val parsed = klaxon.parseJsonObject(StringReader(response))
    val data = parsed.array<Any>("ParsedResults")
    //return data?.let { klaxon.parseFromJsonArray<PR>() }*/
/*    val gson = Gson()
    val json = gson.fromJson<OCRdata>(response,OCRdata::class.java)

    if (json.ParsedResults.ErrorMessage!=null){
        return json.ParsedResults.ErrorMessage
    }
    else{
        return json.ParsedResults.ParsedText
    }*/
    //return response

}
