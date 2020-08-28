package pigeon.qqbot

import com.beust.klaxon.Klaxon
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import net.mamoe.mirai.message.sendAsImageTo
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class Img(val url: String)
data class Rpl(val code: Int, val data: List<Img>)

private val lastTime = mutableMapOf<Long, Long>()

fun Bot.setu() {
    this.subscribeMessages {
        startsWith("#色图", true) {
            if (lastTime.containsKey(this.sender.id) && System.currentTimeMillis() - lastTime[this.sender.id]!! <= 1000 * 60 * 10) {
                reply(messageChainOf(PlainText("冲太多了不好哦"), At(this.sender as Member)))
            } else {
                lastTime[this.sender.id] = System.currentTimeMillis()
                val http =
                        URL("https://api.lolicon.app/setu/?apikey=432105395f48f8888acb81&keyword=${URLEncoder.encode(it, "UTF-8")}")
                                .openConnection() as HttpURLConnection
                http.requestMethod = "GET"
                val json = Klaxon().parse<Rpl>(http.inputStream)
                when (json?.code) {
                    0 -> {
                        val md5 = saveImg(json.data.random().url, "setu")
                        File("/src/img/setu/$md5.jpg").sendAsImageTo(subject)
                    }
                    404 -> reply("找不到关键词为${it}的色图")
                    429 -> reply("今日色图配额已用尽，你们这能冲啊")
                }
            }
        }
    }
}
