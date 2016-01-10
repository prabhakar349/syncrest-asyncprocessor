package com.iamiddy;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

/**
 * Created by iddymagohe on 1/9/16.
 */
public interface ResponseObserver<T extends AbstractResponse> extends Callable<T>{

    /**
     *
     * @return currentRequestId
     */
    String getEventId();

    /**
     * Applies a response to ResponseObserver and delete this Observer from responseObservable
     * @see com.iamiddy.ResponseObservable#deleteObserver(com.iamiddy.ResponseObserver)
     * @see com.iamiddy.ResponseObservable#notifyObservers(com.iamiddy.AbstractResponse)
     *
     * @param responseObservable
     * @param response
     */
    void applyResponse(ResponseObservable responseObservable, T response);
}
