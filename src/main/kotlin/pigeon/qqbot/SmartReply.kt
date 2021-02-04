package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import kotlin.random.Random

fun Bot.smartReply() {
    registerProbability("smart")
    this.subscribeAlways<GroupMessageEvent> {
        if ((message.content.contains("我") || message.content.contains("你")) && !message.content.startsWith("#"))
            if (Random.nextDouble(1.0, 100.0) <= getProbability("smart")) {
                reply(message.content.replace("我", "肏").replace("你", "我").replace("肏", "你"))
            }
    }
}
