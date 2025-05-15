package nix.cake.android.ui.main.product.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.databinding.ItemCategoryFilterBinding;
public class CategoryFilterItemAdapter extends RecyclerView.Adapter<CategoryFilterItemAdapter.CategoryFilterItemViewHolder> {
    private List<CategoryResponse> data;
    private CategoryFilterItemAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CategoryResponse category);
    }
    public CategoryFilterItemAdapter() {
        this.data = new ArrayList<>();
    }

    public CategoryFilterItemAdapter(CategoryFilterItemAdapter.OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public CategoryFilterItemAdapter.CategoryFilterItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryFilterBinding binding = ItemCategoryFilterBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CategoryFilterItemAdapter.CategoryFilterItemViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CategoryFilterItemAdapter.CategoryFilterItemViewHolder holder, int position) {
        CategoryResponse category = data.get(position);
        holder.binding.name.setText(category.getName());

        if (category.getIsSelected()) {
            holder.binding.name.setBackgroundResource(R.drawable.background_cate_filter_select);
        } else {
            holder.binding.name.setBackgroundResource(R.drawable.background_cate_filter);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setIsSelected(false);
                }
                category.setIsSelected(true);
                notifyDataSetChanged();
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
    public static class CategoryFilterItemViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryFilterBinding binding;

        public CategoryFilterItemViewHolder(@NonNull ItemCategoryFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
