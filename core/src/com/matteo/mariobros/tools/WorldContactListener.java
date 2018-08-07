package com.matteo.mariobros.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.matteo.mariobros.sprites.entities.Enemy;
import com.matteo.mariobros.sprites.tiles.InteractiveTileObject;
import com.matteo.mariobros.util.Constants;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact)
    {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if ("head".equals(fixA.getUserData()) || "head".equals(fixB.getUserData()))
        {
            Fixture head = "head".equals(fixA.getUserData()) ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() instanceof InteractiveTileObject)
            {
                ((InteractiveTileObject) object.getUserData()).onHeadHIt();
            }
        }

        switch(cDef)
        {
            case Constants.ENEMY_HEAD_BIT | Constants.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == Constants.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case Constants.ENEMY_BIT | Constants.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Constants.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Constants.MARIO_BIT | Constants.ENEMY_BIT:
                Gdx.app.log("Mario","died");
                break;
            case Constants.ENEMY_BIT | Constants.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
        }
    }

    @Override
    public void endContact(Contact contact)
    {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {

    }
}
