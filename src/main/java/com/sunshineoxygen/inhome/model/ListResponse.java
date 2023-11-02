package com.sunshineoxygen.inhome.model;

import java.util.List;

public class ListResponse<T> {

    Long count;
    List<T> items;
    private int page;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}