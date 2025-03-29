package nix.cake.android.ui.main.profile.order.detail.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ItemOrderDetailBinding;

public class OrderDetailItemAdapter extends RecyclerView.Adapter<OrderDetailItemAdapter.OrderDetailItemViewHolder> {
    private List<OrderResponse> data;
    private OrderDetailItemAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
    }
    public OrderDetailItemAdapter() {
        this.data = new ArrayList<>();
    }

    public OrderDetailItemAdapter(OrderDetailItemAdapter.OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderDetailItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = ItemOrderDetailBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new OrderDetailItemAdapter.OrderDetailItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailItemViewHolder holder, int position) {
        OrderResponse order = data.get(position);
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

    public static class OrderDetailItemViewHolder extends RecyclerView.ViewHolder {
        ItemOrderDetailBinding binding;

        public OrderDetailItemViewHolder(@NonNull ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
