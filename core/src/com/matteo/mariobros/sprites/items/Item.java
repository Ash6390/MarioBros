package com.matteo.mariobros.sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.matteo.mariobros.screens.PlayScreen;
import com.matteo.mariobros.util.Constants;

public abstract class Item extends Sprite
{
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;
    public Item(PlayScreen screen, float x, float y)
    {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), Constants.ITEM_SIZE/ Constants.PPM, Constants.ITEM_SIZE/Constants.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineItem();

    public abstract void useItem();

    public void update(float dt)
    {
        if (toDestroy && !destroyed)
        {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch)
    {
        if (!destroyed)
            super.draw(batch);
    }

    public void destroy()
    {
        toDestroy = true;
    }
}
