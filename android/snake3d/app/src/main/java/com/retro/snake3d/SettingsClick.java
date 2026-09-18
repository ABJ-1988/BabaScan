package com.retro.snake3d;

import android.view.View;

/* loaded from: classes3.dex */
public final class SettingsClick implements View.OnClickListener {
    private final MainActivity a;
    private final int k;

    public SettingsClick(MainActivity mainActivity, int i) {
        this.a = mainActivity;
        this.k = i;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.a.onSettingsClick(this.k);
    }
}
