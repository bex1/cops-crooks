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

    /**
     * Checks if sound is enabled.
     * @return true if sound is enabled, false othwerwise.
     */
    public boolean isSoundEnabled() {
        return soundOn;
    }

    /**
     * Enables/disables the sound.
     * @param soundEffectsEnabled true if enabled, false if disabled.
     */
    public void setSoundEnabled(boolean soundEffectsEnabled ){
        soundOn = soundEffectsEnabled;
    }

    /**
     * Returns the volume, will be a float in the interval [0,1]
     * @return the volume.
     */
    public float getVolume() {
    	return volume;
    }

    /**
     * Sets the volume.
     * Should be a float in the interval [0,1]
     * If not it will be converted to such a number.
     * @param volume
     */
    public void setVolume(float volume ) {
        this.volume = volume % 1;
    }
}
