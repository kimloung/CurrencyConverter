package com.example.currencyconverter;

import java.util.Collections;
import java.util.List;

public class BUS_item {

    public List<String> get_items()
    {
        DAO_item dao_item=new DAO_item();
        List<String> rs= dao_item.get_items();
        Collections.sort(rs);
        return rs;
    }
}
