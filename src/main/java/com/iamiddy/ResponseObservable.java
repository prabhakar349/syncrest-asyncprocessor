package com.iamiddy;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by iddymagohe on 1/9/16.
 */
public class ResponseObservable {

    private boolean changed = false;
    private ConcurrentHashMap<String, ResponseObserver> obs;

    //Constructor with zero observers
    public ResponseObservable() {
        this.obs = new ConcurrentHashMap<>();
    }


    /**
     * Adds an observer to the Map of responseObservers for this response, provided
     * that it is not the same as some observer already in the set.
     *
     * @param ro responseObserver to be added
     * @throws NullPointerException
     * @throws InvalidStateException
     */
    public synchronized void addObservers(ResponseObserver ro) {
        if (ro == null || ro.getEventId() == null)
            throw new NullPointerException();
        if (obs.containsKey(ro.getEventId()))
            throw new InvalidStateException("Only one observer is allowed per request : " + ro.getEventId());
        obs.putIfAbsent(ro.getEventId(), ro);
    }

    /**
     * Deletes an observer from a map of observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     *
     * @param ro the observer to be deleted.
     */
    public synchronized void deleteObserver(ResponseObserver ro) {
        if (ro == null || ro.getEventId() == null) return;
        obs.remove(ro.getEventId(), ro);
    }


    /**
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then find and notify only one Observer
     * and then call the <code>clearChanged</code> method to indicate
     * that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and the <code>r</code> argument.
     *
     * @param r any child object of AbstractResponse.
     * @see com.iamiddy.ResponseObservable#clearChanged()
     * @see com.iamiddy.ResponseObservable#hasChanged()
     * @see com.iamiddy.ResponseObserver#applyResponse(com.iamiddy.ResponseObservable, com.iamiddy.AbstractResponse)
     */
    public <T extends AbstractResponse> void notifyObservers(T r) {

        //holds the an observer to notify in the current map snapshot
        ResponseObserver localRo;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary code while holding its own Monitor.
             * The code where we extract each Observable from
             * the Map and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
            if (!changed) return;
            localRo = obs.get(r.getEventId());

            if (localRo == null) {
                clearChanged(); return;
            }

            obs.remove(localRo.getEventId());
            clearChanged();
        }

        localRo.applyResponse(this,r);
    }

    /**
     * Marks this <tt>Observable</tt> object as having been changed; the
     * <tt>hasChanged</tt> method will now return <tt>true</tt>.
     */
    protected synchronized void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has
     * already notified all of its observers of its most recent change,
     * so that the <tt>hasChanged</tt> method will now return <tt>false</tt>.
     * This method is called automatically by the
     * <code>notifyObservers</code> methods.
     *
     * @see java.util.Observable#notifyObservers()
     * @see java.util.Observable#notifyObservers(java.lang.Object)
     */
    protected synchronized void clearChanged() {
        changed = false;
    }

    /**
     * Tests if this object has changed.
     *
     * @return <code>true</code> if and only if the <code>setChanged</code>
     * method has been called more recently than the
     * <code>clearChanged</code> method on this object;
     * <code>false</code> otherwise.
     * @see ResponseObservable#clearChanged()
     * @see ResponseObservable#setChanged()
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * Returns the number of observers of this <tt>Observable</tt> object.
     *
     * @return the number of observers of this object.
     */
    public synchronized int countObservers() {
        return obs.size();
    }
}
