package com.despat.stealth.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.despat.stealth.controller.PlayerController;
import com.despat.stealth.core.Camera;
import com.despat.stealth.core.Checkpoint;
import com.despat.stealth.core.Guard;
import com.despat.stealth.core.Item;
import com.despat.stealth.core.Switch;
import com.despat.stealth.core.Trap;
import com.despat.stealth.core.Laser;
import com.despat.stealth.core.Platform;
import com.despat.stealth.core.Player;
import com.despat.stealth.core.Stair;
import com.despat.stealth.core.TouchBlock;
import com.despat.stealth.utils.Assets;
import com.despat.stealth.utils.Collider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Main game screen / level
public class FirstLevel extends State {

    private Player player;
    private ShapeRenderer shapeRenderer;
    private ShapeRenderer interfaceShapeRenderer;
    private SpriteBatch interfaceSpriteBatch;

    private Assets gameAssets;
    private PlayerController playerController;
    private TouchBlock touchBlock;

    private List<Platform> platforms = new ArrayList<Platform>();
    private List<Platform> walls = new ArrayList<Platform>();

    private float peekAngleX, peekAngleY;
    private Sprite background1, fallBackground;
    private Vector2 positionBG1, positionBG2, positionBG3, positionBG4, backgroundScale;

    float wallWidth, wallHeight, groundWidth, groundHeight;
    Stair stair1, stair2, stair3;

    boolean firstTime;

    Guard guard, guard2;
    Laser laser, laser2, laser3;

    private List<Guard> guards = new ArrayList<Guard>();
    private List<Laser> lasers = new ArrayList<Laser>();
    private List<Stair> stairs = new ArrayList<Stair>();
    private List<Trap> traps = new ArrayList<Trap>();
    private List<Item> items = new ArrayList<Item>();

    private Checkpoint checkpoint1, checkpoint2;
    private Vector3 currentCheckpoint = new Vector3(0, 0, 0);
    private List<Checkpoint> checkpoints = new ArrayList<Checkpoint>();

    private Camera cameraSearch, cameraSearch2;

    private Laser specialLaser;
    private Vector3 input;
    private Collider backgroundCollider;

    private Item item;
    private Collider finishCollider;

    private int itemTotal = 0;
    private int itemsFound = 0;
    private boolean canWin = false;
    private GlyphLayout glyphLayout;
    private BitmapFont font;
    private String message = "";

    private Switch switchLaser;

    private TextureAtlas exitAtlas;
    private Animation<TextureRegion> exitAnimation;
    private Vector2 exitPosition;
    private Collider exitCollider;

    private float startCountdown = 0;
    private float endingCountdown = 0;
    private boolean beginCountdown = false;

    enum WindowState {
        OPEN,
        CLOSED
    }

    private WindowState windowState;

    public FirstLevel(StateManager gsm) {
        super(gsm);

        font = new BitmapFont();
        font.setColor(Color.PINK);
        font.getData().setScale(3);
        glyphLayout = new GlyphLayout();

        shapeRenderer = new ShapeRenderer();
        interfaceShapeRenderer = new ShapeRenderer();
        interfaceSpriteBatch = new SpriteBatch();

        worldCamera.setToOrtho(false);
        interfaceCamera.setToOrtho(false);
        interfaceCamera.position.set(0, 0, 0);
        worldCamera.viewportHeight *= 0.5f;
        worldCamera.viewportWidth *= 0.5f;

        gameAssets = new Assets();
        gameAssets.Load();

        LoadLevelAssets();

        for (Item i : items) {
            itemTotal++;
        }
        message = itemsFound + "/" + itemTotal;

        player = new Player(-150, 500, platforms, walls);
        playerController = new PlayerController(
                null, null, null, null,
                null, null, null, null,
                null, interfaceCamera, player);


        firstTime = true;

        guard = new Guard(new Vector2(1420, 250), true);
        guard2 = new Guard(new Vector2(500, 450), false);
        guards.add(guard);
        guards.add(guard2);

        checkpoint1 = new Checkpoint(new Vector3(80, 80, 0));
        checkpoints.add(checkpoint1);
        currentCheckpoint = checkpoint1.getPosition();
        checkpoint2 = new Checkpoint(new Vector3(890, 80, 0));
        checkpoints.add(checkpoint2);
    }

    @Override
    public void handleInput() {
        // keyboard/mouse input handled in update
    }

    @Override
    public void update(float deltaTime) {
        if (firstTime) {
            worldCamera.position.x = -200;
            worldCamera.position.y = -200;
            worldCamera.update();

            startCountdown += deltaTime;
            if (startCountdown >= 4) {
                firstTime = false;
            }
        } else {
            input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            worldCamera.unproject(input);

            if (player.playerAction != Player.PlayerActions.DYING
                    && player.playerAction != Player.PlayerActions.HIDING
                    && player.playerAction != Player.PlayerActions.UNHIDING) {
                playerController.update(deltaTime, interfaceCamera);
            }

            player.update(deltaTime,
                    playerController.isPressLeft(),
                    playerController.isPressRight(),
                    playerController.isPressCameraButton(),
                    playerController.isPressUp(),
                    playerController.isPressDown());

            for (Stair s : stairs) {
                if (player.checkStairsCollision(s)) {
                    playerController.setUpDownVisible(true);
                    break;
                } else {
                    playerController.setUpDownVisible(false);
                }
            }

            if (playerController.isPressCameraButton()) {
                player.canHide = false;
                peekAngleX = playerController.getCurrentAccelerometerValues().y;
                peekAngleY = playerController.getCurrentAccelerometerValues().x;
                worldCamera.position.x = player.getPosition().x + player.getPlayerWidth() / 2 + (int) peekAngleX * 3;
                worldCamera.position.y = player.getPosition().y + player.getPlayerHeight() / 2 + 140 - (int) peekAngleY * 3;
            } else {
                player.canHide = playerController.isLaydown();
                peekAngleX = 0;
                peekAngleY = 0;
                worldCamera.position.x = player.getPosition().x + player.getPlayerWidth() / 2;
                worldCamera.position.y = player.getPosition().y + player.getPlayerHeight() / 2;
            }

            worldCamera.update();
            interfaceCamera.update();

            touchBlock.Update(deltaTime, worldCamera);

            if (player.playerAction != Player.PlayerActions.DYING) {
                for (Guard g : guards) {
                    g.setSoundwaves(touchBlock.getSoundwaves());
                    g.CheckCollisionWithPlayer(player);
                    if (g.isRegularGuard())
                        g.Update(deltaTime, worldCamera);
                    else
                        g.Update2(deltaTime);
                }
            }

            for (Laser l : lasers) {
                l.Update(deltaTime);
                l.checkCollision(player);
            }

            for (Iterator<Item> iterator = items.iterator(); iterator.hasNext();) {
                Item i = iterator.next();
                if (i.getCollider().getRectangle().overlaps(player.getPlayerCollider().getRectangle())) {
                    iterator.remove();
                    itemsFound++;
                    message = itemsFound + "/" + itemTotal;
                    if (itemsFound >= itemTotal) {
                        canWin = true;
                        windowState = WindowState.OPEN;
                    }
                    break;
                }
            }

            cameraSearch.Update(deltaTime);
            cameraSearch.CheckCollisionWithPlayer(player);
            cameraSearch2.Update(deltaTime);
            cameraSearch2.CheckCollisionWithPlayer(player);

            for (Checkpoint check : checkpoints) {
                if (check.getCollider().CheckCollisionRectangle(player.getPlayerCollider().getRectangle()))
                    currentCheckpoint = check.getPosition();
            }

            if (player.playerAction == Player.PlayerActions.DYING) {
                specialLaser.laserActions = Laser.LaserActions.ON;
            }
            switchLaser.Update(worldCamera, player, specialLaser);

            // Pause with P or Escape
            if (Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                stateManager.push(new PauseState(stateManager));
            }

            if (player.isDead()) {
                stateManager.push(new GameOverState(stateManager, currentCheckpoint, player, guards));
            }

            if (exitCollider.getRectangle().contains(player.getNextPosition().x, player.getPosition().y) && !canWin) {
                player.setPosition(new Vector3(player.getPosition().sub(player.getVelocity().x * deltaTime, 0, 0)));
            }

            if (finishCollider.getRectangle().overlaps(player.getPlayerCollider().getRectangle()) && canWin) {
                beginCountdown = true;
            }

            if (beginCountdown) {
                endingCountdown += deltaTime;
                if (endingCountdown >= 6.7f) {
                    stateManager.set(new WinState(stateManager));
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(worldCamera.combined);
        shapeRenderer.setProjectionMatrix(worldCamera.combined);
        shapeRenderer.setAutoShapeType(true);
        interfaceSpriteBatch.setProjectionMatrix(interfaceCamera.combined);
        interfaceShapeRenderer.setProjectionMatrix(interfaceCamera.combined);
        interfaceShapeRenderer.setAutoShapeType(true);

        spriteBatch.begin();
        spriteBatch.draw(background1, positionBG1.x, positionBG1.y, backgroundScale.x, backgroundScale.y);
        spriteBatch.draw(background1, positionBG2.x, positionBG2.y, backgroundScale.x, backgroundScale.y);
        spriteBatch.draw(background1, positionBG3.x, positionBG3.y, backgroundScale.x, backgroundScale.y);
        spriteBatch.draw(fallBackground, positionBG4.x, positionBG4.y);
        spriteBatch.end();

        for (Platform plat : platforms) {
            plat.Draw(spriteBatch);
        }
        for (Platform w : walls) {
            w.Draw(spriteBatch);
        }
        for (Stair s : stairs) {
            s.Draw(spriteBatch);
        }

        touchBlock.Draw(spriteBatch);
        switchLaser.Draw(spriteBatch, shapeRenderer);

        spriteBatch.begin();
        spriteBatch.draw(gameAssets.sewer1, groundWidth * 10, -gameAssets.sewer1.getHeight() / 4,
                gameAssets.sewer1.getWidth() / 3, gameAssets.sewer2.getHeight() / 3);
        spriteBatch.end();

        for (Trap i : traps) {
            spriteBatch.begin();
            spriteBatch.draw(gameAssets.trap, i.getPosition().x, i.getPosition().y, 50, 50);
            spriteBatch.end();
        }

        player.render(spriteBatch, shapeRenderer);

        spriteBatch.begin();
        spriteBatch.draw(gameAssets.sewer2, groundWidth * 10, -gameAssets.sewer1.getHeight() / 4,
                gameAssets.sewer1.getWidth() / 3, gameAssets.sewer2.getHeight() / 3);
        spriteBatch.end();

        playerController.draw(interfaceSpriteBatch, interfaceShapeRenderer);

        for (Guard g : guards) {
            g.render(spriteBatch, shapeRenderer);
        }

        for (Laser l : lasers) {
            l.Draw(spriteBatch);
        }

        for (Item i : items) {
            i.Draw(spriteBatch);
        }

        spriteBatch.begin();
        spriteBatch.draw(getExitFrame(Gdx.graphics.getDeltaTime()), exitPosition.x, exitPosition.y);
        spriteBatch.end();

        cameraSearch.Draw(spriteBatch, shapeRenderer);
        cameraSearch2.Draw(spriteBatch, shapeRenderer);

        glyphLayout.setText(font, message);
        interfaceSpriteBatch.begin();
        font.draw(interfaceSpriteBatch, glyphLayout,
                interfaceCamera.position.x - interfaceCamera.viewportWidth / 2 + 10,
                interfaceCamera.position.y + interfaceCamera.viewportHeight / 2 - 10);
        interfaceSpriteBatch.end();
    }

    @Override
    public void dispose() {
        player.dispose();
        gameAssets.dispose();
        shapeRenderer.dispose();
        interfaceShapeRenderer.dispose();
        interfaceSpriteBatch.dispose();
        font.dispose();
        exitAtlas.dispose();
    }

    private void LoadLevelAssets() {
        groundWidth = gameAssets.floorTexture.getWidth();
        groundHeight = gameAssets.floorTexture.getHeight();
        wallHeight = gameAssets.wallTexture.getHeight();
        wallWidth = gameAssets.wallTexture.getWidth();

        touchBlock = new TouchBlock(gameAssets.woodTexture, gameAssets.soundWaveTexture,
                new Vector2(groundWidth * 6.5f, 260), 300);

        for (int h = 0; h < 4; h++) {
            for (int i = 0; i < 10; i++) {
                platforms.add(new Platform(new Vector2(groundWidth * i, h * 200), gameAssets.floorTexture));
            }
        }

        platforms.add(new Platform(new Vector2(-groundWidth, 0), gameAssets.floorTexture));
        platforms.add(new Platform(new Vector2(groundWidth * 9.2f, 2 * 200), gameAssets.floorTexture));

        walls.add(new Platform(new Vector2(groundWidth * 5.5f, 50), gameAssets.wallTexture));
        walls.add(new Platform(new Vector2(groundWidth * 5.5f, 150), gameAssets.wallTexture));

        for (int i = 0; i < 6; i++) {
            walls.add(new Platform(new Vector2(-groundWidth, i * wallHeight), gameAssets.wallTexture));
            if (i != 0 && i != 1)
                walls.add(new Platform(new Vector2(0, i * wallHeight), gameAssets.wallTexture));
            if (i != 4 && i != 5)
                walls.add(new Platform(new Vector2((groundWidth * 10) - wallWidth, i * wallHeight), gameAssets.wallTexture));
        }

        background1 = new Sprite(gameAssets.background, gameAssets.background.getWidth(), gameAssets.background.getHeight());
        fallBackground = new Sprite(gameAssets.startBackground, gameAssets.startBackground.getWidth(), gameAssets.startBackground.getHeight());
        positionBG1 = new Vector2(wallWidth, groundHeight);
        positionBG2 = new Vector2(wallWidth, wallHeight * 2 + groundHeight);
        positionBG3 = new Vector2(wallWidth, wallHeight * 3.99f + groundHeight);
        positionBG4 = new Vector2(-groundWidth, groundHeight);
        backgroundScale = new Vector2(background1.getWidth() * 0.98f, background1.getHeight() * 1.45f);

        stair1 = new Stair(new Vector2(groundWidth * 4.5f, groundHeight), gameAssets.stairs);
        stairs.add(stair1);
        stair2 = new Stair(new Vector2(groundWidth * 8, groundHeight), gameAssets.stairs);
        stairs.add(stair2);
        stair3 = new Stair(new Vector2(groundWidth, groundHeight * 3.70f + groundHeight), gameAssets.stairs);
        stairs.add(stair3);

        laser = new Laser(new Vector2(groundWidth * 2f, groundHeight + 400), false, true, 0.25f);
        lasers.add(laser);
        laser2 = new Laser(new Vector2(groundWidth * 2.5f, groundHeight + 400), false, true, 0.45f);
        lasers.add(laser2);
        laser3 = new Laser(new Vector2(groundWidth * 7f, groundHeight), false, true, 0.65f);
        lasers.add(laser3);
        lasers.add(new Laser(new Vector2(groundWidth * 3f, groundHeight + 400), false, true, 0.85f));
        lasers.add(new Laser(new Vector2(groundWidth * 2.5f, groundHeight), false, true, 0.65f));
        lasers.add(new Laser(new Vector2(groundWidth * 1f, groundHeight), false, true, 0.45f));

        cameraSearch = new Camera(new Vector2(groundWidth * 4f, groundHeight + 400));
        cameraSearch2 = new Camera(new Vector2(groundWidth * 6.5f, groundHeight + 400));

        specialLaser = new Laser(new Vector2(groundWidth * 3f, groundHeight + 200), false, false, 0f);
        specialLaser.laserActions = Laser.LaserActions.ON;
        lasers.add(specialLaser);

        backgroundCollider = new Collider(new Vector2(wallWidth, groundHeight),
                groundWidth * 10 - wallWidth * 2, wallHeight * 6 - groundHeight);

        items.add(new Item(new Vector2(1150, 500)));
        items.add(new Item(new Vector2(70, 300)));
        items.add(new Item(new Vector2(50, 500)));
        items.add(new Item(new Vector2(1000, 90)));
        items.add(new Item(new Vector2(1800, 90)));
        items.add(new Item(new Vector2(1900, 300)));
        items.add(new Item(new Vector2(700, 300)));

        exitAtlas = new TextureAtlas(Gdx.files.internal("exitDoor.pack"));
        exitAnimation = new Animation<TextureRegion>(0.1f, exitAtlas.getRegions());
        exitPosition = new Vector2(groundWidth * 9.9f, wallHeight * 3.5f);
        exitCollider = new Collider(exitPosition,
                exitAnimation.getKeyFrame(0.2f).getRegionWidth(),
                exitAnimation.getKeyFrame(0.2f).getRegionHeight());
        windowState = WindowState.CLOSED;

        finishCollider = new Collider(new Vector2(groundWidth * 9.9f, wallHeight * 4.5f), 100, 200);

        switchLaser = new Switch();
    }

    private TextureRegion getExitFrame(float deltaTime) {
        switch (windowState) {
            case OPEN:
                return exitAnimation.getKeyFrame(0.2f, false);
            case CLOSED:
            default:
                return exitAnimation.getKeyFrame(0, false);
        }
    }
}
