package nix.cake.android.ui.main.profile.address.detail.fragment.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.address.NationResponse;
import nix.cake.android.databinding.ItemAddressProvinceBinding;

public class AddressProvinceAdapter extends RecyclerView.Adapter<AddressProvinceAdapter.AddressProvinceItemViewHolder> {
    private List<NationResponse> data;
    private List<NationResponse> dataFull;
    private OnItemClickListener listener;
    private int selectedPosition = -1;


    public interface OnItemClickListener {
        void onItemClick(NationResponse address);
    }

    public AddressProvinceAdapter() {
        this.data = new ArrayList<>();
        this.dataFull = new ArrayList<>();
    }

    public AddressProvinceAdapter(OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.dataFull = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public AddressProvinceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressProvinceBinding binding = ItemAddressProvinceBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AddressProvinceItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressProvinceItemViewHolder holder, int position) {
        NationResponse nationResponse = data.get(position);
        holder.binding.name.setText(nationResponse.getName());
        updateDefaultUI(holder, nationResponse);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSelected(false);
                }
                nationResponse.setSelected(true);
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onItemClick(nationResponse);
                }

            }
        });
    }
    private void updateDefaultUI(AddressProvinceItemViewHolder holder, NationResponse nation) {
        if (nation.isSelected()) {
            holder.binding.name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.binding.ticked.setVisibility(View.VISIBLE);

        } else {
            holder.binding.name.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.binding.ticked.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<NationResponse> nation) {
        this.data.clear();
        this.data.addAll(nation);
        this.dataFull.clear();
        this.dataFull.addAll(nation);
        selectedPosition = -1;

        notifyDataSetChanged();
    }

    public void filter(String text) {
        data.clear();
        if(text.isEmpty()){
            data.addAll(dataFull);
        } else{
            text = text.toLowerCase().trim();
            for(NationResponse item: dataFull){
                if(item.getName().toLowerCase().contains(text)){
                    data.add(item);
                }
            }
        }
        selectedPosition = -1;
        notifyDataSetChanged();
    }
    private boolean isDuplicate(NationResponse newItem) {
        for (NationResponse existingItem : data) {
            if (existingItem.getName().equals(newItem.getName())) {
                return true;
            }
        }
        return false;
    }

    public void addData(List<NationResponse> newList) {
        if (newList == null) return;

        int startPosition = data.size();
        for (NationResponse item : newList) {
            if (!isDuplicate(item)) {
                data.add(item);
                dataFull.add(item);
            }
        }
        notifyItemRangeInserted(startPosition, data.size() - startPosition);
    }

    public static class AddressProvinceItemViewHolder extends RecyclerView.ViewHolder {
        ItemAddressProvinceBinding binding;

        public AddressProvinceItemViewHolder(@NonNull ItemAddressProvinceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
