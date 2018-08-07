package com.matteo.mariobros.sprites.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.matteo.mariobros.screens.PlayScreen;
import com.matteo.mariobros.util.Constants;
import com.matteo.mariobros.util.FilePaths;

public class Mario extends Sprite
{
    public enum State { FALLING, JUMPING, STANDING, RUNNING }
    public State currentState;
    public State previousState;

    public World world;
    public Body b2Body;

    private TextureRegion marioStand;
    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private float stateTimer;
    private boolean runningRight;

    public Mario(PlayScreen screen)
    {
        super(screen.getAtlas().findRegion(FilePaths.PLAYER_TEXTURE_FILE_REGION));
        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++)
        {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        }
        marioRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 4; i < 6; i++)
        {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        }
        marioJump = new Animation<TextureRegion>(0.1f, frames );

        marioStand = new TextureRegion(getTexture(), 0, 0, 16, 16);

        defineMario();
        setBounds(0, 0, Constants.SMALL_MARIO_WIDTH /Constants.PPM, Constants.SMALL_MARIO_HEIGHT /Constants.PPM);
        //setBounds(0, 0, 8/Constants.PPM, 8/Constants.PPM);
        setRegion(marioStand);
    }

    public void update(float dt)
    {
        setPosition(b2Body.getPosition().x-getWidth()/2, b2Body.getPosition().y-getHeight()/2-(0.5f/Constants.PPM));
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt)
    {
        currentState = getState();
        TextureRegion region;
        switch(currentState)
        {
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if ((b2Body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX())
        {
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2Body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX())
        {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState()
    {
        if (b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2Body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2Body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void defineMario()
    {
        BodyDef bDef = new BodyDef();
        bDef.position.set((Constants.SMALL_MARIO_WIDTH+1)/Constants.PPM, (Constants.SMALL_MARIO_HEIGHT+1)/Constants.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        float xPos = b2Body.getPosition().x;
        float yPos = b2Body.getPosition().y;
        float ppm = Constants.PPM;
        float HAWmT = Constants.SMALL_MARIO_WIDTH /2-3;
        float HAHmO = Constants.SMALL_MARIO_HEIGHT /2-1;

        FixtureDef fDefFeet = new FixtureDef();
        FixtureDef fDefHead = new FixtureDef();
        FixtureDef fDefLeft = new FixtureDef();
        FixtureDef fDefRight = new FixtureDef();

        EdgeShape shapeFeet = new EdgeShape();
        shapeFeet.set((xPos-HAWmT)/ppm, (yPos-HAHmO)/ppm, (xPos+HAWmT)/ppm, (yPos-HAHmO)/ppm);
        EdgeShape shapeHead = new EdgeShape();
        shapeHead.set((xPos-HAWmT)/ppm, (yPos+HAHmO)/ppm, (xPos+HAWmT)/ppm, (yPos+HAHmO)/ppm);
        EdgeShape shapeLeft = new EdgeShape();
        shapeLeft.set((xPos-HAWmT)/ppm, (yPos+HAHmO)/ppm, (xPos-HAWmT)/ppm, (yPos-HAHmO)/ppm);
        EdgeShape shapeRight = new EdgeShape();
        shapeRight.set((xPos+HAWmT)/ppm, (yPos+HAHmO)/ppm, (xPos+HAWmT)/ppm, (yPos-HAHmO)/ppm);

        fDefFeet.filter.categoryBits = Constants.MARIO_BIT;
        fDefFeet.filter.maskBits = Constants.GROUND_BIT | Constants.COIN_BIT | Constants.BRICK_BIT | Constants.OBJECT_BIT | Constants.ENEMY_BIT | Constants.ENEMY_HEAD_BIT;
        fDefHead.filter.categoryBits = Constants.MARIO_BIT;
        fDefHead.filter.maskBits = Constants.GROUND_BIT | Constants.COIN_BIT | Constants.BRICK_BIT | Constants.OBJECT_BIT | Constants.ENEMY_BIT | Constants.ENEMY_HEAD_BIT;
        fDefLeft.filter.categoryBits = Constants.MARIO_BIT;
        fDefLeft.filter.maskBits = Constants.GROUND_BIT | Constants.COIN_BIT | Constants.BRICK_BIT | Constants.OBJECT_BIT | Constants.ENEMY_BIT | Constants.ENEMY_HEAD_BIT;
        fDefRight.filter.categoryBits = Constants.MARIO_BIT;
        fDefRight.filter.maskBits = Constants.GROUND_BIT | Constants.COIN_BIT | Constants.BRICK_BIT | Constants.OBJECT_BIT | Constants.ENEMY_BIT | Constants.ENEMY_HEAD_BIT;


        fDefFeet.shape = shapeFeet;
        fDefHead.shape = shapeHead;
        fDefLeft.shape = shapeLeft;
        fDefRight.shape = shapeRight;

        b2Body.createFixture(fDefFeet);
        b2Body.createFixture(fDefHead);
        b2Body.createFixture(fDefLeft);
        b2Body.createFixture(fDefRight);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/ppm, 7/ppm), new Vector2(2/ppm, 7/ppm));

        fDefHead.shape = head;
        fDefHead.isSensor = true;
        b2Body.createFixture(fDefHead).setUserData("head");
    }
}
