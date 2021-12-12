package com.textifly.quickmudra.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.Model.DrawerModel;
import com.textifly.quickmudra.R;

import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {
    private MainActivity.OnDrawerMenuListener mListener;
    List<DrawerModel> modelList;
    Context context;
    public static int selected_postion = -1;

    public DrawerAdapter(List<DrawerModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public DrawerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DrawerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerViewHolder holder, int position) {
        holder.tvMenu.setText(modelList.get(position).getName());
        Glide.with(context).load(modelList.get(position).getImage()).into(holder.ivMenu);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mListener.onDrawerMenuClick(position);
                } catch (NullPointerException e) {
                }
            }
        });


        //holder.ivMenu.setImageTintList();
        int goldenColor = Color.parseColor("#F7DC5F");
        int whiteColor = Color.parseColor("#FFFFFF");

        holder.ivMenu.setImageTintList(DrawerAdapter.selected_postion == position ? ColorStateList.valueOf(goldenColor) : ColorStateList.valueOf(whiteColor));
        holder.tvMenu.setTextColor(DrawerAdapter.selected_postion == position ? context.getResources().getColor(R.color.goldenColor) : context.getResources().getColor(R.color.white));

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(selected_postion);
                selected_postion = holder.getLayoutPosition();
                notifyItemChanged(selected_postion);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class DrawerViewHolder extends RecyclerView.ViewHolder {
        public static ImageView ivMenu;
        public static TextView tvMenu;

        public DrawerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            tvMenu = itemView.findViewById(R.id.tvMenu);
        }
    }

    public void setListenerDrawerMenu(MainActivity.OnDrawerMenuListener listener) {
        mListener = listener;
    }

    public void colorChange(int pos){

    }

}
