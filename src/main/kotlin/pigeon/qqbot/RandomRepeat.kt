package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.GroupMessageEvent
import kotlin.random.Random

fun Bot.randomRepeat() {
    var randomRepeatProbability = 0.6
    this.subscribeAlways<GroupMessageEvent> {
        if (Random.nextDouble(1.0,100.0) <= randomRepeatProbability) {
            reply(message)
        }
    }
    this.subscribeMessages {
        startsWith("#config ", true) {
            val key = it.split(" ")[0]
            val value = it.split(" ")[1]
            if (key == "repeatP" && value.toDouble() in 0.0..100.0) {
                randomRepeatProbability = value.toDouble()
                reply("自动复读概率改为$value%")
            }
        }
    }
}