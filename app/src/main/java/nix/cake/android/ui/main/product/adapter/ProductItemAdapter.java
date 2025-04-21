package nix.cake.android.ui.main.product.adapter;

import static nix.cake.android.utils.DisplayUtils.convertDoubleTwoDecimalsHasCurrency;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.databinding.ItemProductBinding;
import nix.cake.android.utils.DisplayUtils;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductItemViewHolder> {

    private List<ProductResponse> data;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductResponse product);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        ProductResponse product = data.get(position);

        holder.binding.category.setText(product.getCategory().getName());
        holder.binding.name.setText(product.getName());

        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .placeholder(R.drawable.cake_default)
                .error(R.drawable.cake_default)
                .into(holder.binding.img);

        if (product.getTotalSold() == 0) {
            holder.binding.lSold.setVisibility(View.INVISIBLE);
        } else {
            holder.binding.lSold.setVisibility(View.VISIBLE);
            holder.binding.sold.setText(DisplayUtils.formatLongToShortString(product.getTotalSold()));
        }
        if (product.getDiscount() == null) {
            holder.binding.percentSale.setVisibility(View.GONE);
            holder.binding.priceAfterSale.setVisibility(View.GONE);

            holder.binding.price.setText(convertDoubleTwoDecimalsHasCurrency(product.getPrice()));
        } else {
            holder.binding.percentSale.setVisibility(View.VISIBLE);
            holder.binding.priceAfterSale.setVisibility(View.VISIBLE);

            holder.binding.percentSale.setText(String.format("-%d%%", product.getDiscount().getDiscountPercentage()));
            holder.binding.price.setText(convertDoubleTwoDecimalsHasCurrency(product.getPrice()));
            holder.binding.price.setPaintFlags(holder.binding.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            double discountedPrice = product.getPrice() * (1 - product.getDiscount().getDiscountPercentage() / 100.0);
            holder.binding.priceAfterSale.setText(convertDoubleTwoDecimalsHasCurrency(discountedPrice));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });
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

    public void addItems(List<ProductResponse> products) {
        int startPosition = this.data.size();
        this.data.addAll(products);
        notifyItemRangeInserted(startPosition, products.size());
        notifyDataSetChanged();
    }

    public void addItem(ProductResponse product) {
        this.data.add(product);
        notifyItemInserted(data.size() - 1);
    }

    public void removeItem(int position) {
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
