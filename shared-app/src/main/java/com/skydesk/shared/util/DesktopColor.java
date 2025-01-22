package com.skydesk.shared.util;

import java.io.IOException;

public class DesktopColor {

    public static void changeDesktopColor(String color) throws Exception {
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            String[] command1 = {"gsettings", "set", "org.gnome.desktop.background", "picture-options", "none"};
            Runtime.getRuntime().exec(command1).waitFor();
            String[] command2 = {"gsettings", "set", "org.gnome.desktop.background", "primary-color", "%s".formatted(color)};
            Runtime.getRuntime().exec(command1).waitFor();
            Runtime.getRuntime().exec(command2);
        } else {
            throw new UnsupportedOperationException("Unsupported OS");
        }
    }

    public static void revertDesktop() throws IOException {
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            String[] setColor = {"gsettings", "set", "org.gnome.desktop.background", "picture-options", "zoom"};
            Runtime.getRuntime().exec(setColor);
        } else {
            throw new UnsupportedOperationException("Unsupported OS");
        }
    }
}
