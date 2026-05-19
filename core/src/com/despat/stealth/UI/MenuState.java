package com.despat.stealth.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.despat.stealth.utils.Collider;

public class MenuState extends State {

    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private BitmapFont controlsFont;
    private GlyphLayout layout;

    private ShapeRenderer shapeRenderer;

    // Button
    private float btnX, btnY, btnW, btnH;
    private boolean btnHovered = false;
    private Collider startButtonCollider;
    private Vector3 input;

    public MenuState(StateManager stateManager) {
        super(stateManager);
        worldCamera.setToOrtho(false);

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        titleFont = new BitmapFont();
        titleFont.getData().setScale(4.5f);
        titleFont.setColor(new Color(0.95f, 0.85f, 0.2f, 1f)); // gold

        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(2.2f);
        buttonFont.setColor(Color.WHITE);

        controlsFont = new BitmapFont();
        controlsFont.getData().setScale(1.4f);
        controlsFont.setColor(new Color(0.85f, 0.85f, 0.85f, 1f));

        float W = worldCamera.viewportWidth;
        float H = worldCamera.viewportHeight;

        btnW = 220;
        btnH = 55;
        btnX = W / 2f - btnW / 2f;
        btnY = H / 2f - 60;

        startButtonCollider = new Collider(new Vector2(btnX, btnY), btnW, btnH);
    }

    @Override
    public void handleInput() {
        input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCamera.unproject(input);

        btnHovered = startButtonCollider.CheckCollisionRectangle(new Vector2(input.x, input.y));

        if (Gdx.input.justTouched() && btnHovered) {
            stateManager.set(new FirstLevel(stateManager));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        float W = worldCamera.viewportWidth;
        float H = worldCamera.viewportHeight;

        // Dark background
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(worldCamera.combined);

        // Subtle top gradient line accent
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.95f, 0.85f, 0.2f, 1f));
        shapeRenderer.rect(0, H - 4, W, 4);
        shapeRenderer.setColor(new Color(0.95f, 0.85f, 0.2f, 0.3f));
        shapeRenderer.rect(0, H - 8, W, 4);
        shapeRenderer.end();

        // Draw button background
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (btnHovered) {
            shapeRenderer.setColor(new Color(0.95f, 0.85f, 0.2f, 1f));
        } else {
            shapeRenderer.setColor(new Color(0.2f, 0.2f, 0.28f, 1f));
        }
        shapeRenderer.rect(btnX, btnY, btnW, btnH);
        shapeRenderer.end();

        // Button border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0.95f, 0.85f, 0.2f, 1f));
        shapeRenderer.rect(btnX, btnY, btnW, btnH);
        shapeRenderer.end();

        // Controls panel background (left side)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.12f, 0.12f, 0.18f, 1f));
        shapeRenderer.rect(20, H / 2f - 120, 260, 230);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0.4f, 0.4f, 0.5f, 1f));
        shapeRenderer.rect(20, H / 2f - 120, 260, 230);
        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(worldCamera.combined);
        spriteBatch.begin();

        // Title
        String title = "Stealth Escape";
        layout.setText(titleFont, title);
        titleFont.draw(spriteBatch, title,
                W / 2f - layout.width / 2f,
                H / 2f + 130);

        // Button text
        String btnText = btnHovered ? "> START <" : "  START  ";
        layout.setText(buttonFont, btnText);
        if (btnHovered) {
            buttonFont.setColor(new Color(0.1f, 0.1f, 0.1f, 1f));
        } else {
            buttonFont.setColor(Color.WHITE);
        }
        buttonFont.draw(spriteBatch, btnText,
                btnX + btnW / 2f - layout.width / 2f,
                btnY + btnH / 2f + layout.height / 2f);

        // Controls header
        BitmapFont headerFont = new BitmapFont();
        headerFont.getData().setScale(1.6f);
        headerFont.setColor(new Color(0.95f, 0.85f, 0.2f, 1f));
        headerFont.draw(spriteBatch, "Controls", 30, H / 2f + 95);
        headerFont.dispose();

        // Controls text
        String[] keys   = { "A / \u2190",  "D / \u2192",  "W / \u2191",  "S / \u2193",  "H",       "P / Esc" };
        String[] actions= { "Move left", "Move right", "Climb up", "Climb down", "Hide", "Pause" };
        float cy = H / 2f + 68;
        for (int i = 0; i < keys.length; i++) {
            controlsFont.setColor(new Color(0.95f, 0.85f, 0.2f, 1f));
            controlsFont.draw(spriteBatch, keys[i], 30, cy);
            controlsFont.setColor(new Color(0.85f, 0.85f, 0.85f, 1f));
            controlsFont.draw(spriteBatch, actions[i], 130, cy);
            cy -= 30;
        }

        // ESC hint bottom
        controlsFont.setColor(new Color(0.5f, 0.5f, 0.5f, 1f));
        layout.setText(controlsFont, "Press ESC to quit");
        controlsFont.draw(spriteBatch, "Press ESC to quit",
                W / 2f - layout.width / 2f, 25);

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        titleFont.dispose();
        buttonFont.dispose();
        controlsFont.dispose();
        shapeRenderer.dispose();
    }
}
