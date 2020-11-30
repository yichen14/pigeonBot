package pigeon.qqbot

import com.beust.klaxon.Klaxon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import net.mamoe.mirai.message.sendAsImageTo
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class LoliconImg(val url: String)
data class LoliconRpl(val code: Int, val data: List<LoliconImg>)

private val lastTime = mutableMapOf<Long, Long>()

fun Bot.setu(username: String, password: String) {
    GlobalScope.launch { Runtime.getRuntime().exec("python3 src/main/setuserver.py $username $password") }
    this.subscribeMessages {
        startsWith("#色图", true) {
            if (lastTime.containsKey(this.sender.id) && System.currentTimeMillis() - lastTime[this.sender.id]!! <= 1000 * 60 * 3) {
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
                        xp = it.replace("pixiv", "").trim()
                        try {
                            val proc = Runtime.getRuntime().exec("python3 src/main/setusearch.py $xp")
                            val md5 = saveImg(BufferedReader(InputStreamReader(proc.inputStream)).readLine(), "setu")
                            File("src/img/setu/$md5.jpg").sendAsImageTo(subject)
                            lastTime[this.sender.id] = System.currentTimeMillis()
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

