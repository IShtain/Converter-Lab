package com.shtainyky.converterlab.activities.models.modelRetrofit.organization;

public class CurrencyType {
    private String id;
    private AskBid mAskBid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AskBid getAskBid() {
        return mAskBid;
    }

    public void setAskBid(AskBid askBid) {
        mAskBid = askBid;
    }

    private class AskBid {
        private int ask;
        private int bid;

        public AskBid(int ask, int bid) {
            this.ask = ask;
            this.bid = bid;
        }

        public int getAsk() {
            return ask;
        }

        public void setAsk(int ask) {
            this.ask = ask;
        }

        public int getBid() {
            return bid;
        }

        public void setBid(int bid) {
            this.bid = bid;
        }


    }

}
