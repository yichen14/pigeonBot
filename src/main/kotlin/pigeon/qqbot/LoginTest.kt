package pigeon.qqbot

import kotlinx.coroutines.delay
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.sendAsImageTo
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.File

data class Config(var qqID: Long = 0, var password: String = "")

const val configPath = "src/main/resources/config.yml"

suspend fun main() {
    val config = Yaml(Constructor(Config::class.java)).load(File(configPath).inputStream()) as Config
    val qqId = config.qqID//Bot的QQ号，需为Long类型，在结尾处添加大写L
    val password = config.password//Bot的密码
    val miraiBot = Bot(qqId, password) {
        fileBasedDeviceInfo()
    }.alsoLogin()//新建Bot并登录
    miraiBot.keywordReply()
    miraiBot.randomRepeat()
    miraiBot.welcome()
    miraiBot.keywordAutoReply()
    delay(2000L)
    miraiBot.LiveStreamDetect(3484)
    miraiBot.openGame()
    miraiBot.quote()
    miraiBot.push()
    miraiBot.setu()
    miraiBot.join() // 等待 Bot 离线, 避免主线程退出
}

fun randomImg(path: String) = File("src/img/$path").listFiles()?.random()

fun Bot.keywordReply() {
    this.subscribeMessages {
        case("#查询二次元浓度") {
            reply("${(0..100).random()}%")
        }
        contains("nmsl") {
            reply("nmysl")
        }
        case("芳芳") {
            reply("芳芳是神")
        }
        (contains("技校") or contains("废物")) {
            reply("虚伪b快爬")
        }
        (contains("fm") or contains("bfm")) {
            randomImg("cats")?.sendAsImageTo(subject)
        }
    }
}

fun Bot.randomRepeat() {
    this.subscribeAlways<GroupMessageEvent> {
        if ((1..50).random() == 1) {
            reply(message)//2%概率复读
        }
    }
}

fun Bot.welcome() {
    this.subscribeAlways<NewFriendRequestEvent> { event ->
        if (this.fromGroupId == 596870824L) {//好友请求来自组群
            event.accept()
            delay(3000L)
            bot.getFriend(this.fromId).sendMessage("test")
        }
    }
}