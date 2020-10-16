package pigeon.qqbot

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot

data class Douyu(val error: Int, val data: Data)
data class Data(val room_name: String, val room_status: String)


fun Bot.liveStreamDetect(roomNumber: Int) {
    GlobalScope.launch {
        var liveStatus = "0"
        while (true) {
            val json = getJson<Douyu>("http://open.douyucdn.cn/api/RoomApi/room/$roomNumber")
            if (json?.error == 0) {
                if (json.data.room_status == "1" && json.data.room_status != liveStatus)
                    getGroup(596870824L).sendMessage("$roomNumber 播了")
                liveStatus = json.data.room_status
            }
            delay(100000L)//100s
        }
    }
}