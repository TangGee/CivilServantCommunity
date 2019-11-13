package com.mdove.civilservantcommunity.plan.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.feed.MainFeedActivity

/**
 * Created by MDove on 2019-11-13.
 */
class TimeScheduleReceiver :BroadcastReceiver() {
    companion object{
        const val NOTIFICATION_ID = 1000
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        if (intent?.action.equals("NOTIFICATION")) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent2 = Intent(context, MainFeedActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0)
            val notify =  NotificationCompat.Builder(context,"1000")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("您的***项目即将到期，请及时处理！")
                .setContentTitle("项目到期提醒")
                .setStyle(NotificationCompat.BigTextStyle().bigText("此处注明的是有关需要提醒项目的某些重要内容"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setNumber(1).build()
            manager.notify(NOTIFICATION_ID, notify)
        }
    }
}