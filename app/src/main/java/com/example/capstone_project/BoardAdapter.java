package com.example.capstone_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public  class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    private ArrayList<BoardItem> mData = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        ViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.tm_item1);
        }
    }

    BoardAdapter(ArrayList<BoardItem> list)
    {
        mData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.place_recycler_item, parent, false) ;
        BoardAdapter.ViewHolder bVH = new BoardAdapter.ViewHolder(view) ;
        return bVH;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BoardItem item = mData.get(position);
        holder.textView1.setText(item.getPlace());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
