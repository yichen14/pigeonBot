package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages

fun Bot.misc() {
    this.subscribeMessages {
        case("#查询二次元浓度") {
            reply("${(0..100).random()}%")
        }
        case("nmsl"){
            reply("nmysl")
        }
    }
}