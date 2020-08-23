package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.sendAsImageTo
import net.mamoe.mirai.message.uploadAsImage
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.toExternalImage
import net.mamoe.mirai.utils.upload
import java.io.File
import kotlin.math.roundToInt


suspend fun main() {
    val qqId = 1L//Bot的QQ号，需为Long类型，在结尾处添加大写L
    val password = "!"//Bot的密码
    val miraiBot = Bot(qqId, password).alsoLogin()//新建Bot并登录
    miraiBot.subscribeMessages {
        "你好" reply "你好!"
        "#查询二次元浓度" reply "${(Math.random()*100)}%" as String
        case("at me") {
            reply(At(sender as Member) + " 给爷爬 ")
        }

        (contains("舔") or contains("veraku",true)) {
            reply("veraku是神")
        }
        (contains("nmsl")) {
            reply("nmysl")
        }
       // (contains("#神话语录")){
         //   reply(randomMythQuote().uploadAsImage())
       // }
        case("芳芳"){
            reply("芳芳是神")
        }
        case("神话语录"){
            randomImg("mythquotes").sendAsImageTo(subject)
        }
        case("bfm"){
            randomImg("cats").sendAsImageTo(subject)
        }
    }
    miraiBot.join() // 等待 Bot 离线, 避免主线程退出
}


fun randomImg(path:String) : File {
    val PATH = "src/img/$path"
    val file = File(PATH)
    var fs = file.listFiles()
    return fs.random()
}
