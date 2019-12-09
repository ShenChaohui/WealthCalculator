package com.zydl.wealthcalculator.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Sch.
 * Date: 2019/11/27
 * description:
 */
@DatabaseTable
public class HistoryBean {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String time;
    @DatabaseField
    private double num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }
}
