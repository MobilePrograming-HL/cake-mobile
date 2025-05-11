package nix.cake.android.ui.main.home.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.databinding.ItemCategoryHomeBinding;

public class ItemCategoryHomeAdapter extends RecyclerView.Adapter<ItemCategoryHomeAdapter.ItemCategoryHomeViewHolder> {
    private List<CategoryResponse> data;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemCateSearchClick(CategoryResponse category);
    }
    public ItemCategoryHomeAdapter() {
        this.data = new ArrayList<>();
    }

    public ItemCategoryHomeAdapter(OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public ItemCategoryHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryHomeBinding binding = ItemCategoryHomeBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ItemCategoryHomeViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemCategoryHomeViewHolder holder, int position) {
        CategoryResponse category = data.get(position);
        holder.binding.name.setText(category.getName());
        Glide.with(holder.itemView.getContext())
                .load(category.getImage())
                .placeholder(R.color.img_default)
                .error(R.color.img_default)
                .into(holder.binding.img);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemCateSearchClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<CategoryResponse> categories) {
        this.data.clear();
        this.data.addAll(categories);
        notifyDataSetChanged();
    }
    public static class ItemCategoryHomeViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryHomeBinding binding;

        public ItemCategoryHomeViewHolder(@NonNull ItemCategoryHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
