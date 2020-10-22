package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.queryUrl
import net.mamoe.mirai.message.sendAsImageTo

fun Bot.quote() {
    this.subscribeMessages {
        startsWith("#上传语录") {
            if(message[At]!=null && message[Image] != null) {
                val img = message[Image]
                val at = message[At]
                val md5 = saveImg(img?.queryUrl(), "${at?.target}quotes")
                reply("添加成功")
            }else{
                reply("添加失败 缺少图片或用户信息")
            }
        }
        startsWith("#语录") {
            if(message[At]!=null)
                randomImg("${message[At]?.target}quotes")?.sendAsImageTo(subject)
        }
    }
}