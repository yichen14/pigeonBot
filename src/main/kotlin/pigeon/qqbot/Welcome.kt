package pigeon.qqbot

import kotlinx.coroutines.delay
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.event.subscribeAlways

fun Bot.welcome() {
    this.subscribeAlways<NewFriendRequestEvent> { event ->
        if (this.fromGroupId == 596870824L) {//好友请求来自组群
            event.accept()
            delay(3000L)
            bot.getFriend(this.fromId).sendMessage("test")
        }
    }
}