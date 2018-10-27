package co.krishna.diary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by srijan on 6/3/18.
 */

public class DBHandler extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public DBHandler(Context context) {
        super(context, "DiaryInfoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table DiaryInfo(" +
                "diaryID Text Primary Key," +
                "diaryTitle Text," +
                "diaryInfo Text," +
                "diaryDate Text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public long insertDiaryData(String dId, String dTitle, String dInfo, String dDate) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("diaryID", dId);
        cv.put("diaryTitle", dTitle);
        cv.put("diaryInfo", dInfo);
        cv.put("diaryDate", dDate);

        return db.insert("DiaryInfo", null, cv);
    }

    public long updateDiaryData(String dId, String dTitle, String dInfo, String dDate) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("diaryTitle", dTitle);
        cv.put("diaryInfo", dInfo);
        cv.put("diaryDate", dDate);
        return (long) db.update("DiaryInfo", cv, "diaryID=?", new String[]{dId});
    }

}
