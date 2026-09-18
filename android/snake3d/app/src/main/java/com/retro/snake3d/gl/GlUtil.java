package com.retro.snake3d.gl;

import android.opengl.GLES30;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: GlUtil.kt */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0014\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0015\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tJ\u000e\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\tJ\u0006\u0010\u0013\u001a\u00020\tJ\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0016J\u0016\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/retro/snake3d/gl/GlUtil;", "", "()V", "TAG", "", "checkError", "", "tag", "compile", "", "type", "src", "directFloats", "Ljava/nio/FloatBuffer;", "count", "floatBuffer", "data", "", "genBuffer", "genVertexArray", "intBuffer", "Ljava/nio/IntBuffer;", "", "program", "vs", "fs", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes4.dex */
public final class GlUtil {
    public static final GlUtil INSTANCE = new GlUtil();
    private static final String TAG = "Snake3D/GL";

    private GlUtil() {
    }

    public final int compile(int type, String src) {
        Intrinsics.checkNotNullParameter(src, "src");
        int id = GLES30.glCreateShader(type);
        GLES30.glShaderSource(id, src);
        GLES30.glCompileShader(id);
        int[] status = new int[1];
        GLES30.glGetShaderiv(id, 35713, status, 0);
        if (status[0] == 0) {
            String log = GLES30.glGetShaderInfoLog(id);
            GLES30.glDeleteShader(id);
            throw new RuntimeException("Shader-Fehler (" + type + "): " + log);
        }
        return id;
    }

    public final int program(String vs, String fs) {
        Intrinsics.checkNotNullParameter(vs, "vs");
        Intrinsics.checkNotNullParameter(fs, "fs");
        int v = compile(35633, vs);
        int f = compile(35632, fs);
        int p = GLES30.glCreateProgram();
        GLES30.glAttachShader(p, v);
        GLES30.glAttachShader(p, f);
        GLES30.glLinkProgram(p);
        int[] status = new int[1];
        GLES30.glGetProgramiv(p, 35714, status, 0);
        GLES30.glDeleteShader(v);
        GLES30.glDeleteShader(f);
        if (status[0] == 0) {
            String log = GLES30.glGetProgramInfoLog(p);
            GLES30.glDeleteProgram(p);
            throw new RuntimeException("Link-Fehler: " + log);
        }
        return p;
    }

    public final FloatBuffer floatBuffer(float[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        FloatBuffer $this$floatBuffer_u24lambda_u240 = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        $this$floatBuffer_u24lambda_u240.put(data);
        $this$floatBuffer_u24lambda_u240.position(0);
        Intrinsics.checkNotNullExpressionValue($this$floatBuffer_u24lambda_u240, "apply(...)");
        return $this$floatBuffer_u24lambda_u240;
    }

    public final IntBuffer intBuffer(int[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        IntBuffer $this$intBuffer_u24lambda_u241 = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        $this$intBuffer_u24lambda_u241.put(data);
        $this$intBuffer_u24lambda_u241.position(0);
        Intrinsics.checkNotNullExpressionValue($this$intBuffer_u24lambda_u241, "apply(...)");
        return $this$intBuffer_u24lambda_u241;
    }

    public final FloatBuffer directFloats(int count) {
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(count * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        Intrinsics.checkNotNullExpressionValue(asFloatBuffer, "asFloatBuffer(...)");
        return asFloatBuffer;
    }

    public final int genBuffer() {
        int[] a = new int[1];
        GLES30.glGenBuffers(1, a, 0);
        return a[0];
    }

    public final int genVertexArray() {
        int[] a = new int[1];
        GLES30.glGenVertexArrays(1, a, 0);
        return a[0];
    }

    public final void checkError(String tag) {
        Intrinsics.checkNotNullParameter(tag, "tag");
        for (int e = GLES30.glGetError(); e != 0; e = GLES30.glGetError()) {
            Log.e(TAG, "GL-Fehler bei " + tag + ": 0x" + Integer.toHexString(e));
        }
    }
}
