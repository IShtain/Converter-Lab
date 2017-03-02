package com.shtainyky.converterlab.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.db.converters.ConvertData;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.activities.services.HttpManager;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Logger mLogger = LogManager.getLogger();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvBankName_RV)
    TextView tvBankName;

    @BindView(R.id.tvRegionName)
    TextView tvRegionName;

    @BindView(R.id.tvCityName)
    TextView tvCityName;

    @BindView(R.id.tvPhone)
    TextView tvPhone;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        HttpManager.getInstance().init();
        HttpManager.getInstance().getResponse(new HttpManager.OnResponseListener() {
            @Override
            public void onSuccess(RootModel rootModel) {
                ConvertData.insertCurrencyMap(rootModel.getCurrencies());
                ConvertData.insertCityMap(rootModel.getCities());
                ConvertData.insertRegionMap(rootModel.getRegions());
                ConvertData.insertOrganization(rootModel.getOrganizations());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<OrganizationUI> list = ConvertData.getListOrganizationsUI();
                mLogger.d(TAG, "organization.getName()=" + list.get(0).getName());
                mLogger.d(TAG, "list.get(0).getRegionName()=" + list.get(0).getRegionName());
                mLogger.d(TAG, "list.get(0).getCityName()=" + list.get(0).getCityName());
                mLogger.d(TAG, "list.get(0).getPhone()=" + list.get(0).getPhone());
                mLogger.d(TAG, "list.get(0).getAddress()=" + list.get(0).getAddress());
                Map<String, OrganizationUI.CurrencyUI> currencies = list.get(0).getCurrencies();
                for (String key : currencies.keySet())
                {
                    mLogger.d(TAG, "VaLuta key=" + key);
                    OrganizationUI.CurrencyUI currencyUI = currencies.get(key);
                    mLogger.d(TAG, "currencyUI.getAsk()=" + currencyUI.getAsk());
                    mLogger.d(TAG, "currencyUI.getBid()=" + currencyUI.getBid());

                }


            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
