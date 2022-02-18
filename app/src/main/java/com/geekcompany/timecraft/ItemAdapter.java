package com.geekcompany.timecraft;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<FarmData> data;

    public ItemAdapter(ArrayList<FarmData> data){
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mGeneration;
        private TextView mTotal;

        private final Handler aff = new Handler();

        public ViewHolder(final View view){
            super(view);
            mTitle = view.findViewById(R.id.farm_title);
            mGeneration = view.findViewById(R.id.farm_gen);
            mTotal = view.findViewById(R.id.farm_total);
        }
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_component,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        FarmData current = data.get(position);
        holder.mTitle.setText(current.getName());
        holder.mGeneration.setText(current.getCountPerSeconds()+"/s");
        holder.mTotal.setText(current.getCurCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
