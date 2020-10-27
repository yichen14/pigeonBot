package pigeon.qqbot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.sendAsImageTo

fun Bot.Help(){
    this.subscribeMessages {
        case("#help"){
            reply("鸽舍bot说明:\n" +
                    "1)\t欢迎功能: 进群新人可以加bot好友，bot会发欢迎信息。\n" +
                    "2)\t催更功能: 群内发 #催更，bot会自动发催更信息。\n" +
                    "3)\t随机复读功能: 在群内发消息，bot有一定概率复读群友发的消息。\n" +
                    "4)\t语录功能: 发送 #语录 @XX（语录的主人）语录截图 到群里。bot会接受并回复添加成功。 发送 #语录 @XX(语录的主人)，bot会随机回复该群友的一条语录。查询语录还在开发。\n" +
                    "5)\t查询二次元浓度功能: 发送 #查询二次元浓度，bot会回复群内二次元浓度。\n" +
                    "6)\t关键词回复功能: 如果群友的消息中有某些关键词，bot有概率自ying动yang回guai复qi你。\n" +
                    "7)\t玩机器开播查询功能: 神话是可恶的玩小酱，玩大人开播需要第一时间提醒，bot会在玩大人开播时自动发送开播时间和直播标题。\n" +
                    "8)\t自助色图功能: 发送 #色图+想看的色图的关键词，bot会回复根据关键词搜索色图并发给群友。涩图虽好，群友们可要注意别冲太多哦。\n" +
                    "9)\t发猫功能: 发fm或者bfm，bot会自动发猫图。\n")
        }
    }
}