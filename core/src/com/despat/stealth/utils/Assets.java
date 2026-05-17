package com.despat.stealth.utils;

import com.badlogic.gdx.graphics.Texture;

public class Assets {

    public Texture sewer1, sewer2;
    public Texture trap;
    public Texture woodTexture;
    public Texture soundWaveTexture;
    public Texture floorTexture;
    public Texture wallTexture;
    public Texture background;
    public Texture startBackground;
    public Texture stairs;

    public void Load() {
        woodTexture       = new Texture("TouchTexture.png");
        soundWaveTexture  = new Texture("Touch.png");
        floorTexture      = new Texture("floor.png");
        wallTexture       = new Texture("wall.png");
        background        = new Texture("background3.png");
        startBackground   = new Texture("fallTexture.png");
        stairs            = new Texture("Stairs.png");
        sewer1            = new Texture("WinTextureBack.png");
        sewer2            = new Texture("WinTextureFront.png");
        trap              = new Texture("trap.png");
    }

    public void dispose() {
        woodTexture.dispose();
        soundWaveTexture.dispose();
        floorTexture.dispose();
        wallTexture.dispose();
        background.dispose();
        startBackground.dispose();
        stairs.dispose();
        sewer1.dispose();
        sewer2.dispose();
        trap.dispose();
    }
}
