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

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ItemAddressBinding;

public class AddressItemAdapter extends RecyclerView.Adapter<AddressItemAdapter.AddressItemViewHolder> {
    private List<AddressResponse> data;
    private AddressItemAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(AddressResponse address);
        void onDeleteClick(AddressResponse address);
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
    public void onBindViewHolder(@NonNull AddressItemAdapter.AddressItemViewHolder holder, int position) {
        AddressResponse address = data.get(position);

        holder.binding.name.setText(address.getName());
        holder.binding.more.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.popup_address_menu);

            popupMenu.setGravity(Gravity.END | Gravity.TOP);  // Chỉnh vị trí menu, có thể dùng Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM, Gravity.END tùy nhu cầu

            try {
                Field field = PopupMenu.class.getDeclaredField("mPopup");
                field.setAccessible(true);
                Object menuPopupHelper = field.get(popupMenu);
                Method setForceShowIcon = menuPopupHelper.getClass().getDeclaredMethod("setForceShowIcon", boolean.class);
                setForceShowIcon.setAccessible(true);
                setForceShowIcon.invoke(menuPopupHelper, true);

                Method setPopupLocation = menuPopupHelper.getClass().getDeclaredMethod("setPopupLocation", int.class, int.class);
                setPopupLocation.setAccessible(true);
                setPopupLocation.invoke(menuPopupHelper, 100, 200);
            } catch (Exception e) {
                e.printStackTrace();
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.edit:
                        listener.onEditClick(address);
                        break;
                    case R.id.delete:
                        listener.onDeleteClick(address);
                        removeAddress(position);
                        break;
                }
                return true;
            });

            popupMenu.show();
        });
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
