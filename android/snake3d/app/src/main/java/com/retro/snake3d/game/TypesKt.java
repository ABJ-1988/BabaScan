package com.retro.snake3d.game;

import kotlin.Metadata;

/* compiled from: Types.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\t\u001a\u0016\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001\u001a\u000e\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0001\u001a\u000e\u0010\b\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0001\u001a\u000e\u0010\t\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0001\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0086T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0086T¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"BOARD_D", "", "BOARD_W", "cell", "x", "z", "cx", "c", "cz", "idx", "app_debug"}, k = 2, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class TypesKt {
    public static final int BOARD_D = 21;
    public static final int BOARD_W = 13;

    public static final int cell(int x, int z) {
        return (x << 8) | z;
    }

    public static final int cx(int c) {
        return c >> 8;
    }

    public static final int cz(int c) {
        return c & 255;
    }

    public static final int idx(int c) {
        return (cx(c) * 21) + cz(c);
    }
}
