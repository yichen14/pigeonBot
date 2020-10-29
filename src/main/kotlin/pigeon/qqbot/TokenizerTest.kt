package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.content

fun Bot.tokenizerTest(){
    this.subscribeMessages {
        startsWith("#分词器测试 ", true) {
            val arg1 = it.split(" ")[0]
            //val arg2 = it.split(" ")[1]
            var words: MutableList<MutableList<String>> = ArrayList()
            if(message.content.contains("-i")) {
                val arg2 = it.split(" ")[1]
                words = tokenizer(arg2, true)
            } else {
                words = tokenizer(arg1, false)
            }
            var result = "分词结果为："
            for (word in words) {
                for(i in word){
                    result+="$i "
                }
            }
            reply(result)
        }
    }
}