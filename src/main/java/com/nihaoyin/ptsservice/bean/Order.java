package com.nihaoyin.ptsservice.bean;

public class Order {
    private String src;
    private String dst;
    private String pallet;


    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public void setPallet(String pallet) {
        this.pallet = pallet;
    }

    public String getDst() {
        return dst;
    }

    @Override
    public String toString() {
        return "Order{" +
                "src='" + src + '\'' +
                ", dst='" + dst + '\'' +
                ", pallet='" + pallet + '\'' +
                '}';
    }

    public String getPallet() {
        return pallet;
    }
}
