package nix.cake.android.ui.main.profile.address.adapter;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.data.model.api.response.profile.address.NationResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ItemAddressBinding;

public class AddressItemAdapter extends RecyclerView.Adapter<AddressItemAdapter.AddressItemViewHolder> {
    private List<AddressResponse> data;
    private AddressItemAdapter.OnItemClickListener listener;
    private int selectedPosition = -1;

    public interface OnItemClickListener {
        void onEditClick(AddressResponse address);
        void onDeleteClick(AddressResponse address, int position);
        void onDefaultClick(AddressResponse address, int position);
    }
    public AddressItemAdapter() {
        this.data = new ArrayList<>();
    }

    public AddressItemAdapter(AddressItemAdapter.OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public AddressItemAdapter.AddressItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressBinding binding = ItemAddressBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AddressItemAdapter.AddressItemViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AddressItemViewHolder holder, int position) {
        AddressResponse address = data.get(position);

        holder.binding.name.setText(address.getFullName());
        holder.binding.phone.setText(address.getPhoneNumber());
        holder.binding.addressDetails.setText(address.getDetails());
        holder.binding.address.setText(address.getCommune().getName() + ", " + address.getDistrict().getName()
                + ", " + address.getProvince().getName());

        updateDefaultUI(holder, address);
        holder.binding.delete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(address, holder.getAdapterPosition());
            }
        });
        holder.binding.lDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(address);
            }
        });
        holder.binding.lCheck.setOnClickListener(v -> {
            if (listener != null) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setIsDefault("false");
                }
                address.setIsDefault("true");
                notifyDataSetChanged();

                listener.onDefaultClick(address, holder.getAdapterPosition());
            }
        });
    }

    private void updateDefaultUI(AddressItemViewHolder holder, AddressResponse address) {
        if (Objects.equals(address.getIsDefault(), "true")) {
            holder.binding.checkDefault.setImageResource(R.drawable.checked);
            holder.binding.tvDefault.setText(R.string.default_address);
        } else {
            holder.binding.checkDefault.setImageResource(R.drawable.unchecked);
            holder.binding.tvDefault.setText(R.string.set_as_default_address);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<AddressResponse> addresses) {
        this.data.clear();
        this.data.addAll(addresses);
        selectedPosition = -1;
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
    public static class AddressItemViewHolder extends RecyclerView.ViewHolder {
        ItemAddressBinding binding;

        public AddressItemViewHolder(@NonNull ItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
