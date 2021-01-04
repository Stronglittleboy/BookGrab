package com.hlw.webmagic.begin.beginTwo;

import lombok.Data;

/**
 * @author 侯龙旺
 * @version 1.0
 * @description: TODO
 * @date 2020/12/31 17:51
 */
@Data
public class AreaModel {
        private String code;
        private String name;
        private String leave;

    public static void main(String[] args) {
        String insertSQL = getInsertSQL("1000", "200", "1");
        System.out.println(insertSQL);
    }
    public static String getInsertSQL(String code,String name,String leave){
        return  "INSERT INTO `cim`.`t_province_city_area`( `code`, " +
                "`name`, `leave`) VALUES ( "+code+", "+name+", "+leave+");";
    }
}
