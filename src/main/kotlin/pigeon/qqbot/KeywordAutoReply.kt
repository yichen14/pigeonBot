package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.queryUrl
import java.io.File
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

var keywordMap = mutableMapOf<String, MutableList<String>>()
const val autoReplyFilePath = "src/main/resources/autoReply.txt"
val autoReplyFile = File(autoReplyFilePath)

//TODO: 支持图片作为value 建议和语录上传一起搞

fun Bot.keywordAutoReply() {
    for (line in autoReplyFile.readLines()) {
        val words = line.split(" ")
        keywordMap[words[0]] = words.subList(1, words.lastIndex + 1).toMutableList()
    }
    this.subscribeAlways<GroupMessageEvent> {
        if(subject.id==1143577518L) {
            for((key,value) in keywordMap){
                    if (message.content.indexOf(key) != -1) {
                        try{
                            reply(keywordMap[key]!!.random())
                        }
                        catch(e:IllegalArgumentException){
                            //do nothing
                        }
                    }
            }
            if (message[Image]!=null){
                reply(message[Image]!!.queryUrl())
            }
            /*
            if (keywordMap.contains(message.content)) {
                reply(keywordMap[message.content]!!.random())//废弃
            }*/
        }
    }
    this.subscribeMessages {
        startsWith("#add", true) {
            val key = it.split(" ")[0]
            val value = it.split(" ")[1]
            if (key != "" && value != "" && key != "[图片]" && value != "[图片]" && !key.contains("#list ")) {
                if (keywordMap.containsKey(key))
                    keywordMap.getValue(key).add(value)
                else
                    keywordMap[key] = mutableListOf(value)
                saveAutoReplyList()
                reply("添加\"${value}\"到\"${key}\"")
            }
        }
        startsWith("#del", true) {
            keywordMap.remove(it)
            saveAutoReplyList()
            reply("删除\"$it\"")
        }
        startsWith("#list ", true){
            var rpl = ""
            if(keywordMap.containsKey(it)){
                keywordMap[it]!!.forEach {
                    rpl+=(it+"\n")
                }
                reply(rpl)
            }
        }

    }
}

fun saveAutoReplyList() {
    val writer = autoReplyFile.writer()
    for (pair in keywordMap) {
        writer.write(pair.key + " ")
        for (word in pair.value)
            writer.write("$word ")
        writer.write("\n")
    }
    writer.flush()
    writer.close()
}
