package pigeon.qqbot

import com.beust.klaxon.Klaxon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL
import net.mamoe.mirai.Bot
data class Douyu(val error: Int, val data: data)
data class data(val room_name:String, val room_status: String)


fun Bot.LiveStreamDetect(roomNumber:Int) = runBlocking<Unit> {
    val job = launch {
        var liveStatus: String = "0"
        while (true) {
            val http =
                    URL("http://open.douyucdn.cn/api/RoomApi/room/$roomNumber")
                            .openConnection() as HttpURLConnection
            http.requestMethod = "GET"
            val json = Klaxon().parse<Douyu>(http.inputStream)
            if (json?.error == 0) {
                if (json.data.room_status == "1" && json.data.room_status != liveStatus) {
                    liveStatus = json.data.room_status
                    getGroup(596870824L).sendMessage("$roomNumber 播了\n${json.data.room_name}")
                } else {
                    liveStatus = json.data.room_status
                }
            }
            delay(100000L)
        }
    }
}