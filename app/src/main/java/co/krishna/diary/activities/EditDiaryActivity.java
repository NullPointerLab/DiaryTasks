package co.krishna.diary.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import co.krishna.diary.R;
import co.krishna.diary.database.DBHandler;
import co.krishna.diary.utils.Constantz;
import co.krishna.diary.utils.PrefManager;

public class EditDiaryActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBtnCancel;
    private TextView tvBtnSaveNewDiary, tvEditDiaryDate;
    private EditText etNewDiaryName, etNewDiaryInfo;
    private FloatingActionButton fabBtnEdit;
    private String mDiaryDate;
    private String diaryListId = "";
    int Year, Month, Date;
    Calendar calendar;

    DBHandler dbHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        imgBtnCancel = findViewById(R.id.imgBtnCancel1);
        tvBtnSaveNewDiary = findViewById(R.id.tvBtnSaveNewDiary1);
        tvEditDiaryDate = findViewById(R.id.tvNewDiaryDate1);
        etNewDiaryName = findViewById(R.id.etNewDiaryName1);
        etNewDiaryInfo = findViewById(R.id.etNewDiaryInfo1);
        fabBtnEdit = findViewById(R.id.fabBtnEdit);
        dbHandler = new DBHandler(getApplicationContext());

        calendar = Calendar.getInstance();
        imgBtnCancel.setOnClickListener(this);
        tvBtnSaveNewDiary.setOnClickListener(this);
        tvEditDiaryDate.setOnClickListener(this);
        fabBtnEdit.setOnClickListener(this);

        isButtonsEnable(false);
        diaryListId = PrefManager.getPrefValue(EditDiaryActivity.this, Constantz.DIARY_LIST_ID);
        getDataFromDB(diaryListId);
    }

    private void getDataFromDB(String diaryListId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        /** this "?" mark error is bug in Android Studio 3.0 */
        Cursor cursor = db.rawQuery("select * from DiaryInfo where diaryID=?", new String[]{diaryListId});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String dTitle = cursor.getString(cursor.getColumnIndex("diaryTitle"));
                String dInfo = cursor.getString(cursor.getColumnIndex("diaryInfo"));
                String dDate = cursor.getString(cursor.getColumnIndex("diaryDate"));
                etNewDiaryName.setText(dTitle);
                etNewDiaryInfo.setText(dInfo);
                tvEditDiaryDate.setText(dDate);
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBtnSaveNewDiary1:
                String diaryTitle = etNewDiaryName.getText().toString().trim();
                String diaryInfo = etNewDiaryInfo.getText().toString().trim();
                if (Constantz.isEmpty(diaryTitle) && Constantz.isEmpty(diaryInfo)) {
                    Toast.makeText(this, "Cannot save empty Diary", Toast.LENGTH_SHORT).show();
                } else {
                    updateDiaryInfo();
                }
                break;
            case R.id.imgBtnCancel1:
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.tvNewDiaryDate1:
                datePickerDialog();
                break;
            case R.id.fabBtnEdit:
//                tvBtnSaveNewDiary.setVisibility(View.VISIBLE);
                fabBtnEdit.setVisibility(View.INVISIBLE);
                isButtonsEnable(true);
                break;
        }
    }

    private void isButtonsEnable(boolean b) {
        if (b) {
            tvBtnSaveNewDiary.setTextColor(getResources().getColor(R.color.white));
        }
        tvBtnSaveNewDiary.setEnabled(b);
        tvEditDiaryDate.setEnabled(b);
        etNewDiaryName.setEnabled(b);
        etNewDiaryInfo.setEnabled(b);
    }


    private void updateDiaryInfo() {
        String diaryTitle = etNewDiaryName.getText().toString().trim();
        String diaryInfo = etNewDiaryInfo.getText().toString().trim();
        String diaryDate = tvEditDiaryDate.getText().toString().trim();
        long id = dbHandler.updateDiaryData(diaryListId, diaryTitle, diaryInfo, diaryDate);
        if (id != -1) {
            Intent in = new Intent();
            in.putExtra("result", 1);
            setResult(Activity.RESULT_OK, in);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Diary Not Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void datePickerDialog() {
        DatePickerDialog dpd = new DatePickerDialog(EditDiaryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                Year = year;
                Month = month + 1;
                Date = date;
                mDiaryDate = Month + "/" + Date + "/" + Year;
                tvEditDiaryDate.setText("Date: " + mDiaryDate);

                Toast.makeText(getApplicationContext(), "Date Updated", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        dpd.show();
    }


}
