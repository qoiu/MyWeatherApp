package com.geekbrains.myweather.presenters;

import androidx.annotation.NonNull;
import java.lang.ref.WeakReference;

public abstract class BasePresenter<V> {
    private WeakReference<V> view;

    public void setModel() {
        resetState();
    }

    private void resetState() {
    }

    public void bindView(@NonNull V view) {
        this.view = new WeakReference<>(view);
    }

    public void unbindView() {
        this.view = null;
    }

    protected V view() {
        if (view == null) {
            return null;
        } else {
            return view.get();
        }
    }

    private boolean setupDone() {
        return view() != null;
    }
}
