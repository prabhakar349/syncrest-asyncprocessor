package com.github.iamiddy.syncrest.asynchprocessor.service;

import com.github.iamiddy.syncrest.asynchprocessor.AbstractResponse;
import com.github.iamiddy.syncrest.asynchprocessor.ResponseObservable;
import com.github.iamiddy.syncrest.asynchprocessor.ResponseObserver;
import com.github.iamiddy.syncrest.asynchprocessor.ResponseObserverImpl;

import java.util.concurrent.*;

/**
 * @author iddymagohe
 * Created by iddymagohe on 1/9/16.
 */
public class SyncAsyncProcessor {
    private static SyncAsyncProcessor syncAsyncProcessorInstance = null;

    private ExecutorService executorService;
    private MessageConsumer observable;
    private MessageProducer producer;


    private SyncAsyncProcessor(MessageProducer producer, MessageConsumer messageConsumer) {
        this.producer = producer;
        this.observable = messageConsumer;
        this.executorService = Executors.newCachedThreadPool();
    }

    public static SyncAsyncProcessor getInstance(MessageProducer producer, MessageConsumer messageConsumer) {
        synchronized (SyncAsyncProcessor.class) {
            if (syncAsyncProcessorInstance == null)
                syncAsyncProcessorInstance = new SyncAsyncProcessor(producer, messageConsumer);
        }
        return syncAsyncProcessorInstance;
    }

    /**
     * takes in a request
     * creates an Observer to subscribe for a response
     * submits a task asynchronously to executorService for a response whenever it's available or timesout
     * Produces a message to a message bus for backend processing
     *
     * @param <R>     extends AbstractResponse
     * @param <T>     extends AbstractResponse
     * @param timeout in Milliseconds
     * @param request for processing
     * @return T extends AbstractResponse
     * @throws NullPointerException null request or eventId
     * @throws InterruptedException interruption caused
     * @throws ExecutionException execution issue
     * @throws java.util.concurrent.TimeoutException took longer than the passed milliseconds
     * @see ResponseObservable#addObservers(ResponseObserver)
     * @see ExecutorService#submit(Callable)
     * @see MessageProducer#produceMessage(AbstractResponse)
     */
    public <R extends AbstractResponse, T extends AbstractResponse> T sendAndReceive(R request, long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        if (request == null || request.getEventId() == null)
            throw new NullPointerException();
        ResponseObserver callableObserver = new ResponseObserverImpl<>(request.getEventId());
        observable.addObservers(callableObserver);
        Future<T> response = executorService.submit(callableObserver);
        this.producer.produceMessage(request);
        return response.get(timeout, TimeUnit.MILLISECONDS);
    }


}
