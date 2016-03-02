package com.example.bogdan.opengl1.utils;

import android.nfc.Tag;
import android.util.Log;

import static android.opengl.GLES20.*;

/**
 * Created by bogdan on 02.03.2016.
 */
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);
        final int[] compileStatus = new int[1];

        if (shaderObjectId == 0) {
            if (LoggerConfig.ON)
                Log.w(TAG, "Невозможно создать новый шейдер.");

            return 0;
        }

        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        getCompileStatus(shaderObjectId, shaderCode);

        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId);

            if (LoggerConfig.ON) {
                Log.w(TAG, "Компиляция шейдера неудалась.");
            }

            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = glCreateProgram();
        final int[] linkStatus = new int[1];
        if (programObjectId == 0) {
            if (LoggerConfig.ON)
                Log.w(TAG, "Невозможно создать новую программу.");

            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        glLinkProgram(programObjectId);
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        if (LoggerConfig.ON)
            Log.v(TAG, "Результат линковки программы: \n" + glGetProgramInfoLog(programObjectId));

        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);

            if (LoggerConfig.ON)
                Log.w(TAG, "Линковка программы не удалась.");

            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        final int[] validateStatus = new int[1];

        glValidateProgram(programObjectId);
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);

        Log.v(TAG, "Результат проверки программы: " + validateStatus[0]
        + "\nLog: " + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }

    private static void getCompileStatus(int shaderObjectId, String shaderCode) {
        if (LoggerConfig.ON)
            Log.v(TAG, "Результат компиляции ресурса:" + '\n' + shaderCode + "\n:"
                    + glGetShaderInfoLog(shaderObjectId));
    }


}
