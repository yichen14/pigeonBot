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

