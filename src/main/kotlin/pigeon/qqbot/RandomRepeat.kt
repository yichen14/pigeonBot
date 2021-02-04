package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import kotlin.random.Random

fun Bot.randomRepeat() {
    registerProbability("repeat")
    subscribeAlways<GroupMessageEvent> {
        if (Random.nextDouble(0.0,100.0) <= getProbability("repeat") && !message.content.startsWith("#")) {
            reply(message)
        }
    }
}