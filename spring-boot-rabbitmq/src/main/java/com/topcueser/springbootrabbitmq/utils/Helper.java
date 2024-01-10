package com.topcueser.springbootrabbitmq.utils;

public class Helper {

    public static String getExtensionByFilename(String fileName) {
        if (fileName != null && fileName.lastIndexOf('.') >= 0) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        }
        return null;
    }
}
