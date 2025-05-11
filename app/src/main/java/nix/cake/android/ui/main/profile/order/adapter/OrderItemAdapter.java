package nix.cake.android.ui.main.profile.order.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.order.OrderItemResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ItemOrderBinding;
import nix.cake.android.utils.DisplayUtils;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<OrderResponse> data;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(OrderResponse order);
        void onCancelClick(OrderItemResponse order);
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
        holder.binding.setTotal(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(order.getTotalAmount()));
        holder.binding.statusOrder.setText(order.getStatus().getOrderStatus(order.getStatus().getStatus()));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(order);
            }
        });
        if (order.getOrderItems().size() <= 1) {
            holder.binding.more.setVisibility(View.GONE);
            ItemOrderItemAdapter childAdapter = new ItemOrderItemAdapter(order.getOrderItems());
            holder.binding.rvProduct.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.binding.rvProduct.setAdapter(childAdapter);
        }
        else {
            List<OrderItemResponse> firstOrderItem = order.getOrderItems().subList(0, 1);
            ItemOrderItemAdapter childAdapter = new ItemOrderItemAdapter(firstOrderItem);
            holder.binding.rvProduct.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.binding.rvProduct.setAdapter(childAdapter);

            holder.binding.more.setVisibility(View.VISIBLE);

            holder.binding.more.setOnClickListener(v -> {
                ItemOrderItemAdapter fullAdapter = new ItemOrderItemAdapter(order.getOrderItems());
                holder.binding.rvProduct.setAdapter(fullAdapter);
                holder.binding.more.setVisibility(View.GONE);
            });
        }
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
    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;

        public OrderItemViewHolder(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}