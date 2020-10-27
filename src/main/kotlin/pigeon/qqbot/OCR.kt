package pigeon.qqbot

import com.beust.klaxon.JsonValue
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Image
import java.io.File
import okhttp3.*

class OCR {
    private val apiKey = "140b4b8ee688957"
    private val url = "https://api.ocr.space/parse/image"
    private val lang = "chs"
    val client = new OkHttpClient()

    val request = new Request.Builder()
        .url(url)
        .header("apikey", apiKey)
        .header("language",lang)
        .build()

    private fun fetchJson(imageUrl: String?):String{
        request.add("url",imageUrl)
        Response response = client.newCall(request).execute();
        if (response.has("ErrorMessage")){
            val error = response.getString("ErrorMessage")
            return error
        }
        else{
            val text = response.getString("ParsedText")
            return text
        }

    }
    fun Bot.OCR(){
        this.subscribeMessages{
            startsWith("#OCR"){
                if（message[Image] != null） {
                    val img = message[Image]
                    val md5 = saveImg(img?.queryUrl(),"114514")
                    val text = fetchJson(File("src/img/114514/$md5.jpg"))
                    reply(text) 
                }
                else{
                    reply("ERROR")
                }
            }
        }   
    }
}