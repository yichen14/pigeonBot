package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import kotlin.random.Random.Default.nextInt

fun Bot.roll(){
    this.subscribeMessages {
        startsWith("#r ",true){
            val times = it.split("d",ignoreCase = true, limit = 0)[0].toInt()
            val dice = it.split("d", ignoreCase = true, limit = 0)[1].toInt()
            var output = 0
            for (i in 1..times){
                output += nextInt(1, dice)
            }
            reply(output.toString())
        }
    }
}