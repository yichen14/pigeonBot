package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.queryUrl
import net.mamoe.mirai.message.sendAsImageTo

fun Bot.quote() {
    this.subscribeMessages {
        startsWith("#上传语录") {
            val img = message[Image]
            val at = message[At]
            saveImg(img?.queryUrl(), "${at?.target}quotes")
        }
        startsWith("#语录") {
            randomImg("${message[At]?.target}quotes")?.sendAsImageTo(subject)
        }
    }
}