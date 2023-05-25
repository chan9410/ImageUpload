package com.example.imageupload.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DbUtil {

    public static boolean execQeury(SQLiteDatabase db, String query) {
        try {
            db.execSQL(query);
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    public static <T> List<T> getList(SQLiteDatabase db, String query,
                                      ItemMaker<T> generator) {
        List<T> list = new ArrayList<>();

        try {

            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    T item = generator.getItem(c);
                    if (item != null)
                        list.add(item);
                } while (c.moveToNext());
            } else {
                return list;
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    public static <T> T getItem(SQLiteDatabase db, String query, ItemMaker<T> generator) {
        T item;
        try {

            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                item = generator.getItem(c);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return item;
    }

    public interface ItemMaker<T> {
        T getItem(Cursor c);
    }
}
