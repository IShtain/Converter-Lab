package com.shtainyky.converterlab.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.db.converter.ConvertData;
import com.shtainyky.converterlab.db.storedata.StoreData;
import com.shtainyky.converterlab.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.service.serverconection.HttpManager;
import com.shtainyky.converterlab.util.Util;
import com.shtainyky.converterlab.util.logger.LogManager;
import com.shtainyky.converterlab.util.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.shtainyky.converterlab.util.Constants.DATABASE_NOT_CREATED;
import static com.shtainyky.converterlab.util.Constants.SERVICE_IS_BINDER;
import static com.shtainyky.converterlab.util.Constants.SERVICE_MESSAGE_USER_HAS_INTERNET;
import static com.shtainyky.converterlab.util.Constants.SERVICE_MESSAGE_USER_HAS_NOT_CREATED_DB_AND_INTERNET;
import static com.shtainyky.converterlab.util.Constants.SERVICE_MESSAGE_USER_HAS_NOT_INTERNET;

public class LoadingBindService extends Service {
    private static final String TAG = "LoadingBindService";
    private static Logger mLogger = LogManager.getLogger();
    private IBinder mBinder;
    private ArrayList<OnDataLoadedListener> mListeners;


    public LoadingBindService() {
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
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

    public static boolean isServiceAlarmOn(Context context) {
        mLogger.d(TAG, "isServiceAlarmOn isOn -- > ");
        if (context != null) {
            Intent i = new Intent(context, LoadingBindService.class);
            PendingIntent pi = PendingIntent
                    .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
            return pi != null;
        }
        else  return true;
    }

    @Override
    public void onCreate() {
        mLogger.d(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mLogger.d(TAG, "onDestroy");
        if (mListeners != null)
            mListeners.clear();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        mLogger.d(TAG, "onBind");
        if (intent.getBooleanExtra(SERVICE_IS_BINDER, false)) {
            mBinder = new MyBinder();
            return mBinder;
        } else {
            return null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBinder = null;
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLogger.d(TAG, "onStartCommand");
        if (mBinder == null)
            loadAndSaveData();
        else
            stopSelf();
        return START_NOT_STICKY;
    }

    public void loadAndSaveData() {
        final String oldDate = StoreData.getInstance().getDate();
        if (Util.isOnline(getApplicationContext())) {
            HttpManager.getInstance().init();
            HttpManager.getInstance().getResponse(new HttpManager.OnResponseListener() {
                @Override
                public void onSuccess(RootModel rootModel) {
                    mLogger.d(TAG, "onSuccess");
                    if (rootModel.getDate().equals(oldDate)) {
                        ConvertData.convertDate(rootModel.getDate());
                        StoreData.getInstance().insertDate(rootModel.getDate());
                        mLogger.d(TAG, "oldDate -- > " + oldDate);
                        sendFailureMessage(oldDate);
                    } else {
                        mLogger.d(TAG, "NEW DATE rootModel.getDate() -- > " + rootModel.getDate());
                        StoreData.getInstance().saveData(rootModel, new StoreData.OnAllDBTransactionFinishedListener() {
                            @Override
                            public void onSuccess() {
                                Util.sendNotification(getApplicationContext(), getString(R.string.data_update), 0);
                                sendData();
                            }

                            @Override
                            public void onError() {
                                sendFailureMessage(oldDate);
                            }
                        });
                    }
                }

                @Override
                public void onError(String message) {
                    mLogger.d(TAG, "message -- > " + message);
                    sendFailureMessage(oldDate);
                }
            });
        } else {
            sendFailureMessage(oldDate);
        }


    }

    private void sendData() {
        if (mBinder == null) {
            mLogger.d(TAG, "sendDataorstopSelf -- > ");
            stopSelf();
        }
        if (mListeners == null) return;
        for (OnDataLoadedListener listener : mListeners) {
            mLogger.d(TAG, "sendData working-- > ");
            if (listener != null) {
                List<OrganizationUI> organizationUIs = StoreData.getInstance().getListOrganizationsUI();
                mLogger.d(TAG, "OnDataLoadedListener list.size()-- > " + organizationUIs.size());
                listener.onUpdateDB(organizationUIs);
            }
        }
    }

    private void sendFailureMessage(String oldDate) {
        mLogger.d(TAG, "sendFailureMessage working-- > ");
        if (mBinder == null) {
            mLogger.d(TAG, "sendFailureMessageorstopSelf -- > ");
            stopSelf();
        }
        if (mListeners == null) return;
        for (OnDataLoadedListener listener : mListeners) {
            if (listener != null) {
                if (oldDate.equals(DATABASE_NOT_CREATED)) {
                    listener.onFailure(SERVICE_MESSAGE_USER_HAS_NOT_CREATED_DB_AND_INTERNET);
                } else {
                    if (Util.isOnline(getApplicationContext())) {
                        listener.onFailure(SERVICE_MESSAGE_USER_HAS_INTERNET);
                    } else {
                        listener.onFailure(SERVICE_MESSAGE_USER_HAS_NOT_INTERNET);
                    }
                }
            }
        }
    }

    public class MyBinder extends Binder {
        public LoadingBindService getService() {
            return LoadingBindService.this;
        }
    }

    public void addOnSomeListener(OnDataLoadedListener listener) {
        if (mListeners == null)
            mListeners = new ArrayList<>();

        mListeners.add(listener);
    }

    public void removeOnSomeListener(OnDataLoadedListener listener) {
        mListeners.remove(listener);
    }

    public interface OnDataLoadedListener {
        void onUpdateDB(List<OrganizationUI> updatedOrganizationUIs);

        void onFailure(String message);
    }
}
