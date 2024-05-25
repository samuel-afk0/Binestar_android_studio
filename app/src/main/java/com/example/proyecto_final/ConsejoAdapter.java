package com.example.proyecto_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ConsejoAdapter extends RecyclerView.Adapter<ConsejoAdapter.MyViewHolder>{
    private List<ConsejoModelo> consejoList;
    ConsejoAdapter(List<ConsejoModelo> consejoList) {
        this.consejoList = consejoList;
    }
    @NonNull
    @Override
    public ConsejoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consejos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsejoAdapter.MyViewHolder holder, int position) {
        ConsejoModelo consejo = consejoList.get(position);
        holder.title.setText(consejo.getTitle());
        holder.text1.setText(consejo.getText1());
        holder.text2.setText(consejo.getText2());
        Glide.with(holder.img.getContext()).load(consejo.getImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return consejoList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, text1, text2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imgConsejo);
            title = (TextView)itemView.findViewById(R.id.titleConsejo);
            text1 = (TextView)itemView.findViewById(R.id.textConsejo1);
            text2 = (TextView)itemView.findViewById(R.id.textConsejo2);
        }
    }
}