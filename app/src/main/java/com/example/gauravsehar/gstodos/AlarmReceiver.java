package com.example.gauravsehar.gstodos;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Gaurav Sehar on 15-Mar-18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra(Constants.NAME_KEY);
        String /*name="default",*/desc = "none";
//        if (id > 0) {
//            TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(context);
//            SQLiteDatabase sqLiteDatabase = todoOpenHelper.getReadableDatabase();
//            String[] selectionArgs = {id + ""};
//            Cursor cursor = sqLiteDatabase.query(Contract.Todo.TABLE_NAME, null, Contract.Todo.ID + " = ?", selectionArgs, null, null, null);
//            if (cursor.moveToFirst()) {
//                name = cursor.getString(cursor.getColumnIndex(Contract.Todo.NAME));
//                desc = cursor.getString(cursor.getColumnIndex(Contract.Todo.DESCRIPTION));
////                int priority = cursor.getInt(cursor.getColumnIndex(Contract.Todo.PRIORITY));
//            }
//            cursor.close();
//        }
        Toast.makeText(context, "OnReceive Called.", Toast.LENGTH_LONG).show();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // only for gingerbread and newer versions
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_KEY, "My Channel", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_KEY)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(name)
                .setContentText(desc)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        manager.notify(1, mBuilder.build());
//        NotificationCompat.Builder builder = new N
//        Notification notification = new Notification(parcel);
//        manager.notify(1,notification);

    }
}
