package com.example.imageupload.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DbManager extends SQLiteOpenHelper {
    private static final String NAME = "ITEM_INFO.db";
    private static final int VERSION = 1;

    private static DbManager manager = null;

    public DbManager(Context context){
        super(context, NAME, null, VERSION);
    }

    //데이터베이스 인스턴스 생성하거나 이미 존재하면 기존 데이터베이스 반환
    public static DbManager getInstance(Context context){
        if(manager == null){
            manager = new DbManager(context);
        }
        return manager;
    }
    public static DbManager getInstance() {return manager;}

    @Override
    public void onCreate(SQLiteDatabase db){
        if(!createTables(db)){

            return;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(!dropTables(db)){
            return;
        }
        if(!createTables(db)){
            return;
        }
    }

    //테이블 생성
    private boolean createTables(SQLiteDatabase db){
        String[] querys = new String[]{
                "CREATE TABLE RFID_ASSET_FILE (\n" +
                        " ASSET_NO VARCHAR(20) NOT NULL,\n" +
                        " FILE_DT CHAR(14) NOT NULL,\n" +
                        " FILE_PATH VARCHAR(200) DEFAULT NULL,\n" +
                        " FILE_NM VARCHAR(200) DEFAULT NULL,\n" +
                        " ORIGNL_FILE_NM VARCHAR(200) DEFAULT NULL,\n" +
                        " FILE_TYPE VARCHAR(20) DEFAULT NULL,\n" +
                        " USE_YN CHAR(1) DEFAULT NULL,\n" +
                        " UPLOAD_YN CHAR(1) DEFAULT NULL,\n" +
                        " CONSTRAINT PK_rfid_asset_file\n" +
                        " PRIMARY KEY (ASSET_NO ASC, FILE_DT ASC)\n" +
                        ");\n"
        };

        db.beginTransaction();
        for(String query: querys){
            if(!DbUtil.execQeury(db, query)){
                db.endTransaction();

                return false;
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        return true;
    }

    private boolean dropTables(SQLiteDatabase db){
        String[] querys = new String[]{
                "DROP TABLE IF EXISTS RFID_ASSET_FILE"
        };

        db.beginTransaction();
        for(String query : querys){
            if(!DbUtil.execQeury(db, query)){

                return false;
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return true;
    }


    public void checkTables(){
        String[] tableNames = new String[]{
                "RFID_ASSET_FILE"
        };
        String query = "SELECT name FROM sqlite_master WHERE type='table';";
        List<String> list = DbUtil.getList(getReadableDatabase(), query,
                new DbUtil.ItemMaker<String>() {
                    @Override
                    public String getItem(Cursor c) {
                        int index = c.getColumnIndex("name");
                        String name = c.getString(index);
                        return name;
                    }
                });
        if (list.size() <= 0) {
            return;
        }
        for (String table : tableNames) {
            if (!list.contains(table)) {
            }
        }
    }

    public boolean insertData(String assetNo, String fileDateTime, String filePath,
                              String fileName, String originalFileName, String fileType,
                              String useYN, String uploadYN){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ASSET_NO",assetNo);
        contentValues.put("FILE_DT",fileDateTime);
        contentValues.put("FILE_PATH",filePath);
        contentValues.put("FILE_NM",fileName);
        contentValues.put("ORIGNL_FILE_NM",originalFileName);
        contentValues.put("FILE_TYPE",fileType);
        contentValues.put("USE_YN",useYN);
        contentValues.put("UPLOAD_YN",uploadYN);

        long result = db.insert("RFID_ASSET_FILE", null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor selectQuery(String query, String...params){
        Cursor cursor = null;
        SQLiteDatabase db = getWritableDatabase();

        try {
            cursor = db.rawQuery(query, params);
        } catch (Exception e){
            e.printStackTrace();
        }

        return cursor;
    }

    public void updateQuery(String query, String...strings) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(query, strings);
    }
}
