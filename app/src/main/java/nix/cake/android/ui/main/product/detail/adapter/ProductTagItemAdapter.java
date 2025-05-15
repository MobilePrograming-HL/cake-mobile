package nix.cake.android.ui.main.product.detail.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.data.model.api.response.product.TagResponse;
import nix.cake.android.databinding.ItemProductTagBinding;
import nix.cake.android.R;

public class ProductTagItemAdapter extends RecyclerView.Adapter<ProductTagItemAdapter.ProductTagItemViewHolder> {
    private List<TagResponse> data = new ArrayList<>();
    private OnItemClickListener listener;
    private int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(TagResponse tag);
    }

    public ProductTagItemAdapter() {}

    public ProductTagItemAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductTagItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductTagBinding binding = ItemProductTagBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductTagItemViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductTagItemViewHolder holder, int position) {
        TagResponse tag = data.get(position);
        holder.binding.name.setText(tag.getName());
        holder.binding.name.setBackgroundResource(
                position == selectedPosition ? R.drawable.background_product_tag_select : R.drawable.background_product_tag);
        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if (previousSelected != -1) {
                notifyItemChanged(previousSelected);
            }
            notifyItemChanged(selectedPosition);
            if (listener != null) {
                listener.onItemClick(tag);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<TagResponse> tags) {
        this.data.clear();
        this.data.addAll(tags);
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public void addItem(TagResponse category) {
        this.data.add(category);
        notifyItemInserted(data.size() - 1);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            if (selectedPosition == position) {
                selectedPosition = -1;
            }
            notifyItemRemoved(position);
        }
    }

    public static class ProductTagItemViewHolder extends RecyclerView.ViewHolder {
        ItemProductTagBinding binding;

        public ProductTagItemViewHolder(@NonNull ItemProductTagBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}