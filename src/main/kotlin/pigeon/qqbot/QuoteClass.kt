package pigeon.qqbot

import java.util.*

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

    /*
    * Using OCR to recognize word content
    * */
    private fun setContent(){
        this.content = "test"//没写OCR，可以先测试一下list结构和搜索功能
    }

}