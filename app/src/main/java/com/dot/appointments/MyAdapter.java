package com.dot.appointments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.dot.appointments.custom.Tools.dpToPx;
import static com.dot.appointments.custom.Tools.pxToDp;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] moviesList;

    private int height; //in pixels

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout linearLayout;

        MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            linearLayout =(LinearLayout) view.findViewById(R.id.my_row_ll);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(pxToDp(height)/12));

            linearLayout.setLayoutParams(layoutParams);
        }
    }

    MyAdapter(String[] moviesList,int height) {
        this.moviesList = moviesList;
        this.height = height;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(moviesList[position]);
        }


    @Override
    public int getItemCount() {
        return moviesList.length;
    }

}