package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.sendAsImageTo

fun Bot.catImage() {
    this.subscribeMessages {
        (contains("fm") or contains("fdm")) {
            randomImg("cats")?.sendAsImageTo(subject)
        }
    }
}