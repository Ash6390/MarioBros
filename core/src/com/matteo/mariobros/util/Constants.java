package com.matteo.mariobros.util;

public class Constants
{
    //screen size
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;

    //tile size
    public static final float TILE_SIZE = 16;

    //item size
    public static final float ITEM_SIZE = 16;

    //pixels/meter
    public static final float PPM = 100;

    //movement constants
    public static final float JUMP_IMPULSE_SPEED = 4.0f;
    public static final float WALK_SPEED_LIMIT = 2.0f;
    public static final float WALK_IMPULSE_SPEED = 0.1f;

    public static final float GRAVITY_ACCELERATION = -10;

    //physics "refresh rate"
    public static final float PHYSICS_TIME_STEP = 1/60f;

    //entity dimensions
    public static final int SMALL_MARIO_WIDTH = 16;
    public static final int SMALL_MARIO_HEIGHT = 16;

    public static final int GOOMBA_WIDTH = 16;
    public static final int GOOMBA_HEIGHT = 16;

    //collision bits
    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;

    //distance (in pixels) to wake up enemies
    public static final int ENEMY_WAKE_DISTANCE = 256;
}
