package com.retro.snake3d.gl;

import android.opengl.GLES30;
import java.nio.FloatBuffer;
import java.util.Random;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* compiled from: ParticleSystem.kt */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0018\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004JV\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020$2\u0006\u0010&\u001a\u00020$2\u0006\u0010'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020$2\u0006\u0010)\u001a\u00020$2\u0006\u0010*\u001a\u00020$2\u0006\u0010+\u001a\u00020$2\u0006\u0010,\u001a\u00020$2\u0006\u0010-\u001a\u00020$J\u0006\u0010.\u001a\u00020\"J\u0006\u0010/\u001a\u00020\"J\u0016\u00100\u001a\u00020\"2\u0006\u00101\u001a\u00020\t2\u0006\u00102\u001a\u00020$J\u0006\u00103\u001a\u00020\"JF\u00104\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020$2\u0006\u0010&\u001a\u00020$2\u0006\u0010'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020$2\u0006\u0010)\u001a\u00020$2\u0006\u0010*\u001a\u00020$2\u0006\u0010+\u001a\u00020$Jf\u00105\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020$2\u0006\u0010&\u001a\u00020$2\u0006\u00106\u001a\u00020$2\u0006\u00107\u001a\u00020$2\u0006\u00108\u001a\u00020$2\u0006\u0010)\u001a\u00020$2\u0006\u0010*\u001a\u00020$2\u0006\u0010+\u001a\u00020$2\u0006\u0010,\u001a\u00020$2\u0006\u0010-\u001a\u00020$2\u0006\u00109\u001a\u00020$J\u000e\u0010:\u001a\u00020\"2\u0006\u0010;\u001a\u00020$R\u000e\u0010\u0005\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u000b\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006<"}, d2 = {"Lcom/retro/snake3d/gl/ParticleSystem;", "", "max", "", "(I)V", "alive", "buffer", "Ljava/nio/FloatBuffer;", "cb", "", "cg", "count", "getCount", "()I", "cr", "data", "drag", "life", "maxLife", "program", "px", "py", "pz", "rnd", "Ljava/util/Random;", "size", "uScale", "uViewProj", "vao", "vbo", "vx", "vy", "vz", "burst", "", "x", "", "y", "z", "n", "speed", "r", "g", "b", "lifeSec", "sz", "clear", "create", "draw", "viewProj", "pixelScale", "release", "ring", "spawn", "dx", "dy", "dz", "dragK", "update", "dt", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes4.dex */
public final class ParticleSystem {
    private int alive;
    private final FloatBuffer buffer;
    private final float[] cb;
    private final float[] cg;
    private final float[] cr;
    private final float[] data;
    private final float[] drag;
    private final float[] life;
    private final int max;
    private final float[] maxLife;
    private int program;
    private final float[] px;
    private final float[] py;
    private final float[] pz;
    private final Random rnd;
    private final float[] size;
    private int uScale;
    private int uViewProj;
    private int vao;
    private int vbo;
    private final float[] vx;
    private final float[] vy;
    private final float[] vz;

    public ParticleSystem() {
        this(0, 1, null);
    }

    public ParticleSystem(int max) {
        this.max = max;
        this.px = new float[this.max];
        this.py = new float[this.max];
        this.pz = new float[this.max];
        this.vx = new float[this.max];
        this.vy = new float[this.max];
        this.vz = new float[this.max];
        this.cr = new float[this.max];
        this.cg = new float[this.max];
        this.cb = new float[this.max];
        this.life = new float[this.max];
        this.maxLife = new float[this.max];
        this.size = new float[this.max];
        this.drag = new float[this.max];
        this.data = new float[this.max * 8];
        this.buffer = GlUtil.INSTANCE.directFloats(this.max * 8);
        this.rnd = new Random();
    }

    public /* synthetic */ ParticleSystem(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 2400 : i);
    }

    /* renamed from: getCount, reason: from getter */
    public final int getAlive() {
        return this.alive;
    }

    public final void create() {
        this.program = GlUtil.INSTANCE.program(Shaders.PARTICLE_VS, Shaders.PARTICLE_FS);
        this.uViewProj = GLES30.glGetUniformLocation(this.program, "uViewProj");
        this.uScale = GLES30.glGetUniformLocation(this.program, "uScale");
        this.vao = GlUtil.INSTANCE.genVertexArray();
        GLES30.glBindVertexArray(this.vao);
        this.vbo = GlUtil.INSTANCE.genBuffer();
        GLES30.glBindBuffer(34962, this.vbo);
        GLES30.glBufferData(34962, this.max * 8 * 4, null, 35048);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 3, 5126, false, 32, 0);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(1, 4, 5126, false, 32, 12);
        GLES30.glEnableVertexAttribArray(2);
        GLES30.glVertexAttribPointer(2, 1, 5126, false, 32, 28);
        GLES30.glBindVertexArray(0);
        GLES30.glBindBuffer(34962, 0);
    }

    public final void clear() {
        this.alive = 0;
    }

    public final void spawn(float x, float y, float z, float dx, float dy, float dz, float r, float g, float b, float lifeSec, float sz, float dragK) {
        if (this.alive >= this.max) {
            return;
        }
        int i = this.alive;
        this.alive = i + 1;
        this.px[i] = x;
        this.py[i] = y;
        this.pz[i] = z;
        this.vx[i] = dx;
        this.vy[i] = dy;
        this.vz[i] = dz;
        this.cr[i] = r;
        this.cg[i] = g;
        this.cb[i] = b;
        this.life[i] = lifeSec;
        this.maxLife[i] = lifeSec;
        this.size[i] = sz;
        this.drag[i] = dragK;
    }

    public final void burst(float x, float y, float z, int n, float speed, float r, float g, float b, float lifeSec, float sz) {
        for (int i = 0; i < n; i++) {
            float theta = this.rnd.nextFloat() * 6.2831855f;
            float u = (this.rnd.nextFloat() * 2.0f) - 1.0f;
            float s = (float) Math.sqrt(1.0f - (u * u));
            float sp = speed * ((this.rnd.nextFloat() * 0.9f) + 0.35f);
            spawn(x, y, z, ((float) Math.cos(theta)) * s * sp, ((u * 0.55f) + 0.55f) * sp, ((float) Math.sin(theta)) * s * sp, r, g, b, lifeSec * ((this.rnd.nextFloat() * 0.7f) + 0.6f), sz * ((this.rnd.nextFloat() * 0.8f) + 0.6f), 1.9f);
        }
    }

    public final void ring(float x, float y, float z, int n, float speed, float r, float g, float b) {
        for (int i = 0; i < n; i++) {
            float a = ((i / n) * 6.2831855f) + (this.rnd.nextFloat() * 0.2f);
            spawn(x, y, z, ((float) Math.cos(a)) * speed, (this.rnd.nextFloat() * 0.4f) + 0.35f, ((float) Math.sin(a)) * speed, r, g, b, (this.rnd.nextFloat() * 0.35f) + 0.5f, 0.16f, 2.6f);
        }
    }

    public final void update(float dt) {
        int i = 0;
        while (i < this.alive) {
            float[] fArr = this.life;
            fArr[i] = fArr[i] - dt;
            if (this.life[i] <= 0.0f) {
                int last = this.alive - 1;
                if (i != last) {
                    this.px[i] = this.px[last];
                    this.py[i] = this.py[last];
                    this.pz[i] = this.pz[last];
                    this.vx[i] = this.vx[last];
                    this.vy[i] = this.vy[last];
                    this.vz[i] = this.vz[last];
                    this.cr[i] = this.cr[last];
                    this.cg[i] = this.cg[last];
                    this.cb[i] = this.cb[last];
                    this.life[i] = this.life[last];
                    this.maxLife[i] = this.maxLife[last];
                    this.size[i] = this.size[last];
                    this.drag[i] = this.drag[last];
                }
                this.alive--;
            } else {
                float d = 1.0f - (this.drag[i] * dt);
                float[] fArr2 = this.vx;
                fArr2[i] = fArr2[i] * d;
                float[] fArr3 = this.vz;
                fArr3[i] = fArr3[i] * d;
                this.vy[i] = (this.vy[i] * d) - (6.2f * dt);
                float[] fArr4 = this.px;
                fArr4[i] = fArr4[i] + (this.vx[i] * dt);
                float[] fArr5 = this.py;
                fArr5[i] = fArr5[i] + (this.vy[i] * dt);
                float[] fArr6 = this.pz;
                fArr6[i] = fArr6[i] + (this.vz[i] * dt);
                if (this.py[i] < 0.06f) {
                    this.py[i] = 0.06f;
                    this.vy[i] = (-this.vy[i]) * 0.38f;
                    float[] fArr7 = this.vx;
                    fArr7[i] = fArr7[i] * 0.72f;
                    float[] fArr8 = this.vz;
                    fArr8[i] = fArr8[i] * 0.72f;
                }
                i++;
            }
        }
    }

    public final void draw(float[] viewProj, float pixelScale) {
        Intrinsics.checkNotNullParameter(viewProj, "viewProj");
        if (this.alive == 0) {
            return;
        }
        int o = 0;
        int i = this.alive;
        for (int i2 = 0; i2 < i; i2++) {
            float t = RangesKt.coerceIn(this.life[i2] / this.maxLife[i2], 0.0f, 1.0f);
            float fade = t * t;
            this.data[o] = this.px[i2];
            this.data[o + 1] = this.py[i2];
            this.data[o + 2] = this.pz[i2];
            this.data[o + 3] = this.cr[i2];
            this.data[o + 4] = this.cg[i2];
            this.data[o + 5] = this.cb[i2];
            this.data[o + 6] = fade;
            this.data[o + 7] = this.size[i2] * ((0.75f * t) + 0.45f);
            o += 8;
        }
        this.buffer.position(0);
        this.buffer.put(this.data, 0, this.alive * 8);
        this.buffer.position(0);
        GLES30.glUseProgram(this.program);
        GLES30.glUniformMatrix4fv(this.uViewProj, 1, false, viewProj, 0);
        GLES30.glUniform1f(this.uScale, pixelScale);
        GLES30.glBindVertexArray(this.vao);
        GLES30.glBindBuffer(34962, this.vbo);
        GLES30.glBufferSubData(34962, 0, this.alive * 8 * 4, this.buffer);
        GLES30.glEnable(3042);
        GLES30.glBlendFunc(770, 1);
        GLES30.glDepthMask(false);
        GLES30.glDrawArrays(0, 0, this.alive);
        GLES30.glDepthMask(true);
        GLES30.glDisable(3042);
        GLES30.glBindVertexArray(0);
    }

    public final void release() {
        int[] ids = new int[1];
        if (this.program != 0) {
            GLES30.glDeleteProgram(this.program);
        }
        ids[0] = this.vbo;
        GLES30.glDeleteBuffers(1, ids, 0);
        ids[0] = this.vao;
        GLES30.glDeleteVertexArrays(1, ids, 0);
    }
}
