package nix.cake.android.ui.main.profile.address.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.databinding.ItemAddressSelectBinding;
import nix.cake.android.databinding.ItemAddressSelectBinding;

public class AddressSelectItemAdapter extends RecyclerView.Adapter<AddressSelectItemAdapter.AddressSelectItemViewHolder> {
    private List<AddressResponse> data;
    private AddressSelectItemAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(AddressResponse address);
        void onSelectClick(AddressResponse address);
    }
    public AddressSelectItemAdapter() {
        this.data = new ArrayList<>();
    }

    public AddressSelectItemAdapter(AddressSelectItemAdapter.OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public AddressSelectItemAdapter.AddressSelectItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressSelectBinding binding = ItemAddressSelectBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AddressSelectItemAdapter.AddressSelectItemViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AddressSelectItemViewHolder holder, int position) {
        AddressResponse address = data.get(position);

        holder.binding.name.setText(address.getFullName());
        holder.binding.phone.setText(address.getPhoneNumber());
        holder.binding.addressDetails.setText(address.getDetails());
        holder.binding.address.setText(address.getCommune().getName() + ", " + address.getDistrict().getName()
                + ", " + address.getProvince().getName());

        updateDefaultUI(holder, address);
        updateSelectUI(holder,address);
        holder.binding.edit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(address);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                address.setIsSelect(true);
                listener.onSelectClick(address);
            }
        });
    }

    private void updateDefaultUI(AddressSelectItemViewHolder holder, AddressResponse address) {
        if (address.getIsDefault()) {
            holder.binding.tvDefault.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvDefault.setVisibility(View.INVISIBLE);
        }
    }

    private void updateSelectUI(AddressSelectItemViewHolder holder, AddressResponse address) {
        if (address.getIsSelect()) {
            holder.binding.checkDefault.setImageResource(R.drawable.checked);
        } else {
            holder.binding.checkDefault.setImageResource(R.drawable.unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<AddressResponse> addresses) {
        this.data.clear();
        this.data.addAll(addresses);
        notifyDataSetChanged();
    }
    private boolean isDuplicate(AddressResponse newItem) {
        for (AddressResponse existingItem : data) {
            if (existingItem.getId().equals(newItem.getId())) {
                return true;
            }
        }
        return false;
    }

    public void addData(List<AddressResponse> newList) {
        if (newList == null) return;

        int startPosition = data.size();
        for (AddressResponse item : newList) {
            if (!isDuplicate(item)) {
                data.add(item);
            }
        }
        notifyItemRangeInserted(startPosition, data.size() - startPosition);
    }
    public void addAddress(AddressResponse address) {
        this.data.add(address);
        notifyItemInserted(data.size() - 1);
        notifyDataSetChanged();
    }

    public void removeAddress(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }
    public Boolean isEmptyData() {
        return data.isEmpty();
    }
    public static class AddressSelectItemViewHolder extends RecyclerView.ViewHolder {
        ItemAddressSelectBinding binding;

        public AddressSelectItemViewHolder(@NonNull ItemAddressSelectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
