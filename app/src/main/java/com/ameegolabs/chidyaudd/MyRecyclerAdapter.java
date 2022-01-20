package com.ameegolabs.chidyaudd;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerHolder> {

    Context context;
    ArrayList<String> arrayList;
    Boolean isFlying;
    DBHelper dbHelper;

    public MyRecyclerAdapter(Context context, ArrayList<String> arrayList, Boolean isFlying) {
        this.context = context;
        this.arrayList = arrayList;
        this.isFlying = isFlying;
        dbHelper = new DBHelper(context);
    }

    @Override
    public MyRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recyclerview, parent, false);
        if (!isFlying) {
            ((CardView) view).setCardBackgroundColor(context.getResources().getColor(R.color.colorRipplePlayer2));
        }
        MyRecyclerHolder holder = new MyRecyclerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerHolder holder, int position) {
        holder.textView.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void deleteItem(View view, final int position) {

        final String itemName = arrayList.get(position);
        arrayList.remove(position);
        if (isFlying) {
            dbHelper.deleteFlyingObject(itemName);
        }else{
            dbHelper.deleteNonFlyingObject(itemName);
        }

        MyRecyclerAdapter.this.notifyDataSetChanged();

        /*Snackbar.make(view, itemName + " Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        arrayList.add(position,itemName);
                        MyRecyclerAdapter.this.notifyDataSetChanged();
                    }
                })
                .show();*/
        Snackbar snackbar;
        snackbar = Snackbar.make(view, itemName + " Deleted", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.add(position, itemName);
                if(isFlying) {
                    dbHelper.addFlyingObject(itemName);
                }else{
                    dbHelper.addNonFlyingObject(itemName);
                }
                MyRecyclerAdapter.this.notifyDataSetChanged();
            }
        });
        snackbar.show();
    }

    public class MyRecyclerHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyRecyclerHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.textView);

        }
    }
}
