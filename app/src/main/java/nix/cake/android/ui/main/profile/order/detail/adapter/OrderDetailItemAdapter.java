package nix.cake.android.ui.main.profile.order.detail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.data.model.api.response.profile.order.OrderItemResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ItemOrderDetailBinding;
import nix.cake.android.utils.DisplayUtils;

public class OrderDetailItemAdapter extends RecyclerView.Adapter<OrderDetailItemAdapter.OrderDetailItemViewHolder> {
    private List<OrderItemResponse> data;
    private Integer orderStatus;
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
        OrderItemResponse item = data.get(position);
        holder.binding.name.setText(item.getProduct().getName());
        holder.binding.count.setText("x" + " " + item.getQuantity());
        holder.binding.total.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(item.getPriceOfProduct()));
        Glide.with(holder.itemView.getContext())
                .load(item.getProduct().getImage())
                .placeholder(R.color.img_default)
                .error(R.color.img_default)
                .into(holder.binding.img);

        if (orderStatus != null && orderStatus == Constants.ORDER_STATUS_DELIVERED) {
            holder.binding.review.setVisibility(View.VISIBLE);
        } else {
            holder.binding.review.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<OrderItemResponse> orders, Integer orderStatus) {
        this.data.clear();
        this.data.addAll(orders);
        this.orderStatus = orderStatus;
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
