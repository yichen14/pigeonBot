package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.At
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

val gameWaiting = mutableMapOf<String, ArrayList<String>>()
fun Bot.openGame(){
    this.subscribeMessages {
        startsWith("#open", true){
            val game = it.split(" ")[0]
            val timeLast = it.split(" ")[1]
            val gamerNo = it.split(" ")[2]
            //val time = this.time
            val timeStamp = System.currentTimeMillis()
            val timeEnd = timeStamp + (timeLast.toLong()*60*1000)
            val sender = this.senderName
            if(game != "" && timeLast != ""){
                if (gameWaiting.containsKey(game)){
                    reply("已有此名称，请使用其他名称")
                }else {
                    gameWaiting[game] = arrayListOf(gamerNo, timeEnd.toString(), sender)
                    reply(
                        "\"${sender}\"于\"${SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z").format(Date(timeStamp))}\"\n" +
                                "添加\"${game}\",时长\"${timeLast}\"min\n" +
                                "会在\"${SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z").format(Date(timeEnd))}\"结束"
                    )
                }
            }
        }
        startsWith("#check", true){
            var rpl = ""
            if(gameWaiting.containsKey(it)){
                gameWaiting[it]!!.forEach {
                    rpl+=("\n"+it)
                }
                reply(rpl)
            }
        }
        startsWith("#join", true){
            val sender = this.senderName
            if(gameWaiting.containsKey(it)) {
                if (checkNo(it) > 0) {
                    gameWaiting[it]!!.add(sender)
                    if (checkNo(it) != 0) {
                        reply("\"${sender}\"已加入\"${it}\"\n还有\"${checkNo(it)}\"个空位")
                    } else {
                        var gamer = "@"
                        for (i in 2 until gameWaiting[it]!!.size) {
                            gamer += gameWaiting[it]!![i] + "\n@"
                        }
                        reply("\"${it}\"人已齐，开局\n\"${gamer}\"")
                    }
                }else{
                    reply("残念，人已经满了")
                }
            } else {
                reply("该游戏尚未创建或已过期，请重新创建")
            }
        }
    }
}

fun checkNo(game: String): Int {
    val length = gameWaiting[game]!!.size
    return gameWaiting[game]!![0].toInt() - length +2
}

//fun checkTime(game: String): Boolean{


