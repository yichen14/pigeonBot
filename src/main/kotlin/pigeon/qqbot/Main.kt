package pigeon.qqbot

import com.beust.klaxon.Klaxon
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import java.io.File

data class Config(
    var qqID: Long,
    var qqPassword: String,
    var pixivToken: String,
    var probabilities: Map<String, Double>
)

const val configPath = "src/main/resources/config.json"

suspend fun main() {
    val config = Klaxon().parse<Config>(File(configPath))
    val qqId = config!!.qqID//Bot的QQ号，需为Long类型，在结尾处添加大写L
    val password = config.qqPassword//Bot的密码
    val miraiBot = Bot(qqId, password) { fileBasedDeviceInfo() }.alsoLogin()//新建Bot并登录
    for((key,value) in config.probabilities)
        miraiBot.registerProbability(key,value)
    miraiBot.liveStreamDetect(6979222, "wjqbl")
    miraiBot.catImage()
    miraiBot.randomRepeat()
    miraiBot.welcome()
    miraiBot.quote()
    miraiBot.keywordAutoReply()
    miraiBot.roll()
    miraiBot.openGame()
    miraiBot.push()
    miraiBot.setu(config.pixivToken)
    miraiBot.misc()
    miraiBot.help()
    miraiBot.smartReply()
    miraiBot.nbnhhsh()
    miraiBot.join() // 等待 Bot 离线, 避免主线程退出
}
