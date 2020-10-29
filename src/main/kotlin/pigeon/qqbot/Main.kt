package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.File

data class Config(var qqID: Long = 0, var password: String = "")

const val configPath = "src/main/resources/config.yml"

suspend fun main() {
    val config = Yaml(Constructor(Config::class.java)).load(File(configPath).inputStream()) as Config
    val qqId = config.qqID//Bot的QQ号，需为Long类型，在结尾处添加大写L
    val password = config.password//Bot的密码
    val pigeonBot = Bot(qqId, password) { fileBasedDeviceInfo() }.alsoLogin()//新建Bot并登录
    pigeonBot.liveStreamDetect(6979222, "wjqbl")
    pigeonBot.catImage()
    pigeonBot.randomRepeat()
    pigeonBot.welcome()
    pigeonBot.keywordAutoReply()
    pigeonBot.openGame()
    pigeonBot.quote()
    pigeonBot.push()
    pigeonBot.setu()
    pigeonBot.misc()
    pigeonBot.help()
    pigeonBot.smartReply()
    pigeonBot.tokenizerTest()
    //miraiBot.ocr() //测试中，暂不上线
    pigeonBot.join() // 等待 Bot 离线, 避免主线程退出
}
