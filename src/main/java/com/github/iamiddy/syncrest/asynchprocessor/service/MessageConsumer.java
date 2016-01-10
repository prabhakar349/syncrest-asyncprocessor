package com.github.iamiddy.syncrest.asynchprocessor.service;

import com.github.iamiddy.syncrest.asynchprocessor.AbstractResponse;
import com.github.iamiddy.syncrest.asynchprocessor.ResponseObservable;

/**
 * @author iddymagohe
 * Created by iddymagohe on 1/9/16.
 *
 */
public abstract class MessageConsumer extends ResponseObservable {


    /**
     * On a message from message-bus, remember to call
     * <code>setChanged</code> and <code>notifyObservers</code> otherwise requests will never get a response
     *
     * @param response extends AbstractResponse
     * @param <R> response
     * @see ResponseObservable#setChanged()
     * @see ResponseObservable#notifyObservers(AbstractResponse)
     */
    public <R extends AbstractResponse> void onMessage(R response) {
        setChanged();
        notifyObservers(response);
    }
}
