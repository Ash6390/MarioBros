package com.matteo.mariobros.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.matteo.mariobros.screens.PlayScreen;
import com.matteo.mariobros.sprites.entities.Goomba;
import com.matteo.mariobros.sprites.tiles.Brick;
import com.matteo.mariobros.sprites.tiles.Coin;
import com.matteo.mariobros.util.Constants;

import java.util.Iterator;

public class B2WorldCreator {
    private AssetManager assetManager;
    private Array<Goomba> goombas;
    public B2WorldCreator(PlayScreen screen, AssetManager assetManager)
    {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        this.assetManager = assetManager;
        //temporary
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;


        //create ground
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth()/2)/ Constants.PPM, (rect.getY() + rect.getHeight()/2)/ Constants.PPM);
            body = world.createBody(bDef);

            shape.setAsBox((rect.getWidth()/2)/ Constants.PPM, (rect.getHeight()/2)/ Constants.PPM);
            fDef.shape = shape;
            body.createFixture(fDef);
        }

        //create pipes
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth()/2)/ Constants.PPM, (rect.getY() + rect.getHeight()/2)/ Constants.PPM);
            body = world.createBody(bDef);

            shape.setAsBox((rect.getWidth()/2)/ Constants.PPM, (rect.getHeight()/2)/ Constants.PPM);
            fDef.shape = shape;
            fDef.filter.categoryBits = Constants.OBJECT_BIT;
            body.createFixture(fDef);
        }

        //create bricks
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(screen, rect, this.assetManager);
        }

        //create coins
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(screen, rect, this.assetManager);
        }

        //create goombas
        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            goombas.add(new Goomba(screen,rect.getX()/Constants.PPM, rect.getY()/Constants.PPM, this.assetManager));
        }
    }

    public Array<Goomba> getGoombas()
    {
        return goombas;
    }

    public Iterator<Goomba> getGoombaIterator()
    {
        return goombas.iterator();
    }
}
