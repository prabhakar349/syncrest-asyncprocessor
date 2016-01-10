package com.iamiddy.service;

import com.iamiddy.AbstractResponse;
import com.iamiddy.ResponseObservable;

public class MessageConsumerImpl extends ResponseObservable implements MessageConsumer {

    /**
     * On a message from message-bus, remember to call
     * <code>setChanged</code> and <code>notifyObservers</code> otherwise requests will never get a response
     *
     * @param response extends AbstractResponse
     * @param <R>
     *
     *@see ResponseObservable#setChanged()
     * @see ResponseObservable#notifyObservers(AbstractResponse)
     *
     */
    @Override
    public <R extends AbstractResponse> void onMessage(R response) {
        setChanged();
        notifyObservers(response);
    }
}
