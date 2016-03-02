package com.example.bogdan.opengl1.utils;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by bogdan on 02.03.2016.
 */
public class TextResourceReader {

    public static String readTextFileFromResource(Context context, int resourceId) {
        StringBuilder body = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append("\r\n");
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Невозможно открыть ресурс: " + resourceId, ioe);
        } catch (Resources.NotFoundException nfe) {
            throw new RuntimeException("Ресурс не найден: " + resourceId, nfe);
        }

        return body.toString();
    }
}
