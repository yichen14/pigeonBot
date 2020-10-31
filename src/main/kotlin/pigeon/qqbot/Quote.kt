package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.queryUrl
import net.mamoe.mirai.message.sendAsImageTo
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val json = File("QuoteConfig.json").readText()
val type = object : TypeToken<MutableList<QuoteClass>>(){}.type
val QuoteList = Gson().fromJson<MutableList<QuoteClass>>(json,type)

fun Bot.quote() {
    this.subscribeMessages {
        startsWith("#上传语录") {
            if(message[At]!=null && message[Image] != null) {
                val img = message[Image]
                val at = message[At]
                val md5 = saveImg(img?.queryUrl(), "${at?.target}quotes")
                val q = QuoteClass(md5,at!!.target)
                QuoteList.add(q)
                File("QuoteConfig.json").writeText(Gson().toJson(QuoteList))
                reply("添加成功")
            }else{
                reply("添加失败 缺少图片或用户信息")
            }
        }
        startsWith("#语录") {
            if(message[At]!=null)
                randomImg("${message[At]?.target}quotes")?.sendAsImageTo(subject)
        }
        startsWith("#搜索语录"){
            val keyWord = it.split(" ")[0]
            //val ImageList = mutableListOf<QuoteClass>()
            for(quote in QuoteList){

               if(quote.content.contains(keyWord, ignoreCase = true)){
                    //ImageList.add(quote)
                    val path = "${quote.memberQQ}quotes"
                    val md5 = quote.md5
                    reply(quote.printInfo())
                    //File("src/img/$path/$$md5.jpg").sendAsImageTo(subject)
                }
            }
/*            val img = ImageList.random();
            val path = "${img.getMemberQQ()}quotes"
            val md5 = img.getMd5()
            File("src/img/$path/$$md5.jpg").sendAsImageTo(subject)*/
        }
    }
}