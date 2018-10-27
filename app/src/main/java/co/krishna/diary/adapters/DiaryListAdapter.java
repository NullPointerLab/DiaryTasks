package co.krishna.diary.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.krishna.diary.R;
import co.krishna.diary.models.DiaryInfo;
import co.krishna.diary.utils.Constantz;

/**
 * Created by srijan on 6/3/18.
 */

public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.MyViewHolder> implements Filterable {

    private List<DiaryInfo> diaryInfoList;
    private List<DiaryInfo> filterableList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRvDiaryTitle, tvRvDiaryInfo, tvRvDiaryDate;
        CardView cardView;
        MyViewHolder(View view) {
            super(view);
            tvRvDiaryTitle = view.findViewById(R.id.tvRvDiaryTitle);
            tvRvDiaryInfo = view.findViewById(R.id.tvRvDiaryInfo);
            tvRvDiaryDate = view.findViewById(R.id.tvRvDiaryDate);
            cardView = view.findViewById(R.id.cardViewRv);
        }
    }

    public DiaryListAdapter(List<DiaryInfo> diaryInfos) {
        this.diaryInfoList = diaryInfos;
        this.filterableList = diaryInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_items_layout_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DiaryInfo diaryInfo = filterableList.get(position);
        holder.tvRvDiaryTitle.setText(diaryInfo.getDiaryTitle());
        holder.tvRvDiaryInfo.setText(diaryInfo.getDiaryInfo());
        holder.tvRvDiaryDate.setText(diaryInfo.getDiaryDate());
//        holder.cardView.setCardBackgroundColor(Constantz.getRandomColor());
    }

    @Override
    public int getItemCount() {
        if (filterableList == null) {
            return 0;
        } else {
            return filterableList.size();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = String.valueOf(constraint);
                if (Constantz.isEmpty(charString)) {
                    filterableList = diaryInfoList;
                } else {
                    ArrayList<DiaryInfo> filterbleInfoList = new ArrayList<>();
                    for (DiaryInfo diaryInfoInfo : diaryInfoList) {
                        if (diaryInfoInfo.getDiaryTitle().toLowerCase().contains(charString) || diaryInfoInfo.getDiaryDate().toLowerCase().contains(charString)) {
                            filterbleInfoList.add(diaryInfoInfo);
                        }
                    }
                    filterableList = filterbleInfoList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterableList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterableList = (ArrayList<DiaryInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}