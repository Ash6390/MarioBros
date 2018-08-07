package com.matteo.mariobros.sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.matteo.mariobros.screens.PlayScreen;
import com.matteo.mariobros.util.Constants;
import com.matteo.mariobros.util.FilePaths;

public class Mushroom extends Item
{

    public Mushroom(PlayScreen screen, float x, float y)
    {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion(FilePaths.MUSHROOM_TEXTURE_FILE_REGION), 0, 0, 16, 16);
        velocity = new Vector2(0,0);
    }

    @Override
    public void defineItem()
    {

        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/Constants.PPM);
        fDef.shape = shape;

        body.createFixture(fDef).setUserData(this);

    }

    @Override
    public void useItem()
    {
        destroy();
    }

    @Override
    public void update(float dt)
    {
        super.update(dt);
        setPosition(body.getPosition().x-getWidth()/2, body.getPosition().y-getHeight()/2);
        body.setLinearVelocity(velocity);
    }
}
