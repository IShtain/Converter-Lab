package com.shtainyky.converterlab.activities.models.modelUI;


import java.util.Map;

public class OrganizationUI {
    private String name;
    private String phone;
    private String address;
    private String link;
    private String regionName;
    private String cityName;
    private Map<String, CurrencyUI> currencies;

    public class CurrencyUI {
        private double ask;
        private double bid;

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

    public Map<String, CurrencyUI> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, CurrencyUI> currencies) {
        this.currencies = currencies;
    }
}
