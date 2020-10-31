package pigeon.qqbot

import java.io.File


data class QuoteClass(var md5: String,
                      var memberQQ: Long,
                      var content: String = ocr(File("src/img/${memberQQ}quotes/${md5}.jpg"))){

    fun printInfo():String{
        val info = "Quote Object Info: memberQQ["+this.memberQQ+"]; md5["+this.md5+"]; content["+this.content+"]"
        return info
    }
}


/*
class QuoteClass {
    private var content = "";
    private var md5 = "";
    private var memberQQ = 0L;

    constructor(md5: String, memberQQ: Long){
        this.md5 = md5;
        this.memberQQ = memberQQ;
        this.content = ocr(File("src/img/${memberQQ}quotes/${md5}.jpg"))
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
    /**
    * Using OCR to recognize word content
    */
    /*
    private fun setContent(){
        this.content = ocr(File("src/img/${memberQQ}quotes/${md5}.jpg"))
    }
     */
}*/