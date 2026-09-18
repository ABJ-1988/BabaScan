package com.retro.snake3d.gl;

import android.opengl.GLES30;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: GroundRenderer.kt */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0011\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0006\u0010\u001a\u001a\u00020\u001bJv\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u00032\u0006\u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020\t2\u0006\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020\t2\u0006\u0010%\u001a\u00020\t2\u0006\u0010&\u001a\u00020\u00032\u0006\u0010'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u00032\u0006\u0010)\u001a\u00020\u00032\u0006\u0010*\u001a\u00020\u0003J\u0006\u0010+\u001a\u00020\u001bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006,"}, d2 = {"Lcom/retro/snake3d/gl/GroundRenderer;", "", "extentX", "", "extentZ", "halfX", "halfZ", "(FFFF)V", "identity", "", "program", "", "uAccent", "uCamPos", "uFloor", "uFogColor", "uGrid", "uHalf", "uHead", "uModel", "uPulse", "uPulseOrigin", "uTime", "uViewProj", "vao", "vbo", "create", "", "draw", "viewProj", "camX", "camY", "camZ", "time", "grid", "floor", "accent", "fog", "headX", "headZ", "pulseX", "pulseZ", "pulse", "release", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes4.dex */
public final class GroundRenderer {
    private final float extentX;
    private final float extentZ;
    private final float halfX;
    private final float halfZ;
    private final float[] identity = {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private int program;
    private int uAccent;
    private int uCamPos;
    private int uFloor;
    private int uFogColor;
    private int uGrid;
    private int uHalf;
    private int uHead;
    private int uModel;
    private int uPulse;
    private int uPulseOrigin;
    private int uTime;
    private int uViewProj;
    private int vao;
    private int vbo;

    public GroundRenderer(float extentX, float extentZ, float halfX, float halfZ) {
        this.extentX = extentX;
        this.extentZ = extentZ;
        this.halfX = halfX;
        this.halfZ = halfZ;
    }

    public final void create() {
        this.program = GlUtil.INSTANCE.program(Shaders.GROUND_VS, Shaders.GROUND_FS);
        this.uViewProj = GLES30.glGetUniformLocation(this.program, "uViewProj");
        this.uModel = GLES30.glGetUniformLocation(this.program, "uModel");
        this.uCamPos = GLES30.glGetUniformLocation(this.program, "uCamPos");
        this.uTime = GLES30.glGetUniformLocation(this.program, "uTime");
        this.uHalf = GLES30.glGetUniformLocation(this.program, "uHalf");
        this.uGrid = GLES30.glGetUniformLocation(this.program, "uGrid");
        this.uFloor = GLES30.glGetUniformLocation(this.program, "uFloor");
        this.uAccent = GLES30.glGetUniformLocation(this.program, "uAccent");
        this.uFogColor = GLES30.glGetUniformLocation(this.program, "uFogColor");
        this.uHead = GLES30.glGetUniformLocation(this.program, "uHead");
        this.uPulseOrigin = GLES30.glGetUniformLocation(this.program, "uPulseOrigin");
        this.uPulse = GLES30.glGetUniformLocation(this.program, "uPulse");
        float ex = this.extentX;
        float ez = this.extentZ;
        float[] verts = {-ex, 0.0f, ez, ex, 0.0f, ez, ex, 0.0f, -ez, -ex, 0.0f, ez, ex, 0.0f, -ez, -ex, 0.0f, -ez};
        this.vao = GlUtil.INSTANCE.genVertexArray();
        GLES30.glBindVertexArray(this.vao);
        this.vbo = GlUtil.INSTANCE.genBuffer();
        GLES30.glBindBuffer(34962, this.vbo);
        GLES30.glBufferData(34962, verts.length * 4, GlUtil.INSTANCE.floatBuffer(verts), 35044);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 3, 5126, false, 12, 0);
        GLES30.glBindVertexArray(0);
        GLES30.glBindBuffer(34962, 0);
    }

    public final void draw(float[] viewProj, float camX, float camY, float camZ, float time, float[] grid, float[] floor, float[] accent, float[] fog, float headX, float headZ, float pulseX, float pulseZ, float pulse) {
        Intrinsics.checkNotNullParameter(viewProj, "viewProj");
        Intrinsics.checkNotNullParameter(grid, "grid");
        Intrinsics.checkNotNullParameter(floor, "floor");
        Intrinsics.checkNotNullParameter(accent, "accent");
        Intrinsics.checkNotNullParameter(fog, "fog");
        GLES30.glUseProgram(this.program);
        GLES30.glUniformMatrix4fv(this.uViewProj, 1, false, viewProj, 0);
        GLES30.glUniformMatrix4fv(this.uModel, 1, false, this.identity, 0);
        GLES30.glUniform3f(this.uCamPos, camX, camY, camZ);
        GLES30.glUniform1f(this.uTime, time);
        GLES30.glUniform2f(this.uHalf, this.halfX, this.halfZ);
        GLES30.glUniform3f(this.uGrid, grid[0], grid[1], grid[2]);
        GLES30.glUniform3f(this.uFloor, floor[0], floor[1], floor[2]);
        GLES30.glUniform3f(this.uAccent, accent[0], accent[1], accent[2]);
        GLES30.glUniform3f(this.uFogColor, fog[0], fog[1], fog[2]);
        GLES30.glUniform2f(this.uHead, headX, headZ);
        GLES30.glUniform2f(this.uPulseOrigin, pulseX, pulseZ);
        GLES30.glUniform1f(this.uPulse, pulse);
        GLES30.glBindVertexArray(this.vao);
        GLES30.glDrawArrays(4, 0, 6);
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
