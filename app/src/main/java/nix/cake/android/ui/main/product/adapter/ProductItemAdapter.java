package nix.cake.android.ui.main.product.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ItemProductBinding;
import nix.cake.android.ui.main.profile.order.adapter.OrderItemAdapter;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductItemViewHolder> {

    private List<ProductResponse> data;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(OrderResponse order);
    }
    public ProductItemAdapter() {
        this.data = new ArrayList<>();
    }

    public ProductItemAdapter(OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ProductItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        ProductResponse product = data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<ProductResponse> products) {
        this.data.clear();
        this.data.addAll(products);
        notifyDataSetChanged();
    }

    public void addOrder(ProductResponse product) {
        this.data.add(product);
        notifyItemInserted(data.size() - 1);
    }

    public void removeOrder(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
    public static class ProductItemViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ProductItemViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
