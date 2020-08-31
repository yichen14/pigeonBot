package pigeon.qqbot

import com.beust.klaxon.Json
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

data class LoliconImg(val url: String)
data class LoliconRpl(val code: Int, val data: List<LoliconImg>)

data class DanbooruImg(@Json(name = "file_url") val fileURL: String)

private val lastTime = mutableMapOf<Long, Long>()

fun Bot.setu() {
    this.subscribeMessages {
        startsWith("#色图", true) {
            if (lastTime.containsKey(this.sender.id) && System.currentTimeMillis() - lastTime[this.sender.id]!! <= 1000 * 60 * 10) {
                reply(messageChainOf(PlainText("冲太多了不好哦"), At(this.sender as Member)))
            } else if (it.contains("danbooru")) {
                val xp = URLEncoder.encode(it.replace("danbooru", "").trim().replace(" ", "_"), "UTF-8")
                val http = URL("https://danbooru.donmai.us/posts.json?tags=$xp")
                        .openConnection() as HttpURLConnection
                http.requestMethod = "GET"
                val json = Klaxon().parseArray<DanbooruImg>(http.inputStream)
                if (json?.isEmpty()!!)
                    reply("找不到关键词为${xp}的色图")
                else {
                    val md5 = saveImg(json.random().fileURL, "setu")
                    File("src/img/setu/$md5.jpg").sendAsImageTo(subject)
                }
            } else {
                val xp = URLEncoder.encode(it, "UTF-8")
                val http =
                        URL("https://api.lolicon.app/setu/?apikey=432105395f48f8888acb81&keyword=$xp")
                                .openConnection() as HttpURLConnection
                http.requestMethod = "GET"
                val json = Klaxon().parse<LoliconRpl>(http.inputStream)
                when (json?.code) {
                    0 -> {
                        val md5 = saveImg(json.data.random().url, "setu")
                        File("src/img/setu/$md5.jpg").sendAsImageTo(subject)
                        lastTime[this.sender.id] = System.currentTimeMillis()
                    }
                    404 -> reply("找不到关键词为${it}的色图")
                    429 -> reply("今日色图配额已用尽，你们真能冲啊")
                }
            }
        }
    }
}
