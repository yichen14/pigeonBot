package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

val gameWaiting = mutableMapOf<String, ArrayList<String>>()
fun Bot.openGame(){
    this.subscribeMessages {
        startsWith("#open", true){
            val game = it.split(" ")[0]
            //val timeLast = it.split(" ")[1]
            val gamerNo = it.split(" ")[1]
            //val time = this.time
            val timeStamp = System.currentTimeMillis()
            //val timeEnd = timeStamp + (timeLast.toLong()*60*1000)
            val senderName = this.senderName
            val sender = this.sender.id
            if (game != ""){
                if (gameWaiting.containsKey(game)){
                    reply("已有此名称，请使用其他名称")
                }else {
                    gameWaiting[game] = arrayListOf(gamerNo, sender.toString())
                    reply(
                        "\"${senderName}\"于\"${SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z").format(Date(timeStamp))}\"\n" +
                                "添加\"${game}\",需要\"${gamerNo}\"个人,回复#join即可加入"
                    )
                }
            }
        //judgment and reply contain time parameter
        /*    if(game != "" && timeLast != ""){
                if (gameWaiting.containsKey(game)){
                    reply("已有此名称，请使用其他名称")
                }else {
                    gameWaiting[game] = arrayListOf(gamerNo, timeEnd.toString(), sender.toString())
                    reply(
                        "\"${senderName}\"于\"${SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z").format(Date(timeStamp))}\"\n" +
                                "添加\"${game}\",时长\"${timeLast}\"min\n" +
                                "会在\"${SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z").format(Date(timeEnd))}\"结束"
                    )
                }
            }*/
        }
        startsWith("#check", true){
            var rpl = ""
            if(gameWaiting.containsKey(it)){
                gameWaiting[it]!!.forEach {
                    rpl+=(it+"\n")
                }
                reply(rpl)
            }
        }
        startsWith("#allGame",true){
            var rpl = ""
            for (key in gameWaiting.keys){
                rpl += (key+"\n")
                gameWaiting[key]!!.forEach{
                    rpl += ("\t"+it+"\n")
                }
            }
            reply(rpl)
        }
        startsWith("#join", true){
            if(gameWaiting.containsKey(it)) {
                if (checkNo(it) > 0) {
                    gameWaiting[it]!!.add(sender.id.toString())
                    if (checkNo(it) != 0) {
                        reply("\"${senderName}\"已加入\"${it}\"\n还有\"${checkNo(it)}\"个空位")
                    } else {
                        reply("\"${it}\"人已齐，开局")
                    }
                }else{
                    reply("残念，人已经满了")
                }
            } else {
                reply("该游戏尚未创建或已过期，请重新创建")
            }
        }
        startsWith("#remove",true){
            if (gameWaiting.containsKey(it)){
                gameWaiting.remove(it)
                reply("已删除\"${it}\"")
            }
        }
    }
    this.subscribeAlways<GroupMessageEvent>{
        if (subject.id == 1143577518L ){
            for ((key, _) in gameWaiting){
                if (gameWaiting[key]!!.size == gameWaiting[key]!![0].toInt()+2 ) {//
                    var reminder = buildMessageChain {  }
                    val a = PlainText("你们的游戏人凑齐了")
                    reminder += a
                    for (i in 2 until gameWaiting[key]!!.size){
                        val gamer = At(subject[gameWaiting[key]!![i].toLong()])
                        reminder += gamer
                    }
                    reply(reminder)
                    gameWaiting.remove(key)
                }
            }
        }
    }
}

fun checkNo(game: String): Int {
    val length = gameWaiting[game]!!.size
    return gameWaiting[game]!![0].toInt() - length +2
}

//fun checkTime(game: String): Boolean{


