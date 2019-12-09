package com.zydl.wealthcalculator.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Sch.
 * Date: 2019/11/26
 * description:
 */
@DatabaseTable
public class FundAccount {
    @DatabaseField(generatedId = true)
    private int id ;
    @DatabaseField
    private String mName;
    @DatabaseField
    private double mNum;
    @DatabaseField
    private int mType;//0资产  1负债

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getNum() {
        return mNum;
    }

    public void setNum(double num) {
        mNum = num;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
}
