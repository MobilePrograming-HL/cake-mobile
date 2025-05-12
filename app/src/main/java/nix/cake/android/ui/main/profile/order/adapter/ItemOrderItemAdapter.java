package nix.cake.android.ui.main.profile.order.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.order.OrderItemResponse;
import nix.cake.android.databinding.ItemOrderItemBinding;
import nix.cake.android.utils.DisplayUtils;

public class ItemOrderItemAdapter extends RecyclerView.Adapter<ItemOrderItemAdapter.ItemOrderItemViewHolder> {

    private List<OrderItemResponse> data;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(OrderItemResponse order);
    }
    public ItemOrderItemAdapter(List<OrderItemResponse> data) {
        this.data = data;
    }

    public ItemOrderItemAdapter(OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public ItemOrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderItemBinding binding = ItemOrderItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ItemOrderItemViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemOrderItemViewHolder holder, int position) {
        OrderItemResponse item = data.get(position);
        holder.binding.name.setText(item.getProduct().getName());
        holder.binding.count.setText("x" + " " + item.getQuantity());
        holder.binding.total.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(item.getPriceOfProduct()));
        Glide.with(holder.itemView.getContext())
                .load(item.getProduct().getImage())
                .placeholder(R.color.img_default)
                .error(R.color.img_default)
                .into(holder.binding.img);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<OrderItemResponse> orderItems) {
        this.data.clear();
        this.data.addAll(orderItems);
        notifyDataSetChanged();
    }
    public static class ItemOrderItemViewHolder extends RecyclerView.ViewHolder {
        ItemOrderItemBinding binding;

        public ItemOrderItemViewHolder(@NonNull ItemOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}