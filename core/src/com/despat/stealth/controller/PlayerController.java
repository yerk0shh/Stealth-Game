package com.despat.stealth.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.despat.stealth.core.Player;

public class PlayerController {

    boolean pressLeft;
    boolean pressRight;
    boolean pressUp;
    boolean pressDown;
    boolean pressCameraButton;
    boolean upDownVisible;

    private Player player;
    private final Vector3 currentAccelerometerValues = new Vector3(0, 0, 0);

    public PlayerController(Texture rightN, Texture rightP, Texture leftN, Texture leftP,
                            Texture upN, Texture upP, Texture downN, Texture downP,
                            Texture cameraTexture, OrthographicCamera camera, Player player) {
        this.player = player;
    }

    public boolean isPressLeft()         { return pressLeft; }
    public boolean isPressRight()        { return pressRight; }
    public boolean isPressUp()           { return pressUp; }
    public boolean isPressDown()         { return pressDown; }
    public boolean isPressCameraButton() { return pressCameraButton; }
    public boolean isUpDownVisible()     { return upDownVisible; }
    public void setUpDownVisible(boolean v) { this.upDownVisible = v; }
    public Vector3 getCurrentAccelerometerValues() { return currentAccelerometerValues; }

    // In original: isLaydown() used accelerometer for hiding on mobile.
    // On desktop H key handles hiding directly in Player.update, so always return false here.
    public boolean isLaydown() { return false; }

    public void update(float deltaTime, OrthographicCamera camera) {
        boolean keyLeft  = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean keyRight = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean keyUp    = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean keyDown  = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (!upDownVisible) {
            pressUp   = false;
            pressDown = false;
        }

        if (keyLeft && player.playerAction != Player.PlayerActions.FALLING) {
            player.playerAction = Player.PlayerActions.WALKING;
            pressLeft  = true;
            pressRight = false;
        } else if (keyRight && player.playerAction != Player.PlayerActions.FALLING) {
            player.playerAction = Player.PlayerActions.WALKING;
            pressLeft  = false;
            pressRight = true;
        } else {
            // Exact copy of original else branch: reset to IDLE only if not on stairs
            if (player.playerAction != Player.PlayerActions.STAIRS)
                player.playerAction = Player.PlayerActions.IDLE;

            pressLeft  = false;
            pressRight = false;
            pressCameraButton = false;
        }

        if (upDownVisible && keyUp)   { pressUp = true;  pressDown = false; }
        if (upDownVisible && keyDown) { pressDown = true; pressUp = false; }
        if (!keyUp && !keyDown)       { pressUp = false;  pressDown = false; }
    }

    public void draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        // no on-screen buttons on desktop
    }
}
