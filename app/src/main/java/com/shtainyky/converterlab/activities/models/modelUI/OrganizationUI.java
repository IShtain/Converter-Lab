package com.shtainyky.converterlab.activities.models.modelUI;


import java.util.List;

public class OrganizationUI {
    private String mId;
    private String mName;
    private String mPhone;
    private String mAddress;
    private String mLink;
    private String mRegionName;
    private String mCityName;
    private List<CurrencyUI> mCurrencies;

    public class CurrencyUI {
        private String mCurrencyId;
        private String mName;
        private double mAsk;
        private double mDiffAsk;
        private double mBid;
        private double mDiffBid;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            this.mName = name;
        }

        public double getAsk() {
            return mAsk;
        }

        public double getBid() {
            return mBid;
        }

        public void setAsk(double ask) {
            this.mAsk = ask;
        }

        public void setBid(double bid) {
            this.mBid = bid;
        }

        public double getDiffAsk() {
            return mDiffAsk;
        }

        public void setDiffAsk(double diffAsk) {
            this.mDiffAsk = diffAsk;
        }

        public double getDiffBid() {
            return mDiffBid;
        }

        public void setDiffBid(double diffBid) {
            this.mDiffBid = diffBid;
        }

        public String getCurrencyId() {
            return mCurrencyId;
        }

        public void setCurrencyId(String currencyId) {
            this.mCurrencyId = currencyId;
        }
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public String getRegionName() {
        return mRegionName;
    }

    public void setRegionName(String regionName) {
        this.mRegionName = regionName;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        this.mCityName = cityName;
    }

    public List<CurrencyUI> getCurrencies() {
        return mCurrencies;
    }

    public void setCurrencies(List<CurrencyUI> currencies) {
        this.mCurrencies = currencies;
    }
}
