package co.krishna.diary.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import co.krishna.diary.R;
import co.krishna.diary.database.DBHandler;
import co.krishna.diary.utils.Constantz;
import co.krishna.diary.utils.PrefManager;

public class NewDiaryActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBtnCancel;
    private TextView tvBtnSaveNewDiary, tvNewDiaryDate;
    private EditText etNewDiaryName, etNewDiaryInfo;
    private String mDiaryDate;
    int Year, Month, Date;
    Calendar calendar;

    DBHandler dbHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diary);
        imgBtnCancel = findViewById(R.id.imgBtnCancel);
        tvBtnSaveNewDiary = findViewById(R.id.tvBtnSaveNewDiary);
        tvNewDiaryDate = findViewById(R.id.tvNewDiaryDate);
        etNewDiaryName = findViewById(R.id.etNewDiaryName);
        etNewDiaryInfo = findViewById(R.id.etNewDiaryInfo);

        dbHandler = new DBHandler(getApplicationContext());

        calendar = Calendar.getInstance();
        imgBtnCancel.setOnClickListener(this);
        tvBtnSaveNewDiary.setOnClickListener(this);
        tvNewDiaryDate.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        mDiaryDate = sdf.format(date);
        tvNewDiaryDate.setText("Date: " + mDiaryDate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBtnSaveNewDiary:
                String strNewDiaryName = etNewDiaryName.getText().toString().trim();
                String strNewDiaryInfo = etNewDiaryInfo.getText().toString().trim();
                if (Constantz.isEmpty(strNewDiaryName) && Constantz.isEmpty(strNewDiaryInfo)) {
                    Toast.makeText(this, "Can't save empty Diary", Toast.LENGTH_SHORT).show();
                } else {
                    saveDiaryInfo();
                }
                break;
            case R.id.imgBtnCancel:
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.tvNewDiaryDate:
                datePickerDialog();
                break;
        }
    }

    private void saveDiaryInfo() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String diaryId = sdf.format(date);
        String diaryTitle = etNewDiaryName.getText().toString().trim();
        String diaryInfo = etNewDiaryInfo.getText().toString().trim();
        String diaryDate = mDiaryDate;

        long id = dbHandler.insertDiaryData(diaryId, diaryTitle, diaryInfo, diaryDate);
        if (id != -1) {
            Intent in = new Intent();
            in.putExtra("result", 1);
            setResult(Activity.RESULT_OK, in);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to save", Toast.LENGTH_SHORT).show();
        }
    }

    private void datePickerDialog() {
        DatePickerDialog dpd = new DatePickerDialog(NewDiaryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                Year = year;
                Month = month + 1;
                Date = date;
                mDiaryDate = Month + "/" + Date + "/" + Year;
                tvNewDiaryDate.setText("Date: " + mDiaryDate);

                Toast.makeText(getApplicationContext(), "Date Updated", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        dpd.show();
    }


}
