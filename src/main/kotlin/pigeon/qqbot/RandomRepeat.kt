package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import kotlin.random.Random

fun Bot.randomRepeat() {
    registerProbability("repeat")
    this.subscribeAlways<GroupMessageEvent> {
        if (Random.nextDouble(1.0,100.0) <= getProbability("repeat")) {
            reply(message)
        }
    }
}