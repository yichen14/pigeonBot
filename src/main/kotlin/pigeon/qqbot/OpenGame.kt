package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.code.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

val gameWaiting = mutableMapOf<String, ArrayList<String>>()
/*gameName
    timeStamp
    gamerNo
    sender(id)
    sender(id)
    ...*/
fun Bot.openGame(){
    this.subscribeMessages {
        startsWith("#game", true){
            val opt = it.split(" ")[0]
            val gameInfo = it.split(" ")[1]
            val senderName = this.senderName
            val sender = this.sender.id
            val groupNum = this.subject.id
            when(opt){
                "-open" -> {
                    val timeStamp = System.currentTimeMillis()
                    val game = gameInfo.split(" ")[0]
                    val gamerNo = gameInfo.split(" ")[1]
                    if (game != ""){
                        if (gameWaiting.containsKey(game)){
                            reply("已有此名称，请使用其他名称")
                        } else {
                            gameWaiting[game] = arrayListOf(timeStamp.toString(), gamerNo, sender.toString())
                            reply(
                                "\"${senderName}\"于\"${SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z").format(Date(timeStamp))}\"\n" +
                                        "添加\"${game}\"，需要\"${gamerNo}\"个人，回复#game -join即可加入"
                            )
                        }
                    }
                }
                "-check" -> {
                    if(gameWaiting.containsKey(it)){
                        var rpl = ""
                        gameWaiting[it]!!.forEach {
                            rpl+=(it+"\n")
                        }
                        reply(rpl)
                    } else {
                        reply("没有这个游戏，不如开一个")
                    }
                }
                "-list" -> {
                    var rpl = ""
                    for (key in gameWaiting.keys){
                        rpl += (key+"\n")
                        gameWaiting[key]!!.forEach{
                            rpl += ("\t"+it+"\n")
                        }
                    }
                    reply(rpl)
                }
                "-join" -> {
                    if(gameWaiting.containsKey(it)) {
                        if (checkNo(it) > 0) {//有空位置
                            if (checkGamer(it,sender)!=0){//检查是否已加入
                                reply("你已加入此游戏，请不要重复加入。")
                            } else {
                                gameWaiting[it]!!.add(sender.toString())
                                if (checkNo(it) != 0) {
                                    reply("\"${senderName}\"已加入\"${it}\"\n还有\"${checkNo(it)}\"个空位")
                                } else {
                                    reply("\"${it}\"人已齐，开局")//
                                    var reminder = buildMessageChain {  }
                                    val a = PlainText("你们的游戏人凑齐了")
                                    reminder += a
                                    for (i in 2 until gameWaiting[it]!!.size){
                                        val memberID = gameWaiting[it]!![i].toLong()
                                        val gamer = getGroup(groupNum)[memberID].at()
                                        reminder += gamer
                                    }
                                    reply(reminder)
                                    gameWaiting.remove(it)
                                }
                            }
                        } else {
                            reply("残念，人已经满了")
                        }
                    } else {
                        reply("该游戏尚未创建或已过期，请重新创建")
                    }
                }
                "-quit" -> {
                    if(gameWaiting.containsKey(it)){
                        val checkResult = checkGamer(it,sender)
                        if (checkResult!=0){
                            gameWaiting[it]!![checkResult] = ""
                            reply("\"${senderName}\"已退出\"$it")
                        } else {
                            reply("你都妹加入，退出个锤子")
                        }
                    }
                }
                "-remove" -> {
                    if (gameWaiting.containsKey(it)){
                        gameWaiting.remove(it)
                        reply("已删除\"${it}\"")
                    }
                }
                /*"-remind" -> {

                }*/
            }
            //val timeLast = it.split(" ")[1]
            //val game = gameInfo.split(" ")[0]
            //val gamerNo = gameInfo.split(" ")[1]
            //val time = this.time
            //val timeEnd = timeStamp + (timeLast.toLong()*60*1000)
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
    }
/*    this.subscribeAlways<GroupMessageEvent>{
        if (subject.id == 1143577518L ){
            for ((key, _) in gameWaiting){
                if (gameWaiting[key]!!.size == gameWaiting[key]!![1].toInt()+2 ) {
                }
            }
        }
    }*/
}

fun checkNo(game: String): Int {
    val length = gameWaiting[game]!!.size
    return gameWaiting[game]!![0].toInt() - length +2
}

fun checkGamer(game: String, gamer: Long): Int{
    for (i in 2 until gameWaiting[game]!!.size){
        if (gamer == gameWaiting[game]!![i].toLong()){
            return i
        }
    }
    return 0
}

//fun checkTime(game: String): Boolean{


