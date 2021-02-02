package pigeon.qqbot

import com.beust.klaxon.Klaxon
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.random.Random


const val API_URL = "https://lab.magiconch.com/api/nbnhhsh/guess"

data class hhshData(
    val name: String,
    val trans: List<String>
)
fun Bot.nbnhhsh() {
    var nbnhhshPossibility = 100.0
    val pattern = "[\\W+]".toRegex()
    this.subscribeAlways<GroupMessageEvent> {
        if (Random.nextDouble(0.0,100.0) <= nbnhhshPossibility){
            if (!message.content.startsWith("#") && message.content.matches(pattern))
                reply(guess(message.content))
        }
    }

}

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
    val result = Klaxon().parse<hhshData>(response)
    val roll = Random.nextInt(0,result!!.trans.size)
    return result!!.trans[roll]
}