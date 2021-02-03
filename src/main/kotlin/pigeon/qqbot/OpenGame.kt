package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.User
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
/*abstract class gameWaiting (key:String){
    var gamerNo:Int = 0
    var timeStamp:Long = 0
    var gamerNumOn:Boolean = false
    lateinit var User:ArrayList<User>
}*/
/*gameName
    timeStamp
    gamerNo
    sender(User)
    sender(User)
    ...*/
fun Bot.openGame(){
    this.subscribeMessages {
        startsWith("#game", true){
            val opt = it.split(" ")[0]
            val senderName = this.senderName
            val sender = this.sender.id
            val groupNum = this.subject.id
            when(opt){
                "-open" -> {
                    val game = it.split(" ")[1]
                    val timeStamp = System.currentTimeMillis()
                    if (game.contains("+")){
                        val gameName = game.split("+")[0]
                        val gamerNo = game.split("+")[1]
                        if (gameWaiting.containsKey(gameName)){
                            reply("已有此名称，请使用其他名称")
                        } else {
                            gameWaiting[gameName] = arrayListOf(timeStamp.toString(), gamerNo, sender.toString())
                            reply(
                                "\"${senderName}\"于\"${SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z").format(Date(timeStamp))}\"\n" +
                                        "添加\"${gameName}\"，需要\"${gamerNo}\"个人，回复#game -join ${gameName}即可加入"
                            )
                        }
                    } else {
                        val gamerNo = -1
                        if (game != "") {
                            if (gameWaiting.containsKey(game)) {
                                reply("已有此名称，请使用其他名称")
                            } else {
                                gameWaiting[game] = arrayListOf(timeStamp.toString(), gamerNo.toString(), sender.toString())
                                reply(
                                    "\"${senderName}\"于\"${
                                        SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z").format(Date(timeStamp))
                                    }\"\n" +
                                            "添加\"$game\"，回复#game -join ${game}即可加入"
                                )
                            }
                        }
                    }
                }
                "-check" -> {
                    val game = it.split(" ")[1]
                    if(gameWaiting.containsKey(game)){
                        var rpl = ""
                        gameWaiting[game]!!.forEach {
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
                    val game = it.split(" ")[1]
                    if(gameWaiting.containsKey(game)) {
                        if ( gameWaiting[game]!![2].toInt()!=-1){
                            if (checkNo(game) > 0) {//有空位置
                                if (checkGamer(game,sender)!=0){//检查是否已加入
                                    reply("你已加入此游戏，请不要重复加入。")
                                } else {
                                    gameWaiting[game]!!.add(sender.toString())
                                    if (checkNo(game) != 0) {
                                        reply("\"${senderName}\"已加入\"${game}\"\n还有\"${checkNo(game)}\"个空位")
                                    } else {
                                        reply("\"${game}\"人已齐，开局")//
                                        var reminder = buildMessageChain {  }
                                        val a = PlainText("你们的游戏人凑齐了")
                                        reminder += a
                                        for (i in 2 until gameWaiting[game]!!.size){
                                            val memberID = gameWaiting[game]!![i].toLong()
                                            val gamer = getGroup(groupNum)[memberID].at()
                                            reminder += gamer
                                        }
                                        reply(reminder)
                                        gameWaiting.remove(game)
                                    }
                                }
                            } else {
                                reply("残念，人已经满了")
                            }
                        } else {
                            if (checkGamer(game,sender)!=0){
                                reply("你已加入此游戏，请不要重复加入。")
                            } else {
                                gameWaiting[game]!!.add(sender.toString())
                                reply("\"${senderName}\"已加入\"${game}\"")
                            }
                        }
                    } else {
                        reply("该游戏尚未创建或已过期，请重新创建")
                    }
                }
                "-quit" -> {
                    val game = it.split(" ")[1]
                    if(gameWaiting.containsKey(game)){
                        val checkResult = checkGamer(game,sender)
                        if (checkResult!=0){
                            gameWaiting[game]!![checkResult] = ""
                            reply("\"${senderName}\"已退出\"$game")
                        } else {
                            reply("你都妹加入，退出个锤子")
                        }
                    } else {
                        reply("该游戏尚未创建或已过期，请创建后再退出")
                    }
                }
                "-remove" -> {
                    val game = it.split(" ")[1]
                    if (gameWaiting.containsKey(game)){
                        gameWaiting.remove(game)
                        reply("已删除\"${game}\"")
                    } else {
                        reply("该游戏尚未创建或已过期")
                    }
                }
                "-remind" -> {
                    val game = it.split(" ")[1]
                    if (gameWaiting.containsKey(game)){
                        var reminder = buildMessageChain {  }
                        val a = PlainText("你们的游戏人凑齐了")
                        reminder += a
                        for (i in 2 until gameWaiting[game]!!.size){
                            val memberID = gameWaiting[game]!![i].toLong()
                            val gamer = getGroup(groupNum)[memberID].at()
                            reminder += gamer
                        }
                        reply(reminder)
                        gameWaiting.remove(game)
                    } else {
                        reply("该游戏尚未创建或已过期")
                    }
                }
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
    return gameWaiting[game]!![1].toInt() - length +2
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


