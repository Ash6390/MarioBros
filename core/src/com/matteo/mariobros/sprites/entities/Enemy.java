package com.matteo.mariobros.sprites.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.matteo.mariobros.screens.PlayScreen;

public abstract class Enemy extends Sprite
{
    protected AssetManager assetManager;
    protected World world;
    protected  PlayScreen screen;
    public Body b2Body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y, AssetManager assetManager)
    {
        this.world = screen.getWorld();
        this.screen = screen;
        this.assetManager = assetManager;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(-0.5f, 0);
        b2Body.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead();

    public abstract boolean getDestroyed();
    public abstract boolean getToDestroy();
    public abstract void setDestroyed(boolean d);

    public abstract void update(float dt);

    public void reverseVelocity (boolean x, boolean y)
    {
        if (x)
            velocity.x *= -1;
        if(y)
            velocity.y *= -1;
    }
}
