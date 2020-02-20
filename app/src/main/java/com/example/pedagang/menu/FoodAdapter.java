package com.example.pedagang.menu;


import android.content.ClipData;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pedagang.R;
import com.example.pedagang.model.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ItemViewHolder> {
    private Context mcontext;
    private List<Food> mItems;
    private OnItemClickListener mListener;

    public FoodAdapter (Context context, List<Food> items){
        mcontext = context;
        mItems = items;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.makanan_row, parent,false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Food itemCurrent = mItems.get(position);
        holder.Namabarang.setText(itemCurrent.getName());
        holder.hargaBarang.setText(itemCurrent.getprice());
        holder.pedagang.setText(itemCurrent.getSeller());
        Glide.with(mcontext).load(itemCurrent.getimage()).into(holder.imgBarang);
    }



    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ImageView imgBarang;
        TextView Namabarang, hargaBarang,pedagang;
        View mView;
        public ItemViewHolder(View itemvView){
            super(itemvView);
            mView = itemvView;
            Namabarang = (TextView) itemvView.findViewById(R.id.nama_barang);
            imgBarang = (ImageView) itemvView.findViewById(R.id.barang_img);
            hargaBarang = (TextView) itemvView.findViewById(R.id.harga);
            pedagang = (TextView) itemvView.findViewById(R.id.pedagang);


            itemvView.setOnClickListener(this);
            itemvView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem Edit = contextMenu.add(Menu.NONE, 1, 1, "Edit Item");
            MenuItem Delete = contextMenu.add(Menu.NONE, 2, 2, "Delete Item");

            Edit.setOnMenuItemClickListener(this);
            Delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
//                        case 1:
//                            mListener.onDetailClick(position);
//                            return true;
                        case 1:
                            mListener.onEditClick(mItems.get(position));
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

//        void onDetailClick(int position);

        void onEditClick(Food food);

        void onDeleteClick(int position);

    }

    public void setOnFoodClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}




