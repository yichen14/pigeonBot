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

data class LoliconImg(val url: String)
data class LoliconRpl(val code: Int, val data: List<LoliconImg>)

data class PixivIcUrl(val original: String)
data class PixivIcIllust(val imageUrls: List<PixivIcUrl>)
data class PixivIcRpl(val data: List<PixivIcIllust>)

private val lastTime = mutableMapOf<Long, Long>()

fun Bot.setu() {
    this.subscribeMessages {

            startsWith("#色图", true) {
            if (lastTime.containsKey(this.sender.id) && System.currentTimeMillis() - lastTime[this.sender.id]!! <= 1000 * 60 * 10) {
                reply(messageChainOf(PlainText("冲太多了不好哦"), At(this.sender as Member)))
            } else {
                var xp = URLEncoder.encode(it, "UTF-8")
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
                    404 -> {
                        try {
                            xp=xp.replace("pixivic","").trim()
                            val http =
                                URL("https://api.pixivic.com/illustrations?keyword=$xp&page=1&illustType=illust")
                                    .openConnection() as HttpURLConnection
                            http.requestMethod = "GET"
                            val json = Klaxon().parse<PixivIcRpl>(http.inputStream)
                            val url= json?.data?.get((0..5).random())?.imageUrls?.random()?.original
                            val md5 = saveImg(url?.replace("pximg.net","pixiv.cat"), "setu")
                            File("src/img/setu/$md5.jpg").sendAsImageTo(subject)
                        } catch(e: Exception) {
                        reply("找不到关键词为${it}的色图")
                        }
                    }
                    429 -> reply("今日色图配额已用尽，你们真能冲啊")
                }
            }
        }
    }
}
