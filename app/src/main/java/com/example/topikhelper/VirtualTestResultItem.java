package com.example.topikhelper;

public class VirtualTestResultItem {
    private String index;
    private String my;
    private String op;
    private String q;


    public void setIndex(String index){this.index=index;}
    public void setMy(String my) {
        this.my = my;
    }
    public void setOp(String op) {
        this.op = op;
    }
    public void setQ(String q) {
        this.q = q;
    }

    public String getIndex(){
        return this.index;
    }
    public String getMy() {
        return this.my;
    }
    public String getOp() {
        return this.op;
    }
    public String getQ() {
        return this.q;
    }

}