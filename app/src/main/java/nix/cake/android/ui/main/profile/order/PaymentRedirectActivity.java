package nix.cake.android.ui.main.profile.order;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentRedirectActivity extends AppCompatActivity {

    public static final String KEY_ORDER_ID = "lastOrderId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = getIntent().getStringExtra("paymentUrl");
        String orderId = getIntent().getStringExtra("orderId");

        SharedPreferences prefs = getSharedPreferences("payment", MODE_PRIVATE);
        prefs.edit().putString(KEY_ORDER_ID, orderId).apply();

        if (url != null && !url.isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }

    }
}
