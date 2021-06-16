package com.example.currencyconverter;

import android.util.Log;

import java.math.BigDecimal;

public class BUS_convert {

    public String getValue(String fr,String to,String h)
    {
        DAO_convert  dao_convert = new DAO_convert();
        String vl=dao_convert.getValue(fr,to);

        double dv=Double.parseDouble(vl);

        BigDecimal bv=new BigDecimal(String.valueOf(dv));
        BigDecimal bh=new BigDecimal(h);
        BigDecimal bx=bv.multiply(bh);
        String tmp=String.format("%,.2f",bx);

        return tmp;
    }
}
