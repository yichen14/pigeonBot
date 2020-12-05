package pigeon.qqbot

import com.beust.klaxon.Klaxon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import java.net.HttpURLConnection
import java.net.URL

data class Douyu(val error: Int, val data: Data)
data class Data(val room_name: String, val room_status: String, val start_time: String)


fun Bot.liveStreamDetect(roomNumber: Int,message:String) {
    GlobalScope.launch {
        var liveStatus = "0"
        while (true) {
            val json = getJson<Douyu>("http://open.douyucdn.cn/api/RoomApi/room/$roomNumber")
            if (json?.error == 0) {
                if (json.data.room_status == "1" && json.data.room_status != liveStatus)
                    getGroup(596870824L).sendMessage("${message}\n${json.data.room_name}\n${json.data.start_time}")
                liveStatus = json.data.room_status
            }
            delay(100000L)//100s
        }
    }
}

inline fun <reified json> getJson(url: String): json? {
    val http = URL(url).openConnection() as HttpURLConnection
    http.requestMethod = "GET"
    return Klaxon().parse<json>(http.inputStream)
}
