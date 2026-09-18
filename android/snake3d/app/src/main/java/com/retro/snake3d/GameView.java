package com.retro.snake3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.core.app.NotificationCompat;
import com.retro.snake3d.game.Dir;
import com.retro.snake3d.game.State;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: GameView.kt */
@Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u001b\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0016J\u0010\u0010\u001d\u001a\u00020\u00192\u0006\u0010\u001e\u001a\u00020\u001fH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lcom/retro/snake3d/GameView;", "Landroid/opengl/GLSurfaceView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "anchorX", "", "anchorY", "downTime", "", "downX", "downY", "onTap", "Lkotlin/Function0;", "", "getOnTap", "()Lkotlin/jvm/functions/Function0;", "setOnTap", "(Lkotlin/jvm/functions/Function0;)V", "renderer", "Lcom/retro/snake3d/GameRenderer;", "swipeThreshold", "swiped", "", "tapSlop", "attach", "r", "onTouchEvent", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes3.dex */
public final class GameView extends GLSurfaceView {
    private float anchorX;
    private float anchorY;
    private long downTime;
    private float downX;
    private float downY;
    private Function0<Unit> onTap;
    private GameRenderer renderer;
    private final float swipeThreshold;
    private boolean swiped;
    private final float tapSlop;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public GameView(Context context) {
        this(context, (AttributeSet) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ GameView(Context context, AttributeSet attributeSet, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : attributeSet);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Intrinsics.checkNotNullParameter(context, "context");
        this.swipeThreshold = getResources().getDisplayMetrics().density * 22.0f;
        this.tapSlop = getResources().getDisplayMetrics().density * 18.0f;
    }

    public final Function0<Unit> getOnTap() {
        return this.onTap;
    }

    public final void setOnTap(Function0<Unit> function0) {
        this.onTap = function0;
    }

    public final void attach(GameRenderer r) {
        Intrinsics.checkNotNullParameter(r, "r");
        setEGLContextClientVersion(3);
        this.renderer = r;
        setRenderer(r);
        setRenderMode(1);
        setPreserveEGLContextOnPause(true);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        final GameRenderer r = this.renderer;
        if (r == null) {
            return false;
        }
        switch (event.getActionMasked()) {
            case 0:
                this.downX = event.getX();
                this.downY = event.getY();
                this.anchorX = this.downX;
                this.anchorY = this.downY;
                this.downTime = System.currentTimeMillis();
                this.swiped = false;
                return true;
            case 1:
                long dt = System.currentTimeMillis() - this.downTime;
                boolean moved = Math.abs(event.getX() - this.downX) > this.tapSlop || Math.abs(event.getY() - this.downY) > this.tapSlop;
                if (!this.swiped && !moved && dt < 300) {
                    if (r.getHudState() == State.PLAYING) {
                        final boolean left = event.getX() < ((float) getWidth()) * 0.5f;
                        queueEvent(new Runnable() { // from class: com.retro.snake3d.GameView$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                GameView.onTouchEvent$lambda$1(r, left);
                            }
                        });
                    } else {
                        Function0<Unit> function0 = this.onTap;
                        if (function0 != null) {
                            function0.invoke();
                        }
                    }
                }
                return true;
            case 2:
                float dx = event.getX() - this.anchorX;
                float dy = event.getY() - this.anchorY;
                if (Math.abs(dx) >= this.swipeThreshold || Math.abs(dy) >= this.swipeThreshold) {
                    final Dir d = Math.abs(dx) > Math.abs(dy) ? dx > 0.0f ? Dir.RIGHT : Dir.LEFT : dy > 0.0f ? Dir.DOWN : Dir.UP;
                    queueEvent(new Runnable() { // from class: com.retro.snake3d.GameView$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            GameView.onTouchEvent$lambda$0(r, d);
                        }
                    });
                    this.swiped = true;
                    this.anchorX = event.getX();
                    this.anchorY = event.getY();
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onTouchEvent$lambda$0(GameRenderer r, Dir d) {
        Intrinsics.checkNotNullParameter(r, "$r");
        Intrinsics.checkNotNullParameter(d, "$d");
        r.getCore().input(d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onTouchEvent$lambda$1(GameRenderer r, boolean $left) {
        Intrinsics.checkNotNullParameter(r, "$r");
        r.getCore().inputRelative($left);
    }
}
