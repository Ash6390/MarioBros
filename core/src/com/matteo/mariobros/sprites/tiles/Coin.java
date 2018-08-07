package com.matteo.mariobros.sprites.tiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.matteo.mariobros.scenes.HUD;
import com.matteo.mariobros.screens.PlayScreen;
import com.matteo.mariobros.sprites.items.ItemDef;
import com.matteo.mariobros.sprites.items.Mushroom;
import com.matteo.mariobros.util.Constants;
import com.matteo.mariobros.util.FilePaths;

public class Coin extends InteractiveTileObject
{
    private AssetManager assetManager;
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    public Coin(PlayScreen screen, Rectangle bounds, AssetManager assetManager)
    {
        super(screen, bounds);
        this.assetManager = assetManager;
        tileSet = map.getTileSets().getTileSet(FilePaths.WOLRD_1_LEVEL_1_TILESET_GUTTER);
        fixture.setUserData(this);
        setCategoryFilter(Constants.COIN_BIT);
    }

    @Override
    public void onHeadHIt()
    {
        if (getCell().getTile().getId() != BLANK_COIN)
        {
            getCell().setTile(tileSet.getTile(BLANK_COIN));
            HUD.addScore(200);
            assetManager.get(FilePaths.COIN_SOUND, Sound.class).play();
            screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y+16/Constants.PPM), Mushroom.class));
        }
        else
        {
            assetManager.get(FilePaths.BUMP_SOUND, Sound.class).play();
        }
    }
}
