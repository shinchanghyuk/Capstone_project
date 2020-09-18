package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public  class RelativeBoardAdapter extends RecyclerView.Adapter<RelativeBoardAdapter.ViewHolder> {

    ArrayList<RelativeBoardItem> mData = null;
    Intent intent;
    static String userName[], title[], matchDate[], num[], region[], date[], cont[], ability[];

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;

        ViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.tm_item1);
            textView2 = itemView.findViewById(R.id.tm_item2);
            textView3 = itemView.findViewById(R.id.tm_item3);
            textView4 = itemView.findViewById(R.id.tm_item4);
        }
    }
    RelativeBoardAdapter(ArrayList<RelativeBoardItem> list, String[] mUserName, String[] mTitle, String[] mMatchDate, String[] mNum,
                         String[] mRegion, String[] mDate, String[] mCont, String[] mAbility) {
        userName = mUserName;
        title = mTitle;
        matchDate = mMatchDate;
        num = mNum;
        region = mRegion;
        date = mDate;
        cont = mCont;
        ability = mAbility;
        mData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recycler_item, parent, false) ;
        RelativeBoardAdapter.ViewHolder vh = new RelativeBoardAdapter.ViewHolder(view) ;
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        RelativeBoardItem item = mData.get(position);

        holder.textView1.setText("  "+item.getMatching_check()+"        ");
        holder.textView2.setText(item.getContents()+"   ");
        holder.textView3.setText(item.getWrite_day()+"   ");
        holder.textView4.setText(item.getWrite_user());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), RelativeBoardContentActivity.class);
                intent.putExtra("number", position);
                intent.putExtra("region", region);
                intent.putExtra("date", matchDate);
                intent.putExtra("title", title);
                intent.putExtra("ability", ability);
                intent.putExtra("num", num);
                intent.putExtra("con", cont);
                intent.putExtra("num", num);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

