package pigeon.qqbot

import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeMessages



suspend fun main() {
    val qqId = 12412L//Bot的QQ号，需为Long类型，在结尾处添加大写L
    val password = "123124"//Bot的密码
    val miraiBot = Bot(qqId, password).alsoLogin()//新建Bot并登录
    miraiBot.subscribeMessages {
        "你好" reply "你好!"
        //"#查询二次元浓度" reply "${(Math.random()*100)}%"
        case("at me") {
            reply(At(sender as Member) + " 给爷爬 ")
        }

        (contains("舔") or contains("veraku",true)) {
            reply("veraku是神")
        }
        (contains("nmsl")) {
            reply("nmysl")
        }

    }
    miraiBot.join() // 等待 Bot 离线, 避免主线程退出
}