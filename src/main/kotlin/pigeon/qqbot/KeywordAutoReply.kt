package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.queryUrl
import net.mamoe.mirai.message.sendAsImageTo
import org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.io.FileOutputStream
import java.net.URL


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
        if (subject.id == 1143577518L || subject.id == 596870824L) {
            for ((key, value) in keywordMap) {
                if (message.content.indexOf(key) != -1 && !message.content.startsWith("#")) {
                    val reply = value.random()
                    if (reply.startsWith("$"))
                        File("src/img/autoreply/$reply.jpg").sendAsImageTo(subject)
                    else
                        reply(reply)
                    break
                }
            }
        }
    }
    this.subscribeMessages {
        startsWith("#add", true) {
            val key = it.split(" ")[0]
            val value = it.split(" ")[1]
            if (key != "" && value != "" && message[Image] == null) {
                if (keywordMap.containsKey(key))
                    keywordMap.getValue(key).add(value)
                else
                    keywordMap[key] = mutableListOf(value)
                saveAutoReplyList()
                reply("添加\"${value}\"到\"${key}\"")
            } else if (message[Image] != null) {
                val str = saveImg(message[Image]!!.queryUrl(), "autoreply")
                if (keywordMap.containsKey(key)) {
                    if (!(keywordMap.getValue(key).contains(str)))
                        keywordMap.getValue(key).add(str)
                } else
                    keywordMap[key] = mutableListOf(str)
                saveAutoReplyList()
                reply("添加\"${value}\"到\"${key}\"")
            }
        }
        startsWith("#del", true) {
            val key = it.split(" ")[0]
            val value = it.split(" ")[1]
            if (message[Image] != null) {
                val str = getMD5(message[Image]!!.queryUrl())
                keywordMap[key]?.remove(str)
            } else {
                keywordMap[key]?.remove(value)
            }
            if (keywordMap[key].isNullOrEmpty()) {
                keywordMap.remove(key)
            }
            saveAutoReplyList()
            reply("删除\"$it\"")
        }
        startsWith("#list ", true) { it ->
            var rpl = ""
            if (keywordMap.containsKey(it)) {
                keywordMap[it]!!.forEach {
                    rpl += "$it\n"
                }
                reply(rpl)
            }
        }
    }
}

fun saveAutoReplyList() {
    val writer = autoReplyFile.writer()

    for (pair in keywordMap) {
        var str = ""
        str += pair.key + " "
        for (word in pair.value)
            str += "$word "
        str.trim()
        str += "\n"
        writer.write(str)
    }
    writer.flush()
    writer.close()
}

/*
下载图片 并保存为$<md5>.jpg
 */
fun saveImg(imageUrl: String?, path: String): String {
    val img = File("src/img/$path/temp.jpg")
    val parentFolder = File("src/img/$path")
    if (!parentFolder.exists())
        parentFolder.mkdirs()
    if (!img.exists())
        img.createNewFile()
    val input = URL(imageUrl).openStream()
    val output = FileOutputStream(img)
    input.copyTo(output)
    val md5 = DigestUtils.md5Hex(input)
    input.close()
    output.close()
    img.renameTo(File("src/img/$path/$$md5.jpg"))
    return ("$$md5")
}

fun getMD5(imageUrl: String?): String = DigestUtils.md5Hex(URL(imageUrl).openStream())