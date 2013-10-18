package com.dat255.project.android.copsandcrooks.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.dat255.project.android.copsandcrooks.utils.LRUCache.CacheEntryRemovedListener;
import com.dat255.project.android.copsandcrooks.utils.SoundManager.CopsAndCrooksSound;

/**
 * A service that manages the sound effects.
 */
public class SoundManager implements CacheEntryRemovedListener<CopsAndCrooksSound,Sound>, Disposable {
    public static final String LOG = SoundManager.class.getSimpleName();
    
    private static SoundManager instance;
	
	/**
     * The available sound files.
     */
    public enum CopsAndCrooksSound {
        CASH( "soundeffects/cash-register.wav" ),
        WIN( "soundeffects/winning.mp3" );

        private final String fileName;

        private CopsAndCrooksSound( String fileName ) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    /**
     * The volume to be set on the sound.
     */
    private float volume = 1f;

    /**
     * Whether the sound is enabled.
     */
    private boolean enabled = true;

    /**
     * The sound cache.
     */
    private final LRUCache<CopsAndCrooksSound,Sound> soundCache;

    /**
     * Creates the sound manager.
     */
    private SoundManager() {
        soundCache = new LRUCache<SoundManager.CopsAndCrooksSound,Sound>( 10 );
        soundCache.setEntryRemovedListener( this );
    }
    
    public static SoundManager getInstance() {
    	if (instance == null) {
    		instance = new SoundManager();
    	}
    	return instance;
    }

    /**
     * Plays the specified sound.
     */
    public void play(CopsAndCrooksSound sound ) {
        // check if the sound is enabled
        if( ! enabled ) return;

        // try and get the sound from the cache
        Sound soundToPlay = soundCache.get( sound );
        if( soundToPlay == null ) {
            FileHandle soundFile = Gdx.files.internal( sound.getFileName() );
            soundToPlay = Gdx.audio.newSound( soundFile );
            soundCache.add( sound, soundToPlay );
        }

        // play the sound
        Gdx.app.log(LOG, "Playing sound: " + sound.name() );
        soundToPlay.play( volume );
    }

    /**
     * Sets the sound volume which must be inside the range [0,1].
     */
    public void setVolume(float volume ) {
        Gdx.app.log(LOG, "Adjusting sound volume to: " + volume );

        // check and set the new volume
        if( volume < 0 || volume > 1f ) {
            throw new IllegalArgumentException( "The volume must be inside the range: [0,1]" );
        }
        this.volume = volume;
    }

    /**
     * Enables or disabled the sound.
     */
    public void setEnabled(boolean enabled ) {
        this.enabled = enabled;
    }

    // EntryRemovedListener implementation

    @Override
    public void notifyEntryRemoved(CopsAndCrooksSound key, Sound value ) {
        Gdx.app.log(LOG, "Disposing sound: " + key.name() );
        value.dispose();
    }

    /**
     * Disposes the sound manager.
     */
    public void dispose() {
        Gdx.app.log(LOG, "Disposing sound manager" );
        for( Sound sound : soundCache.retrieveAll() ) {
            sound.stop();
            sound.dispose();
        }
    }
}
