package com.matteo.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.matteo.mariobros.screens.PlayScreen;
import com.matteo.mariobros.util.FilePaths;

public class MarioBros extends Game
{

	public SpriteBatch batch;

	public AssetManager assetManager;

	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		assetManager.load(FilePaths.OVERWORLD_MUSIC_FILE, Music.class);
		assetManager.load(FilePaths.COIN_SOUND, Sound.class);
		assetManager.load(FilePaths.BUMP_SOUND, Sound.class);
		assetManager.load(FilePaths.BREAKBLOCK_SOUND, Sound.class);
		assetManager.load(FilePaths.JUMP_SMALL_SOUND, Sound.class);
		assetManager.load(FilePaths.STOMP_SOUND, Sound.class);
		assetManager.finishLoading();

		setScreen(new PlayScreen(this, assetManager));
	}

	@Override
	public void render ()
	{
		super.render();
	}

	/*
	@Override
	public void dispose () {

	}
	*/
}
