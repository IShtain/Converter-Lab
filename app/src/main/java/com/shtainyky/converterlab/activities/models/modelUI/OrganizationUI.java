package com.shtainyky.converterlab.activities.models.modelUI;


import java.util.List;
import java.util.Map;

public class OrganizationUI {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String link;
    private String regionName;
    private String cityName;
    private List<CurrencyUI> currencies;

    public class CurrencyUI {
        private String currencyId;
        private String name;
        private double ask;
        private double diffAsk;
        private double bid;
        private double diffBid;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getAsk() {
            return ask;
        }

        public double getBid() {
            return bid;
        }

        public void setAsk(double ask) {
            this.ask = ask;
        }

        public void setBid(double bid) {
            this.bid = bid;
        }

        public double getDiffAsk() {
            return diffAsk;
        }

        public void setDiffAsk(double diffAsk) {
            this.diffAsk = diffAsk;
        }

        public double getDiffBid() {
            return diffBid;
        }

        public void setDiffBid(double diffBid) {
            this.diffBid = diffBid;
        }

        public String getCurrencyId() {
            return currencyId;
        }

        public void setCurrencyId(String currencyId) {
            this.currencyId = currencyId;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<CurrencyUI> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<CurrencyUI> currencies) {
        this.currencies = currencies;
    }
}
