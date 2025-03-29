package nix.cake.android.ui.main;

import nix.cake.android.ui.base.activity.BaseCallback;

public interface MainCalback<T> extends BaseCallback {

    default void doSuccess(T object) {

    }
}