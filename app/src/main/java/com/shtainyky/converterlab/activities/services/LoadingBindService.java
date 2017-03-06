package com.shtainyky.converterlab.activities.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.shtainyky.converterlab.activities.db.converter.ConvertData;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.activities.services.serverconection.HttpManager;

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
        HttpManager.getInstance().init();
        HttpManager.getInstance().getResponse(new HttpManager.OnResponseListener() {
            @Override
            public void onSuccess(RootModel rootModel) {
                mLogger.d(TAG, "onSuccess");
                String oldDate = StoreData.getDate();
                if (rootModel.getDate().equals(oldDate)) {
                    ConvertData.convertDate(rootModel.getDate());
                    StoreData.insertDate();
                    mLogger.d(TAG, "oldDate -- > " + oldDate);
                } else {
                    ConvertData.convertRootModelForStoring(rootModel);
                    StoreData.saveData();
                    mLogger.d(TAG, "NEW DATE rootModel.getDate() -- > " + rootModel.getDate());
                }
                sendMessage();
            }

            @Override
            public void onError(String message) {
                mLogger.d(TAG, "message -- > " + message);
            }
        });


    }

    public class MyBinder extends Binder {
        public LoadingBindService getService() {
            return LoadingBindService.this;
        }
    }

    private void sendMessage() {
        mLogger.d(TAG, "sendMessage");
        Intent intent = new Intent("custom-event-name");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
