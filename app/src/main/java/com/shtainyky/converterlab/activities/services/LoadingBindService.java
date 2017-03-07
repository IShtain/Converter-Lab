package com.shtainyky.converterlab.activities.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.db.converter.ConvertData;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.activities.services.serverconection.HttpManager;
import com.shtainyky.converterlab.activities.util.Constants;
import com.shtainyky.converterlab.activities.util.NotificationAboutLoading;
import com.shtainyky.converterlab.activities.util.Util;

public class LoadingBindService extends Service {
    private static final String TAG = "LoadingBindService";
    private Logger mLogger = LogManager.getLogger();
    private final IBinder mBinder = new MyBinder();

    public LoadingBindService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadAndSaveData();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
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
                        mLogger.d(TAG, "NEW DATE rootModel.getDate() -- > " + rootModel.getDate());
                    }
                    sendMessage(Constants.SERVICE_USER_HAS_INTERNET);
                }
                @Override
                public void onError(String message) {
                    if (oldDate.equals(Constants.DATABASE_NOT_CREATED))
                        sendMessage(Constants.SERVICE_USER_HAS_NOT_CREATED_DB_AND_INTERNET);
                    else
                        sendMessage(Constants.SERVICE_USER_HAS_NOT_INTERNET);
                    mLogger.d(TAG, "message -- > " + message);
                }
            });
        }
        else
        {
            if (oldDate.equals(Constants.DATABASE_NOT_CREATED))
                sendMessage(Constants.SERVICE_USER_HAS_NOT_CREATED_DB_AND_INTERNET);
            else
                sendMessage(Constants.SERVICE_USER_HAS_NOT_INTERNET);

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
