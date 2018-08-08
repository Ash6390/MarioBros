package com.matteo.mariobros.sprites.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.matteo.mariobros.screens.PlayScreen;
import com.matteo.mariobros.util.Constants;
import com.matteo.mariobros.util.FilePaths;

public class Goomba extends Enemy
{
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private AssetManager assetManager;
    public Goomba(PlayScreen screen, float x, float y, AssetManager assetManager)
    {
        super(screen, x, y, assetManager);
        this.assetManager = assetManager;
        frames = new Array<TextureRegion>();
        for (int i = 0; i<2; i++)
        {
            frames.add(new TextureRegion(screen.getAtlas().findRegion(FilePaths.GOOMBA_TEXTURE_FILE_REGION), i*16, 0, 16, 16));
        }
        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16/Constants.PPM, 16/Constants.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    @Override
    protected void defineEnemy()
    {
        BodyDef bDef = new BodyDef();
        bDef.position.set((Constants.GOOMBA_WIDTH+1)/Constants.PPM, (Constants.GOOMBA_HEIGHT+1)/Constants.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(getX(), getY());
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/Constants.PPM);
        fDef.shape = shape;

        fDef.filter.categoryBits = Constants.ENEMY_BIT;
        fDef.filter.maskBits = Constants.GROUND_BIT | Constants.COIN_BIT | Constants.BRICK_BIT | Constants.ENEMY_BIT | Constants.OBJECT_BIT | Constants.MARIO_BIT;
        b2Body.createFixture(fDef).setUserData(this);

        //create head
        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-2.5f, 8).scl(1/Constants.PPM);
        vertices[1] = new Vector2(2.5f, 8).scl(1/Constants.PPM);
        vertices[2] = new Vector2(-1, 3).scl(1/Constants.PPM);
        vertices[3] = new Vector2(1, 3).scl(1/Constants.PPM);
        head.set(vertices);

        fDef.shape = head;
        fDef.restitution = 0.5f;
        fDef.filter.categoryBits = Constants.ENEMY_HEAD_BIT;
        b2Body.createFixture(fDef).setUserData(this);
    }

    @Override
    public void hitOnHead()
    {
        setToDestroy = true;
        assetManager.get(FilePaths.STOMP_SOUND, Sound.class).play();
    }


    public void update(float dt)
    {
        stateTime += dt;
        if (setToDestroy && !destroyed)
        {
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion(FilePaths.GOOMBA_TEXTURE_FILE_REGION), 32, 0, 16, 16));
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2 + (1/Constants.PPM));
            stateTime = 0;
        }
        else if (!destroyed)
        {
            b2Body.setLinearVelocity(velocity);
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2 + (1/Constants.PPM));
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    public boolean getToDestroy()
    {
        return setToDestroy;
    }

    @Override
    public boolean getDestroyed()
    {
        return destroyed;
    }

    @Override
    public void setDestroyed(boolean d)
    {
        destroyed = d;
    }

    public float getStateTime()
    {
        return stateTime;
    }

    @Override
    public void draw(Batch batch)
    {
        if (!destroyed || stateTime < 1)
            super.draw(batch);
    }
}
