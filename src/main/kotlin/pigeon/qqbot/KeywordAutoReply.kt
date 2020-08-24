package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import java.io.*

var keywordMap =  mutableMapOf<String, MutableList<String>>()
const val autoReplyFilePath = "src/main/resources/autoReply.txt"
val autoReplyFile = File(autoReplyFilePath)

fun Bot.keywordAutoReply(){
    for(line in autoReplyFile.readLines()) {
        val words = line.split(" ")
        keywordMap[words[0]]= words.subList(1, words.lastIndex+1).toMutableList()
    }
    //TODO: #del <key> <value>
    //TODO: when any part of message contains key, reply.
    this.subscribeAlways<GroupMessageEvent> {
        if(keywordMap.contains(message.content)){
            reply(keywordMap[message.content]!!.random())
        }
    }
    this.subscribeMessages {
        startsWith("#add", removePrefix = true){
            val key = it.split(" ")[0]
            val value = it.split(" ")[1]
            if(key !=""&& value !=""){
                if(keywordMap.containsKey(key))
                    keywordMap.getValue(key).add(value)
                else
                    keywordMap[key] = mutableListOf(value)
                reply("添加\"${value}\"到\"${key}\"")
            }
        }
    }
}

fun saveAutoReplyList() {
    val writer= autoReplyFile.writer()
    for(pair in keywordMap){
        writer.write(pair.key+" ")
        for(word in pair.value)
            writer.write("$word ")
        writer.write("\n")
    }
    writer.flush()
    writer.close()
}
