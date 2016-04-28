package com.niksoft.android.cheatdatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Nikunj Kacha on 17/04/16.
 */
public class Utils {

    public static void writeFromInputToOutput(InputStream inputStream, OutputStream outputStream) throws IOException {

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        outputStream.flush();
        inputStream.close();
    }
}
