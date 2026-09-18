package com.retro.snake3d;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.WindowCompat;
import com.retro.snake3d.audio.Sfx;
import com.retro.snake3d.game.GameCore;
import com.retro.snake3d.game.State;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;

/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u0000\u0089\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0014\u0018\u0000 E2\u00020\u0001:\u0001EB\u0005¢\u0006\u0002\u0010\u0002J\b\u00101\u001a\u000202H\u0002J\b\u00103\u001a\u000202H\u0016J\u0012\u00104\u001a\u0002022\b\u00105\u001a\u0004\u0018\u000106H\u0014J\b\u00107\u001a\u000202H\u0014J\b\u00108\u001a\u000202H\u0014J\b\u00109\u001a\u000202H\u0002J\u0010\u0010:\u001a\u0002022\u0006\u0010;\u001a\u00020\u001dH\u0002J\u0010\u0010<\u001a\u0002022\u0006\u0010=\u001a\u00020>H\u0016J\b\u0010?\u001a\u000202H\u0002J\b\u0010@\u001a\u000202H\u0002J\u0010\u0010A\u001a\u0002022\u0006\u0010B\u001a\u00020\fH\u0002J\b\u0010C\u001a\u000202H\u0002J\b\u0010D\u001a\u000202H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010/\u001a\u0004\u0018\u000100X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006F"}, d2 = {"Lcom/retro/snake3d/MainActivity;", "Landroid/app/Activity;", "()V", "btnMode", "Landroid/widget/Button;", "btnPause", "btnSound", "core", "Lcom/retro/snake3d/game/GameCore;", "gameOverOverlay", "Landroid/view/View;", "gameOverShownAt", "", "glView", "Lcom/retro/snake3d/GameView;", "handler", "Landroid/os/Handler;", "hud", "Landroid/widget/LinearLayout;", "hudTick", "com/retro/snake3d/MainActivity$hudTick$1", "Lcom/retro/snake3d/MainActivity$hudTick$1;", "lastApples", "", "lastCombo", "lastHigh", "lastLength", "lastScore", "lastState", "Lcom/retro/snake3d/game/State;", "pauseOverlay", "prefs", "Landroid/content/SharedPreferences;", "renderer", "Lcom/retro/snake3d/GameRenderer;", "sfx", "Lcom/retro/snake3d/audio/Sfx;", "titleOverlay", "txtCombo", "Landroid/widget/TextView;", "txtFinalScore", "txtFinalStats", "txtHigh", "txtLength", "txtNewRecord", "txtScore", "txtStart", "vibrator", "Landroid/os/Vibrator;", "goFullscreen", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onPause", "onResume", "onScreenTap", "onStateChanged", "state", "onWindowFocusChanged", "hasFocus", "", "startGame", "syncHud", "tick", "ms", "updateModeButton", "updateSoundButton", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes3.dex */
public final class MainActivity extends Activity {
    private static final long HUD_INTERVAL = 50;
    private static final String KEY_HIGH = "high";
    private static final String KEY_SOUND = "sound";
    private static final String KEY_WRAP = "wrap";
    private static final String PREFS = "snake3d";
    private Button btnMode;
    private Button btnPause;
    private Button btnSound;
    private GameCore core;
    private View gameOverOverlay;
    private long gameOverShownAt;
    private GameView glView;
    private LinearLayout hud;
    private int lastApples;
    private State lastState;
    private View pauseOverlay;
    private SharedPreferences prefs;
    private GameRenderer renderer;
    private View titleOverlay;
    private TextView txtCombo;
    private TextView txtFinalScore;
    private TextView txtFinalStats;
    private TextView txtHigh;
    private TextView txtLength;
    private TextView txtNewRecord;
    private TextView txtScore;
    private TextView txtStart;
    private Vibrator vibrator;
    private final Sfx sfx = new Sfx();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int lastScore = -1;
    private int lastHigh = -1;
    private int lastCombo = -1;
    private int lastLength = -1;
    private final Runnable hudTick = new Runnable() { // from class: com.retro.snake3d.MainActivity$hudTick$1
        @Override // java.lang.Runnable
        public void run() {
            Handler handler;
            MainActivity.this.syncHud();
            handler = MainActivity.this.handler;
            handler.postDelayed(this, 50L);
        }
    };

    /* compiled from: MainActivity.kt */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[State.values().length];
            try {
                iArr[State.TITLE.ordinal()] = 1;
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
            try {
                iArr[State.PLAYING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private final void tick(long ms) {
    }

    private final void updateSettingsButtons() {
        Sfx sfx = this.sfx;
        Button button = (Button) findViewById(R.id.btnStyle);
        if (button != null) {
            int style = sfx.getStyle();
            String str = "KLANG: RETRO";
            if (style != 0) {
                int i = style - 1;
                str = "KLANG: SOFT";
                if (i != 0) {
                    str = "KLANG: ARCADE";
                    if (i - 1 != 0) {
                        str = "KLANG: LASER";
                    }
                }
            }
            button.setText(str);
        }
        Button button2 = (Button) findViewById(R.id.btnHaptic);
        if (button2 != null) {
            int haptic = sfx.getHaptic();
            String str2 = "VIBRATION: AUS";
            if (haptic != 0) {
                str2 = "VIBRATION: LEICHT";
                if (haptic - 1 != 0) {
                    str2 = "VIBRATION: STARK";
                }
            }
            button2.setText(str2);
        }
    }

    public final void onSettingsClick(int i) {
        int haptic;
        String str;
        Sfx sfx = this.sfx;
        SharedPreferences sharedPreferences = this.prefs;
        if (i == 0) {
            haptic = (sfx.getStyle() + 1) % 4;
            sfx.setStyle(haptic);
            str = "style";
        } else {
            haptic = (sfx.getHaptic() + 1) % 3;
            sfx.setHaptic(haptic);
            str = "haptic";
        }
        if (sharedPreferences != null) {
            sharedPreferences.edit().putInt(str, haptic).apply();
        }
        if (i == 0) {
            sfx.gold();
        } else {
            sfx.buzz(60, 200);
            sfx.turn();
        }
        updateSettingsButtons();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "getSharedPreferences(...)");
        this.prefs = sharedPreferences;
        Object systemService = getSystemService("vibrator");
        TextView textView = null;
        this.vibrator = systemService instanceof Vibrator ? (Vibrator) systemService : null;
        this.core = new GameCore();
        GameCore gameCore = this.core;
        if (gameCore == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore = null;
        }
        SharedPreferences sharedPreferences2 = this.prefs;
        if (sharedPreferences2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("prefs");
            sharedPreferences2 = null;
        }
        gameCore.setHighScore(sharedPreferences2.getInt(KEY_HIGH, 0));
        GameCore gameCore2 = this.core;
        if (gameCore2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore2 = null;
        }
        SharedPreferences sharedPreferences3 = this.prefs;
        if (sharedPreferences3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("prefs");
            sharedPreferences3 = null;
        }
        gameCore2.setWrapMode(sharedPreferences3.getBoolean(KEY_WRAP, false));
        Sfx sfx = this.sfx;
        SharedPreferences sharedPreferences4 = this.prefs;
        if (sharedPreferences4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("prefs");
            sharedPreferences4 = null;
        }
        sfx.setEnabled(sharedPreferences4.getBoolean(KEY_SOUND, true));
        this.sfx.setMusicEnabled(this.sfx.getEnabled());
        GameCore gameCore3 = this.core;
        if (gameCore3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore3 = null;
        }
        this.renderer = new GameRenderer(gameCore3, this.sfx);
        View findViewById = findViewById(R.id.glView);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(...)");
        this.glView = (GameView) findViewById;
        GameView gameView = this.glView;
        if (gameView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("glView");
            gameView = null;
        }
        GameRenderer gameRenderer = this.renderer;
        if (gameRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer = null;
        }
        gameView.attach(gameRenderer);
        GameView gameView2 = this.glView;
        if (gameView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("glView");
            gameView2 = null;
        }
        gameView2.setOnTap(new Function0<Unit>() { // from class: com.retro.snake3d.MainActivity$onCreate$1

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.this.onScreenTap();
            }
        });
        View findViewById2 = findViewById(R.id.hud);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(...)");
        this.hud = (LinearLayout) findViewById2;
        View findViewById3 = findViewById(R.id.txtScore);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(...)");
        this.txtScore = (TextView) findViewById3;
        View findViewById4 = findViewById(R.id.txtHigh);
        Intrinsics.checkNotNullExpressionValue(findViewById4, "findViewById(...)");
        this.txtHigh = (TextView) findViewById4;
        View findViewById5 = findViewById(R.id.txtCombo);
        Intrinsics.checkNotNullExpressionValue(findViewById5, "findViewById(...)");
        this.txtCombo = (TextView) findViewById5;
        View findViewById6 = findViewById(R.id.txtLength);
        Intrinsics.checkNotNullExpressionValue(findViewById6, "findViewById(...)");
        this.txtLength = (TextView) findViewById6;
        View findViewById7 = findViewById(R.id.titleOverlay);
        Intrinsics.checkNotNullExpressionValue(findViewById7, "findViewById(...)");
        this.titleOverlay = findViewById7;
        View findViewById8 = findViewById(R.id.gameOverOverlay);
        Intrinsics.checkNotNullExpressionValue(findViewById8, "findViewById(...)");
        this.gameOverOverlay = findViewById8;
        View findViewById9 = findViewById(R.id.pauseOverlay);
        Intrinsics.checkNotNullExpressionValue(findViewById9, "findViewById(...)");
        this.pauseOverlay = findViewById9;
        View findViewById10 = findViewById(R.id.txtFinalScore);
        Intrinsics.checkNotNullExpressionValue(findViewById10, "findViewById(...)");
        this.txtFinalScore = (TextView) findViewById10;
        View findViewById11 = findViewById(R.id.txtFinalStats);
        Intrinsics.checkNotNullExpressionValue(findViewById11, "findViewById(...)");
        this.txtFinalStats = (TextView) findViewById11;
        View findViewById12 = findViewById(R.id.txtNewRecord);
        Intrinsics.checkNotNullExpressionValue(findViewById12, "findViewById(...)");
        this.txtNewRecord = (TextView) findViewById12;
        View findViewById13 = findViewById(R.id.txtStart);
        Intrinsics.checkNotNullExpressionValue(findViewById13, "findViewById(...)");
        this.txtStart = (TextView) findViewById13;
        View findViewById14 = findViewById(R.id.btnMode);
        Intrinsics.checkNotNullExpressionValue(findViewById14, "findViewById(...)");
        this.btnMode = (Button) findViewById14;
        View findViewById15 = findViewById(R.id.btnSound);
        Intrinsics.checkNotNullExpressionValue(findViewById15, "findViewById(...)");
        this.btnSound = (Button) findViewById15;
        View findViewById16 = findViewById(R.id.btnPause);
        Intrinsics.checkNotNullExpressionValue(findViewById16, "findViewById(...)");
        this.btnPause = (Button) findViewById16;
        TextView textView2 = this.txtHigh;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtHigh");
            textView2 = null;
        }
        GameCore gameCore4 = this.core;
        if (gameCore4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore4 = null;
        }
        textView2.setText(String.valueOf(gameCore4.getHighScore()));
        updateModeButton();
        updateSoundButton();
        Button button = this.btnMode;
        if (button == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnMode");
            button = null;
        }
        button.setOnClickListener(new View.OnClickListener() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$0(MainActivity.this, view);
            }
        });
        Button button2 = this.btnSound;
        if (button2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnSound");
            button2 = null;
        }
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$1(MainActivity.this, view);
            }
        });
        Button button3 = this.btnPause;
        if (button3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnPause");
            button3 = null;
        }
        button3.setOnClickListener(new View.OnClickListener() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$3(MainActivity.this, view);
            }
        });
        ((Button) findViewById(R.id.btnResume)).setOnClickListener(new View.OnClickListener() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$5(MainActivity.this, view);
            }
        });
        ((Button) findViewById(R.id.btnAgain)).setOnClickListener(new View.OnClickListener() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$6(MainActivity.this, view);
            }
        });
        ((Button) findViewById(R.id.btnMenu)).setOnClickListener(new View.OnClickListener() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$8(MainActivity.this, view);
            }
        });
        TextView textView3 = this.txtStart;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtStart");
        } else {
            textView = textView3;
        }
        textView.animate().alpha(0.15f).setDuration(650L).withEndAction(new Runnable() { // from class: com.retro.snake3d.MainActivity$onCreate$8
            @Override // java.lang.Runnable
            public void run() {
                TextView textView4;
                TextView textView5;
                textView4 = MainActivity.this.txtStart;
                TextView textView6 = null;
                if (textView4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("txtStart");
                    textView4 = null;
                }
                float to = textView4.getAlpha() > 0.5f ? 0.15f : 1.0f;
                textView5 = MainActivity.this.txtStart;
                if (textView5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("txtStart");
                } else {
                    textView6 = textView5;
                }
                textView6.animate().alpha(to).setDuration(650L).withEndAction(this).start();
            }
        }).start();
        Sfx sfx2 = this.sfx;
        sfx2.setVibrator(this.vibrator);
        SharedPreferences sharedPreferences5 = this.prefs;
        sfx2.setStyle(sharedPreferences5.getInt("style", 0));
        sfx2.setHaptic(sharedPreferences5.getInt("haptic", 2));
        findViewById(R.id.btnStyle).setOnClickListener(new SettingsClick(this, 0));
        findViewById(R.id.btnHaptic).setOnClickListener(new SettingsClick(this, 1));
        updateSettingsButtons();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$0(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        GameCore gameCore = this$0.core;
        GameCore gameCore2 = null;
        if (gameCore == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore = null;
        }
        GameCore gameCore3 = this$0.core;
        if (gameCore3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore3 = null;
        }
        gameCore.setWrapMode(!gameCore3.getWrapMode());
        SharedPreferences sharedPreferences = this$0.prefs;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("prefs");
            sharedPreferences = null;
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        GameCore gameCore4 = this$0.core;
        if (gameCore4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
        } else {
            gameCore2 = gameCore4;
        }
        edit.putBoolean(KEY_WRAP, gameCore2.getWrapMode()).apply();
        this$0.updateModeButton();
        this$0.sfx.uiClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$1(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        boolean on = !this$0.sfx.getEnabled();
        this$0.sfx.setEnabled(on);
        this$0.sfx.setMusicEnabled(on);
        SharedPreferences sharedPreferences = this$0.prefs;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("prefs");
            sharedPreferences = null;
        }
        sharedPreferences.edit().putBoolean(KEY_SOUND, on).apply();
        this$0.updateSoundButton();
        if (on) {
            this$0.sfx.uiClick();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$3(final MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.sfx.uiClick();
        GameView gameView = this$0.glView;
        if (gameView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("glView");
            gameView = null;
        }
        gameView.queueEvent(new Runnable() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.onCreate$lambda$3$lambda$2(this$0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$3$lambda$2(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        GameCore gameCore = this$0.core;
        if (gameCore == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore = null;
        }
        gameCore.pause();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$5(final MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.sfx.uiClick();
        GameView gameView = this$0.glView;
        if (gameView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("glView");
            gameView = null;
        }
        gameView.queueEvent(new Runnable() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.onCreate$lambda$5$lambda$4(this$0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$5$lambda$4(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        GameCore gameCore = this$0.core;
        if (gameCore == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore = null;
        }
        gameCore.resume();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$6(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.sfx.gameStart();
        this$0.startGame();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$8(final MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.sfx.uiClick();
        GameRenderer gameRenderer = this$0.renderer;
        GameView gameView = null;
        if (gameRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer = null;
        }
        gameRenderer.resetFx();
        GameView gameView2 = this$0.glView;
        if (gameView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("glView");
        } else {
            gameView = gameView2;
        }
        gameView.queueEvent(new Runnable() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.onCreate$lambda$8$lambda$7(this$0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$8$lambda$7(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        GameCore gameCore = this$0.core;
        if (gameCore == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore = null;
        }
        gameCore.backToTitle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onScreenTap() {
        GameRenderer gameRenderer = this.renderer;
        GameView gameView = null;
        if (gameRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer = null;
        }
        switch (WhenMappings.$EnumSwitchMapping$0[gameRenderer.getHudState().ordinal()]) {
            case 1:
                this.sfx.gameStart();
                startGame();
                break;
            case 2:
                if (System.currentTimeMillis() - this.gameOverShownAt > 700) {
                    this.sfx.gameStart();
                    startGame();
                    break;
                }
                break;
            case 3:
                GameView gameView2 = this.glView;
                if (gameView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("glView");
                } else {
                    gameView = gameView2;
                }
                gameView.queueEvent(new Runnable() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.onScreenTap$lambda$9(MainActivity.this);
                    }
                });
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onScreenTap$lambda$9(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        GameCore gameCore = this$0.core;
        if (gameCore == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore = null;
        }
        gameCore.resume();
    }

    private final void startGame() {
        GameRenderer gameRenderer = this.renderer;
        GameView gameView = null;
        if (gameRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer = null;
        }
        gameRenderer.resetFx();
        GameView gameView2 = this.glView;
        if (gameView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("glView");
        } else {
            gameView = gameView2;
        }
        gameView.queueEvent(new Runnable() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.startGame$lambda$10(MainActivity.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void startGame$lambda$10(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        GameCore gameCore = this$0.core;
        if (gameCore == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore = null;
        }
        gameCore.startGame();
    }

    private final void updateModeButton() {
        Button button = this.btnMode;
        GameCore gameCore = null;
        if (button == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnMode");
            button = null;
        }
        GameCore gameCore2 = this.core;
        if (gameCore2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
        } else {
            gameCore = gameCore2;
        }
        button.setText(gameCore.getWrapMode() ? R.string.mode_wrap : R.string.mode_classic);
    }

    private final void updateSoundButton() {
        Button button = this.btnSound;
        if (button == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnSound");
            button = null;
        }
        button.setText(this.sfx.getEnabled() ? R.string.sound_on : R.string.sound_off);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void syncHud() {
        GameRenderer gameRenderer = this.renderer;
        TextView textView = null;
        if (gameRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer = null;
        }
        int score = gameRenderer.getHudScore();
        GameRenderer gameRenderer2 = this.renderer;
        if (gameRenderer2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer2 = null;
        }
        int high = gameRenderer2.getHudHigh();
        GameRenderer gameRenderer3 = this.renderer;
        if (gameRenderer3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer3 = null;
        }
        int combo = gameRenderer3.getHudCombo();
        GameRenderer gameRenderer4 = this.renderer;
        if (gameRenderer4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer4 = null;
        }
        int length = gameRenderer4.getHudLength();
        GameRenderer gameRenderer5 = this.renderer;
        if (gameRenderer5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer5 = null;
        }
        int apples = gameRenderer5.getHudApples();
        GameRenderer gameRenderer6 = this.renderer;
        if (gameRenderer6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer6 = null;
        }
        State state = gameRenderer6.getHudState();
        if (score != this.lastScore) {
            this.lastScore = score;
            TextView textView2 = this.txtScore;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtScore");
                textView2 = null;
            }
            textView2.setText(String.valueOf(score));
            TextView textView3 = this.txtScore;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtScore");
                textView3 = null;
            }
            textView3.setScaleX(1.18f);
            TextView textView4 = this.txtScore;
            if (textView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtScore");
                textView4 = null;
            }
            textView4.setScaleY(1.18f);
            TextView textView5 = this.txtScore;
            if (textView5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtScore");
                textView5 = null;
            }
            textView5.animate().scaleX(1.0f).scaleY(1.0f).setDuration(140L).start();
        }
        if (high != this.lastHigh) {
            this.lastHigh = high;
            TextView textView6 = this.txtHigh;
            if (textView6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtHigh");
                textView6 = null;
            }
            textView6.setText(String.valueOf(high));
            SharedPreferences sharedPreferences = this.prefs;
            if (sharedPreferences == null) {
                Intrinsics.throwUninitializedPropertyAccessException("prefs");
                sharedPreferences = null;
            }
            sharedPreferences.edit().putInt(KEY_HIGH, high).apply();
        }
        if (length != this.lastLength) {
            this.lastLength = length;
            TextView textView7 = this.txtLength;
            if (textView7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtLength");
                textView7 = null;
            }
            textView7.setText(getString(R.string.length) + " " + length);
        }
        if (combo != this.lastCombo) {
            this.lastCombo = combo;
            if (combo > 1) {
                TextView textView8 = this.txtCombo;
                if (textView8 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("txtCombo");
                    textView8 = null;
                }
                textView8.setText("x" + combo);
                TextView textView9 = this.txtCombo;
                if (textView9 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("txtCombo");
                    textView9 = null;
                }
                textView9.setAlpha(1.0f);
                TextView textView10 = this.txtCombo;
                if (textView10 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("txtCombo");
                    textView10 = null;
                }
                textView10.setScaleX(1.5f);
                TextView textView11 = this.txtCombo;
                if (textView11 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("txtCombo");
                    textView11 = null;
                }
                textView11.setScaleY(1.5f);
                TextView textView12 = this.txtCombo;
                if (textView12 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("txtCombo");
                } else {
                    textView = textView12;
                }
                textView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(180L).start();
            } else {
                TextView textView13 = this.txtCombo;
                if (textView13 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("txtCombo");
                } else {
                    textView = textView13;
                }
                textView.animate().alpha(0.0f).setDuration(220L).start();
            }
        }
        if (apples > this.lastApples) {
            this.lastApples = apples;
            tick(10L);
        } else if (apples < this.lastApples) {
            this.lastApples = apples;
        }
        if (state != this.lastState) {
            onStateChanged(state);
            this.lastState = state;
        }
    }

    private final void onStateChanged(State state) {
        View view = this.titleOverlay;
        TextView textView = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("titleOverlay");
            view = null;
        }
        view.setVisibility(state == State.TITLE ? 0 : 8);
        LinearLayout linearLayout = this.hud;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("hud");
            linearLayout = null;
        }
        linearLayout.setVisibility(state == State.TITLE ? 4 : 0);
        View view2 = this.pauseOverlay;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pauseOverlay");
            view2 = null;
        }
        view2.setVisibility(state == State.PAUSED ? 0 : 8);
        Button button = this.btnPause;
        if (button == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnPause");
            button = null;
        }
        button.setVisibility(state == State.PLAYING ? 0 : 8);
        if (state == State.GAMEOVER) {
            this.gameOverShownAt = System.currentTimeMillis();
            TextView textView2 = this.txtFinalScore;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtFinalScore");
                textView2 = null;
            }
            GameRenderer gameRenderer = this.renderer;
            if (gameRenderer == null) {
                Intrinsics.throwUninitializedPropertyAccessException("renderer");
                gameRenderer = null;
            }
            textView2.setText(String.valueOf(gameRenderer.getHudScore()));
            GameRenderer gameRenderer2 = this.renderer;
            if (gameRenderer2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("renderer");
                gameRenderer2 = null;
            }
            int secs = (int) gameRenderer2.getHudTime();
            TextView textView3 = this.txtFinalStats;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtFinalStats");
                textView3 = null;
            }
            GameRenderer gameRenderer3 = this.renderer;
            if (gameRenderer3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("renderer");
                gameRenderer3 = null;
            }
            int hudApples = gameRenderer3.getHudApples();
            GameRenderer gameRenderer4 = this.renderer;
            if (gameRenderer4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("renderer");
                gameRenderer4 = null;
            }
            int hudLength = gameRenderer4.getHudLength();
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String format = String.format("%d:%02d", Arrays.copyOf(new Object[]{Integer.valueOf(secs / 60), Integer.valueOf(secs % 60)}, 2));
            Intrinsics.checkNotNullExpressionValue(format, "format(...)");
            textView3.setText(hudApples + " ÄPFEL · LÄNGE " + hudLength + " · " + format);
            TextView textView4 = this.txtNewRecord;
            if (textView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtNewRecord");
                textView4 = null;
            }
            GameRenderer gameRenderer5 = this.renderer;
            if (gameRenderer5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("renderer");
                gameRenderer5 = null;
            }
            textView4.setVisibility(gameRenderer5.getHudNewRecord() ? 0 : 4);
            View view3 = this.gameOverOverlay;
            if (view3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("gameOverOverlay");
                view3 = null;
            }
            view3.setVisibility(0);
            View view4 = this.gameOverOverlay;
            if (view4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("gameOverOverlay");
                view4 = null;
            }
            view4.setAlpha(0.0f);
            View view5 = this.gameOverOverlay;
            if (view5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("gameOverOverlay");
                view5 = null;
            }
            view5.animate().alpha(1.0f).setDuration(260L).start();
        } else {
            View view6 = this.gameOverOverlay;
            if (view6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("gameOverOverlay");
                view6 = null;
            }
            view6.setVisibility(8);
        }
        if (state == State.DYING) {
            tick(70L);
        }
        if (state == State.PLAYING) {
            GameRenderer gameRenderer6 = this.renderer;
            if (gameRenderer6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("renderer");
                gameRenderer6 = null;
            }
            this.lastApples = gameRenderer6.getHudApples();
            TextView textView5 = this.txtCombo;
            if (textView5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtCombo");
            } else {
                textView = textView5;
            }
            textView.setAlpha(0.0f);
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        goFullscreen();
        GameView gameView = this.glView;
        if (gameView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("glView");
            gameView = null;
        }
        gameView.onResume();
        this.sfx.start();
        this.handler.post(this.hudTick);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.handler.removeCallbacks(this.hudTick);
        GameView gameView = this.glView;
        GameRenderer gameRenderer = null;
        if (gameView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("glView");
            gameView = null;
        }
        gameView.queueEvent(new Runnable() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.onPause$lambda$11(MainActivity.this);
            }
        });
        GameView gameView2 = this.glView;
        if (gameView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("glView");
            gameView2 = null;
        }
        gameView2.onPause();
        this.sfx.stop();
        SharedPreferences sharedPreferences = this.prefs;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("prefs");
            sharedPreferences = null;
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        GameRenderer gameRenderer2 = this.renderer;
        if (gameRenderer2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
        } else {
            gameRenderer = gameRenderer2;
        }
        edit.putInt(KEY_HIGH, gameRenderer.getHudHigh()).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onPause$lambda$11(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        GameCore gameCore = this$0.core;
        if (gameCore == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore = null;
        }
        gameCore.pause();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            goFullscreen();
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        GameRenderer gameRenderer = this.renderer;
        GameView gameView = null;
        if (gameRenderer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            gameRenderer = null;
        }
        switch (WhenMappings.$EnumSwitchMapping$0[gameRenderer.getHudState().ordinal()]) {
            case 2:
            case 3:
                GameRenderer gameRenderer2 = this.renderer;
                if (gameRenderer2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("renderer");
                    gameRenderer2 = null;
                }
                gameRenderer2.resetFx();
                GameView gameView2 = this.glView;
                if (gameView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("glView");
                } else {
                    gameView = gameView2;
                }
                gameView.queueEvent(new Runnable() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.onBackPressed$lambda$13(MainActivity.this);
                    }
                });
                break;
            case 4:
                GameView gameView3 = this.glView;
                if (gameView3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("glView");
                } else {
                    gameView = gameView3;
                }
                gameView.queueEvent(new Runnable() { // from class: com.retro.snake3d.MainActivity$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.onBackPressed$lambda$12(MainActivity.this);
                    }
                });
                break;
            default:
                super.onBackPressed();
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onBackPressed$lambda$12(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        GameCore gameCore = this$0.core;
        if (gameCore == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore = null;
        }
        gameCore.pause();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onBackPressed$lambda$13(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        GameCore gameCore = this$0.core;
        if (gameCore == null) {
            Intrinsics.throwUninitializedPropertyAccessException("core");
            gameCore = null;
        }
        gameCore.backToTitle();
    }

    private final void goFullscreen() {
        if (Build.VERSION.SDK_INT >= 30) {
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
            WindowInsetsController it = getWindow().getInsetsController();
            if (it != null) {
                it.hide(WindowInsets.Type.systemBars());
                it.setSystemBarsBehavior(2);
                return;
            }
            return;
        }
        getWindow().getDecorView().setSystemUiVisibility(5894);
    }
}
