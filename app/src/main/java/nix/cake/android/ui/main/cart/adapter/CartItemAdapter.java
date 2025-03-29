package nix.cake.android.ui.main.cart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nix.cake.android.R;
import nix.cake.android.data.model.api.response.cart.CartItemResponse;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItemResponse> cartItems;

    public CartItemAdapter(List<CartItemResponse> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
//        CartItem cartItem = cartItems.get(position);
//
//        holder.imageView.setImageResource(R.drawable.cake_default);
//        holder.nameTextView.setText(cartItem.getName());
//        holder.flavorTextView.setText(cartItem.getFlavor());
//        holder.sizeTextView.setText(cartItem.getSize());
//        holder.priceTextView.setText(cartItem.getPrice());
//        holder.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
//
//        holder.removeItemButton.setOnClickListener(v -> {
//            int quantity = cartItem.getQuantity();
//            if (quantity > 1) {
//                cartItem.setQuantity(quantity - 1);
//                holder.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
//                notifyItemChanged(position);
//            }
//        });
//
//        holder.addItemButton.setOnClickListener(v -> {
//            cartItem.setQuantity(cartItem.getQuantity() + 1);
//            holder.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
//            notifyItemChanged(position);
//        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView price, priceSale, count, priceTextView, quantityTextView;
        ImageView remove, add;
        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgItem);
            price = itemView.findViewById(R.id.price);
            priceSale = itemView.findViewById(R.id.priceSale);
            count = itemView.findViewById(R.id.countItem);

            remove = itemView.findViewById(R.id.removeItem);
            add = itemView.findViewById(R.id.addItem);
        }
    }
}
