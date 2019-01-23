package com.gnm.kamus.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnm.kamus.activity.DetailActivity;
import com.gnm.kamus.R;
import com.gnm.kamus.model.Kamus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterKamus extends RecyclerView.Adapter<AdapterKamus.ItemHolder> {

    private ArrayList<Kamus> list = new ArrayList<>();

    public AdapterKamus() {
    }

    public void replaceAll(ArrayList<Kamus> items) {
        list = items;
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_list, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtKata)
        TextView txtKata;

        @BindView(R.id.txtTerjemahan)
        TextView txtTerjemahan;

        ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(final Kamus detail) {
            txtKata.setText(detail.getKata());
            txtTerjemahan.setText(detail.getTerjemahan());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    intent.putExtra("Kata", detail.getKata());
                    intent.putExtra("Terjemahan", detail.getTerjemahan());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
