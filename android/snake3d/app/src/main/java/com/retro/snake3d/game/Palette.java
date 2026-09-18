package com.retro.snake3d.game;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* compiled from: Types.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003¢\u0006\u0002\u0010\tR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000b¨\u0006\u0012"}, d2 = {"Lcom/retro/snake3d/game/Palette;", "", "snakeHead", "", "snakeBody", "grid", "floor", "fog", "accent", "(IIIIII)V", "getAccent", "()I", "getFloor", "getFog", "getGrid", "getSnakeBody", "getSnakeHead", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class Palette {
    private final int accent;
    private final int floor;
    private final int fog;
    private final int grid;
    private final int snakeBody;
    private final int snakeHead;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Palette[] ALL = {new Palette(11992990, 6021227, 3121991, 529424, 264712, 16732120), new Palette(10417663, 5229040, 2850728, 462874, 198413, 16761419), new Palette(16763378, 16736198, 10695051, 1378328, 656140, 5761279), new Palette(16769162, 16753210, 11756826, 1576970, 788228, 7077754), new Palette(14141183, 10122239, 5980080, 920092, 394254, 16772693), new Palette(16752286, 16726843, 10690592, 1705992, 852996, 5761279)};

    public Palette(int snakeHead, int snakeBody, int grid, int floor, int fog, int accent) {
        this.snakeHead = snakeHead;
        this.snakeBody = snakeBody;
        this.grid = grid;
        this.floor = floor;
        this.fog = fog;
        this.accent = accent;
    }

    public final int getSnakeHead() {
        return this.snakeHead;
    }

    public final int getSnakeBody() {
        return this.snakeBody;
    }

    public final int getGrid() {
        return this.grid;
    }

    public final int getFloor() {
        return this.floor;
    }

    public final int getFog() {
        return this.fog;
    }

    public final int getAccent() {
        return this.accent;
    }

    /* compiled from: Types.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000f"}, d2 = {"Lcom/retro/snake3d/game/Palette$Companion;", "", "()V", "ALL", "", "Lcom/retro/snake3d/game/Palette;", "getALL", "()[Lcom/retro/snake3d/game/Palette;", "[Lcom/retro/snake3d/game/Palette;", "b", "", "c", "", "g", "r", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Palette[] getALL() {
            return Palette.ALL;
        }

        public final float r(int c) {
            return ((c >> 16) & 255) / 255.0f;
        }

        public final float g(int c) {
            return ((c >> 8) & 255) / 255.0f;
        }

        public final float b(int c) {
            return (c & 255) / 255.0f;
        }
    }
}
