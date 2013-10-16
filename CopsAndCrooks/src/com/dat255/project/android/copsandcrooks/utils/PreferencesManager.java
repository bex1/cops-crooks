package com.dat255.project.android.copsandcrooks.utils;


/**
 * Handles the game preferences.
 */
public class PreferencesManager {
	private static PreferencesManager instance;
    
    private boolean soundOn;
    private float volume;

    private PreferencesManager() {
    	soundOn = true;
    	volume = 1f;
    }
    
    public static PreferencesManager getInstance() {
    	if (instance == null) {
    		instance = new PreferencesManager();
    	}
    	return instance;
    }

    public boolean isSoundEnabled() {
        return soundOn;
    }

    public void setSoundEnabled(boolean soundEffectsEnabled ){
        soundOn = soundEffectsEnabled;
    }

    public float getVolume() {
    	return volume;
    }

    public void setVolume(float volume ) {
        this.volume = volume % 1;
    }
}
