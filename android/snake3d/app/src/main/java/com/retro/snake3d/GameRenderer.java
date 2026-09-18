package com.retro.snake3d;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.retro.snake3d.audio.Sfx;
import com.retro.snake3d.game.Dir;
import com.retro.snake3d.game.FxEvent;
import com.retro.snake3d.game.GameCore;
import com.retro.snake3d.game.Palette;
import com.retro.snake3d.game.State;
import com.retro.snake3d.gl.CubeBatch;
import com.retro.snake3d.gl.GroundRenderer;
import com.retro.snake3d.gl.ParticleSystem;
import com.retro.snake3d.gl.PostFx;
import java.util.ArrayList;
import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import kotlin.Metadata;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* compiled from: GameRenderer.kt */
@Metadata(d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u0000 \u008e\u00012\u00020\u0001:\u0002\u008e\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0018\u0010b\u001a\u00020c2\u0006\u0010d\u001a\u00020$2\u0006\u0010e\u001a\u000204H\u0002J\b\u0010f\u001a\u00020cH\u0002J\b\u0010g\u001a\u00020cH\u0002J\b\u0010h\u001a\u00020cH\u0002J\b\u0010i\u001a\u00020cH\u0002J\b\u0010j\u001a\u00020cH\u0002J\b\u0010k\u001a\u00020cH\u0002J\b\u0010l\u001a\u00020cH\u0002J\u0018\u0010m\u001a\u00020c2\u0006\u0010n\u001a\u00020\n2\u0006\u0010o\u001a\u00020$H\u0002J\u0010\u0010p\u001a\u00020\b2\u0006\u0010q\u001a\u00020$H\u0002J\u0010\u0010r\u001a\u00020\b2\u0006\u0010q\u001a\u00020$H\u0002J \u0010s\u001a\u00020\b2\u0006\u0010t\u001a\u00020\b2\u0006\u0010u\u001a\u00020\b2\u0006\u0010v\u001a\u00020\bH\u0002J\u0010\u0010w\u001a\u00020c2\u0006\u0010x\u001a\u00020\bH\u0002J\u0012\u0010y\u001a\u00020c2\b\u0010z\u001a\u0004\u0018\u00010{H\u0016J\u0006\u0010|\u001a\u00020cJ\"\u0010}\u001a\u00020c2\b\u0010z\u001a\u0004\u0018\u00010{2\u0006\u0010~\u001a\u00020$2\u0006\u0010\u007f\u001a\u00020$H\u0016J\u001f\u0010\u0080\u0001\u001a\u00020c2\b\u0010z\u001a\u0004\u0018\u00010{2\n\u0010\u0081\u0001\u001a\u0005\u0018\u00010\u0082\u0001H\u0016J\t\u0010\u0083\u0001\u001a\u00020cH\u0002J\u0007\u0010\u0084\u0001\u001a\u00020cJ,\u0010\u0085\u0001\u001a\u00020c2\u0007\u0010\u0086\u0001\u001a\u00020\b2\u0007\u0010\u0087\u0001\u001a\u00020\b2\u0007\u0010\u0088\u0001\u001a\u00020\b2\u0006\u0010u\u001a\u00020\bH\u0002J\u001b\u0010\u0089\u0001\u001a\u00020c2\u0007\u0010\u008a\u0001\u001a\u00020\b2\u0007\u0010\u008b\u0001\u001a\u00020\bH\u0002J\u0011\u0010\u008c\u0001\u001a\u00020c2\u0006\u0010x\u001a\u00020\bH\u0002J\u0011\u0010\u008d\u0001\u001a\u00020c2\u0006\u0010x\u001a\u00020\bH\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010%\u001a\u00020$X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)R\u001a\u0010*\u001a\u00020$X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010'\"\u0004\b,\u0010)R\u001a\u0010-\u001a\u00020$X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010'\"\u0004\b/\u0010)R\u001a\u00100\u001a\u00020$X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010'\"\u0004\b2\u0010)R\u001a\u00103\u001a\u000204X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b5\u00106\"\u0004\b7\u00108R\u001a\u00109\u001a\u00020$X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010'\"\u0004\b;\u0010)R\u001a\u0010<\u001a\u00020=X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b>\u0010?\"\u0004\b@\u0010AR\u001a\u0010B\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bC\u0010D\"\u0004\bE\u0010FR\u0010\u0010G\u001a\u0004\u0018\u00010HX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020$X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020KX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020MX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u00020PX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010Q\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010S\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010U\u001a\u00020VX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010W\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010X\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010Y\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010Z\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010[\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\\\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010]\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010^\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010_\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010`\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010a\u001a\u00020$X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u008f\u0001"}, d2 = {"Lcom/retro/snake3d/GameRenderer;", "Landroid/opengl/GLSurfaceView$Renderer;", "core", "Lcom/retro/snake3d/game/GameCore;", "sfx", "Lcom/retro/snake3d/audio/Sfx;", "(Lcom/retro/snake3d/game/GameCore;Lcom/retro/snake3d/audio/Sfx;)V", "baseDist", "", "cAccent", "", "cBody", "cFloor", "cFog", "cGrid", "cHead", "camDist", "camEyeX", "camEyeY", "camEyeZ", "camPitch", "camYaw", "getCore", "()Lcom/retro/snake3d/game/GameCore;", "cubes", "Lcom/retro/snake3d/gl/CubeBatch;", "deathHide", "desat", "dustAcc", "flash", "flashColor", "focusX", "focusZ", "ground", "Lcom/retro/snake3d/gl/GroundRenderer;", "height", "", "hudApples", "getHudApples", "()I", "setHudApples", "(I)V", "hudCombo", "getHudCombo", "setHudCombo", "hudHigh", "getHudHigh", "setHudHigh", "hudLength", "getHudLength", "setHudLength", "hudNewRecord", "", "getHudNewRecord", "()Z", "setHudNewRecord", "(Z)V", "hudScore", "getHudScore", "setHudScore", "hudState", "Lcom/retro/snake3d/game/State;", "getHudState", "()Lcom/retro/snake3d/game/State;", "setHudState", "(Lcom/retro/snake3d/game/State;)V", "hudTime", "getHudTime", "()F", "setHudTime", "(F)V", "lastDir", "Lcom/retro/snake3d/game/Dir;", "lastLevel", "lastNanos", "", "particles", "Lcom/retro/snake3d/gl/ParticleSystem;", "pixelScale", "post", "Lcom/retro/snake3d/gl/PostFx;", "proj", "pulse", "pulseX", "pulseZ", "rnd", "Ljava/util/Random;", "shake", "tAccent", "tBody", "tFloor", "tFog", "tGrid", "tHead", "time", "view", "viewProj", "width", "applyPalette", "", "level", "instant", "consumeEvents", "drawFood", "drawHeadDetails", "drawObstacles", "drawSnake", "drawWalls", "explodeSnake", "fill", "dst", "c", "interpX", "i", "interpZ", "lerpCell", "a", "b", "t", "lerpColors", "dt", "onDrawFrame", "gl", "Ljavax/microedition/khronos/opengles/GL10;", "onGlDestroy", "onSurfaceChanged", "w", "h", "onSurfaceCreated", "config", "Ljavax/microedition/khronos/egl/EGLConfig;", "publishHud", "resetFx", "setFlash", "v", "r", "g", "startPulse", "x", "z", "updateCamera", "updateEffects", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes3.dex */
public final class GameRenderer implements GLSurfaceView.Renderer {
    public static final float AIM_Y = -2.0f;
    private static final float DEG = 0.017453292f;
    public static final float FOV_Y = 52.0f;
    private float baseDist;
    private final float[] cAccent;
    private final float[] cBody;
    private final float[] cFloor;
    private final float[] cFog;
    private final float[] cGrid;
    private final float[] cHead;
    private float camDist;
    private float camEyeX;
    private float camEyeY;
    private float camEyeZ;
    private float camPitch;
    private float camYaw;
    private final GameCore core;
    private final CubeBatch cubes;
    private float deathHide;
    private float desat;
    private float dustAcc;
    private float flash;
    private final float[] flashColor;
    private float focusX;
    private float focusZ;
    private final GroundRenderer ground;
    private int height;
    private volatile int hudApples;
    private volatile int hudCombo;
    private volatile int hudHigh;
    private volatile int hudLength;
    private volatile boolean hudNewRecord;
    private volatile int hudScore;
    private volatile State hudState;
    private volatile float hudTime;
    private Dir lastDir;
    private int lastLevel;
    private long lastNanos;
    private final ParticleSystem particles;
    private float pixelScale;
    private final PostFx post;
    private final float[] proj;
    private float pulse;
    private float pulseX;
    private float pulseZ;
    private final Random rnd;
    private final Sfx sfx;
    private float shake;
    private final float[] tAccent;
    private final float[] tBody;
    private final float[] tFloor;
    private final float[] tFog;
    private final float[] tGrid;
    private final float[] tHead;
    private float time;
    private final float[] view;
    private final float[] viewProj;
    private int width;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final float HALF_X = 6.5f;
    private static final float HALF_Z = 10.5f;

    /* compiled from: GameRenderer.kt */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[State.values().length];
            try {
                iArr[State.DYING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[State.GAMEOVER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[State.PAUSED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public GameRenderer(GameCore core, Sfx sfx) {
        Intrinsics.checkNotNullParameter(core, "core");
        Intrinsics.checkNotNullParameter(sfx, "sfx");
        this.core = core;
        this.sfx = sfx;
        this.cubes = new CubeBatch(4096);
        this.ground = new GroundRenderer(HALF_X + 20.0f, HALF_Z + 26.0f, HALF_X, HALF_Z);
        this.particles = new ParticleSystem(2600);
        this.post = new PostFx();
        this.proj = new float[16];
        this.view = new float[16];
        this.viewProj = new float[16];
        this.camPitch = 1.012291f;
        this.camDist = 30.0f;
        this.baseDist = 30.0f;
        this.flashColor = new float[]{1.0f, 1.0f, 1.0f};
        this.pulse = 1.0f;
        this.cHead = new float[3];
        this.cBody = new float[3];
        this.cGrid = new float[3];
        this.cFloor = new float[3];
        this.cFog = new float[3];
        this.cAccent = new float[3];
        this.tHead = new float[3];
        this.tBody = new float[3];
        this.tGrid = new float[3];
        this.tFloor = new float[3];
        this.tFog = new float[3];
        this.tAccent = new float[3];
        this.lastLevel = -1;
        this.width = 1;
        this.height = 1;
        this.pixelScale = 600.0f;
        this.rnd = new Random();
        this.hudCombo = 1;
        this.hudState = State.TITLE;
    }

    public final GameCore getCore() {
        return this.core;
    }

    /* compiled from: GameRenderer.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u0004X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u0004X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\t¨\u0006\f"}, d2 = {"Lcom/retro/snake3d/GameRenderer$Companion;", "", "()V", "AIM_Y", "", "DEG", "FOV_Y", "HALF_X", "getHALF_X", "()F", "HALF_Z", "getHALF_Z", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final float getHALF_X() {
            return GameRenderer.HALF_X;
        }

        public final float getHALF_Z() {
            return GameRenderer.HALF_Z;
        }
    }

    public final int getHudScore() {
        return this.hudScore;
    }

    public final void setHudScore(int i) {
        this.hudScore = i;
    }

    public final int getHudHigh() {
        return this.hudHigh;
    }

    public final void setHudHigh(int i) {
        this.hudHigh = i;
    }

    public final int getHudCombo() {
        return this.hudCombo;
    }

    public final void setHudCombo(int i) {
        this.hudCombo = i;
    }

    public final int getHudLength() {
        return this.hudLength;
    }

    public final void setHudLength(int i) {
        this.hudLength = i;
    }

    public final int getHudApples() {
        return this.hudApples;
    }

    public final void setHudApples(int i) {
        this.hudApples = i;
    }

    public final State getHudState() {
        return this.hudState;
    }

    public final void setHudState(State state) {
        Intrinsics.checkNotNullParameter(state, "<set-?>");
        this.hudState = state;
    }

    public final boolean getHudNewRecord() {
        return this.hudNewRecord;
    }

    public final void setHudNewRecord(boolean z) {
        this.hudNewRecord = z;
    }

    public final float getHudTime() {
        return this.hudTime;
    }

    public final void setHudTime(float f) {
        this.hudTime = f;
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.cubes.create();
        this.ground.create();
        this.particles.create();
        this.post.create();
        applyPalette(this.core.getLevel(), true);
        GLES30.glEnable(2929);
        GLES30.glEnable(2884);
        GLES30.glCullFace(1029);
        this.lastNanos = System.nanoTime();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        this.width = Math.max(1, w);
        this.height = Math.max(1, h);
        float aspect = this.width / this.height;
        Matrix.perspectiveM(this.proj, 0, 52.0f, aspect, 0.1f, 160.0f);
        this.post.resize(this.width, this.height);
        float tanY = (float) Math.tan(0.4537856f);
        float tanX = tanY * aspect;
        this.pixelScale = this.height / (2.0f * tanY);
        float needW = (HALF_X + 1.6f) / tanX;
        float needD = ((HALF_Z * ((float) Math.sin(this.camPitch))) + 2.2f) / tanY;
        this.baseDist = RangesKt.coerceIn(Math.max(needW, needD), 16.0f, 90.0f);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl) {
        float headX;
        float headZ;
        long now = System.nanoTime();
        float dt = (now - this.lastNanos) / 1.0E9f;
        this.lastNanos = now;
        if (dt > 0.05f) {
            dt = 0.05f;
        }
        if (dt < 0.0f) {
            dt = 0.0f;
        }
        this.time += dt;
        this.core.update(dt);
        if (this.core.getState() == State.PLAYING && this.core.getDir() != this.lastDir) {
            if (this.lastDir != null) {
                this.sfx.turn();
            }
            this.lastDir = this.core.getDir();
        } else if (this.core.getState() != State.PLAYING) {
            this.lastDir = null;
        }
        consumeEvents();
        updateEffects(dt);
        this.particles.update(dt);
        publishHud();
        updateCamera(dt);
        this.post.beginScene();
        GLES30.glClearColor(this.cFog[0], this.cFog[1], this.cFog[2], 1.0f);
        GLES30.glClear(16640);
        GLES30.glEnable(2929);
        GLES30.glEnable(2884);
        GLES30.glDisable(3042);
        if (this.core.getLength() > 0) {
            headX = interpX(0);
            headZ = interpZ(0);
        } else {
            headX = 0.0f;
            headZ = 0.0f;
        }
        this.ground.draw(this.viewProj, this.camEyeX, this.camEyeY, this.camEyeZ, this.time, this.cGrid, this.cFloor, this.cAccent, this.cFog, headX, headZ, this.pulseX, this.pulseZ, this.pulse);
        this.cubes.begin();
        drawWalls();
        drawObstacles();
        drawFood();
        drawSnake();
        this.cubes.flush(this.viewProj, this.camEyeX, this.camEyeY, this.camEyeZ, this.cFog);
        this.particles.draw(this.viewProj, this.pixelScale);
        float bloom = (this.core.getCombo() > 1 ? Math.min(this.core.getCombo(), 8) * 0.045f : 0.0f) + 0.85f;
        this.post.resolve(this.time, bloom, (this.shake * 0.012f) + 0.0016f + (this.flash * 0.004f), 0.09f, 0.16f, RangesKt.coerceIn(this.flash, 0.0f, 0.85f), this.flashColor[0], this.flashColor[1], this.flashColor[2], this.desat);
    }

    private final void updateCamera(float dt) {
        float sin;
        float targetPitch;
        float hx = this.core.getLength() > 0 ? interpX(0) : 0.0f;
        float hz = this.core.getLength() > 0 ? interpZ(0) : 0.0f;
        float tx = hx * 0.3f;
        float tz = 0.22f * hz;
        float k = 1.0f - ((float) Math.exp((-dt) * 3.2f));
        this.focusX += (tx - this.focusX) * k;
        this.focusZ += (tz - this.focusZ) * k;
        boolean playing = this.core.getState() == State.PLAYING || this.core.getState() == State.PAUSED;
        if (this.core.getState() == State.TITLE) {
            sin = (((float) Math.sin(this.time * 0.16f)) * 0.3f) + (this.time * 0.045f);
        } else {
            sin = (((float) Math.sin(this.time * 0.21f)) * 0.035f) + (0.012f * hx);
        }
        float targetYaw = sin;
        this.camYaw += (targetYaw - this.camYaw) * (1.0f - ((float) Math.exp((-dt) * 2.4f)));
        if (this.core.getState() == State.TITLE) {
            targetPitch = ((((float) Math.sin(this.time * 0.13f)) * 5.0f) + 50.0f) * DEG;
        } else if (this.core.getState() == State.DYING || this.core.getState() == State.GAMEOVER) {
            targetPitch = 0.7679449f;
        } else {
            targetPitch = 1.012291f;
        }
        this.camPitch += (targetPitch - this.camPitch) * (1.0f - ((float) Math.exp((-dt) * 2.0f)));
        float growZoom = (Math.min(this.core.getLength(), 90) * 0.0016f) + 1.0f;
        float dyingZoom = this.core.getState() == State.DYING ? 0.9f : 1.0f;
        float targetDist = this.baseDist * growZoom * dyingZoom * (playing ? 1.0f : 1.04f);
        this.camDist += (targetDist - this.camDist) * (1.0f - ((float) Math.exp((-dt) * 2.5f)));
        float cp = (float) Math.cos(this.camPitch);
        float sp = (float) Math.sin(this.camPitch);
        float sx = this.shake * ((float) Math.sin(this.time * 51.0f)) * 0.9f;
        float sy = this.shake * ((float) Math.sin((this.time * 43.0f) + 1.3f)) * 0.9f;
        float sz = this.shake * ((float) Math.sin((this.time * 37.0f) + 2.7f)) * 0.9f;
        this.camEyeX = this.focusX + (((float) Math.sin(this.camYaw)) * cp * this.camDist) + sx;
        this.camEyeY = (this.camDist * sp) + sy;
        this.camEyeZ = this.focusZ + (((float) Math.cos(this.camYaw)) * cp * this.camDist) + sz;
        float[] fArr = this.view;
        float f = this.camEyeX;
        float f2 = this.camEyeY;
        float cp2 = this.camEyeZ;
        float sp2 = this.focusX;
        Matrix.setLookAtM(fArr, 0, f, f2, cp2, sp2 + (sx * 0.4f), (sy * 0.4f) - 2.0f, this.focusZ + (0.4f * sz), 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(this.viewProj, 0, this.proj, 0, this.view, 0);
    }

    private final float lerpCell(float a, float b, float t) {
        return Math.abs(b - a) > 1.5f ? t < 0.5f ? a : b : ((b - a) * t) + a;
    }

    private final float interpX(int i) {
        return lerpCell(this.core.worldX(this.core.segPrev(i)), this.core.worldX(this.core.seg(i)), this.core.getStepAlpha());
    }

    private final float interpZ(int i) {
        return lerpCell(this.core.worldZ(this.core.segPrev(i)), this.core.worldZ(this.core.seg(i)), this.core.getStepAlpha());
    }

    private final void drawSnake() {
        int n = this.core.getLength();
        if (n != 0 && this.deathHide <= 0.0f) {
            float denom = Math.max(1, n - 1);
            float comboGlow = (this.core.getCombo() - 1) * 0.06f;
            int i = n - 1;
            while (-1 < i) {
                float f = i / denom;
                float x = interpX(i);
                float z = interpZ(i);
                float w = (float) Math.sin((this.time * 9.0f) - (i * 0.5f));
                boolean isHead = i == 0;
                float baseSize = isHead ? 0.92f : 0.84f - (0.3f * f);
                float s = baseSize + (0.035f * w);
                float y = (Math.abs((float) Math.sin((this.time * 6.0f) - (i * 0.4f))) * 0.05f) + 0.46f;
                float mixT = isHead ? 0.0f : Math.min(1.0f, i / 3.0f);
                float r = this.cHead[0] + ((this.cBody[0] - this.cHead[0]) * mixT);
                float g = this.cHead[1] + ((this.cBody[1] - this.cHead[1]) * mixT);
                float b = this.cHead[2] + ((this.cBody[2] - this.cHead[2]) * mixT);
                float emis = (isHead ? 0.35f : 0.1f) + (Math.max(0.0f, w) * 0.22f) + comboGlow;
                this.cubes.box(x, y, z, s, s, s, 0.0f, r, g, b, 1.0f, emis, 1.0f);
                i--;
            }
            drawHeadDetails();
        }
    }

    private final void drawHeadDetails() {
        boolean z;
        float x = interpX(0);
        float z2 = interpZ(0);
        Dir d = this.core.getDir();
        float fx = d.getDx();
        float fz = d.getDz();
        float rx = -fz;
        boolean z3 = true;
        int progressionLastElement = ProgressionUtilKt.getProgressionLastElement(-1, 1, 2);
        if (-1 <= progressionLastElement) {
            int side = -1;
            while (true) {
                float ex = (fx * 0.4f) + x + (rx * 0.23f * side);
                float ez = (0.4f * fz) + z2 + (0.23f * fx * side);
                int side2 = side;
                int i = progressionLastElement;
                z = z3;
                this.cubes.box(ex, 0.62f, ez, 0.18f, 0.18f, 0.18f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.6f, 0.0f);
                this.cubes.box((fx * 0.07f) + ex, 0.62f, ez + (0.07f * fz), 0.1f, 0.1f, 0.1f, 0.0f, 0.02f, 0.02f, 0.04f, 1.0f, 0.0f, 0.0f);
                if (side2 == i) {
                    break;
                }
                side = side2 + 2;
                progressionLastElement = i;
                z3 = z;
            }
        } else {
            z = true;
        }
        float tongue = (float) Math.sin(this.time * 3.1f);
        if (tongue > 0.86f) {
            float l = ((tongue - 0.86f) * 1.6f) + 0.3f;
            CubeBatch cubeBatch = this.cubes;
            float f = x + (((l * 0.5f) + 0.46f) * fx);
            float f2 = z2 + (((0.5f * l) + 0.46f) * fz);
            float f3 = !((fx > 0.0f ? 1 : (fx == 0.0f ? 0 : -1)) == 0 ? z : false) ? l : 0.08f;
            if (fz != 0.0f) {
                z = false;
            }
            cubeBatch.box(f, 0.46f, f2, f3, 0.07f, !z ? l : 0.08f, 0.0f, 1.0f, 0.25f, 0.55f, 1.0f, 1.2f, 0.0f);
        }
    }

    private final void drawFood() {
        if (this.core.getAppleCell() >= 0) {
            float x = this.core.worldX(this.core.getAppleCell());
            float z = this.core.worldZ(this.core.getAppleCell());
            float y = (((float) Math.sin(this.time * 3.2f)) * 0.11f) + 0.58f;
            float s = (((float) Math.sin(this.time * 6.4f)) * 0.045f) + 0.5f;
            this.cubes.box(x, y, z, s, s, s, this.time * 1.5f, this.cAccent[0], this.cAccent[1], this.cAccent[2], 1.0f, 1.5f, 1.0f);
            this.cubes.box(x, 0.04f, z, 0.86f, 0.03f, 0.86f, 0.0f, this.cAccent[0], this.cAccent[1], this.cAccent[2], 1.0f, 0.5f, 1.0f);
        }
        if (this.core.getBonusCell() >= 0) {
            float x2 = this.core.worldX(this.core.getBonusCell());
            float z2 = this.core.worldZ(this.core.getBonusCell());
            float ttl = this.core.getBonusTtl();
            boolean blink = ttl > 2.0f || ((float) Math.sin((double) (this.time * 22.0f))) > 0.0f;
            if (blink) {
                if (this.core.getBonusType() == 1) {
                    float y2 = (((float) Math.sin(this.time * 4.4f)) * 0.13f) + 0.62f;
                    float s2 = (((float) Math.sin(this.time * 9.0f)) * 0.05f) + 0.54f;
                    this.cubes.box(x2, y2, z2, s2, s2, s2, this.time * 2.8f, 1.0f, 0.8f, 0.24f, 1.0f, 2.0f, 1.0f);
                    for (int k = 0; k < 6; k++) {
                        float a = (this.time * 2.2f) + (k * 1.0472f);
                        this.cubes.box(x2 + (((float) Math.cos(a)) * 0.62f), y2 + (((float) Math.sin(1.7f * a)) * 0.12f), z2 + (((float) Math.sin(a)) * 0.62f), 0.12f, 0.12f, 0.12f, a, 1.0f, 0.92f, 0.5f, 1.0f, 2.2f, 0.0f);
                    }
                } else {
                    float y3 = (((float) Math.sin(this.time * 2.4f)) * 0.09f) + 0.6f;
                    float rot = this.time * 1.1f;
                    this.cubes.box(x2, y3, z2, 0.62f, 0.1f, 0.1f, rot, 0.35f, 0.92f, 1.0f, 1.0f, 1.7f, 1.0f);
                    this.cubes.box(x2, y3, z2, 0.1f, 0.1f, 0.62f, rot, 0.35f, 0.92f, 1.0f, 1.0f, 1.7f, 1.0f);
                    this.cubes.box(x2, y3, z2, 0.1f, 0.62f, 0.1f, rot, 0.55f, 0.97f, 1.0f, 1.0f, 1.7f, 1.0f);
                }
            }
            float k2 = RangesKt.coerceIn(ttl / Math.max(0.001f, this.core.getBonusTtlMax()), 0.0f, 1.0f);
            this.cubes.box(x2, 0.05f, z2, k2 * 0.9f, 0.03f, k2 * 0.9f, this.time * 0.8f, 1.0f, 0.8f, 0.3f, 1.0f, 0.7f, 1.0f);
        }
    }

    private final void drawObstacles() {
        int obstacleCount = this.core.getObstacleCount();
        for (int i = 0; i < obstacleCount; i++) {
            int c = this.core.obstacle(i);
            float x = this.core.worldX(c);
            float z = this.core.worldZ(c);
            float h = (((((-1640531535) * c) >>> 28) & 3) * 0.14f) + 0.8f;
            this.cubes.box(x, h * 0.5f, z, 0.94f, h, 0.94f, 0.0f, (this.cFloor[0] * 3.2f) + 0.04f, (this.cFloor[1] * 3.2f) + 0.04f, (this.cFloor[2] * 3.2f) + 0.05f, 1.0f, 0.0f, 0.85f);
        }
    }

    private final void drawWalls() {
        boolean wrap = this.core.getWrapMode();
        float f = 0.5f;
        float zEdge = HALF_Z + 0.5f;
        float xEdge = HALF_X + 0.5f;
        int i = 21;
        if (!wrap) {
            float xEdge2 = xEdge;
            float wr = (this.cAccent[0] * 0.45f) + 0.03f;
            float wg = (this.cAccent[1] * 0.45f) + 0.03f;
            float wb = (this.cAccent[2] * 0.45f) + 0.04f;
            for (int i2 = -1; i2 < 14; i2++) {
                float x = i2 - 6;
                float h = (((float) Math.sin((i2 * 1.13f) + (this.time * 0.7f))) * 0.12f) + 0.62f;
                this.cubes.box(x, h * 0.5f, -zEdge, 1.0f, h, 1.0f, 0.0f, wr, wg, wb, 1.0f, 0.12f, 1.0f);
                this.cubes.box(x, h * 0.5f, zEdge, 1.0f, h, 1.0f, 0.0f, wr, wg, wb, 1.0f, 0.12f, 1.0f);
            }
            int i3 = 0;
            while (i3 < 21) {
                float z = i3 - 10;
                float h2 = (((float) Math.sin((i3 * 0.91f) + (this.time * 0.7f) + 2.0f)) * 0.12f) + 0.62f;
                float xEdge3 = xEdge2;
                this.cubes.box(-xEdge3, h2 * 0.5f, z, 1.0f, h2, 1.0f, 0.0f, wr, wg, wb, 1.0f, 0.12f, 1.0f);
                this.cubes.box(xEdge3, h2 * 0.5f, z, 1.0f, h2, 1.0f, 0.0f, wr, wg, wb, 1.0f, 0.12f, 1.0f);
                i3++;
                xEdge2 = xEdge3;
            }
            return;
        }
        int i4 = 0;
        while (i4 < 13) {
            float x2 = i4 - 6;
            float g = (((float) Math.sin((this.time * 3.0f) + (i4 * f))) * 0.45f) + 0.55f;
            this.cubes.box(x2, 0.06f, -zEdge, 0.9f, 0.07f, 0.5f, 0.0f, this.cAccent[0], this.cAccent[1], this.cAccent[2], 1.0f, g, 1.0f);
            this.cubes.box(x2, 0.06f, zEdge, 0.9f, 0.07f, 0.5f, 0.0f, this.cAccent[0], this.cAccent[1], this.cAccent[2], 1.0f, g, 1.0f);
            i4++;
            i = i;
            xEdge = xEdge;
            f = 0.5f;
        }
        int i5 = i;
        float xEdge4 = xEdge;
        int i6 = 0;
        while (i6 < i5) {
            float z2 = i6 - 10;
            float g2 = (((float) Math.sin((this.time * 3.0f) + (i6 * 0.5f))) * 0.45f) + 0.55f;
            float xEdge5 = xEdge4;
            this.cubes.box(-xEdge5, 0.06f, z2, 0.5f, 0.07f, 0.9f, 0.0f, this.cAccent[0], this.cAccent[1], this.cAccent[2], 1.0f, g2, 1.0f);
            this.cubes.box(xEdge5, 0.06f, z2, 0.5f, 0.07f, 0.9f, 0.0f, this.cAccent[0], this.cAccent[1], this.cAccent[2], 1.0f, g2, 1.0f);
            i6++;
            xEdge4 = xEdge5;
        }
    }

    private final void consumeEvents() {
        ArrayList<FxEvent> evs = this.core.getEvents();
        int size = evs.size();
        for (int i = 0; i < size; i++) {
            FxEvent fxEvent = evs.get(i);
            Intrinsics.checkNotNullExpressionValue(fxEvent, "get(...)");
            FxEvent e = fxEvent;
            switch (e.kind) {
                case 0:
                    this.particles.burst(e.x, e.y, e.z, 34, 4.6f, this.cAccent[0], this.cAccent[1], this.cAccent[2], 0.7f, 0.19f);
                    this.particles.ring(e.x, 0.15f, e.z, 14, 3.4f, this.cHead[0], this.cHead[1], this.cHead[2]);
                    startPulse(e.x, e.z);
                    this.shake = Math.max(this.shake, (Math.min(e.power, 8.0f) * 0.012f) + 0.1f);
                    setFlash(0.16f, this.cAccent[0], this.cAccent[1], this.cAccent[2]);
                    this.sfx.eat((int) e.power);
                    break;
                case 1:
                    this.particles.burst(e.x, e.y, e.z, 90, 6.4f, 1.0f, 0.82f, 0.3f, 1.1f, 0.24f);
                    this.particles.ring(e.x, 0.15f, e.z, 26, 5.2f, 1.0f, 0.9f, 0.45f);
                    startPulse(e.x, e.z);
                    this.shake = Math.max(this.shake, 0.26f);
                    setFlash(0.34f, 1.0f, 0.85f, 0.4f);
                    this.sfx.gold();
                    break;
                case 2:
                    this.particles.burst(e.x, e.y, e.z, 60, 4.0f, 0.4f, 0.9f, 1.0f, 1.3f, 0.22f);
                    startPulse(e.x, e.z);
                    setFlash(0.3f, 0.4f, 0.85f, 1.0f);
                    this.sfx.chill();
                    break;
                case 3:
                    explodeSnake();
                    this.shake = Math.max(this.shake, 0.85f);
                    setFlash(0.8f, 1.0f, 0.16f, 0.22f);
                    this.sfx.die();
                    break;
                case 5:
                    this.particles.burst(e.x, 0.2f, e.z, 26, 3.0f, 0.55f, 0.55f, 0.6f, 0.7f, 0.17f);
                    this.shake = Math.max(this.shake, 0.08f);
                    break;
                case 6:
                    this.particles.burst(e.x, e.y, e.z, 16, 2.0f, 0.7f, 0.7f, 0.75f, 0.45f, 0.13f);
                    this.sfx.expire();
                    break;
                case 7:
                    this.particles.ring(0.0f, 0.2f, 0.0f, 60, 9.0f, this.cAccent[0], this.cAccent[1], this.cAccent[2]);
                    setFlash(0.3f, 1.0f, 1.0f, 1.0f);
                    this.shake = Math.max(this.shake, 0.16f);
                    this.sfx.levelUp();
                    break;
            }
        }
        this.core.recycleEvents();
    }

    private final void explodeSnake() {
        int n = this.core.getLength();
        this.deathHide = 1.0f;
        for (int i = 0; i < n; i++) {
            float x = interpX(i);
            float z = interpZ(i);
            float f = i / Math.max(1, n - 1);
            float r = this.cHead[0] + ((this.cBody[0] - this.cHead[0]) * f);
            float g = this.cHead[1] + ((this.cBody[1] - this.cHead[1]) * f);
            float b = this.cHead[2] + ((this.cBody[2] - this.cHead[2]) * f);
            this.particles.burst(x, 0.5f, z, 14, 5.2f, r, g, b, 1.5f, 0.21f);
        }
        this.particles.burst(interpX(0), 0.6f, interpZ(0), 120, 9.0f, 1.0f, 0.35f, 0.3f, 1.7f, 0.3f);
    }

    private final void startPulse(float x, float z) {
        this.pulse = 0.0f;
        this.pulseX = x;
        this.pulseZ = z;
    }

    private final void setFlash(float v, float r, float g, float b) {
        this.flash = Math.max(this.flash, v);
        this.flashColor[0] = r;
        this.flashColor[1] = g;
        this.flashColor[2] = b;
    }

    private final void updateEffects(float dt) {
        this.shake *= (float) Math.exp((-dt) * 5.5f);
        if (this.shake < 0.001f) {
            this.shake = 0.0f;
        }
        this.flash *= (float) Math.exp((-dt) * 6.5f);
        if (this.flash < 0.002f) {
            this.flash = 0.0f;
        }
        if (this.pulse < 1.0f) {
            this.pulse = Math.min(1.0f, this.pulse + (1.15f * dt));
        }
        float f = 0.85f;
        switch (WhenMappings.$EnumSwitchMapping$0[this.core.getState().ordinal()]) {
            case 1:
                f = Math.min(0.85f, this.core.getDeathTimer() / 0.9f);
                break;
            case 2:
                break;
            case 3:
                f = 0.7f;
                break;
            default:
                f = 0.0f;
                break;
        }
        float wantDesat = f;
        this.desat += (wantDesat - this.desat) * (1.0f - ((float) Math.exp((-dt) * 6.0f)));
        if (this.core.getState() != State.DYING && this.core.getState() != State.GAMEOVER) {
            this.deathHide = 0.0f;
        }
        applyPalette(this.core.getLevel(), false);
        lerpColors(dt);
        this.dustAcc += dt;
        while (this.dustAcc > 0.06f) {
            this.dustAcc -= 0.06f;
            float x = ((this.rnd.nextFloat() * 2.0f) - 1.0f) * (HALF_X + 5.0f);
            float z = ((this.rnd.nextFloat() * 2.0f) - 1.0f) * (HALF_Z + 6.0f);
            this.particles.spawn(x, (this.rnd.nextFloat() * 2.4f) + 0.2f, z, 0.0f, (this.rnd.nextFloat() * 0.3f) + 0.25f, 0.0f, this.cGrid[0], this.cGrid[1], this.cGrid[2], (this.rnd.nextFloat() * 1.5f) + 2.2f, 0.07f, 0.1f);
        }
        this.sfx.setIntensity(Math.min(1.0f, this.core.getApples() / 34.0f));
    }

    private final void applyPalette(int level, boolean instant) {
        if (level != this.lastLevel || instant) {
            this.lastLevel = level;
            Palette p = Palette.INSTANCE.getALL()[RangesKt.coerceIn(level, 0, Palette.INSTANCE.getALL().length - 1)];
            fill(this.tHead, p.getSnakeHead());
            fill(this.tBody, p.getSnakeBody());
            fill(this.tGrid, p.getGrid());
            fill(this.tFloor, p.getFloor());
            fill(this.tFog, p.getFog());
            fill(this.tAccent, p.getAccent());
            if (instant) {
                System.arraycopy(this.tHead, 0, this.cHead, 0, 3);
                System.arraycopy(this.tBody, 0, this.cBody, 0, 3);
                System.arraycopy(this.tGrid, 0, this.cGrid, 0, 3);
                System.arraycopy(this.tFloor, 0, this.cFloor, 0, 3);
                System.arraycopy(this.tFog, 0, this.cFog, 0, 3);
                System.arraycopy(this.tAccent, 0, this.cAccent, 0, 3);
            }
        }
    }

    private final void fill(float[] dst, int c) {
        dst[0] = Palette.INSTANCE.r(c);
        dst[1] = Palette.INSTANCE.g(c);
        dst[2] = Palette.INSTANCE.b(c);
    }

    private final void lerpColors(float dt) {
        float k = 1.0f - ((float) Math.exp((-dt) * 2.2f));
        for (int i = 0; i < 3; i++) {
            float[] fArr = this.cHead;
            fArr[i] = fArr[i] + ((this.tHead[i] - this.cHead[i]) * k);
            float[] fArr2 = this.cBody;
            fArr2[i] = fArr2[i] + ((this.tBody[i] - this.cBody[i]) * k);
            float[] fArr3 = this.cGrid;
            fArr3[i] = fArr3[i] + ((this.tGrid[i] - this.cGrid[i]) * k);
            float[] fArr4 = this.cFloor;
            fArr4[i] = fArr4[i] + ((this.tFloor[i] - this.cFloor[i]) * k);
            float[] fArr5 = this.cFog;
            fArr5[i] = fArr5[i] + ((this.tFog[i] - this.cFog[i]) * k);
            float[] fArr6 = this.cAccent;
            fArr6[i] = fArr6[i] + ((this.tAccent[i] - this.cAccent[i]) * k);
        }
    }

    private final void publishHud() {
        this.hudScore = this.core.getScore();
        this.hudHigh = this.core.getHighScore();
        this.hudCombo = this.core.getCombo();
        this.hudLength = this.core.getLength();
        this.hudApples = this.core.getApples();
        this.hudState = this.core.getState();
        this.hudNewRecord = this.core.getNewRecord();
        this.hudTime = this.core.getPlayTime();
    }

    public final void onGlDestroy() {
        this.cubes.release();
        this.ground.release();
        this.particles.release();
        this.post.release();
    }

    public final void resetFx() {
        this.particles.clear();
        this.shake = 0.0f;
        this.flash = 0.0f;
        this.deathHide = 0.0f;
        this.pulse = 1.0f;
    }
}
