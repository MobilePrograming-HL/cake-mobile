package nix.cake.android.ui.main.profile.order.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ItemOrderBinding;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<OrderResponse> data;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(OrderResponse order);
        void onCancelClick(OrderResponse order);
    }
    public OrderItemAdapter() {
        this.data = new ArrayList<>();
    }

    public OrderItemAdapter(OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new OrderItemViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderResponse order = data.get(position);

        holder.binding.orderNumber.setText(order.getId().toString());
        holder.binding.quantity.setText(String.valueOf(order.getQuantity()));
        holder.binding.statusOrder.setText(order.getStatus());
        holder.binding.dateOrder.setText(order.getDateOrder());
        holder.binding.total.setText(order.getTotal().toString());

        holder.binding.cancelOrder.setOnClickListener(v -> listener.onCancelClick(order));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(order));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<OrderResponse> orders) {
        this.data.clear();
        this.data.addAll(orders);
        notifyDataSetChanged();
    }

    public void addOrder(OrderResponse order) {
        this.data.add(order);
        notifyItemInserted(data.size() - 1);
    }

    public void removeOrder(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;

        public OrderItemViewHolder(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}