package com.despat.stealth.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Renderable {
    void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer);
}