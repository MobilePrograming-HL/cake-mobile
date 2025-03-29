package nix.cake.android.ui.base.activity;

public interface BaseCallback {
    void doError(Throwable error);
    void doSuccess();
    void doFail();
}
