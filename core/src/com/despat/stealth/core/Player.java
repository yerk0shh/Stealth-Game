package com.despat.stealth.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.despat.stealth.utils.Collider;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    public enum PlayerActions
    {
        IDLE,
        WALKING,
        STAIRS,
        HIDING,
        UNHIDING,
        DYING,
        FALLING
    }

    public PlayerActions playerAction;
    private PlayerActions previousPlayerAction;
    private Vector3 position;

    public Vector3 getNextPosition()
    {
        return nextPosition;
    }

    private Vector3 nextPosition;

    public Vector3 getVelocity() {
        return velocity;
    }

    private Vector3 velocity;

    public float getPlayerWidth() {
        return playerWidth;
    }

    public float getPlayerHeight() {
        return playerHeight;
    }

    private float playerWidth;
    private float playerHeight;

    public Collider getPlayerCollider() {
        return playerCollider;
    }

    private Collider playerCollider;
    private Collider playerStairsCollider;
    private boolean stairsDown = false;

    private List<Platform> platforms = new ArrayList<Platform>();
    private List<Platform> walls = new ArrayList<Platform>();
    private List<Platform> platformsToCheck = new ArrayList<Platform>();

    private TextureAtlas playerIdleAtlas;
    private TextureAtlas playerWalkingAtlas;
    private TextureAtlas playerDyingAtlas;
    private TextureAtlas playerStairsAtlas;
    private TextureAtlas playerHidingAtlas;
    private TextureAtlas playerUnhidingAtlas;
    private TextureAtlas playerFallingAtlas;
    private Animation<TextureRegion> playerIdleAnimation;
    private Animation<TextureRegion> playerWalkingAnimation;
    private Animation<TextureRegion> playerDyingAnimation;
    private Animation<TextureRegion> playerStairsAnimation;
    private Animation<TextureRegion> playerHidingAnimation;
    private Animation<TextureRegion> playerUnhidingAnimation;
    private Animation<TextureRegion> playerFallingAnimation;
    private float stateTime;
    public boolean canHide;
    private boolean facingRight = true;

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    private boolean dead = false;

    public boolean isDisableGameOver() { return false; }
    private float gameOverTimer = 0;


    private boolean moveUpOrDown = false;

    public Player(int x, int y, List<Platform> platforms, List<Platform> walls)
    {
        this.platforms = platforms;
        this.walls = walls;
        position = new Vector3(x, y, 0);
        nextPosition = position;
        velocity = new Vector3(100, -100, 0);
        playerWidth = 45;
        playerHeight = 106;

        playerIdleAtlas = new TextureAtlas(Gdx.files.internal("Player_IdleAnimation.pack"));
        playerIdleAnimation = new Animation<TextureRegion>(0.2f, playerIdleAtlas.getRegions());

        playerWalkingAtlas = new TextureAtlas(Gdx.files.internal("Player_WalkAnimation.pack"));
        playerWalkingAnimation = new Animation<TextureRegion>(0.1f, playerWalkingAtlas.getRegions());

        playerDyingAtlas = new TextureAtlas(Gdx.files.internal("Player_DeadAnimation.pack"));
        playerDyingAnimation = new Animation<TextureRegion>(0.1f, playerDyingAtlas.getRegions());

        playerStairsAtlas = new TextureAtlas(Gdx.files.internal("Player_ClimbAnimation.pack"));
        playerStairsAnimation = new Animation<TextureRegion>(0.1f, playerStairsAtlas.getRegions());

        playerHidingAtlas = new TextureAtlas(Gdx.files.internal("Player_HideAnimation.pack"));
        playerHidingAnimation = new Animation<TextureRegion>(0.1f, playerHidingAtlas.getRegions());

        playerUnhidingAtlas = new TextureAtlas(Gdx.files.internal("Player_UnhideAnimation.pack"));
        playerUnhidingAnimation = new Animation<TextureRegion>(0.1f, playerUnhidingAtlas.getRegions());

        playerFallingAtlas = new TextureAtlas(Gdx.files.internal("Player_FallAnimation.pack"));
        playerFallingAnimation = new Animation<TextureRegion>(0.1f, playerFallingAtlas.getRegions());

        stateTime = 0;
        playerAction = PlayerActions.IDLE;
        previousPlayerAction = playerAction;

        playerCollider = new Collider(new Vector2(position.x + playerWidth/3, position.y), playerWidth / 2, playerHeight);
        playerStairsCollider = new Collider(new Vector2(position.x + playerWidth/5, position.y + 6), playerWidth/5, playerHeight - 6);

    }

    private TextureRegion getFrameFromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame;

        switch (playerAction)
        {
            case IDLE:
            default:
                frame = playerIdleAnimation.getKeyFrame(stateTime, true);
                break;
            case WALKING:
                frame = playerWalkingAnimation.getKeyFrame(stateTime, true);
                break;
            case STAIRS:
                if(moveUpOrDown)
                    frame = playerStairsAnimation.getKeyFrame(stateTime, true);
                else frame = playerStairsAnimation.getKeyFrame(0, true);
                break;
            case HIDING:
                if(previousPlayerAction != playerAction.HIDING)
                    stateTime = 0;

                if(stateTime >= 0.7f)
                    stateTime = 0.7f;
                frame = playerHidingAnimation.getKeyFrame(stateTime, true);
                break;
            case UNHIDING:
                if(previousPlayerAction != playerAction.UNHIDING)
                    stateTime = 0;

                frame = playerUnhidingAnimation.getKeyFrame(stateTime, true);
                if(stateTime >= 0.7f)
                    playerAction = PlayerActions.IDLE;
                break;
            case DYING:
                frame = playerDyingAnimation.getKeyFrame(stateTime, true);
                break;
            case FALLING:
                frame = playerFallingAnimation.getKeyFrame(stateTime, false);
                break;
        }

        if(!facingRight && frame.isFlipX())
        {
            frame.flip(true, false);
        }
        else if(facingRight && !frame.isFlipX())
        {
            frame.flip(true, false);
        }

        if(previousPlayerAction == playerAction)
            stateTime += deltaTime;
        else
            stateTime = 0;

        previousPlayerAction = playerAction;

        return frame;
    }

    public void update(float deltaTime, boolean left, boolean right, boolean cameraButton, boolean up, boolean down)
    {
        if(up || down)
            moveUpOrDown = true;
        else moveUpOrDown = false;

        findPlatformsUnderPlayer();

        if(!isGrounded() && !up && !down && playerAction != playerAction.STAIRS && playerAction != playerAction.DYING)
        {
            playerAction = PlayerActions.FALLING;
            position.add(0, velocity.y * deltaTime, 0);
        }


        if(playerAction == PlayerActions.WALKING)
            move(deltaTime, left, right);

        playerCollider.Update(new Vector2(position.x + playerWidth / 4, position.y));
        playerStairsCollider.Update(new Vector2(position.x + playerWidth / 2.5f, position.y));

        if(playerAction.equals(PlayerActions.DYING))
        {
            left = false;
            right = false;
            up = false;
            down = false;

            gameOverTimer += Gdx.graphics.getDeltaTime();

            if(gameOverTimer > 4)
            {
                gameOverTimer = 0;
                setDead(true);
            }
        }

        checkColwithWalls(deltaTime, left, right);

        climb(deltaTime, up, down);

        if(Gdx.input.isKeyJustPressed(Input.Keys.H))
        {
            if (playerAction == PlayerActions.HIDING)
            {
                playerAction = PlayerActions.UNHIDING;
            }
            else if(playerAction != PlayerActions.HIDING)
            {
                playerAction = PlayerActions.HIDING;
            }
        }


    }

    public void move(float deltaTime, boolean left, boolean right)
    {
        if(right)
        {
            facingRight = false;

            position.add(velocity.x * deltaTime, 0, 0);
            nextPosition = new Vector3(position.x + playerWidth / 1.5f, position.y, position.z).add(velocity.x * deltaTime, 0, 0);
        }
        else if(left)
        {
            facingRight = true;

            position.add(-velocity.x * deltaTime, 0, 0);
            nextPosition = new Vector3(position.x + playerWidth / 3f, position.y, position.z).add(-velocity.x * deltaTime, 0, 0);
        }
    }

    public void climb(float deltaTime, boolean up, boolean down)
    {
        if(up)
        {
            playerAction = PlayerActions.STAIRS;
            position.add(0, -velocity.y * deltaTime, 0);
            nextPosition = new Vector3(position.x, position.y, position.z).add(0, -velocity.y * deltaTime, 0);
        }
        else if(down && stairsDown)
        {
            playerAction = PlayerActions.STAIRS;

            position.add(0, velocity.y * deltaTime, 0);
            nextPosition = new Vector3(position.x, position.y, position.z).add(0, velocity.y * deltaTime, 0);
        }
    }

    private void findPlatformsUnderPlayer()
    {
        platformsToCheck = new ArrayList<Platform>();

        for (Platform plat: platforms)
        {
            if (plat.getCollider().getRectangle().y + plat.getCollider().getRectangle().getHeight() - 5 <= playerCollider.getRectangle().y)
            {
                platformsToCheck.add(plat);
            }
        }
    }

    public boolean isGrounded()
    {
        for (Platform plat : platformsToCheck)
        {
            if(plat.getCollider().getRectangle().overlaps(playerCollider.getRectangle()))
            {
                return true;
            }
        }

        return false;
    }

    public void checkColwithWalls(float deltaTime, boolean left, boolean right)
    {
        for (Platform w: walls)
        {
            if(w.getCollider().getRectangle().contains(new Vector2(nextPosition.x, nextPosition.y)))
            {
                if(right)
                {
                    position.sub(velocity.x * deltaTime, 0, 0);
                }
                else if(left)
                {
                    position.sub(-velocity.x * deltaTime, 0, 0);
                }
            }
        }
    }

    public boolean checkStairsCollision(Stair s)
    {
        if(s.getAuxCollider().getRectangle().overlaps(playerStairsCollider.getRectangle()))
        {
            stairsDown = false;
        }
        else stairsDown = true;

        if(s.getCollider().getRectangle().contains(playerStairsCollider.getRectangle()) && playerAction != playerAction.DYING)
        {
            playerAction = playerAction.STAIRS;
            return true;
        }

        return false;
    }

    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
    {
        spriteBatch.begin();

        if(playerAction == playerAction.DYING)
            spriteBatch.draw(getFrameFromCurrentAnimation(Gdx.graphics.getDeltaTime()), position.x - 40, position.y, 124, 124);
        else if(playerAction == playerAction.UNHIDING || playerAction == playerAction.HIDING )
            spriteBatch.draw(getFrameFromCurrentAnimation(Gdx.graphics.getDeltaTime()), position.x -14f, position.y, 73, 106);
        else
            spriteBatch.draw(getFrameFromCurrentAnimation(Gdx.graphics.getDeltaTime()), position.x, position.y, playerWidth, playerHeight);

        spriteBatch.end();
    }

    public void dispose()
    {
        playerIdleAtlas.dispose();
        playerWalkingAtlas.dispose();
        playerDyingAtlas.dispose();
        playerStairsAtlas.dispose();
        playerHidingAtlas.dispose();
        playerUnhidingAtlas.dispose();
        playerFallingAtlas.dispose();
    }
}
