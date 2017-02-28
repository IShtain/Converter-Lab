package com.shtainyky.converterlab.activities.models.modelRetrofit.organization;

import java.util.List;

public class CurrencyBand {
    private List<CurrencyType> mCurrenciesType;

    public List<CurrencyType> getCurrenciesType() {
        return mCurrenciesType;
    }

    public void setCurrenciesType(List<CurrencyType> currenciesType) {
        mCurrenciesType = currenciesType;
    }
}
