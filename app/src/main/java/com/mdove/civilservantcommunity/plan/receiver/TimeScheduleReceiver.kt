package com.mdove.civilservantcommunity.plan.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.feed.MainFeedActivity
import com.mdove.dependent.common.utils.NotificationUtils

/**
 * Created by MDove on 2019-11-13.
 */
class TimeScheduleReceiver : BroadcastReceiver() {
    companion object{
        const val ACTION = "mdove.time_schedule"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        if (intent?.action.equals(ACTION)) {
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainFeedActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                ,
                0)
            NotificationUtils.notify(555) { param ->
                param.setContentTitle("My notification")
                    .setContentText("Much longer text that cannot fit one line...")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Much longer text that cannot fit one line...")
                    ).priority =
                    NotificationCompat.PRIORITY_DEFAULT
                null
            }
        }
    }
}