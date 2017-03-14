package com.shtainyky.converterlab.activities.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.db.converter.ConvertData;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.activities.service.serverconection.HttpManager;
import com.shtainyky.converterlab.activities.util.Constants;
import com.shtainyky.converterlab.activities.util.NotificationAboutLoading;
import com.shtainyky.converterlab.activities.util.Util;

public class LoadingBindService extends Service {
    private static final String TAG = "LoadingBindService";
    private Logger mLogger = LogManager.getLogger();
    private final IBinder mBinder = new MyBinder();

    public LoadingBindService() {
    }

    public void setServiceAlarm(Context context, boolean isOn) {
        mLogger.d(TAG, "setServiceAlarm isOn -- > " + isOn);
        Intent i = new Intent(context, LoadingBindService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), 2 * AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }

    }

    public boolean isServiceAlarmOn(Context context) {
        mLogger.d(TAG, "isServiceAlarmOn isOn -- > ");
        Intent i = new Intent(context, LoadingBindService.class);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    @Override
    public void onCreate() {
        mLogger.d(TAG, "onCreate");
        super.onCreate();
        loadAndSaveData();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLogger.d(TAG, "onStartCommand");
        loadAndSaveData();
        stopSelf();
        return START_STICKY;
    }


    public void loadAndSaveData() {
        final String oldDate = StoreData.getDate();
        if (Util.isOnline(getApplicationContext())) {
            HttpManager.getInstance().init();
            HttpManager.getInstance().getResponse(new HttpManager.OnResponseListener() {
                @Override
                public void onSuccess(RootModel rootModel) {
                    mLogger.d(TAG, "onSuccess");
                    if (rootModel.getDate().equals(oldDate)) {
                        ConvertData.convertDate(rootModel.getDate());
                        StoreData.insertDate();
                        mLogger.d(TAG, "oldDate -- > " + oldDate);
                    } else {
                        ConvertData.convertRootModelForStoring(rootModel);
                        StoreData.saveData();
                        NotificationAboutLoading.sendNotification(getApplicationContext(), getString(R.string.data_update), 0);
                        if (oldDate.equals(Constants.DATABASE_NOT_CREATED))
                            sendMessage(Constants.SERVICE_MESSAGE_USER_HAS_FIRST_INSTALLATION);
                        else
                            sendMessage(Constants.SERVICE_MESSAGE_DATA_UPDATED);
                        mLogger.d(TAG, "NEW DATE rootModel.getDate() -- > " + rootModel.getDate());
                    }
                }
                @Override
                public void onError(String message) {
                    if (oldDate.equals(Constants.DATABASE_NOT_CREATED))
                        sendMessage(Constants.SERVICE_MESSAGE_USER_HAS_NOT_CREATED_DB_AND_INTERNET);
                    else
                        sendMessage(Constants.SERVICE_MESSAGE_USER_HAS_NOT_INTERNET);
                    mLogger.d(TAG, "message -- > " + message);
                }
            });
        } else {
            if (oldDate.equals(Constants.DATABASE_NOT_CREATED))
                sendMessage(Constants.SERVICE_MESSAGE_USER_HAS_NOT_CREATED_DB_AND_INTERNET);
            else
                sendMessage(Constants.SERVICE_MESSAGE_USER_HAS_NOT_INTERNET);
        }
    }

    public class MyBinder extends Binder {
        public LoadingBindService getService() {
            return LoadingBindService.this;
        }
    }

    private void sendMessage(String message) {
        mLogger.d(TAG, "sendMessage");
        Intent intent = new Intent(Constants.LOCAL_BROADCAST_EVENT_NAME);
        intent.putExtra(Constants.SERVICE_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
