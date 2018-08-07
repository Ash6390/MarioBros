package com.matteo.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.matteo.mariobros.MarioBros;
import com.matteo.mariobros.scenes.HUD;
import com.matteo.mariobros.sprites.entities.Mario;
import com.matteo.mariobros.sprites.entities.Enemy;
import com.matteo.mariobros.sprites.entities.Goomba;
import com.matteo.mariobros.sprites.items.Item;
import com.matteo.mariobros.sprites.items.ItemDef;
import com.matteo.mariobros.sprites.items.Mushroom;
import com.matteo.mariobros.tools.B2WorldCreator;
import com.matteo.mariobros.tools.WorldContactListener;
import com.matteo.mariobros.util.Constants;
import com.matteo.mariobros.util.FilePaths;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen
{

    private MarioBros game;

    private TextureAtlas atlas;

    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private HUD hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;
    private Iterator<Goomba> goombaIterator;

    private Mario player;

    private AssetManager assetManager;
    private Music overworldMusic;

    private Array<Item> items;

    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    public PlayScreen(MarioBros game, AssetManager assetManager)
    {
        this.game = game;
        this.assetManager = assetManager;
        gameCam = new OrthographicCamera();

        gamePort = new FitViewport(Constants.V_WIDTH/ Constants.PPM, Constants.V_HEIGHT/ Constants.PPM, gameCam);

        hud = new HUD(game.batch);

        atlas = new TextureAtlas(FilePaths.PLAYER_ENEMIES_ATLAS_FILE);

        mapLoader =  new TmxMapLoader();
        map = mapLoader.load(FilePaths.WORLD_1_LEVEL_1);
        renderer = new OrthogonalTiledMapRenderer(map, 1/ Constants.PPM);

        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(0, Constants.GRAVITY_ACCELERATION), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this, assetManager);

        //create the player in the world
        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        overworldMusic = assetManager.get(FilePaths.OVERWORLD_MUSIC_FILE, Music.class);
        overworldMusic.setLooping(true);
        overworldMusic.setVolume(0.5f);
        overworldMusic.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
    }

    public void update(float dt)
    {
        handleInput(dt);
        handleSpawningItems();

        world.step(Constants.PHYSICS_TIME_STEP, 6, 2);

        player.update(dt);
        goombaIterator = creator.getGoombaIterator();
        while(goombaIterator.hasNext())
        {
            Goomba nextGoomba = goombaIterator.next();
            nextGoomba.update(dt);
            if(nextGoomba.getX()<player.getX()+(Constants.ENEMY_WAKE_DISTANCE/Constants.PPM))
                nextGoomba.b2Body.setActive((true));
            if (nextGoomba.getToDestroy() && !nextGoomba.getDestroyed())
            {
                world.destroyBody(nextGoomba.b2Body);
                nextGoomba.setDestroyed(true);
            }

            if (nextGoomba.getStateTime() >= 1 && nextGoomba.getDestroyed())
            {
                Gdx.app.log("removing goomba from array", "");
                goombaIterator.remove();
            }

        }

        for (Item item : items)
            item.update(dt);

        hud.update(dt);

        gameCam.position.x = player.b2Body.getPosition().x;

        gameCam.update();
        renderer.setView(gameCam);
    }

    public void handleInput(float dt)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            assetManager.get(FilePaths.JUMP_SMALL_SOUND, Sound.class).play();
            player.b2Body.applyLinearImpulse(new Vector2(0, Constants.JUMP_IMPULSE_SPEED), player.b2Body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2Body.getLinearVelocity().x <= Constants.WALK_SPEED_LIMIT)
            player.b2Body.applyLinearImpulse(new Vector2(Constants.WALK_IMPULSE_SPEED, 0), player.b2Body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2Body.getLinearVelocity().x >= -Constants.WALK_SPEED_LIMIT)
            player.b2Body.applyLinearImpulse(new Vector2(-Constants.WALK_IMPULSE_SPEED, 0), player.b2Body.getWorldCenter(), true);

    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }

    public void spawnItem(ItemDef iDef)
    {
        itemsToSpawn.add(iDef);
    }

    public void handleSpawningItems()
    {
        if(!itemsToSpawn.isEmpty())
        {
            ItemDef iDef = itemsToSpawn.poll();
            if(iDef.type == Mushroom.class)
                items.add(new Mushroom(this, iDef.pos.x, iDef.pos.y));
        }
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render world
        renderer.render();

        //render bodies
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        player.draw(game.batch);

        for (Enemy enemy : creator.getGoombas())
            enemy.draw(game.batch);

        for (Item item : items)
            item.draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public TiledMap getMap()
    {
        return map;
    }

    public World getWorld()
    {
        return world;
    }

    @Override
    public void resize(int width, int height)
    {
        gamePort.update(width, height);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
