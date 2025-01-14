package com.skydesk.shared;

import java.io.IOException;

public class Desktop {

    public static void main(String[] args) throws Exception {
        String osName = System.getProperty("os.name").toLowerCase();
        String color = "000000"; // Black in hex for Windows, "black" for Linux

        if (osName.contains("linux")) {
            changeDesktopColorLinux(color);
            revertDesktopLinux();
        } else if (osName.contains("windows")) {
            changeDesktopColorWindows(color);
            revertDesktopWindows();
        } else {
            throw new UnsupportedOperationException("Unsupported OS");
        }
    }

    // Linux-specific methods
    public static void changeDesktopColorLinux(String color) throws Exception {
        String[] command1 = {"gsettings", "set", "org.gnome.desktop.background", "picture-options", "none"};
        Runtime.getRuntime().exec(command1).waitFor();
        String[] command2 = {"gsettings", "set", "org.gnome.desktop.background", "primary-color", color};
        Runtime.getRuntime().exec(command2).waitFor();
    }

    public static void revertDesktopLinux() throws IOException {
        String[] setColor = {"gsettings", "set", "org.gnome.desktop.background", "picture-options", "zoom"};
        Runtime.getRuntime().exec(setColor);
    }

    // Windows-specific methods
    public static void changeDesktopColorWindows(String hexColor) throws Exception {
        String regCommand1 = String.format("reg add \"HKEY_CURRENT_USER\\Control Panel\\Colors\" /v Background /t REG_SZ /d %s /f", hexToRgb(hexColor));
        Runtime.getRuntime().exec(regCommand1).waitFor();

        String regCommand2 = "reg add \"HKEY_CURRENT_USER\\Control Panel\\Desktop\" /v Wallpaper /t REG_SZ /d \"\" /f";
        Runtime.getRuntime().exec(regCommand2).waitFor();

        String refreshCommand = "RUNDLL32.EXE user32.dll, UpdatePerUserSystemParameters";
        Runtime.getRuntime().exec(refreshCommand);
    }

    public static void revertDesktopWindows() throws IOException, InterruptedException {
        String regCommand = "reg add \"HKEY_CURRENT_USER\\Control Panel\\Desktop\" /v Wallpaper /t REG_SZ /d \"C:\\Windows\\Web\\Wallpaper\\Windows\\img0.jpg\" /f";
        Runtime.getRuntime().exec(regCommand).waitFor();

        String refreshCommand = "RUNDLL32.EXE user32.dll, UpdatePerUserSystemParameters";
        Runtime.getRuntime().exec(refreshCommand);
    }

    private static String hexToRgb(String hexColor) {
        int r = Integer.parseInt(hexColor.substring(0, 2), 16);
        int g = Integer.parseInt(hexColor.substring(2, 4), 16);
        int b = Integer.parseInt(hexColor.substring(4, 6), 16);
        return r + " " + g + " " + b;
    }

}