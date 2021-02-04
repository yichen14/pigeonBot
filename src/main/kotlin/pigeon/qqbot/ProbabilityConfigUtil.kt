package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages

var probabilities = mutableMapOf<String, Double>()
fun Bot.registerProbability(name: String, defaultPossibility: Double = 0.0) {
    if (probabilities.contains(name))
        return
    probabilities[name] = defaultPossibility
    subscribeMessages {
        startsWith("#config $name", true) {
            if (it.isBlank())
                reply("${name}概率为${probabilities[name]}")
            else {
                val value = it.toDouble()
                if (value in 0.0..100.0) {
                    probabilities[name] = value
                    reply("${name}概率改为$value")
                }
            }
        }
    }
}

fun getProbability(name: String): Double = probabilities[name]!!

