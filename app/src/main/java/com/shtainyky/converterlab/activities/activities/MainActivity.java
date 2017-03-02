package com.shtainyky.converterlab.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.db.converters.ConvertData;
import com.shtainyky.converterlab.activities.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.activities.services.HttpManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
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
                List<OrganizationUI> list = ConvertData.getListOrganizationsUI();
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
