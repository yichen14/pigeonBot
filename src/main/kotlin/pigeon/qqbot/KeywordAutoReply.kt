package pigeon.qqbot

import com.beust.klaxon.Klaxon
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
import kotlin.random.Random

const val autoReplyFilePath = "src/main/resources/autoReply.json"
val autoReplyFile = File(autoReplyFilePath)

fun Bot.keywordAutoReply() {
    registerProbability("reply")
    val keywordMap = Klaxon().parse<MutableMap<String, MutableList<String>>>(autoReplyFile)!!
    this.subscribeAlways<GroupMessageEvent> {
        if (subject.id == 1143577518L || subject.id == 596870824L)
            for ((key, value) in keywordMap) {
                if (Random.nextDouble(1.0, 100.0) <= getProbability("reply")) {
                    if (message.content.contains(key) && !message.content.startsWith("#")) {
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
                    keywordMap[key]?.add(value)
                else
                    keywordMap[key] = mutableListOf(value)
                autoReplyFile.writeText(Klaxon().toJsonString(keywordMap))
                reply("添加\"${value}\"到\"${key}\"")
            } else if (message[Image] != null) {
                val str = saveImg(message[Image]!!.queryUrl(), "autoreply")
                if (keywordMap.containsKey(key)) {
                    if (!(keywordMap.getValue(key).contains(str)))
                        keywordMap.getValue(key).add(str)
                } else
                    keywordMap[key] = mutableListOf(str)
                autoReplyFile.writeText(Klaxon().toJsonString(keywordMap))
                reply("添加\"${value}\"到\"${key}\"")
            }
        }
        startsWith("#del", true) {
            val key = it.split(" ")[0]
            val value = it.split(" ")[1]
            if (message[Image] != null) {
                val str = getMD5(message[Image]!!.queryUrl())
                keywordMap[key]?.remove(str)
            } else
                keywordMap[key]?.remove(value)
            if (keywordMap[key].isNullOrEmpty())
                keywordMap.remove(key)
            reply("删除\"$it\"")
        }
        startsWith("#list ", true) {
            reply(
                if (it.isNotBlank()) {
                    if (keywordMap.containsKey(it))
                        keywordMap[it].toString()
                    else
                        "未找到关键字$it"
                } else
                    keywordMap.keys.toString()
            )
        }
    }
}


/**
下载图片 并保存为$md5.jpg
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
    input.close()
    output.close()
    val md5: String
    img.inputStream().let {
        md5 = DigestUtils.md5Hex(it)
        it.close()
    }
    img.renameTo(File("src/img/$path/$$md5.jpg"))
    return ("$$md5")
}

fun getMD5(imageUrl: String?): String = DigestUtils.md5Hex(URL(imageUrl).openStream())

fun randomImg(path: String) = File("src/img/$path").listFiles()?.random()
