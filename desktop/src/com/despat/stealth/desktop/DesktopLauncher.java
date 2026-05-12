package com.despat.stealth.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.Files;
import com.despat.stealth.UI.GameManager;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Stealth Escape";
        config.width = 1280;
        config.height = 720;
        config.resizable = true;
        config.addIcon("camera.png", Files.FileType.Internal);
        new LwjglApplication(new GameManager(), config);
    }
}
