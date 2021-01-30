package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages

fun Bot.help(){
    this.subscribeMessages {
        case("#help"){
            File("src/resources/help.jpg").sendAsImageTo(subject)
        }
    }
}