package com.despat.stealth.utils;

public interface Updatable {
    void update(float deltaTime, boolean left, boolean right, boolean cameraButton, boolean up, boolean down);
}