package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.sendAsImageTo
import java.io.File

fun Bot.help(){
    this.subscribeMessages {
        case("#help"){
            File("src/main/resources/help.jpg").sendAsImageTo(subject)
        }
    }
}