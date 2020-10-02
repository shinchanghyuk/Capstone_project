package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RelativeMyBoardAdapter extends RecyclerView.Adapter<RelativeMyBoardAdapter.ViewHolder> {

    private ArrayList<RelativeMyBoardItem> rmbDat = null;
    private Intent intent;
    static String username;

    static private String userName, title[], matchDate[], num[], region[], date[], cont[], ability[], check[];

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.tv1);
            textView2 = itemView.findViewById(R.id.tv1);
            textView3 = itemView.findViewById(R.id.tv1);
            textView4 = itemView.findViewById(R.id.tv1);
        }
    }
    RelativeMyBoardAdapter(ArrayList<RelativeMyBoardItem> list, String mUserName, String[] mTitle, String[] mMatchDate, String[] mNum,
                           String[] mRegion, String[] mDate, String[] mCont, String[] mAbility, String[] matchC) {
        rmbDat = list;

        userName = mUserName;
        title = mTitle;
        matchDate = mMatchDate;
        num = mNum;
        region = mRegion;
        date = mDate;
        cont = mCont;
        ability = mAbility;
        check = matchC;
    }
    @NonNull
    @Override
    public RelativeMyBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.board_recycler_item, parent, false) ;
        RelativeMyBoardAdapter.ViewHolder vh = new RelativeMyBoardAdapter.ViewHolder(view) ;
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        RelativeMyBoardItem item = rmbDat.get(position);

        holder.textView1.setText("  "+item.getMatching_check()+"        ");
        holder.textView2.setText(item.getContents()+"   ");
        holder.textView3.setText(item.getWrite_day()+"   ");
        holder.textView4.setText(item.getWrite_user());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), RelativeMyContentActivity.class);
                intent.putExtra("number", position);
                intent.putExtra("name", username);
                intent.putExtra("region", region);
                intent.putExtra("date", matchDate);
                intent.putExtra("title", title);
                intent.putExtra("ability", ability);
                intent.putExtra("num", num);
                intent.putExtra("con", cont);
                intent.putExtra("num", num);
                intent.putExtra("check", check);
                v.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return rmbDat.size();
    }
}
