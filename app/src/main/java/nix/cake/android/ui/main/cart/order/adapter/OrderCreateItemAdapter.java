package nix.cake.android.ui.main.cart.order.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.cart.CartItemResponse;
import nix.cake.android.databinding.ItemOrderItemBinding;
import nix.cake.android.utils.DisplayUtils;
public class OrderCreateItemAdapter extends RecyclerView.Adapter<OrderCreateItemAdapter.CreateOrderItemViewHolder> {

    private List<CartItemResponse> data;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CartItemResponse cartItemResponse);
    }
    public OrderCreateItemAdapter(List<CartItemResponse> data) {
        this.data = data;
    }

    public OrderCreateItemAdapter(OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public CreateOrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderItemBinding binding = ItemOrderItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CreateOrderItemViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CreateOrderItemViewHolder holder, int position) {
        CartItemResponse item = data.get(position);
        holder.binding.name.setText(item.getProduct().getName());
        holder.binding.flavor.setText(item.getTag().getName());
        holder.binding.count.setText("x" + " " + item.getQuantity());
        holder.binding.total.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(item.getTotal()));
        Glide.with(holder.itemView.getContext())
                .load(item.getProduct().getImage())
                .placeholder(R.color.img_default)
                .error(R.color.img_default)
                .into(holder.binding.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<CartItemResponse> orderItems) {
        this.data.clear();
        this.data.addAll(orderItems);
        notifyDataSetChanged();
    }
    public static class CreateOrderItemViewHolder extends RecyclerView.ViewHolder {
        ItemOrderItemBinding binding;

        public CreateOrderItemViewHolder(@NonNull ItemOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}