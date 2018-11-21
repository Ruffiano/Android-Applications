package com.example.user.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.demo.R;
import com.example.user.demo.models.BluetoothModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by User on 01.03.2016.
 */
public class BlueAdapter extends RecyclerView.Adapter<BlueAdapter.ViewHolder> {

    private Context context;
    private List<BluetoothModel> models;
    private ItemClickListener itemClickListener;

    public BlueAdapter(Context context, List<BluetoothModel> models, ItemClickListener itemClickListener) {
        this.context = context;
        this.models = models;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bluetooth_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BluetoothModel model = getItem(position);
        holder.nameBlue.setText(model.getName());
        holder.addressNBlue.setText(model.getAddress());
    }

    public BluetoothModel getItem(int position) {
        if (models != null && position >= 0 && position < getItemCount()) {
            return models.get(position);

        }
        return null;
    }


    @Override
    public int getItemCount() {
        return models != null ? models.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.nameBlue)
        TextView nameBlue;
        @Bind(R.id.addressBlue)
        TextView addressNBlue;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.bluetoothItem)
        void clickItem() {
            BluetoothModel model = getItem(getAdapterPosition());
            if (model != null && itemClickListener != null) {
                itemClickListener.onItemClick(model);
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(BluetoothModel model);
    }
}
