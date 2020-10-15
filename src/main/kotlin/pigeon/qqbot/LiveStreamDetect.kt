package pigeon.qqbot

import com.beust.klaxon.Klaxon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import java.net.HttpURLConnection
import java.net.URL

data class Douyu(val error: Int, val data: data)
data class data(val room_name: String, val room_status: String)


fun Bot.liveStreamDetect(roomNumber: Int) {
    GlobalScope.launch {
        var liveStatus = "0"
        while (true) {
            val http =
                URL("http://open.douyucdn.cn/api/RoomApi/room/$roomNumber")
                    .openConnection() as HttpURLConnection
            http.requestMethod = "GET"
            val json = Klaxon().parse<Douyu>(http.inputStream)
            if (json?.error == 0) {
                if (json.data.room_status == "1" && json.data.room_status != liveStatus)
                    getGroup(596870824L).sendMessage("$roomNumber 播了")
                liveStatus = json.data.room_status
            }
            delay(1000L)
        }
    }
}