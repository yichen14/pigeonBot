package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages

fun Bot.misc() {
    this.subscribeMessages {
        case("#查询二次元浓度") {
            val ecy = (0..101).random()
            if (ecy==101)
                reply("我靠，二次元浓度达到了$ecy%，爆表了！")
            else if (ecy>=99)
                reply("当前二次元浓度是……$ecy%!做二次元不丢人！")
            else if (ecy>=80)
                reply("二次元浓度居然达到了$ecy%！真有人二次元？")
            else
                reply("二次元浓度才$ecy%，群友们完全不行啊，不够二次元啊……")
        }
        case("nmsl"){
            reply("nmysl")
        }
        case("查询群内卷浓度"){
            reply("当前群内卷浓度为${(0..100).random()}%，不行啊群友这根本不够卷")
        }
    }
}