package com.despat.stealth.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class WinState extends State {

    private BitmapFont titleFont;
    private BitmapFont subFont;
    private BitmapFont hintFont;
    private GlyphLayout layout;
    private ShapeRenderer shapeRenderer;
    private float time = 0;

    public WinState(StateManager stateManager) {
        super(stateManager);
        worldCamera.setToOrtho(false);

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        titleFont = new BitmapFont();
        titleFont.getData().setScale(5f);
        titleFont.setColor(new Color(0.95f, 0.85f, 0.2f, 1f));

        subFont = new BitmapFont();
        subFont.getData().setScale(2f);
        subFont.setColor(Color.WHITE);

        hintFont = new BitmapFont();
        hintFont.getData().setScale(1.4f);
        hintFont.setColor(new Color(0.6f, 0.6f, 0.6f, 1f));

    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()
                || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void update(float deltaTime) {
        time += deltaTime;
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        float W = worldCamera.viewportWidth;
        float H = worldCamera.viewportHeight;

        // Deep dark background
        Gdx.gl.glClearColor(0.05f, 0.07f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(worldCamera.combined);

        // Pulsing gold accent lines
        float pulse = 0.6f + 0.4f * (float)Math.sin(time * 2.5f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.95f, 0.85f, 0.2f, pulse));
        shapeRenderer.rect(0, H - 5, W, 5);
        shapeRenderer.rect(0, 0, W, 5);
        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(worldCamera.combined);
        spriteBatch.begin();

        // "You Escaped!" title
        String title = "You Escaped!";
        layout.setText(titleFont, title);
        titleFont.draw(spriteBatch, title, W / 2f - layout.width / 2f, H / 2f + 90);

        // Subtitle
        String sub = "All keys collected. Mission complete.";
        layout.setText(subFont, sub);
        subFont.draw(spriteBatch, sub, W / 2f - layout.width / 2f, H / 2f + 10);

        // Hint
        String hint = "Press Enter or click to exit";
        layout.setText(hintFont, hint);
        hintFont.draw(spriteBatch, hint, W / 2f - layout.width / 2f, H / 2f - 50);

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        titleFont.dispose();
        subFont.dispose();
        hintFont.dispose();
        shapeRenderer.dispose();
    }
}
