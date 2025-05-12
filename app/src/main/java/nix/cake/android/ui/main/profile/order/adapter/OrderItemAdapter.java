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
        void onCancelClick(OrderResponse order, int position);
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
        holder.binding.orderCode.setText("# " + order.getCode());
        holder.binding.orderTime.setText(DisplayUtils.formatIsoToLocal(order.getCreatedAt()));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(order);
            }
        });
        if (order.getStatus().getStatus() == 1 || order.getStatus().getStatus() == 2) {
            holder.binding.cancel.setVisibility(View.VISIBLE);
            holder.binding.cancel.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCancelClick(order, position);
                }
            });
        } else {
            holder.binding.cancel.setVisibility(View.GONE);
        }

        if (order.getStatus().getStatus() == 3) {
            holder.binding.received.setVisibility(View.VISIBLE);
        } else {
            holder.binding.received.setVisibility(View.GONE);
        }
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

    public void removeItem(int position) {
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