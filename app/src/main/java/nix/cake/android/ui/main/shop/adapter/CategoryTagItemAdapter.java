package nix.cake.android.ui.main.shop.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.databinding.ItemCategoryTagBinding;

public class CategoryTagItemAdapter extends RecyclerView.Adapter<CategoryTagItemAdapter.CategoryTagItemViewHolder> {
    private List<CategoryResponse> data;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CategoryResponse category);
    }
    public CategoryTagItemAdapter() {
        this.data = new ArrayList<>();
    }

    public CategoryTagItemAdapter(OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public CategoryTagItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryTagBinding binding = ItemCategoryTagBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CategoryTagItemViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CategoryTagItemViewHolder holder, int position) {
        CategoryResponse category = data.get(position);
        holder.binding.name.setText(category.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(category);
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

    public void addItem(CategoryResponse category) {
        this.data.add(category);
        notifyItemInserted(data.size() - 1);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
    public static class CategoryTagItemViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryTagBinding binding;

        public CategoryTagItemViewHolder(@NonNull ItemCategoryTagBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
