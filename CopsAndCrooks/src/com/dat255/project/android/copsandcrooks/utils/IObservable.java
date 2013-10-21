package com.dat255.project.android.copsandcrooks.utils;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * Interface allowing registering listeners for changes in state.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public interface IObservable extends Serializable {
    
        /**
         * Add a PropertyChangeListener to the listener list. The listener is registered for all properties. 
         * 
         * @param l The PropertyChangeListener to be added.
         */
        void addObserver(PropertyChangeListener l);
        
        /**
         * Removes a PropertyChangeListener from the listener list.
         * 
         * @param l The PropertyChangeListener to be removed.
         */
        void removeObserver(PropertyChangeListener l);
}