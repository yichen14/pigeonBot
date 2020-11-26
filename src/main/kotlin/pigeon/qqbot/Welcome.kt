package pigeon.qqbot

import kotlinx.coroutines.delay
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.event.subscribeAlways

fun Bot.welcome() {
    this.subscribeAlways<NewFriendRequestEvent> {
        if (this.fromGroupId == 596870824L) {//好友请求来自组群
            it.accept()
            delay(3000L)
            bot.getFriend(it.fromId).sendMessage("test")
        }
    }

    this.subscribeAlways<MemberJoinEvent> {
        bot.getGroup(it.group.id).sendMessage("欢迎新人！群地位-1")
    }
}