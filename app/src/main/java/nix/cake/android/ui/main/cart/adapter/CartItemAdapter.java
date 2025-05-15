package nix.cake.android.ui.main.cart.adapter;

import static nix.cake.android.utils.DisplayUtils.convertDoubleTwoDecimalsHasCurrency;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.cart.CartItemResponse;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.databinding.ItemCartBinding;
import nix.cake.android.ui.main.profile.address.adapter.AddressItemAdapter;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItemResponse> data;
    private OnItemClickListener listener;
    
    private String countItemDefault = "1";

    public interface OnItemClickListener {
        void onItemClick(CartItemResponse item);
        void onPlusItem(CartItemResponse item);
        void onMinusItem(CartItemResponse item);
        void onTextCountItemChange(CartItemResponse item);
        void onDelete(CartItemResponse item, int position);
        void onSelectClick(CartItemResponse item);
    }
    public CartItemAdapter(OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }
    public Boolean isEmptyData() {
        return data.isEmpty();
    }
    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CartItemViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItemResponse item = data.get(position);
        holder.binding.name.setText(item.getProduct().getName());
        holder.binding.tag.setText(item.getTag().getName());
        holder.binding.countItemCart.setText(item.getQuantity().toString());
        updateDefaultUI(holder,item);
        Glide.with(holder.itemView.getContext())
                .load(item.getProduct().getImage())
                .placeholder(R.color.img_default)
                .error(R.color.img_default)
                .into(holder.binding.img);
        if (item.getProduct().getDiscount() == null) {
            holder.binding.priceAfterSale.setVisibility(View.GONE);

            holder.binding.price.setText(convertDoubleTwoDecimalsHasCurrency(item.getProduct().getPrice()));
        } else {
            holder.binding.priceAfterSale.setVisibility(View.VISIBLE);

            holder.binding.price.setText(convertDoubleTwoDecimalsHasCurrency(item.getProduct().getPrice()));
            holder.binding.price.setPaintFlags(holder.binding.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            double discountedPrice = item.getProduct().getPrice() * (1 - item.getProduct().getDiscount().getDiscountPercentage() / 100.0);
            holder.binding.priceAfterSale.setText(convertDoubleTwoDecimalsHasCurrency(discountedPrice));
        }
        holder.binding.lDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
        holder.binding.img.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
        holder.binding.checked.setOnClickListener(v -> {
            if (listener != null) {
                if(item.getIsSelect()) {
                    holder.binding.checked.setImageResource(R.drawable.unchecked);
                    item.setIsSelect(false);
                } else {
                    holder.binding.checked.setImageResource(R.drawable.checked);
                    item.setIsSelect(true);
                }
                listener.onSelectClick(item);
            }
        });

        holder.binding.removeItem.setOnClickListener(v -> {
            if (listener != null && item.getQuantity() != null) {
                if(item.getQuantity() > 1) {
                    String input = holder.binding.countItemCart.getText().toString();
                    if (input.isEmpty()) input = countItemDefault;
                    item.setQuantity(Integer.parseInt(input) - 1);
                    holder.binding.countItemCart.setText(String.valueOf(item.getQuantity()));
                    listener.onMinusItem(item);
                }
            }
        });

        holder.binding.addItem.setOnClickListener(v -> {
            if (listener != null && item.getQuantity() != null && item.getProduct() != null && item.getProduct().getQuantity() != null) {
                String input = holder.binding.countItemCart.getText().toString();
                if (input.isEmpty()) input = countItemDefault;

                long currentQty = Long.parseLong(input);
                long maxQty = item.getProduct().getQuantity();

                if(currentQty < maxQty) {
                    item.setQuantity(Integer.parseInt(input) + 1);
                    holder.binding.countItemCart.setText(String.valueOf(item.getQuantity()));
                    listener.onPlusItem(item);
                }
            }
        });

        holder.binding.countItemCart.setSelection(holder.binding.countItemCart.getText().length());
        holder.binding.countItemCart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                if (!input.isEmpty()) {
                    try {
                        int value = Integer.parseInt(input);
                        if (value < 1) {
                            holder.binding.countItemCart.setText(countItemDefault);
                            holder.binding.countItemCart.setSelection(holder.binding.countItemCart.getText().length());
                            item.setQuantity(Integer.parseInt(holder.binding.countItemCart.getText().toString()));

                            if (listener != null) {
                                listener.onTextCountItemChange(item);
                            }
                        } else if (value > item.getProduct().getQuantity()) {
                            holder.binding.countItemCart.setText(item.getProduct().getQuantity().toString());
                            holder.binding.countItemCart.setSelection(holder.binding.countItemCart.getText().length());
                            item.setQuantity(Integer.parseInt(holder.binding.countItemCart.getText().toString()));

                            if (listener != null) {
                                listener.onTextCountItemChange(item);
                            }
                        } 
                    } catch (NumberFormatException e) {
                        holder.binding.countItemCart.setText("1");
                        holder.binding.countItemCart.setSelection(holder.binding.countItemCart.getText().length());
                    }
                }
            }
        });
        holder.binding.countItemCart.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {

                String input = holder.binding.countItemCart.getText().toString();
                if (input.isEmpty()) {
                    holder.binding.countItemCart.setText(item.getQuantity().toString());
                    holder.binding.countItemCart.setSelection(holder.binding.countItemCart.getText().length());
                }
                item.setQuantity(Integer.parseInt(holder.binding.countItemCart.getText().toString()));

                if (listener != null) {
                    listener.onTextCountItemChange(item);
                }
            }
        });
        holder.binding.actionDelete.setOnClickListener(v -> {
            if (listener != null ) {
                listener.onDelete(item, holder.getAdapterPosition());
                holder.binding.swipeLayout.close(false);
            }
        });
    }
    private void updateDefaultUI(CartItemViewHolder holder, CartItemResponse cart) {
        if (cart.getIsSelect()) {
            holder.binding.checked.setImageResource(R.drawable.checked);
        } else {
            holder.binding.checked.setImageResource(R.drawable.unchecked);
        }
    }
    public void setData(List<CartItemResponse> cartItemResponses) {
        Map<String, Boolean> oldSelections = this.data.stream()
                .filter(item -> item.getIsSelect() != null && item.getIsSelect())
                .collect(Collectors.toMap(CartItemResponse::getId, cartItemResponse -> true));

        this.data.clear();
        cartItemResponses.forEach(item -> {
            item.setIsSelect(oldSelections.getOrDefault(item.getId(), false));
        });

        this.data.addAll(cartItemResponses);
        notifyDataSetChanged();
    }
    public void removeCartItem(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        public CartItemViewHolder(@NonNull ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}