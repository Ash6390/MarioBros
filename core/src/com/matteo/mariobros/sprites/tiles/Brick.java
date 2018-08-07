package com.matteo.mariobros.sprites.tiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.matteo.mariobros.scenes.HUD;
import com.matteo.mariobros.screens.PlayScreen;
import com.matteo.mariobros.util.Constants;
import com.matteo.mariobros.util.FilePaths;

public class Brick extends InteractiveTileObject
{
    private AssetManager assetManager;
    public Brick(PlayScreen screen, Rectangle bounds, AssetManager assetManager)
    {
        super(screen, bounds);
        this.assetManager = assetManager;
        fixture.setUserData(this);
        setCategoryFilter(Constants.BRICK_BIT);
    }

    @Override
    public void onHeadHIt()
    {
        setCategoryFilter(Constants.DESTROYED_BIT);
        getCell().setTile(null);
        HUD.addScore(200);
        assetManager.get(FilePaths.BREAKBLOCK_SOUND, Sound.class).play();
    }
}
