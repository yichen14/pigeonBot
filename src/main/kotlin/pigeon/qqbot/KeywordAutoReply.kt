package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.queryUrl
import net.mamoe.mirai.message.sendAsImageTo
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest


var keywordMap = mutableMapOf<String, MutableList<String>>()
const val autoReplyFilePath = "src/main/resources/autoReply.txt"
val autoReplyFile = File(autoReplyFilePath)

//TODO: 语录上传

fun Bot.keywordAutoReply() {
    for (line in autoReplyFile.readLines()) {
        val words = line.split(" ")
        keywordMap[words[0]] = words.subList(1, words.lastIndex + 1).toMutableList()
    }
    this.subscribeAlways<GroupMessageEvent> {
        if(subject.id==1143577518L || subject.id==596870824L) {
            for((key,value) in keywordMap){
                    if (message.content.indexOf(key) != -1 && message.content.indexOf("#list ") == -1 && message.content.indexOf("#add ") == -1 && message.content.indexOf("#del ") == -1) {
                        try{
                            val reply = keywordMap[key]!!.random()
                            if (reply.startsWith("$")){
                                File("src/img/groupImg/"+reply+".jpg").sendAsImageTo(subject)
                            }
                            else {
                                reply(reply)
                            }
                        }
                        catch(e:IllegalArgumentException){
                            //do nothing
                        }
                    }
            }
        }
    }
    this.subscribeMessages {
        startsWith("#add", true) {
            val key = it.split(" ")[0]
            val value = it.split(" ")[1]
            if (key != "" && value != "" && !key.contains("#list ") && message[Image]==null) {
                if (keywordMap.containsKey(key))
                    keywordMap.getValue(key).add(value)
                else
                    keywordMap[key] = mutableListOf(value)
                saveAutoReplyList()
                reply("添加\"${value}\"到\"${key}\"")
            }
            if(message[Image]!=null){
                val str = saveImage(message[Image]!!.queryUrl())
                if (keywordMap.containsKey(key)) {
                    if (!(keywordMap.getValue(key).contains(str)))
                        keywordMap.getValue(key).add(str)
                }
                else
                    keywordMap[key] = mutableListOf(str)
                saveAutoReplyList()
                reply("添加\"${value}\"到\"${key}\"")
            }
        }
        startsWith("#del", true) {
            val key = it.split(" ")[0]
            val value = it.split(" ")[1]
            val str = saveImage(message[Image]!!.queryUrl())
            if(value == "[图片]")
                keywordMap[key]!!.remove(str)
            else
                keywordMap[key]!!.remove(value)
            if(keywordMap[key].isNullOrEmpty())
                keywordMap.remove(key)
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
/*
下载图片 并保存为$<md5>.jpg
 */
@Throws(IOException::class)
fun saveImage(imageUrl: String?) : String{
    var img:File = File("src/img/groupImg/temp.jpg")
    if(!img.exists())
        img.createNewFile()
    val url = URL(imageUrl)
    val input = url.openStream()
    val output: OutputStream = FileOutputStream(img)
    val b = ByteArray(2048)
    var length: Int
    var md = MessageDigest.getInstance("MD5")
    while (input.read(b).also { length = it } != -1) {
        md.update(b, 0, length)
        output.write(b, 0, length)
    }
    input.close()
    output.close()
    val md5Bytes = md.digest()
    val bigInt :BigInteger = BigInteger(1, md5Bytes)
    img.renameTo(File("src/img/groupImg/"+"$"+bigInt.toString(16)+".jpg"))
    return ("$"+bigInt.toString(16))
}