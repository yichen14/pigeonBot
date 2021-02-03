package pigeon.qqbot


import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.random.Random


const val API_URL = "https://lab.magiconch.com/api/nbnhhsh/guess"

data class HhshData(
    val name: String,
    val trans: List<String>
)

fun Bot.nbnhhsh() {

    var nbnhhshPossibility = 1.0
    //正则表达式筛选中文
    val pattern = "[\\u4e00-\\u9fa5]".toRegex()

    this.subscribeAlways<GroupMessageEvent> {
        if (subject.id == 1143577518L || subject.id == 596870824L)
        //概率触发
            if (Random.nextDouble(0.0,100.0) <= nbnhhshPossibility){
                if (!message.content.startsWith("#") && !message.content.contains(pattern)) {
                    val hhsh = guess(message.content)
                    if (hhsh.isNotEmpty())
                        reply(hhsh)
                }
            }
    }

    this.subscribeMessages {

        //概率修改
        startsWith("#config",true){
            val key = it.split(" ")[0]
            val value = it.split(" ")[1]
            if (key == "hhshP" && value.toDouble() in 0.00..100.00) {
                nbnhhshPossibility = value.toDouble()
                reply("神必字母随机翻译概率改为$value%")
            }
        }

        //手动查询
        startsWith("#hhsh",true){
            val chouxiang = it.split(" ")[0]
            val hhsh = guess(message.content)
            if (hhsh.isNotEmpty())
                reply(hhsh)
        }
    }

}

//API调用并返回值
fun guess(text: String): String{

    val postBody = FormBody.Builder()
        .add("text", text)
        .build()

    val request = Request.Builder()
        .url(API_URL)
        .post(postBody)
        .build()

    val client = OkHttpClient()
    val response = client.newCall(request).execute().body()!!.string()
    try {
        //这里的result获得的是HhshData的List，因此通过data提取出结果。并选择随机一个翻译结果返回。
        val result = Klaxon().parseArray<HhshData>(response)
        val data: HhshData = result!![0]
        val roll = Random.nextInt(0, data.trans.size)
        return data.trans[roll]
    }catch (e: KlaxonException){
        //当无翻译结果时会抛出KlaxonException。trans为空List。
        return "这说的话也太抽象了吧，我牛津高级阴阳字典都翻烂了，建议以后少点谜语人！"
    }catch (e: IndexOutOfBoundsException){
        //当输入的文本不符合规范（例如只有标点符号）时会抛出IndexOutOfBoundsException。返回空值。
        return ""
    }

}