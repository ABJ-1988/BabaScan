package com.retro.snake3d.audio;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.core.internal.view.SupportMenu;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.ShortCompanionObject;

/* compiled from: Sfx.kt */
@Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0014\n\u0002\u0010\u0014\n\u0002\b\n\u0018\u0000 C2\u00020\u0001:\u0003CDEB\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010&\u001a\u00020'J\u0006\u0010(\u001a\u00020'J\u000e\u0010)\u001a\u00020'2\u0006\u0010*\u001a\u00020\u0017J\u0006\u0010+\u001a\u00020'J\u0006\u0010,\u001a\u00020'J\u0006\u0010-\u001a\u00020'J\u0006\u0010.\u001a\u00020'J\u0010\u0010/\u001a\u00020'2\u0006\u00100\u001a\u00020!H\u0002JB\u00101\u001a\u00020'2\u0006\u00102\u001a\u00020\u00172\u0006\u00103\u001a\u00020\u000e2\u0006\u00104\u001a\u00020\u000e2\u0006\u00105\u001a\u00020\u000e2\u0006\u00106\u001a\u00020\u000e2\u0006\u00107\u001a\u00020\u000e2\b\b\u0002\u00108\u001a\u00020\u000eH\u0002J\u0018\u00109\u001a\u00020'2\u0006\u0010:\u001a\u00020$2\u0006\u0010;\u001a\u00020<H\u0002J\u0010\u0010=\u001a\u00020'2\u0006\u0010>\u001a\u00020\u0017H\u0002J\u0006\u0010?\u001a\u00020'J\u0006\u0010@\u001a\u00020'J\u0006\u0010A\u001a\u00020'J\u0006\u0010B\u001a\u00020'R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\t\"\u0004\b\u0015\u0010\u000bR\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#X\u0082\u0004¢\u0006\u0004\n\u0002\u0010%¨\u0006F"}, d2 = {"Lcom/retro/snake3d/audio/Sfx;", "", "()V", "arpPattern", "", "bassPattern", "enabled", "", "getEnabled", "()Z", "setEnabled", "(Z)V", "hatPattern", "intensity", "", "getIntensity", "()F", "setIntensity", "(F)V", "musicEnabled", "getMusicEnabled", "setMusicEnabled", "noiseState", "", "pending", "Ljava/util/concurrent/ConcurrentLinkedQueue;", "Lcom/retro/snake3d/audio/Sfx$Trigger;", "running", "seqAcc", "seqStep", "thread", "Ljava/lang/Thread;", "track", "Landroid/media/AudioTrack;", "voices", "", "Lcom/retro/snake3d/audio/Sfx$Voice;", "[Lcom/retro/snake3d/audio/Sfx$Voice;", "chill", "", "die", "eat", "combo", "expire", "gameStart", "gold", "levelUp", "loop", "t", "push", "wave", "f0", "f1", "dur", "amp", "decay", "duty", "renderVoice", "v", "mix", "", "sequencer", "frames", "start", "stop", "turn", "uiClick", "Companion", "Trigger", "Voice", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes3.dex */
public final class Sfx {
    private static final int BLOCK = 512;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int MAX_VOICES = 20;
    public static final int NOISE = 2;
    public static final int SQUARE = 0;
    private static final int SR = 44100;
    public static final int TRI = 1;
    private final int[] arpPattern;
    private final int[] bassPattern;
    private volatile boolean enabled;
    private volatile int haptic;
    private final int[] hatPattern;
    private volatile float intensity;
    private volatile boolean musicEnabled;
    private int noiseState;
    private final ConcurrentLinkedQueue<Trigger> pending;
    private volatile boolean running;
    private float seqAcc;
    private int seqStep;
    private volatile int style;
    private Thread thread;
    private AudioTrack track;
    private volatile Vibrator vibrator;
    private final Voice[] voices;

    /* compiled from: Sfx.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\n\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\r\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lcom/retro/snake3d/audio/Sfx$Voice;", "", "()V", "active", "", "amp", "", "attack", "decay", "dur", "duty", "f0", "f1", "phase", "t", "wave", "", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Voice {
        public boolean active;
        public float phase;
        public float t;
        public int wave;
        public float f0 = 440.0f;
        public float f1 = 440.0f;
        public float dur = 0.1f;
        public float amp = 0.25f;
        public float decay = 3.0f;
        public float duty = 0.5f;
        public float attack = 0.004f;
    }

    private final void push(int wave, float f0, float f1, float dur, float amp, float decay, float duty) {
        int i = wave;
        float f = f0;
        float f2 = f1;
        float f3 = dur;
        float f4 = amp;
        float f5 = decay;
        float f6 = duty;
        int i2 = this.style;
        if (i2 == 1) {
            if (i != 2) {
                i = 1;
            }
            f *= 0.5f;
            f2 *= 0.5f;
            f5 *= 0.7f;
            f4 *= 1.2f;
        } else if (i2 != 2) {
            if (i2 == 3 && i != 2) {
                i = 0;
                f6 = 0.125f;
                f2 = f * 0.4f;
                f *= 2.5f;
                f3 *= 1.3f;
            }
        } else if (i != 2) {
            i = 0;
            f6 = 0.25f;
            f *= 1.5f;
            f2 *= 2.0f;
            f3 *= 1.2f;
        }
        pushRaw(i, f, f2, f3, f4, f5, f6);
    }

    public final void buzz(int i, int i2) {
        Vibrator vibrator;
        int i3 = this.haptic;
        if (i3 == 0 || (vibrator = this.vibrator) == null) {
            return;
        }
        long j = i;
        int i4 = i2;
        if (i3 == 1) {
            j = (i * 3) / 5;
            i4 = (i2 * 2) / 5;
            if (i4 <= 0) {
                i4 = 1;
            }
        }
        try {
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT < 26) {
                    vibrator.vibrate(j);
                    return;
                }
                if (!vibrator.hasAmplitudeControl()) {
                    i4 = -1;
                }
                vibrator.vibrate(VibrationEffect.createOneShot(j, i4));
            }
        } catch (Throwable th) {
        }
    }

    public final int getHaptic() {
        return this.haptic;
    }

    public final int getStyle() {
        return this.style;
    }

    public final void setHaptic(int i) {
        this.haptic = i;
    }

    public final void setStyle(int i) {
        this.style = i;
    }

    public final void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    public Sfx() {
        Voice[] voiceArr = new Voice[20];
        for (int i = 0; i < 20; i++) {
            voiceArr[i] = new Voice();
        }
        this.voices = voiceArr;
        this.pending = new ConcurrentLinkedQueue<>();
        this.enabled = true;
        this.musicEnabled = true;
        this.noiseState = 324508639;
        this.bassPattern = new int[]{33, -1, 33, -1, 40, -1, 33, -1, 36, -1, 36, -1, 43, -1, 36, -1, 31, -1, 31, -1, 38, -1, 31, -1, 28, -1, 35, -1, 28, 28, 35, 38};
        this.arpPattern = new int[]{69, 72, 76, 72, 69, 72, 76, 79, 72, 76, 79, 76, 72, 76, 79, 83, 67, 71, 74, 71, 67, 71, 74, 79, 64, 67, 71, 67, 64, 67, 71, 74};
        this.hatPattern = new int[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1};
    }

    /* compiled from: Sfx.kt */
    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/retro/snake3d/audio/Sfx$Companion;", "", "()V", "BLOCK", "", "MAX_VOICES", "NOISE", "SQUARE", "SR", "TRI", "midi", "", "n", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final float midi(float n) {
            return ((float) Math.pow(2.0f, (n - 69.0f) / 12.0f)) * 440.0f;
        }
    }

    /* compiled from: Sfx.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\u0006\u0010\n\u001a\u00020\u0005¢\u0006\u0002\u0010\u000bR\u0010\u0010\b\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lcom/retro/snake3d/audio/Sfx$Trigger;", "", "wave", "", "f0", "", "f1", "dur", "amp", "decay", "duty", "(IFFFFFF)V", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Trigger {
        public final float amp;
        public final float decay;
        public final float dur;
        public final float duty;
        public final float f0;
        public final float f1;
        public final int wave;

        public Trigger(int wave, float f0, float f1, float dur, float amp, float decay, float duty) {
            this.wave = wave;
            this.f0 = f0;
            this.f1 = f1;
            this.dur = dur;
            this.amp = amp;
            this.decay = decay;
            this.duty = duty;
        }
    }

    public final boolean getEnabled() {
        return this.enabled;
    }

    public final void setEnabled(boolean z) {
        this.enabled = z;
    }

    public final boolean getMusicEnabled() {
        return this.musicEnabled;
    }

    public final void setMusicEnabled(boolean z) {
        this.musicEnabled = z;
    }

    public final float getIntensity() {
        return this.intensity;
    }

    public final void setIntensity(float f) {
        this.intensity = f;
    }

    public final void start() {
        if (this.running) {
            return;
        }
        int minBuf = AudioTrack.getMinBufferSize(SR, 4, 2);
        int bufSize = Math.max(minBuf, 4096);
        final AudioTrack t = new AudioTrack.Builder().setAudioAttributes(new AudioAttributes.Builder().setUsage(14).setContentType(4).build()).setAudioFormat(new AudioFormat.Builder().setEncoding(2).setSampleRate(SR).setChannelMask(4).build()).setBufferSizeInBytes(bufSize).setTransferMode(1).build();
        Intrinsics.checkNotNullExpressionValue(t, "build(...)");
        this.track = t;
        this.running = true;
        t.play();
        Thread $this$start_u24lambda_u241 = new Thread(new Runnable() { // from class: com.retro.snake3d.audio.Sfx$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Sfx.start$lambda$0(Sfx.this, t);
            }
        }, "Snake3D-Audio");
        $this$start_u24lambda_u241.setPriority(9);
        $this$start_u24lambda_u241.start();
        this.thread = $this$start_u24lambda_u241;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void start$lambda$0(Sfx this$0, AudioTrack t) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(t, "$t");
        this$0.loop(t);
    }

    public final void stop() {
        this.running = false;
        Thread thread = this.thread;
        if (thread != null) {
            try {
                thread.join(400L);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        this.thread = null;
        try {
            AudioTrack audioTrack = this.track;
            if (audioTrack != null) {
                audioTrack.pause();
            }
            AudioTrack audioTrack2 = this.track;
            if (audioTrack2 != null) {
                audioTrack2.flush();
            }
            AudioTrack audioTrack3 = this.track;
            if (audioTrack3 != null) {
                audioTrack3.release();
            }
        } catch (Throwable th) {
        }
        this.track = null;
        for (Voice v : this.voices) {
            v.active = false;
        }
        this.pending.clear();
    }

    static /* synthetic */ void push$default(Sfx sfx, int i, float f, float f2, float f3, float f4, float f5, float f6, int i2, Object obj) {
        sfx.push(i, f, f2, f3, f4, f5, (i2 & 64) != 0 ? 0.5f : f6);
    }

    static /* synthetic */ void pushRaw$default(Sfx sfx, int i, float f, float f2, float f3, float f4, float f5, float f6, int i2, Object obj) {
        sfx.pushRaw(i, f, f2, f3, f4, f5, (i2 & 64) != 0 ? 0.5f : f6);
    }

    private final void pushRaw(int wave, float f0, float f1, float dur, float amp, float decay, float duty) {
        if (this.enabled && this.pending.size() <= 32) {
            this.pending.add(new Trigger(wave, f0, f1, dur, amp, decay, duty));
        }
    }

    public final void eat(int combo) {
        buzz(25, 150);
        int c = Math.min(combo, 8);
        float base = INSTANCE.midi(((c - 1) * 2.0f) + 72.0f);
        push(0, base, base * 1.5f, 0.09f, 0.22f, 6.0f, 0.25f);
        push(0, base * 2.0f, base * 3.0f, 0.06f, 0.1f, 9.0f, 0.5f);
    }

    public final void gold() {
        buzz(50, 230);
        push(0, INSTANCE.midi(76.0f), INSTANCE.midi(88.0f), 0.16f, 0.22f, 3.5f, 0.25f);
        push(0, INSTANCE.midi(83.0f), INSTANCE.midi(95.0f), 0.22f, 0.15f, 3.0f, 0.125f);
        push$default(this, 1, INSTANCE.midi(64.0f), INSTANCE.midi(76.0f), 0.28f, 0.18f, 2.5f, 0.0f, 64, null);
    }

    public final void chill() {
        buzz(40, 120);
        push$default(this, 1, INSTANCE.midi(84.0f), INSTANCE.midi(60.0f), 0.45f, 0.24f, 2.2f, 0.0f, 64, null);
        push(0, INSTANCE.midi(72.0f), INSTANCE.midi(48.0f), 0.35f, 0.1f, 3.0f, 0.125f);
    }

    public final void turn() {
        buzz(12, 70);
        push(0, 900.0f, 1250.0f, 0.025f, 0.055f, 14.0f, 0.125f);
    }

    public final void levelUp() {
        buzz(90, 210);
        push(0, INSTANCE.midi(72.0f), INSTANCE.midi(72.0f), 0.08f, 0.18f, 6.0f, 0.25f);
        push(0, INSTANCE.midi(76.0f), INSTANCE.midi(76.0f), 0.08f, 0.18f, 6.0f, 0.25f);
        push(0, INSTANCE.midi(79.0f), INSTANCE.midi(91.0f), 0.3f, 0.2f, 3.0f, 0.25f);
    }

    public final void expire() {
        buzz(15, 60);
        push(0, 600.0f, 200.0f, 0.16f, 0.1f, 5.0f, 0.5f);
    }

    public final void die() {
        buzz(260, 255);
        push(0, INSTANCE.midi(60.0f), INSTANCE.midi(28.0f), 0.85f, 0.3f, 1.6f, 0.5f);
        push$default(this, 2, 1400.0f, 120.0f, 0.95f, 0.26f, 1.4f, 0.0f, 64, null);
        push$default(this, 1, INSTANCE.midi(48.0f), INSTANCE.midi(24.0f), 1.1f, 0.22f, 1.2f, 0.0f, 64, null);
    }

    public final void uiClick() {
        buzz(10, 80);
        push(0, 700.0f, 950.0f, 0.05f, 0.14f, 10.0f, 0.25f);
    }

    public final void gameStart() {
        buzz(40, 160);
        push(0, INSTANCE.midi(60.0f), INSTANCE.midi(60.0f), 0.09f, 0.2f, 7.0f, 0.25f);
        push(0, INSTANCE.midi(67.0f), INSTANCE.midi(67.0f), 0.09f, 0.2f, 7.0f, 0.25f);
        push(0, INSTANCE.midi(72.0f), INSTANCE.midi(84.0f), 0.26f, 0.22f, 3.2f, 0.25f);
    }

    private final void loop(AudioTrack t) {
        Voice voice;
        int i = 512;
        short[] out = new short[512];
        float[] mix = new float[512];
        while (this.running) {
            while (true) {
                Trigger tr = this.pending.poll();
                if (tr == null) {
                    break;
                }
                Voice[] voiceArr = this.voices;
                int length = voiceArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 < length) {
                        voice = voiceArr[i2];
                        if (!voice.active) {
                            break;
                        } else {
                            i2++;
                        }
                    } else {
                        voice = null;
                        break;
                    }
                }
                if (voice == null) {
                    voice = this.voices[0];
                }
                Voice v = voice;
                v.active = true;
                v.wave = tr.wave;
                v.f0 = tr.f0;
                v.f1 = tr.f1;
                v.dur = tr.dur;
                v.amp = tr.amp;
                v.decay = tr.decay;
                v.duty = tr.duty;
                v.t = 0.0f;
                v.phase = 0.0f;
                i = 512;
            }
            Arrays.fill(mix, 0.0f);
            sequencer(i);
            for (Voice v2 : this.voices) {
                if (v2.active) {
                    renderVoice(v2, mix);
                }
            }
            for (int i3 = 0; i3 < i; i3++) {
                float s = mix[i3];
                int q = (int) (20000.0f * (s / ((Math.abs(s) * 0.7f) + 1.0f)));
                short s2 = ShortCompanionObject.MAX_VALUE;
                if (q <= 32767) {
                    s2 = ShortCompanionObject.MIN_VALUE;
                    if (q >= -32768) {
                        s2 = (short) q;
                    }
                }
                out[i3] = s2;
            }
            try {
                t.write(out, 0, i);
            } catch (Throwable th) {
                return;
            }
        }
    }

    private final void renderVoice(Voice v, float[] mix) {
        float s;
        for (int i = 0; i < mix.length; i++) {
            if (v.t >= v.dur) {
                v.active = false;
                return;
            }
            float u = v.t / v.dur;
            float f = v.f0 + ((v.f1 - v.f0) * u);
            v.phase += f * 2.2675737E-5f;
            if (v.phase >= 1.0f) {
                v.phase -= (int) v.phase;
            }
            switch (v.wave) {
                case 0:
                    if (v.phase < v.duty) {
                        s = 1.0f;
                        break;
                    } else {
                        s = -1.0f;
                        break;
                    }
                case 1:
                    s = (Math.abs(v.phase - 0.5f) * 4.0f) - 1.0f;
                    break;
                default:
                    this.noiseState ^= this.noiseState << 13;
                    this.noiseState ^= this.noiseState >>> 17;
                    this.noiseState ^= this.noiseState << 5;
                    s = ((this.noiseState & SupportMenu.USER_MASK) / 32768.0f) - 1.0f;
                    break;
            }
            float atk = v.t < v.attack ? v.t / v.attack : 1.0f;
            float env = ((float) Math.exp((-v.decay) * u)) * (1.0f - u) * atk;
            mix[i] = mix[i] + (v.amp * s * env);
            v.t += 2.2675737E-5f;
        }
    }

    private final void sequencer(int frames) {
        int an;
        if (this.musicEnabled && this.enabled) {
            float bpm = (this.intensity * 16.0f) + 84.0f;
            float stepDur = (60.0f / bpm) / 4.0f;
            this.seqAcc += frames / SR;
            while (this.seqAcc >= stepDur) {
                this.seqAcc -= stepDur;
                int s = this.seqStep % 32;
                this.seqStep++;
                int bn = this.bassPattern[s];
                if (bn >= 0) {
                    float f = INSTANCE.midi(bn);
                    pushRaw(1, f, f * 0.98f, stepDur * 3.0f, (this.intensity * 0.05f) + 0.2f, 1.2f, 0.5f);
                }
                if ((this.intensity > 0.05f || this.musicEnabled) && (an = this.arpPattern[s]) >= 0) {
                    float f2 = INSTANCE.midi(an) * 0.5f;
                    pushRaw(1, f2, f2, stepDur * 2.5f, (this.intensity * 0.05f) + 0.05f, 1.8f, 0.25f);
                }
                if (this.hatPattern[s] == 1) {
                    pushRaw$default(this, 2, 7000.0f, 4000.0f, 0.035f, ((this.intensity * 0.03f) + 0.05f) * 0.3f, 30.0f, 0.0f, 64, null);
                }
            }
        }
    }
}
