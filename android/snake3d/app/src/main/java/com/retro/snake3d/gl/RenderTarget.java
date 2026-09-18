package com.retro.snake3d.gl;

import android.opengl.GLES30;
import kotlin.Metadata;

/* compiled from: GlUtil.kt */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0011R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\n¨\u0006\u0013"}, d2 = {"Lcom/retro/snake3d/gl/RenderTarget;", "", "width", "", "height", "withDepth", "", "(IIZ)V", "colorTex", "getColorTex", "()I", "depthRb", "fbo", "getFbo", "getHeight", "getWidth", "bind", "", "release", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes4.dex */
public final class RenderTarget {
    private final int colorTex;
    private final int depthRb;
    private final int fbo;
    private final int height;
    private final int width;

    public RenderTarget(int width, int height, boolean withDepth) {
        this.width = width;
        this.height = height;
        int[] ids = new int[1];
        GLES30.glGenTextures(1, ids, 0);
        this.colorTex = ids[0];
        GLES30.glBindTexture(3553, this.colorTex);
        GLES30.glTexImage2D(3553, 0, 32856, this.width, this.height, 0, 6408, 5121, null);
        GLES30.glTexParameteri(3553, 10241, 9729);
        GLES30.glTexParameteri(3553, 10240, 9729);
        GLES30.glTexParameteri(3553, 10242, 33071);
        GLES30.glTexParameteri(3553, 10243, 33071);
        GLES30.glGenFramebuffers(1, ids, 0);
        this.fbo = ids[0];
        GLES30.glBindFramebuffer(36160, this.fbo);
        GLES30.glFramebufferTexture2D(36160, 36064, 3553, this.colorTex, 0);
        if (withDepth) {
            GLES30.glGenRenderbuffers(1, ids, 0);
            this.depthRb = ids[0];
            GLES30.glBindRenderbuffer(36161, this.depthRb);
            GLES30.glRenderbufferStorage(36161, 33190, this.width, this.height);
            GLES30.glFramebufferRenderbuffer(36160, 36096, 36161, this.depthRb);
        } else {
            this.depthRb = 0;
        }
        int st = GLES30.glCheckFramebufferStatus(36160);
        GLES30.glBindFramebuffer(36160, 0);
        if (st == 36053) {
            return;
        }
        throw new RuntimeException("Framebuffer unvollständig: 0x" + Integer.toHexString(st));
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getFbo() {
        return this.fbo;
    }

    public final int getColorTex() {
        return this.colorTex;
    }

    public final void bind() {
        GLES30.glBindFramebuffer(36160, this.fbo);
        GLES30.glViewport(0, 0, this.width, this.height);
    }

    public final void release() {
        int[] ids = {this.fbo};
        GLES30.glDeleteFramebuffers(1, ids, 0);
        ids[0] = this.colorTex;
        GLES30.glDeleteTextures(1, ids, 0);
        if (this.depthRb != 0) {
            ids[0] = this.depthRb;
            GLES30.glDeleteRenderbuffers(1, ids, 0);
        }
    }
}
