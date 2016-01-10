package com.iamiddy.service;

import com.iamiddy.AbstractResponse;
import com.iamiddy.ResponseObservable;
import com.iamiddy.ResponseObserver;
import com.iamiddy.ResponseObserverImpl;

import java.util.concurrent.*;

/**
 * Created by iddymagohe on 1/9/16.
 */
public class SyncAsyncProcessor {
    private static SyncAsyncProcessor syncAsyncProcessorInstance = null;

    private ExecutorService executorService;
    private MessageConsumerImpl observable;
    private MessageProducer producer;


    private SyncAsyncProcessor(MessageProducer producer, MessageConsumerImpl messageConsumer) {
        this.producer = producer;
        this.observable = messageConsumer;
        this.executorService = Executors.newCachedThreadPool();
    }

    public static SyncAsyncProcessor getInstance(MessageProducer producer, MessageConsumerImpl messageConsumer) {
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
     * @param request
     * @param <R>     extends AbstractResponse
     * @param <T>     extends AbstractResponse
     * @param timeout in Milliseconds
     * @return T extends AbstractResponse
     * @throws NullPointerException
     * @throws InterruptedException,ExecutionException
     * @throws java.util.concurrent.TimeoutException
     *
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
        this.producer.produceMessage(request); //S
        return response.get(timeout, TimeUnit.MILLISECONDS);
    }


}
