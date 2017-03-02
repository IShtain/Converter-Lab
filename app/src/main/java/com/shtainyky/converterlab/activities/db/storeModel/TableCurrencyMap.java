package com.shtainyky.converterlab.activities.db.storeModel;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = ConverterDatabase.class)
public class TableCurrencyMap extends BaseModel{
    @Column
    @PrimaryKey
    private
    String id;

    @Column
    private
    String name;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
