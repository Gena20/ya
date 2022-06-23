package com.enrollment.market.dto;

import java.sql.Timestamp;
import java.util.List;

public class InputImport {
    private List<InputShopUnit> items;

    private Timestamp updateDate;

    public List<InputShopUnit> getItems() {
        return items;
    }

    public void setItems(List<InputShopUnit> items) {
        this.items = items;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
}
