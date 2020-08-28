package pigeon.qqbot

import com.beust.klaxon.Klaxon
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.sendAsImageTo
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class Img(val url:String)
data class Rpl(val data: List<Img>)

private fun getSetu(xp: String): String {
    val http=
            URL("https://api.lolicon.app/setu/?apikey=432105395f48f8888acb81&keyword=${URLEncoder.encode(xp,"UTF-8")}")
                    .openConnection() as HttpURLConnection
    http.requestMethod="GET"
    val json= Klaxon().parse<Rpl>(http.inputStream)
    return saveImg(json?.data?.random()?.url,"setu")
}

fun Bot.setu() {
    this.subscribeMessages {
        startsWith("#色图", true) {
            File("src/img/setu/${getSetu(it)}.jpg").sendAsImageTo(subject)
        }
    }
}
