package nix.cake.android.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import nix.cake.android.R;

public class CustomDialog {
    private Dialog dialog;
    private Context context;
    private String title;
    private OnDialogClickListener listener;

    public interface OnDialogClickListener {
        void onDeleteClick();
        void onCancelClick();
    }

    public CustomDialog(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }

    public CustomDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CustomDialog setOnClickListener(OnDialogClickListener listener) {
        this.listener = listener;
        return this;
    }

    public void show() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_delete);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView btnDelete = dialog.findViewById(R.id.delete);
        TextView btnCancel = dialog.findViewById(R.id.cancel);

        if (title != null) {
            tvTitle.setText(title);
        }

        btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick();
            }
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelClick();
            }
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

