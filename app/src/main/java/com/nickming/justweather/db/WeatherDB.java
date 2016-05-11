package com.nickming.justweather.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * Author:nickming
 * Date:16/4/29
 * Time:00:54
 * E-mail:962570483@qq.com
 */
public class WeatherDB {

    private Context context;

    public WeatherDB(Context context) {
        this.context = context;
    }

    public List<Province> loadProvinces(SQLiteDatabase db) {

        List<Province> list = new ArrayList<>();

        Cursor cursor = db.query("T_Province", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            do {
                Province province = new Province();
                province.ProSort = cursor.getInt(cursor.getColumnIndex("ProSort"));
                province.ProName = cursor.getString(cursor.getColumnIndex("ProName"));
                list.add(province);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<City> loadCities(SQLiteDatabase db, int ProID) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("T_City", null, "ProID = ?", new String[] { String.valueOf(ProID) }, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.CityName = cursor.getString(cursor.getColumnIndex("CityName"));
                city.ProID = ProID;
                city.CitySort = cursor.getInt(cursor.getColumnIndex("CitySort"));
                list.add(city);
                //city.setCityName(cursor.getString(cursor.getColumnIndex("CityName")));
                //city.setProID(ProID);
                //list.add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }
}
