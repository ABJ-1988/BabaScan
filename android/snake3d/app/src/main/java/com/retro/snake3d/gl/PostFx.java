package com.retro.snake3d.gl;

import android.opengl.GLES30;
import kotlin.Metadata;

/* compiled from: PostFx.kt */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0019\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0010\u0007\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010 \u001a\u00020!J \u0010\"\u001a\u00020!2\u0006\u0010#\u001a\u00020\u00072\u0006\u0010$\u001a\u00020\u00072\u0006\u0010%\u001a\u00020\u0007H\u0002J\u0006\u0010&\u001a\u00020!J\b\u0010'\u001a\u00020!H\u0002J\u0006\u0010(\u001a\u00020!J\b\u0010)\u001a\u00020!H\u0002J\u0016\u0010*\u001a\u00020!2\u0006\u0010+\u001a\u00020\u00072\u0006\u0010,\u001a\u00020\u0007JV\u0010-\u001a\u00020!2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020/2\u0006\u00101\u001a\u00020/2\u0006\u00102\u001a\u00020/2\u0006\u00103\u001a\u00020/2\u0006\u00104\u001a\u00020/2\u0006\u00105\u001a\u00020/2\u0006\u00106\u001a\u00020/2\u0006\u00107\u001a\u00020/2\u0006\u00108\u001a\u00020/R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\r\u001a\u0004\u0018\u00010\u00042\b\u0010\f\u001a\u0004\u0018\u00010\u0004@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u00069"}, d2 = {"Lcom/retro/snake3d/gl/PostFx;", "", "()V", "bloomA", "Lcom/retro/snake3d/gl/RenderTarget;", "bloomB", "emptyVao", "", "height", "progBlur", "progBright", "progComposite", "<set-?>", "scene", "getScene", "()Lcom/retro/snake3d/gl/RenderTarget;", "uBlurDir", "uBlurTex", "uBrightTex", "uBrightThreshold", "uCompAberration", "uCompBloom", "uCompBloomAmount", "uCompCurve", "uCompDesat", "uCompFlash", "uCompFlashColor", "uCompRes", "uCompScanline", "uCompScene", "uCompTime", "width", "beginScene", "", "bindTex", "unit", "tex", "uniform", "create", "fullscreen", "release", "releaseTargets", "resize", "w", "h", "resolve", "time", "", "bloomAmount", "aberration", "curve", "scanline", "flash", "flashR", "flashG", "flashB", "desat", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes4.dex */
public final class PostFx {
    private RenderTarget bloomA;
    private RenderTarget bloomB;
    private int emptyVao;
    private int height;
    private int progBlur;
    private int progBright;
    private int progComposite;
    private RenderTarget scene;
    private int uBlurDir;
    private int uBlurTex;
    private int uBrightTex;
    private int uBrightThreshold;
    private int uCompAberration;
    private int uCompBloom;
    private int uCompBloomAmount;
    private int uCompCurve;
    private int uCompDesat;
    private int uCompFlash;
    private int uCompFlashColor;
    private int uCompRes;
    private int uCompScanline;
    private int uCompScene;
    private int uCompTime;
    private int width;

    public final RenderTarget getScene() {
        return this.scene;
    }

    public final void create() {
        this.progBright = GlUtil.INSTANCE.program(Shaders.FULLSCREEN_VS, Shaders.BRIGHT_FS);
        this.uBrightTex = GLES30.glGetUniformLocation(this.progBright, "uTex");
        this.uBrightThreshold = GLES30.glGetUniformLocation(this.progBright, "uThreshold");
        this.progBlur = GlUtil.INSTANCE.program(Shaders.FULLSCREEN_VS, Shaders.BLUR_FS);
        this.uBlurTex = GLES30.glGetUniformLocation(this.progBlur, "uTex");
        this.uBlurDir = GLES30.glGetUniformLocation(this.progBlur, "uDir");
        this.progComposite = GlUtil.INSTANCE.program(Shaders.FULLSCREEN_VS, Shaders.COMPOSITE_FS);
        this.uCompScene = GLES30.glGetUniformLocation(this.progComposite, "uScene");
        this.uCompBloom = GLES30.glGetUniformLocation(this.progComposite, "uBloom");
        this.uCompRes = GLES30.glGetUniformLocation(this.progComposite, "uRes");
        this.uCompTime = GLES30.glGetUniformLocation(this.progComposite, "uTime");
        this.uCompBloomAmount = GLES30.glGetUniformLocation(this.progComposite, "uBloomAmount");
        this.uCompAberration = GLES30.glGetUniformLocation(this.progComposite, "uAberration");
        this.uCompCurve = GLES30.glGetUniformLocation(this.progComposite, "uCurve");
        this.uCompScanline = GLES30.glGetUniformLocation(this.progComposite, "uScanline");
        this.uCompFlash = GLES30.glGetUniformLocation(this.progComposite, "uFlash");
        this.uCompFlashColor = GLES30.glGetUniformLocation(this.progComposite, "uFlashColor");
        this.uCompDesat = GLES30.glGetUniformLocation(this.progComposite, "uDesat");
        this.emptyVao = GlUtil.INSTANCE.genVertexArray();
    }

    public final void resize(int w, int h) {
        if (w == this.width && h == this.height) {
            return;
        }
        releaseTargets();
        this.width = w;
        this.height = h;
        int bw = Math.max(1, w / 4);
        int bh = Math.max(1, h / 4);
        this.scene = new RenderTarget(w, h, true);
        this.bloomA = new RenderTarget(bw, bh, false);
        this.bloomB = new RenderTarget(bw, bh, false);
    }

    public final void beginScene() {
        RenderTarget renderTarget = this.scene;
        if (renderTarget != null) {
            renderTarget.bind();
        }
    }

    private final void fullscreen() {
        GLES30.glBindVertexArray(this.emptyVao);
        GLES30.glDrawArrays(4, 0, 3);
        GLES30.glBindVertexArray(0);
    }

    private final void bindTex(int unit, int tex, int uniform) {
        GLES30.glActiveTexture(33984 + unit);
        GLES30.glBindTexture(3553, tex);
        GLES30.glUniform1i(uniform, unit);
    }

    public final void resolve(float time, float bloomAmount, float aberration, float curve, float scanline, float flash, float flashR, float flashG, float flashB, float desat) {
        RenderTarget a;
        RenderTarget b;
        RenderTarget s = this.scene;
        if (s == null || (a = this.bloomA) == null || (b = this.bloomB) == null) {
            return;
        }
        GLES30.glDisable(2929);
        GLES30.glDisable(3042);
        GLES30.glDisable(2884);
        a.bind();
        GLES30.glUseProgram(this.progBright);
        GLES30.glUniform1f(this.uBrightThreshold, 0.55f);
        bindTex(0, s.getColorTex(), this.uBrightTex);
        fullscreen();
        b.bind();
        GLES30.glUseProgram(this.progBlur);
        GLES30.glUniform2f(this.uBlurDir, 1.0f / a.getWidth(), 0.0f);
        bindTex(0, a.getColorTex(), this.uBlurTex);
        fullscreen();
        a.bind();
        GLES30.glUniform2f(this.uBlurDir, 0.0f, 1.0f / b.getHeight());
        bindTex(0, b.getColorTex(), this.uBlurTex);
        fullscreen();
        b.bind();
        GLES30.glUniform2f(this.uBlurDir, 2.4f / a.getWidth(), 0.0f);
        bindTex(0, a.getColorTex(), this.uBlurTex);
        fullscreen();
        a.bind();
        GLES30.glUniform2f(this.uBlurDir, 0.0f, 2.4f / b.getHeight());
        bindTex(0, b.getColorTex(), this.uBlurTex);
        fullscreen();
        GLES30.glBindFramebuffer(36160, 0);
        GLES30.glViewport(0, 0, this.width, this.height);
        GLES30.glUseProgram(this.progComposite);
        bindTex(0, s.getColorTex(), this.uCompScene);
        bindTex(1, a.getColorTex(), this.uCompBloom);
        GLES30.glUniform2f(this.uCompRes, this.width, this.height);
        GLES30.glUniform1f(this.uCompTime, time);
        GLES30.glUniform1f(this.uCompBloomAmount, bloomAmount);
        GLES30.glUniform1f(this.uCompAberration, aberration);
        GLES30.glUniform1f(this.uCompCurve, curve);
        GLES30.glUniform1f(this.uCompScanline, scanline);
        GLES30.glUniform1f(this.uCompFlash, flash);
        GLES30.glUniform3f(this.uCompFlashColor, flashR, flashG, flashB);
        GLES30.glUniform1f(this.uCompDesat, desat);
        fullscreen();
        GLES30.glEnable(2929);
    }

    private final void releaseTargets() {
        RenderTarget renderTarget = this.scene;
        if (renderTarget != null) {
            renderTarget.release();
        }
        this.scene = null;
        RenderTarget renderTarget2 = this.bloomA;
        if (renderTarget2 != null) {
            renderTarget2.release();
        }
        this.bloomA = null;
        RenderTarget renderTarget3 = this.bloomB;
        if (renderTarget3 != null) {
            renderTarget3.release();
        }
        this.bloomB = null;
    }

    public final void release() {
        releaseTargets();
        if (this.progBright != 0) {
            GLES30.glDeleteProgram(this.progBright);
        }
        if (this.progBlur != 0) {
            GLES30.glDeleteProgram(this.progBlur);
        }
        if (this.progComposite != 0) {
            GLES30.glDeleteProgram(this.progComposite);
        }
        int[] ids = {this.emptyVao};
        GLES30.glDeleteVertexArrays(1, ids, 0);
    }
}
