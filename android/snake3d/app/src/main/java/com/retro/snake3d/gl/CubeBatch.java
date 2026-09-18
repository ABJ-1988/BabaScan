package com.retro.snake3d.gl;

import android.opengl.GLES30;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CubeBatch.kt */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0014\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0016\u0018\u0000 +2\u00020\u0001:\u0001+B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0012\u001a\u00020\u0013Jn\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020\u00162\u0006\u0010 \u001a\u00020\u00162\u0006\u0010!\u001a\u00020\u00162\u0006\u0010\"\u001a\u00020\u0016J\u0006\u0010#\u001a\u00020\u0013J.\u0010$\u001a\u00020\u00132\u0006\u0010%\u001a\u00020\t2\u0006\u0010&\u001a\u00020\u00162\u0006\u0010'\u001a\u00020\u00162\u0006\u0010(\u001a\u00020\u00162\u0006\u0010)\u001a\u00020\tJ\u0006\u0010*\u001a\u00020\u0013R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006,"}, d2 = {"Lcom/retro/snake3d/gl/CubeBatch;", "", "maxInstances", "", "(I)V", "buffer", "Ljava/nio/FloatBuffer;", "count", "data", "", "ebo", "instanceVbo", "program", "uCamPos", "uFogColor", "uViewProj", "vao", "vbo", "begin", "", "box", "x", "", "y", "z", "sx", "sy", "sz", "rotY", "r", "g", "b", "a", "emissive", "edge", "create", "flush", "viewProj", "camX", "camY", "camZ", "fog", "release", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes4.dex */
public final class CubeBatch {
    private static final int FLOATS_PER_INSTANCE = 24;
    private static final int[] INDICES;
    private static final int INSTANCE_STRIDE = 96;
    private final FloatBuffer buffer;
    private int count;
    private final float[] data;
    private int ebo;
    private int instanceVbo;
    private final int maxInstances;
    private int program;
    private int uCamPos;
    private int uFogColor;
    private int uViewProj;
    private int vao;
    private int vbo;
    private static final float[] VERTS = {-0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f};

    public CubeBatch() {
        this(0, 1, null);
    }

    public CubeBatch(int maxInstances) {
        this.maxInstances = maxInstances;
        this.data = new float[this.maxInstances * 24];
        this.buffer = GlUtil.INSTANCE.directFloats(this.maxInstances * 24);
    }

    public /* synthetic */ CubeBatch(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 3072 : i);
    }

    static {
        int[] idx = new int[36];
        for (int f = 0; f < 6; f++) {
            int b = f * 4;
            int o = f * 6;
            idx[o] = b;
            idx[o + 1] = b + 1;
            idx[o + 2] = b + 2;
            idx[o + 3] = b;
            idx[o + 4] = b + 2;
            idx[o + 5] = b + 3;
        }
        INDICES = idx;
    }

    public final void create() {
        this.program = GlUtil.INSTANCE.program(Shaders.CUBE_VS, Shaders.CUBE_FS);
        this.uViewProj = GLES30.glGetUniformLocation(this.program, "uViewProj");
        this.uCamPos = GLES30.glGetUniformLocation(this.program, "uCamPos");
        this.uFogColor = GLES30.glGetUniformLocation(this.program, "uFogColor");
        this.vao = GlUtil.INSTANCE.genVertexArray();
        GLES30.glBindVertexArray(this.vao);
        this.vbo = GlUtil.INSTANCE.genBuffer();
        GLES30.glBindBuffer(34962, this.vbo);
        FloatBuffer vb = GlUtil.INSTANCE.floatBuffer(VERTS);
        GLES30.glBufferData(34962, VERTS.length * 4, vb, 35044);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 3, 5126, false, 24, 0);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(1, 3, 5126, false, 24, 12);
        this.ebo = GlUtil.INSTANCE.genBuffer();
        GLES30.glBindBuffer(34963, this.ebo);
        IntBuffer ib = GlUtil.INSTANCE.intBuffer(INDICES);
        GLES30.glBufferData(34963, INDICES.length * 4, ib, 35044);
        this.instanceVbo = GlUtil.INSTANCE.genBuffer();
        GLES30.glBindBuffer(34962, this.instanceVbo);
        GLES30.glBufferData(34962, this.maxInstances * INSTANCE_STRIDE, null, 35048);
        for (int i = 0; i < 4; i++) {
            int loc = i + 2;
            GLES30.glEnableVertexAttribArray(loc);
            GLES30.glVertexAttribPointer(loc, 4, 5126, false, INSTANCE_STRIDE, i * 16);
            GLES30.glVertexAttribDivisor(loc, 1);
        }
        GLES30.glEnableVertexAttribArray(6);
        GLES30.glVertexAttribPointer(6, 4, 5126, false, INSTANCE_STRIDE, 64);
        GLES30.glVertexAttribDivisor(6, 1);
        GLES30.glEnableVertexAttribArray(7);
        GLES30.glVertexAttribPointer(7, 4, 5126, false, INSTANCE_STRIDE, 80);
        GLES30.glVertexAttribDivisor(7, 1);
        GLES30.glBindVertexArray(0);
        GLES30.glBindBuffer(34962, 0);
        GLES30.glBindBuffer(34963, 0);
    }

    public final void begin() {
        this.count = 0;
    }

    public final void box(float x, float y, float z, float sx, float sy, float sz, float rotY, float r, float g, float b, float a, float emissive, float edge) {
        if (this.count >= this.maxInstances) {
            return;
        }
        int o = this.count * 24;
        float c = (float) Math.cos(rotY);
        float s = (float) Math.sin(rotY);
        this.data[o] = c * sx;
        this.data[o + 1] = 0.0f;
        this.data[o + 2] = (-s) * sx;
        this.data[o + 3] = 0.0f;
        this.data[o + 4] = 0.0f;
        this.data[o + 5] = sy;
        this.data[o + 6] = 0.0f;
        this.data[o + 7] = 0.0f;
        this.data[o + 8] = s * sz;
        this.data[o + 9] = 0.0f;
        this.data[o + 10] = c * sz;
        this.data[o + 11] = 0.0f;
        this.data[o + 12] = x;
        this.data[o + 13] = y;
        this.data[o + 14] = z;
        this.data[o + 15] = 1.0f;
        this.data[o + 16] = r;
        this.data[o + 17] = g;
        this.data[o + 18] = b;
        this.data[o + 19] = a;
        this.data[o + 20] = emissive;
        this.data[o + 21] = 1.0f;
        this.data[o + 22] = edge;
        this.data[o + 23] = 0.0f;
        this.count++;
    }

    public final void flush(float[] viewProj, float camX, float camY, float camZ, float[] fog) {
        Intrinsics.checkNotNullParameter(viewProj, "viewProj");
        Intrinsics.checkNotNullParameter(fog, "fog");
        if (this.count == 0) {
            return;
        }
        GLES30.glUseProgram(this.program);
        GLES30.glUniformMatrix4fv(this.uViewProj, 1, false, viewProj, 0);
        GLES30.glUniform3f(this.uCamPos, camX, camY, camZ);
        GLES30.glUniform3f(this.uFogColor, fog[0], fog[1], fog[2]);
        GLES30.glBindVertexArray(this.vao);
        GLES30.glBindBuffer(34962, this.instanceVbo);
        this.buffer.position(0);
        this.buffer.put(this.data, 0, this.count * 24);
        this.buffer.position(0);
        GLES30.glBufferSubData(34962, 0, this.count * INSTANCE_STRIDE, this.buffer);
        GLES30.glDrawElementsInstanced(4, 36, 5125, 0, this.count);
        GLES30.glBindVertexArray(0);
        this.count = 0;
    }

    public final void release() {
        int[] ids = new int[1];
        if (this.program != 0) {
            GLES30.glDeleteProgram(this.program);
        }
        ids[0] = this.vbo;
        GLES30.glDeleteBuffers(1, ids, 0);
        ids[0] = this.ebo;
        GLES30.glDeleteBuffers(1, ids, 0);
        ids[0] = this.instanceVbo;
        GLES30.glDeleteBuffers(1, ids, 0);
        ids[0] = this.vao;
        GLES30.glDeleteVertexArrays(1, ids, 0);
    }
}
