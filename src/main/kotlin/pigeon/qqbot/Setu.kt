package pigeon.qqbot

import com.beust.klaxon.Klaxon
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import net.mamoe.mirai.message.sendAsImageTo
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
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
                val json =
                    getJson<LoliconRpl>("https://api.lolicon.app/setu/?apikey=432105395f48f8888acb81&keyword=$xp")
                when (json?.code) {
                    0 -> {
                        try {
                            val md5 = saveImg(json.data.random().url, "setu")
                            File("src/img/setu/$md5.jpg").sendAsImageTo(subject)
                            lastTime[this.sender.id] = System.currentTimeMillis()
                        } catch (e: Exception) {
                            reply("发送色图时发生错误")
                        }
                    }
                    404 -> {
                        xp = xp.replace("pixivic", "").trim()
                        try {/*
                            val url =
                                getJson<PixivIcRpl>("https://api.pixivic.com/illustrations?keyword=$xp&page=1&illustType=illust")?.data?.get(
                                    (0..5).random()
                                )?.imageUrls?.random()?.original*/
                            val simpleUrl = "https://api.pixivic.com/illustrations?keyword=$xp&page=1&illustType=illust"
                            val authorization = "eyJhbGciOiJIUzUxMiJ9.eyJwZXJtaXNzaW9uTGV2ZWwiOjIsInJlZnJlc2hDb3VudCI6MSwiaXNCYW4iOjEsInVzZXJJZCI6NTYwMDA4LCJpYXQiOjE2MDUxOTU1NjYsImV4cCI6MTYwNTM2ODM2Nn0.86cYaJsgv_UHh0U5tfQR9D6wzwv6QXZNZGrPlyc61tjHH0EK4fzmrKk73dBDvTiQ_z4T7j6dFWXEfbtntdzayQ"
                            val headers = Headers.Builder()
                                .add("Authorization", authorization)
                                .build()
                            val request = Request.Builder()
                                .url(simpleUrl)
                                .headers(headers)
                                .get()
                                .build()
                            val client = OkHttpClient()
                            val response = client.newCall(request).execute().body()!!.string()
                            println(response)
                            val result = Klaxon().parse<PixivIcRpl>(response)?.data?.get((0..5).random())?.imageUrls?.random()?.original
                            val md5 = saveImg(result?.replace("pximg.net", "pixiv.cat"), "setu")
                            File("src/img/setu/$md5.jpg").sendAsImageTo(subject)
                        } catch (e: Exception) {
                            reply("找不到关键词为${xp}的色图")
                        }
                    }
                    429 -> reply("今日色图配额已用尽，你们真能冲啊")
                }
            }
        }
    }
}

inline fun <reified json> getJson(url: String): json? {
    val http = URL(url).openConnection() as HttpURLConnection
    http.requestMethod = "GET"
    return Klaxon().parse<json>(http.inputStream)
}
