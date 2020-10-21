package com.bethel.mycoolweather.json;

import com.bethel.mycoolweather.db.DBweather;
import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * @author: Bethel
 * @date: 2020-10-15 15:18
 * @desc: Suggestion
 */

public class Suggestion extends DataSupport {

    private String date;
    private String type;
    private String name;
    private String level;
    private String category;
    @SerializedName("text")
    private String textInfo;
    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String getLevel() {
        return level;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setText(String text) {
        this.textInfo = text;
    }
    public String getText() {
        return textInfo;
    }

}
