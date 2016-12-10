package monepla.co.jp.cacom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.widget.RemoteViews;

public class WidgetReceiver extends BroadcastReceiver {
    public WidgetReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if (intent.getAction().equals("UPDATE_WIDGET")) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.plus_app_widget);
// もう一回クリックイベントを登録(毎回登録しないと上手く動かず)

            remoteViews.setOnClickPendingIntent(R.id.appwidget_text, PlusAppWidget.clickButton(context,R.id.appwidget_text));

            PlusAppWidget.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
        }
    }
}
