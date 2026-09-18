package com.retro.snake3d.game;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;

/* compiled from: Types.kt */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u000e\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u001f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0006\u0010\r\u001a\u00020\u0000J\u0006\u0010\u000e\u001a\u00020\u0000J\u0006\u0010\u000f\u001a\u00020\u0000R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013¨\u0006\u0014"}, d2 = {"Lcom/retro/snake3d/game/Dir;", "", "dx", "", "dz", "angle", "", "(Ljava/lang/String;IIIF)V", "getAngle", "()F", "getDx", "()I", "getDz", "opposite", "turnLeft", "turnRight", "UP", "RIGHT", "DOWN", "LEFT", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes5.dex */
public enum Dir {
    UP(0, -1, 0.0f),
    RIGHT(1, 0, -1.5707964f),
    DOWN(0, 1, 3.1415927f),
    LEFT(-1, 0, 1.5707964f);

    private final float angle;
    private final int dx;
    private final int dz;

    /* compiled from: Types.kt */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[Dir.values().length];
            try {
                iArr[Dir.UP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Dir.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Dir.LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Dir.RIGHT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }


    Dir(int dx, int dz, float angle) {
        this.dx = dx;
        this.dz = dz;
        this.angle = angle;
    }

    public final float getAngle() {
        return this.angle;
    }

    public final int getDx() {
        return this.dx;
    }

    public final int getDz() {
        return this.dz;
    }

    public final Dir opposite() {
        switch (WhenMappings.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
                return DOWN;
            case 2:
                return UP;
            case 3:
                return RIGHT;
            case 4:
                return LEFT;
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    public final Dir turnLeft() {
        switch (WhenMappings.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
                return LEFT;
            case 2:
                return RIGHT;
            case 3:
                return DOWN;
            case 4:
                return UP;
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    public final Dir turnRight() {
        switch (WhenMappings.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
                return RIGHT;
            case 2:
                return LEFT;
            case 3:
                return UP;
            case 4:
                return DOWN;
            default:
                throw new NoWhenBranchMatchedException();
        }
    }
}
