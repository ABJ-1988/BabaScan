package com.retro.snake3d.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* compiled from: GameCore.kt */
@Metadata(d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0018\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u0002\n\u0002\b4\u0018\u0000 \u008f\u00012\u00020\u0001:\u0002\u008f\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010Z\u001a\u00020\u001cH\u0002J\u0010\u0010[\u001a\u00020\\2\u0006\u0010]\u001a\u00020\u0004H\u0002J\u0006\u0010^\u001a\u00020\\J\b\u0010_\u001a\u00020\\H\u0002J\u0010\u0010`\u001a\u00020\u000e2\u0006\u0010a\u001a\u000205H\u0002J\u0010\u0010b\u001a\u00020\\2\u0006\u0010a\u001a\u000205H\u0002J \u0010c\u001a\u00020\\2\u0006\u0010d\u001a\u00020\u00042\u0006\u0010]\u001a\u00020\u00042\u0006\u0010e\u001a\u00020\u000eH\u0002J\u0018\u0010f\u001a\u00020\u00042\u0006\u0010g\u001a\u00020\u00042\u0006\u0010h\u001a\u00020\u0004H\u0002J\u000e\u0010i\u001a\u00020\\2\u0006\u0010j\u001a\u00020\u001cJ\u000e\u0010k\u001a\u00020\\2\u0006\u0010l\u001a\u000205J\u0010\u0010m\u001a\u0002052\u0006\u0010]\u001a\u00020\u0004H\u0002J\u0018\u0010n\u001a\u00020\u00042\u0006\u0010o\u001a\u00020\u00042\u0006\u0010p\u001a\u00020\u0004H\u0002J\b\u0010q\u001a\u00020\\H\u0002J\b\u0010r\u001a\u00020\\H\u0002J\u0018\u0010s\u001a\u00020\u00042\u0006\u0010]\u001a\u00020\u00042\u0006\u0010j\u001a\u00020\u001cH\u0002J\b\u0010t\u001a\u00020\u001cH\u0002J\u000e\u0010u\u001a\u00020\u00042\u0006\u0010v\u001a\u00020\u0004J\u0010\u0010w\u001a\u00020\\2\u0006\u0010]\u001a\u00020\u0004H\u0002J\u0010\u0010x\u001a\u00020\\2\u0006\u0010]\u001a\u00020\u0004H\u0002J\u0006\u0010y\u001a\u00020\\J\u0010\u0010z\u001a\u00020\\2\u0006\u0010]\u001a\u00020\u0004H\u0002J\u0010\u0010{\u001a\u00020\u00042\u0006\u0010|\u001a\u00020\u0004H\u0002J\u0018\u0010}\u001a\u0002052\u0006\u0010g\u001a\u00020\u00042\u0006\u0010~\u001a\u00020\u0004H\u0002J\u0006\u0010\u007f\u001a\u00020\\J\t\u0010\u0080\u0001\u001a\u00020\\H\u0002J\t\u0010\u0081\u0001\u001a\u00020\\H\u0002J\u0007\u0010\u0082\u0001\u001a\u00020\\J\u000f\u0010\u0083\u0001\u001a\u00020\u00042\u0006\u0010v\u001a\u00020\u0004J\u000f\u0010\u0084\u0001\u001a\u00020\u00042\u0006\u0010v\u001a\u00020\u0004J\t\u0010\u0085\u0001\u001a\u00020\\H\u0002J\u0007\u0010\u0086\u0001\u001a\u00020\\J\u0011\u0010\u0087\u0001\u001a\u00020\\2\u0006\u0010a\u001a\u000205H\u0002J\u001a\u0010\u0088\u0001\u001a\u00020\\2\u0007\u0010\u0089\u0001\u001a\u00020\u000e2\u0006\u0010a\u001a\u000205H\u0002J\u0012\u0010\u008a\u0001\u001a\u00020\\2\u0007\u0010\u0089\u0001\u001a\u00020\u000eH\u0002J\u0010\u0010\u008b\u0001\u001a\u00020\\2\u0007\u0010\u008c\u0001\u001a\u00020\u000eJ\u000f\u0010\u008d\u0001\u001a\u00020\u000e2\u0006\u0010]\u001a\u00020\u0004J\u000f\u0010\u008e\u0001\u001a\u00020\u000e2\u0006\u0010]\u001a\u00020\u0004R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\f\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0007R\u001e\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000e@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001e\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000e@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u001e\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0007R\u001e\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0007R\u001e\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000e@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0011R\u001e\u0010\u001a\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000e@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0011R\u001e\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u0003\u001a\u00020\u001c@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u001e\u0010 \u001a\u0012\u0012\u0004\u0012\u00020\"0!j\b\u0012\u0004\u0012\u00020\"`#X\u0082\u0004¢\u0006\u0002\n\u0000R!\u0010$\u001a\u0012\u0012\u0004\u0012\u00020\"0!j\b\u0012\u0004\u0012\u00020\"`#¢\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u000e\u0010'\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010+\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u0007\"\u0004\b-\u0010.R\u000e\u0010/\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u00101\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b2\u0010\u0007R\u001e\u00103\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b4\u0010\u0007R\u001e\u00106\u001a\u0002052\u0006\u0010\u0003\u001a\u000205@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u001e\u00109\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b:\u0010\u0007R\u000e\u0010;\u001a\u00020<X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020<X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010?\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000e@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b@\u0010\u0011R\u001e\u0010A\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\bB\u0010\u0007R\u000e\u0010C\u001a\u00020DX\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010E\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\bF\u0010\u0007R\u0011\u0010G\u001a\u0002058F¢\u0006\u0006\u001a\u0004\bH\u00108R\u000e\u0010I\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010L\u001a\u00020K2\u0006\u0010\u0003\u001a\u00020K@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\bM\u0010NR\u0011\u0010O\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\bP\u0010\u0011R\u001e\u0010Q\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000e@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\bR\u0010\u0011R\u000e\u0010S\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010T\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000e@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\bU\u0010\u0011R\u001a\u0010V\u001a\u000205X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bW\u00108\"\u0004\bX\u0010Y¨\u0006\u0090\u0001"}, d2 = {"Lcom/retro/snake3d/game/GameCore;", "", "()V", "<set-?>", "", "appleCell", "getAppleCell", "()I", "apples", "getApples", "body", "", "bonusCell", "getBonusCell", "", "bonusTtl", "getBonusTtl", "()F", "bonusTtlMax", "getBonusTtlMax", "bonusType", "getBonusType", "combo", "getCombo", "comboTimer", "getComboTimer", "deathTimer", "getDeathTimer", "Lcom/retro/snake3d/game/Dir;", "dir", "getDir", "()Lcom/retro/snake3d/game/Dir;", "eventPool", "Ljava/util/ArrayList;", "Lcom/retro/snake3d/game/FxEvent;", "Lkotlin/collections/ArrayList;", "events", "getEvents", "()Ljava/util/ArrayList;", "floodStack", "floodStamp", "growth", "headIdx", "highScore", "getHighScore", "setHighScore", "(I)V", "inputCount", "inputQueue", "length", "getLength", "level", "getLevel", "", "newRecord", "getNewRecord", "()Z", "obstacleCount", "getObstacleCount", "obstacleGrid", "", "obstacleList", "onSnake", "playTime", "getPlayTime", "removedTail", "getRemovedTail", "rnd", "Ljava/util/Random;", "score", "getScore", "slowActive", "getSlowActive", "slowTimer", "stampCounter", "Lcom/retro/snake3d/game/State;", "state", "getState", "()Lcom/retro/snake3d/game/State;", "stepAlpha", "getStepAlpha", "stepInterval", "getStepInterval", "stepTimer", "turnFlash", "getTurnFlash", "wrapMode", "getWrapMode", "setWrapMode", "(Z)V", "aiChooseDir", "appendTail", "", "c", "backToTitle", "bumpCombo", "computeInterval", "demo", "die", "emit", "kind", "power", "floodCount", "from", "limit", "input", "d", "inputRelative", "left", "isFree", "manhattan", "a", "b", "maybeSpawnBonus", "maybeSpawnObstacles", "neighbor", "nextDir", "obstacle", "i", "onApple", "onBonus", "pause", "pushHead", "randomFreeCell", "minDistFromHead", "reachable", "target", "recycleEvents", "removeTail", "resetBoard", "resume", "seg", "segPrev", "spawnApple", "startGame", "step", "stepLoop", "dt", "tickBonus", "update", "dtRaw", "worldX", "worldZ", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class GameCore {
    public static final int CAP = 273;
    public static final float COMBO_WINDOW = 2.6f;
    public static final float DEATH_DURATION = 1.5f;
    public static final int MAX_COMBO = 8;
    public static final int START_LENGTH = 4;
    private int apples;
    private float bonusTtl;
    private float comboTimer;
    private float deathTimer;
    private int growth;
    private int headIdx;
    private int highScore;
    private int inputCount;
    private int length;
    private int level;
    private boolean newRecord;
    private int obstacleCount;
    private float playTime;
    private int score;
    private float slowTimer;
    private int stampCounter;
    private float stepTimer;
    private float turnFlash;
    private boolean wrapMode;
    private static final Dir[] DIRS = Dir.values();
    private State state = State.TITLE;
    private int combo = 1;
    private final int[] body = new int[CAP];
    private int removedTail = -1;
    private final boolean[] onSnake = new boolean[CAP];
    private final boolean[] obstacleGrid = new boolean[CAP];
    private final int[] obstacleList = new int[CAP];
    private Dir dir = Dir.UP;
    private final int[] inputQueue = new int[3];
    private int appleCell = -1;
    private int bonusCell = -1;
    private int bonusType = 1;
    private float bonusTtlMax = 1.0f;
    private float stepInterval = 0.19f;
    private final ArrayList<FxEvent> events = new ArrayList<>(16);
    private final ArrayList<FxEvent> eventPool = new ArrayList<>(32);
    private final Random rnd = new Random();
    private final int[] floodStamp = new int[CAP];
    private final int[] floodStack = new int[CAP];

    /* compiled from: GameCore.kt */
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
                iArr[State.PLAYING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[State.DYING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public GameCore() {
        resetBoard();
    }

    public final boolean getWrapMode() {
        return this.wrapMode;
    }

    public final void setWrapMode(boolean z) {
        this.wrapMode = z;
    }

    public final int getHighScore() {
        return this.highScore;
    }

    public final void setHighScore(int i) {
        this.highScore = i;
    }

    public final State getState() {
        return this.state;
    }

    public final int getScore() {
        return this.score;
    }

    public final int getApples() {
        return this.apples;
    }

    public final int getCombo() {
        return this.combo;
    }

    public final float getComboTimer() {
        return this.comboTimer;
    }

    public final int getLevel() {
        return this.level;
    }

    public final float getDeathTimer() {
        return this.deathTimer;
    }

    public final boolean getNewRecord() {
        return this.newRecord;
    }

    public final float getPlayTime() {
        return this.playTime;
    }

    public final float getTurnFlash() {
        return this.turnFlash;
    }

    public final int getLength() {
        return this.length;
    }

    public final int getRemovedTail() {
        return this.removedTail;
    }

    public final int getObstacleCount() {
        return this.obstacleCount;
    }

    public final Dir getDir() {
        return this.dir;
    }

    public final int getAppleCell() {
        return this.appleCell;
    }

    public final int getBonusCell() {
        return this.bonusCell;
    }

    public final int getBonusType() {
        return this.bonusType;
    }

    public final float getBonusTtl() {
        return this.bonusTtl;
    }

    public final float getBonusTtlMax() {
        return this.bonusTtlMax;
    }

    public final float getStepInterval() {
        return this.stepInterval;
    }

    public final float getStepAlpha() {
        if (this.stepInterval <= 0.0f) {
            return 0.0f;
        }
        return RangesKt.coerceIn(this.stepTimer / this.stepInterval, 0.0f, 1.0f);
    }

    public final boolean getSlowActive() {
        return this.slowTimer > 0.0f;
    }

    public final ArrayList<FxEvent> getEvents() {
        return this.events;
    }

    public final void startGame() {
        resetBoard();
        this.score = 0;
        this.apples = 0;
        this.combo = 1;
        this.comboTimer = 0.0f;
        this.level = 0;
        this.playTime = 0.0f;
        this.newRecord = false;
        this.deathTimer = 0.0f;
        this.slowTimer = 0.0f;
        this.state = State.PLAYING;
    }

    public final void backToTitle() {
        resetBoard();
        this.state = State.TITLE;
    }

    public final void pause() {
        if (this.state == State.PLAYING) {
            this.state = State.PAUSED;
        }
    }

    public final void resume() {
        if (this.state == State.PAUSED) {
            this.state = State.PLAYING;
        }
    }

    public final void input(Dir d) {
        Intrinsics.checkNotNullParameter(d, "d");
        if (this.state != State.PLAYING) {
            return;
        }
        Dir last = this.inputCount > 0 ? DIRS[this.inputQueue[this.inputCount - 1]] : this.dir;
        if (d == last || d == last.opposite() || this.inputCount >= this.inputQueue.length) {
            return;
        }
        int[] iArr = this.inputQueue;
        int i = this.inputCount;
        this.inputCount = i + 1;
        iArr[i] = d.ordinal();
    }

    public final void inputRelative(boolean left) {
        if (this.state != State.PLAYING) {
            return;
        }
        Dir last = this.inputCount > 0 ? DIRS[this.inputQueue[this.inputCount - 1]] : this.dir;
        input(left ? last.turnLeft() : last.turnRight());
    }

    public final void update(float dtRaw) {
        float dt = RangesKt.coerceIn(dtRaw, 0.0f, 0.05f);
        if (this.turnFlash > 0.0f) {
            this.turnFlash = Math.max(0.0f, this.turnFlash - (4.0f * dt));
        }
        switch (WhenMappings.$EnumSwitchMapping$0[this.state.ordinal()]) {
            case 1:
                tickBonus(dt);
                stepLoop(dt, true);
                break;
            case 2:
                this.playTime += dt;
                if (this.comboTimer > 0.0f) {
                    this.comboTimer -= dt;
                    if (this.comboTimer <= 0.0f) {
                        this.combo = 1;
                    }
                }
                if (this.slowTimer > 0.0f) {
                    this.slowTimer = Math.max(0.0f, this.slowTimer - dt);
                }
                tickBonus(dt);
                stepLoop(dt, false);
                break;
            case 3:
                this.deathTimer += dt;
                if (this.deathTimer >= 1.5f) {
                    this.state = State.GAMEOVER;
                    break;
                }
                break;
        }
    }

    private final void stepLoop(float dt, boolean demo) {
        this.stepInterval = computeInterval(demo);
        this.stepTimer += dt;
        int guard = 0;
        while (this.stepTimer >= this.stepInterval && guard < 4) {
            this.stepTimer -= this.stepInterval;
            step(demo);
            guard++;
            if (this.state == State.DYING) {
                this.stepTimer = 0.0f;
                return;
            }
        }
    }

    private final float computeInterval(boolean demo) {
        if (demo) {
            return 0.13f;
        }
        float base = 0.19f - (this.apples * 0.0038f);
        float v = Math.max(0.072f, base);
        return this.slowTimer > 0.0f ? 1.75f * v : v;
    }

    private final void step(boolean demo) {
        boolean hitsSelf = false;
        int head = seg(0);
        Dir newDir = demo ? aiChooseDir() : nextDir();
        if (newDir != this.dir) {
            this.turnFlash = 1.0f;
        }
        this.dir = newDir;
        int nc = neighbor(head, this.dir);
        if (nc < 0) {
            die(demo);
            return;
        }
        int ni = TypesKt.idx(nc);
        if (this.obstacleGrid[ni]) {
            die(demo);
            return;
        }
        boolean eatsApple = nc == this.appleCell;
        boolean eatsBonus = nc == this.bonusCell;
        boolean bonusGrows = eatsBonus && this.bonusType != 2;
        boolean willGrow = this.growth > 0 || eatsApple || bonusGrows;
        int tailCell = seg(this.length - 1);
        if (this.onSnake[ni] && (nc != tailCell || willGrow || this.length <= 1)) {
            hitsSelf = true;
        }
        if (hitsSelf) {
            die(demo);
            return;
        }
        this.removedTail = -1;
        if (willGrow) {
            if (this.growth > 0) {
                this.growth--;
            }
        } else {
            removeTail();
        }
        pushHead(nc);
        if (eatsApple) {
            onApple(nc);
        }
        if (eatsBonus) {
            onBonus(nc);
        }
    }

    private final void onApple(int c) {
        this.apples++;
        bumpCombo();
        this.score += this.combo * 10;
        emit(0, c, this.combo);
        spawnApple();
        maybeSpawnBonus();
        maybeSpawnObstacles();
        int nl = Math.min(this.apples / 8, Palette.INSTANCE.getALL().length - 1);
        if (nl != this.level) {
            this.level = nl;
            emit(7, c, 1.0f);
        }
    }

    private final void onBonus(int c) {
        bumpCombo();
        switch (this.bonusType) {
            case 1:
                this.growth += 2;
                this.score += this.combo * 60;
                emit(1, c, this.combo);
                break;
            case 2:
                this.slowTimer = 4.5f;
                this.score += this.combo * 20;
                emit(2, c, 1.0f);
                break;
        }
        this.bonusCell = -1;
        this.bonusTtl = 0.0f;
    }

    private final void bumpCombo() {
        this.combo = this.comboTimer > 0.0f ? Math.min(this.combo + 1, 8) : 1;
        this.comboTimer = 2.6f;
    }

    private final void tickBonus(float dt) {
        if (this.bonusCell < 0) {
            return;
        }
        this.bonusTtl -= dt;
        if (this.bonusTtl <= 0.0f) {
            emit(6, this.bonusCell, 1.0f);
            this.bonusCell = -1;
        }
    }

    private final void die(boolean demo) {
        if (demo) {
            resetBoard();
            return;
        }
        emit(3, seg(0), this.length);
        this.state = State.DYING;
        this.deathTimer = 0.0f;
        if (this.score > this.highScore) {
            this.highScore = this.score;
            this.newRecord = true;
        }
    }

    public final int seg(int i) {
        return this.body[(this.headIdx + i) % CAP];
    }

    public final int segPrev(int i) {
        return i + 1 < this.length ? seg(i + 1) : this.removedTail >= 0 ? this.removedTail : seg(i);
    }

    private final void pushHead(int c) {
        this.headIdx = ((this.headIdx - 1) + CAP) % CAP;
        this.body[this.headIdx] = c;
        this.length++;
        this.onSnake[TypesKt.idx(c)] = true;
    }

    private final void removeTail() {
        if (this.length <= 0) {
            return;
        }
        int ti = ((this.headIdx + this.length) - 1) % CAP;
        this.removedTail = this.body[ti];
        this.onSnake[TypesKt.idx(this.removedTail)] = false;
        this.length--;
    }

    private final void appendTail(int c) {
        this.body[(this.headIdx + this.length) % CAP] = c;
        this.length++;
        this.onSnake[TypesKt.idx(c)] = true;
    }

    private final Dir nextDir() {
        if (this.inputCount > 0) {
            Dir d = DIRS[this.inputQueue[0]];
            int i = this.inputCount;
            for (int i2 = 1; i2 < i; i2++) {
                this.inputQueue[i2 - 1] = this.inputQueue[i2];
            }
            int i3 = this.inputCount;
            this.inputCount = i3 - 1;
            if (d != this.dir.opposite()) {
                return d;
            }
        }
        return this.dir;
    }

    private final void resetBoard() {
        Arrays.fill(this.onSnake, false);
        Arrays.fill(this.obstacleGrid, false);
        this.obstacleCount = 0;
        this.headIdx = 0;
        this.length = 0;
        this.growth = 0;
        this.removedTail = -1;
        this.stepTimer = 0.0f;
        this.inputCount = 0;
        this.dir = Dir.UP;
        this.appleCell = -1;
        this.bonusCell = -1;
        this.bonusTtl = 0.0f;
        for (int i = 0; i < 4; i++) {
            appendTail(TypesKt.cell(6, Math.min(20, 13 + i)));
        }
        spawnApple();
    }

    private final int neighbor(int c, Dir d) {
        int x = TypesKt.cx(c) + d.getDx();
        int z = TypesKt.cz(c) + d.getDz();
        if (this.wrapMode) {
            if (x < 0) {
                x = 12;
            } else if (x >= 13) {
                x = 0;
            }
            if (z < 0) {
                z = 20;
            } else if (z >= 21) {
                z = 0;
            }
        } else if (x < 0 || x >= 13 || z < 0 || z >= 21) {
            return -1;
        }
        return TypesKt.cell(x, z);
    }

    private final void spawnApple() {
        this.appleCell = randomFreeCell(2);
    }

    private final void maybeSpawnBonus() {
        int c;
        if (this.bonusCell < 0 && this.rnd.nextFloat() <= 0.24f && (c = randomFreeCell(3)) >= 0) {
            this.bonusCell = c;
            this.bonusType = this.rnd.nextFloat() < 0.72f ? 1 : 2;
            this.bonusTtlMax = this.bonusType == 1 ? 7.5f : 9.0f;
            this.bonusTtl = this.bonusTtlMax;
        }
    }

    private final void maybeSpawnObstacles() {
        if (this.apples == 0 || this.apples % 6 != 0 || this.obstacleCount >= 26) {
            return;
        }
        int placed = 0;
        int tries = 0;
        while (placed < 2 && tries < 60) {
            tries++;
            int c = randomFreeCell(5);
            if (c >= 0) {
                int i = TypesKt.idx(c);
                this.obstacleGrid[i] = true;
                if (this.appleCell >= 0 && !reachable(seg(0), this.appleCell)) {
                    this.obstacleGrid[i] = false;
                } else {
                    int[] iArr = this.obstacleList;
                    int i2 = this.obstacleCount;
                    this.obstacleCount = i2 + 1;
                    iArr[i2] = c;
                    placed++;
                    emit(5, c, 1.0f);
                }
            } else {
                return;
            }
        }
    }

    public final int obstacle(int i) {
        return this.obstacleList[i];
    }

    private final int randomFreeCell(int minDistFromHead) {
        int head = this.length > 0 ? seg(0) : TypesKt.cell(6, 10);
        int tries = 0;
        while (tries < 300) {
            tries++;
            int c = TypesKt.cell(this.rnd.nextInt(13), this.rnd.nextInt(21));
            if (isFree(c) && manhattan(c, head) >= minDistFromHead) {
                return c;
            }
        }
        for (int x = 0; x < 13; x++) {
            for (int z = 0; z < 21; z++) {
                int c2 = TypesKt.cell(x, z);
                if (isFree(c2)) {
                    return c2;
                }
            }
        }
        return -1;
    }

    private final boolean isFree(int c) {
        int i = TypesKt.idx(c);
        return (this.onSnake[i] || this.obstacleGrid[i] || c == this.appleCell || c == this.bonusCell) ? false : true;
    }

    private final int manhattan(int a, int b) {
        int dx = Math.abs(TypesKt.cx(a) - TypesKt.cx(b));
        int dz = Math.abs(TypesKt.cz(a) - TypesKt.cz(b));
        if (this.wrapMode) {
            dx = Math.min(dx, 13 - dx);
            dz = Math.min(dz, 21 - dz);
        }
        return dx + dz;
    }

    private final boolean reachable(int from, int target) {
        this.stampCounter++;
        int sp = 0 + 1;
        this.floodStack[0] = from;
        this.floodStamp[TypesKt.idx(from)] = this.stampCounter;
        while (true) {
            if (sp <= 0) {
                return false;
            }
            sp--;
            int c = this.floodStack[sp];
            if (c == target) {
                return true;
            }
            for (Dir d : DIRS) {
                int n = neighbor(c, d);
                if (n >= 0) {
                    int ni = TypesKt.idx(n);
                    if (this.floodStamp[ni] != this.stampCounter && !this.obstacleGrid[ni] && !this.onSnake[ni]) {
                        this.floodStamp[ni] = this.stampCounter;
                        this.floodStack[sp] = n;
                        sp++;
                    }
                }
            }
        }
    }

    private final int floodCount(int from, int limit) {
        this.stampCounter++;
        int count = 0;
        int sp = 0 + 1;
        this.floodStack[0] = from;
        this.floodStamp[TypesKt.idx(from)] = this.stampCounter;
        while (sp > 0 && count < limit) {
            sp--;
            int c = this.floodStack[sp];
            count++;
            for (Dir d : DIRS) {
                int n = neighbor(c, d);
                if (n >= 0) {
                    int ni = TypesKt.idx(n);
                    if (this.floodStamp[ni] != this.stampCounter && !this.obstacleGrid[ni] && !this.onSnake[ni]) {
                        this.floodStamp[ni] = this.stampCounter;
                        this.floodStack[sp] = n;
                        sp++;
                    }
                }
            }
        }
        return count;
    }

    private final Dir aiChooseDir() {
        int n;
        int head = seg(0);
        int target = this.bonusCell >= 0 ? this.bonusCell : this.appleCell;
        Dir best = this.dir;
        int bestScore = Integer.MIN_VALUE;
        for (Dir d : DIRS) {
            if ((this.length <= 1 || d != this.dir.opposite()) && (n = neighbor(head, d)) >= 0) {
                int ni = TypesKt.idx(n);
                if (!this.obstacleGrid[ni] && !this.onSnake[ni]) {
                    int space = floodCount(n, this.length + 24);
                    int s = space * 5;
                    if (target >= 0) {
                        s -= manhattan(n, target) * 3;
                    }
                    if (space < this.length + 2) {
                        s -= 500;
                    }
                    int s2 = s + this.rnd.nextInt(3);
                    if (s2 > bestScore) {
                        bestScore = s2;
                        best = d;
                    }
                }
            }
        }
        return best;
    }

    public final float worldX(int c) {
        return TypesKt.cx(c) - 6;
    }

    public final float worldZ(int c) {
        return TypesKt.cz(c) - 10;
    }

    private final void emit(int kind, int c, float power) {
        FxEvent e;
        if (this.eventPool.isEmpty()) {
            e = new FxEvent(0, 0.0f, 0.0f, 0.0f, 0.0f, 31, null);
        } else {
            FxEvent remove = this.eventPool.remove(this.eventPool.size() - 1);
            Intrinsics.checkNotNullExpressionValue(remove, "removeAt(...)");
            e = remove;
        }
        e.kind = kind;
        e.x = worldX(c);
        e.y = 0.5f;
        e.z = worldZ(c);
        e.power = power;
        this.events.add(e);
    }

    public final void recycleEvents() {
        int size = this.events.size();
        for (int i = 0; i < size; i++) {
            this.eventPool.add(this.events.get(i));
        }
        this.events.clear();
    }
}
