package pigeon.qqbot

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.util.CoreMap
import jdk.xml.internal.SecuritySupport.getResourceAsStream
//import jdk.jfr.internal.SecuritySupport.getResourceAsStream
//import com.sun.org.apache.bcel.internal.util.SecuritySupport.getResourceAsStream
import java.util.*
import kotlin.collections.ArrayList

//language package included in build.gradle already

/***********************************************************************************************************************
 ************************************************LANG PACKAGE NEEDED****************************************************
 *******************http://nlp.stanford.edu/software/stanford-corenlp-4.0.0-models-chinese.jar**************************
 ***********************************************************************************************************************/

val pipeline:StanfordCoreNLP = StanfordCoreNLP(getProps())
fun getProps():Properties{
    val props = Properties()
    props.load(getResourceAsStream("StanfordCoreNLP-chinese.properties"));
    return props
}

//return 2 dimensional mutable list
//0th col:word; 1st:PartOfSpeech; 2nd NamedEntityAnnotation
fun tokenizer(chat: String):MutableList<MutableList<String>> {//input:chat MSG
    val annotation = Annotation(chat)

    val result: MutableList<MutableList<String>> = ArrayList()

    pipeline.annotate(annotation)
    val sentences: List<CoreMap> = annotation.get(SentencesAnnotation::class.java)

    for (sentence: CoreMap in sentences) {
        for (token: CoreLabel in sentence.get(CoreAnnotations.TokensAnnotation::class.java)) {
            val temp: MutableList<String> = ArrayList()

            val word: String = token.get(CoreAnnotations.TextAnnotation::class.java)
            temp.add(word)

            val pos: String = token.get(CoreAnnotations.PartOfSpeechAnnotation::class.java)
            temp.add(pos)

            val ner: String = token.get(CoreAnnotations.NamedEntityTagAnnotation::class.java)
            temp.add(ner)

            result.add(temp)
        }
    }
    return result
}

/*
fun xwbClassifierEntrance(chat:String):Boolean{
    return classifier(dataPreprocess(chat, ::xwbPreprocess))
}

 */


//method selection
/*
fun dataPreprocess(x: String, method: (MutableList<MutableList<String>>)->IntArray): IntArray{
    return method(tokenizer(x))
}
*/


//vectorization
/*
fun xwbPreprocess(x: MutableList<MutableList<String>>):IntArray{

    return
}
*/

//LOGISTIC REGRESSION CLASSIFIER STARTS

//Sigmoid Proba
fun sigmoid(vec: IntArray):Double {
    val b = doubleArrayOf(1.0,2.0,3.0)//beta parameters for the model
    val x = dot(b,vec)
    return kotlin.math.log10(1 / (1 + kotlin.math.exp(-x)))
}


fun dot(v1: DoubleArray, v2: IntArray):Double {
    var vecoutput = 0.0
    for (i in 1..v1.size){
        vecoutput += v1[i-1] * v2[i-1]
    }
    return vecoutput
}

//para: vector input
fun classifier(para: IntArray):Boolean {
    return sigmoid(para) > 0.5//threshold
}

//LOGISTIC REGRESSION CLASSIFIER ENDS

/*
fun DataSave(message:String,filename:String){

}
*/
