package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content

fun Bot.keywordAutoReply(){
    var testlist =  mutableMapOf<String, MutableList<String>>()
    testlist.put("test1", mutableListOf("String","String2","String3"))
    testlist.put("test2", mutableListOf("test","test2","test3"))
    testlist.put("测试3", mutableListOf("测试"))
    //TODO: #del <key> <value>
    //TODO: when any part of message contains key, reply.
    //TODO: save map to local files.
    this.subscribeAlways<GroupMessageEvent> {
        if(testlist.contains(message.content)){
            reply(testlist.get(message.content)!!.random())
        }
    }
    this.subscribeMessages {
        startsWith("#add", removePrefix = true){
            if(it.splitString()[0] !=""&& it.splitString()[1] !=""){
                if(testlist.containsKey(it.splitString()[0])){
                    testlist.getValue(it.splitString()[0]).add(it.splitString()[1])
                    reply("添加\"${it.splitString()[1]}\"到\"${it.splitString()[0]}\"")
                }
                else {
                    testlist.put(it.splitString()[0], mutableListOf(it.splitString()[1]))
                    reply("添加\"${it.splitString()[1]}\"到\"${it.splitString()[0]}\"")
                }
            }

        }
    }
}

fun String.splitString(): Array<String> {
    if(this.indexOf(" ")!=-1){
    return arrayOf(this.substring(0,this.indexOf(" ")),(this.substring(this.indexOf(" ")+1)))
    }
    else return arrayOf("")
}
