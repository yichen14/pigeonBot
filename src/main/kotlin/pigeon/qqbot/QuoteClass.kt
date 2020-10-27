package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.queryUrl
import net.mamoe.mirai.message.sendAsImageTo

class QuoteClass {
    private var content = "";
    private var md5 = "";
    private var memberQQ = 0L;

    constructor(md5: String, memberQQ: Long){
        this.md5 = md5;
        this.memberQQ = memberQQ;
        setContent();
    }
    fun getContent():String{
        return this.content;
    }
    fun getMemberQQ():Long{
        return this.memberQQ;
    }
    fun getMd5():String{
        return this.md5;
    }

    fun printInfo():String{
        val info = "Quote Object Info: memberQQ["+this.memberQQ+"]; md5["+this.md5+"]; content["+this.content+"]"
        return info
    }
    /*
    * Using OCR to recognize word content
    * */
    private fun setContent(){
        this.content = "test"//没写OCR，可以先测试一下list结构和搜索功能
    }

}