package com.bethel.mycoolweather.db;

import com.bethel.mycoolweather.json.Area;

import org.litepal.crud.DataSupport;

/**
 * @author: Solomon
 * @date: 2020-10-12 15:46
 * @desc:
 */
public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public static Province newInstance(Area area) {
        Province province = new Province();
        province.setProvinceCode(area.id);
        province.setProvinceName(area.name);
        return province;
    }
}
