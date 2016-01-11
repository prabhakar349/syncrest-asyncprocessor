package com.github.iamiddy.syncrest.asynchprocessor.service;

import com.github.iamiddy.syncrest.asynchprocessor.domain.RequestEvent;
import com.github.iamiddy.syncrest.asynchprocessor.domain.ResponseEvent;
import com.github.iamiddy.syncrest.asynchprocessor.AbstractResponse;

import java.util.concurrent.BlockingQueue;

/**
 * @author iddymagohe
 * Created by iddymagohe on 1/9/16.
 */
public class SampleService  implements  MessageProducer, Runnable{

    private final BlockingQueue<ResponseEvent> responseQueue;
   private  MessageConsumer consumer;

    public SampleService(BlockingQueue<ResponseEvent> responseQueue, MessageConsumer consumer) {
        this.responseQueue = responseQueue;
        this.consumer = consumer;
    }

    /**
     * Takes in a RequestEvent, reverse it's body and put the response in a blocking queue.
     * @param request message to produce
     * @param <R> type
     */
    @Override
    public <R extends AbstractResponse> void produceMessage(R request) {

        RequestEvent ev = (RequestEvent)request;
        ResponseEvent re = new ResponseEvent(ev.getEventId(), new StringBuilder(ev.getRequestBody()).reverse().toString());
        try {
            responseQueue.put(re);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
      while (true){
          try {
              ResponseEvent re = responseQueue.take();
              consumeMessage(re);
          } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
          }
      }
    }

    private void consumeMessage(ResponseEvent re){
        consumer.onMessage(re);
    }
}
