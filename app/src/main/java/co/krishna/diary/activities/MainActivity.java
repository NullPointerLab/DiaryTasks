package co.krishna.diary.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.krishna.diary.R;
import co.krishna.diary.adapters.DiaryListAdapter;
import co.krishna.diary.database.DBHandler;
import co.krishna.diary.models.DiaryInfo;
import co.krishna.diary.utils.Constantz;
import co.krishna.diary.utils.PrefManager;
import co.krishna.diary.utils.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView tvNothingAddedTitle;
    private List<DiaryInfo> diaryInfoArrayList = new ArrayList<>();
    private DiaryListAdapter mAdapter;
    private RelativeLayout relativeLayout;
    DBHandler dbHandler = null;
    SQLiteDatabase db = null;
    Boolean backButtonPressedOnce = false;
    private static final int ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        tvNothingAddedTitle = findViewById(R.id.tvNothingAddedTitle);
        progressBar = findViewById(R.id.progressBar);
        relativeLayout = findViewById(R.id.mainActivityLayout);

        mAdapter = new DiaryListAdapter(diaryInfoArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fab.setOnClickListener(this);
        getDiaryDataFromDB();

        setRecyclerViewItemClickListener();
    }

    private void setRecyclerViewItemClickListener() {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                CardView btnEdit = view.findViewById(R.id.cardViewRv);
                final String dId = diaryInfoArrayList.get(position).getDiaryId();
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PrefManager.setPrefValue(MainActivity.this, Constantz.DIARY_LIST_ID, dId);
                        Intent in = new Intent(MainActivity.this, EditDiaryActivity.class);
                        startActivityForResult(in, ACTIVITY_REQUEST_CODE);
                    }
                });
            }
        }));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent i = new Intent(this, NewDiaryActivity.class);
                startActivityForResult(i, ACTIVITY_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (diaryInfoArrayList != null) {
                    diaryInfoArrayList.clear();
                }
                getDiaryDataFromDB();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (diaryInfoArrayList == null) {
            recyclerView.setVisibility(View.GONE);
            tvNothingAddedTitle.setVisibility(View.VISIBLE);
        } else if (diaryInfoArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvNothingAddedTitle.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvNothingAddedTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_view, menu);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText.toLowerCase());
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void getDiaryDataFromDB() {
        progressBar.setVisibility(View.VISIBLE);
        dbHandler = new DBHandler(getApplicationContext());
        db = dbHandler.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from DiaryInfo", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    DiaryInfo diaryInfoModel = new DiaryInfo();
                    diaryInfoModel.setDiaryId(cursor.getString(cursor.getColumnIndex("diaryID")));
                    diaryInfoModel.setDiaryTitle(cursor.getString(cursor.getColumnIndex("diaryTitle")));
                    diaryInfoModel.setDiaryInfo(cursor.getString(cursor.getColumnIndex("diaryInfo")));
                    diaryInfoModel.setDiaryDate(cursor.getString(cursor.getColumnIndex("diaryDate")));
                    diaryInfoArrayList.add(diaryInfoModel);
                    mAdapter.notifyDataSetChanged();
                } while (cursor.moveToNext());
            }
        }

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (backButtonPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.backButtonPressedOnce = true;
        Snackbar.make(relativeLayout, "Press again to exit", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backButtonPressedOnce = false;
            }
        }, 2000);
    }
}
