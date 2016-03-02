package com.example.bogdan.opengl1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import com.example.bogdan.opengl1.utils.LoggerConfig;
import com.example.bogdan.opengl1.utils.ShaderHelper;
import com.example.bogdan.opengl1.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

/**
 * Created by bogdan on 01.03.2016.
 */
public class AirHockeyRenderer implements Renderer{
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private static final String A_COLOR = "a_Color";
    private int aColorLocation;
    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectionMatrix = new float[16];
    private int uMatrixLocation;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private Context context;
    private int program;

    private FloatBuffer vertexData;

    public AirHockeyRenderer(Context context) {
        this.context = context;

        float[] tableVerticesWithTriangles = {
                //fan triangle
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

                //center Line
                -0.5f, 0.0f, 1f, 0f, 0f,
                0.5f, 0.0f,1f, 0f, 0f,

                //two Mallets
                0.0f, -0.4f, 0f, 0f, 1f,
                0.0f, 0.4f, 1f, 0f, 0f,
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.vertex_shader);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON)
            ShaderHelper.validateProgram(program);

        glUseProgram(program);

        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        final float aspentRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if (width > height)
            //portrait projection
            orthoM(projectionMatrix, 0, -aspentRatio, aspentRatio, -1f, 1f, -1f, 1f);
        else
            //landscape projection
            orthoM(projectionMatrix, 0, -1f, 1f, -aspentRatio, aspentRatio, -1f, 1f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        glDrawArrays(GL_LINES, 6, 2);

        glDrawArrays(GL_POINTS, 8, 1);

        glDrawArrays(GL_POINTS, 9, 1);
    }
}
