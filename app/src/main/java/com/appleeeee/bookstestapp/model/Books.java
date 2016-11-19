package com.appleeeee.bookstestapp.model;

import java.util.ArrayList;
import java.util.List;

public class Books {

    private List<Item> items = new ArrayList<Item>();

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
