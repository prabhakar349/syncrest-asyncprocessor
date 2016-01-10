package com.iamiddy;

import com.iamiddy.domain.RequestEvent;
import com.iamiddy.domain.ResponseEvent;
import com.iamiddy.service.MessageConsumer;
import com.iamiddy.service.MessageConsumerImpl;
import com.iamiddy.service.SampleService;
import com.iamiddy.service.SyncAsyncProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by iddymagohe on 1/9/16.
 */
public class SampleServiceTest {

    private SyncAsyncProcessor processor;
    private MessageConsumerImpl consumer;
    private SampleService service;
    ExecutorService ex = Executors.newSingleThreadExecutor();


    @Before
    public void setUp(){
        this.consumer = new MessageConsumerImpl();
        this.service = new SampleService(new LinkedBlockingQueue<>(),this.consumer);
        this.processor = SyncAsyncProcessor.getInstance(service,consumer);
        ex.execute(service);


    }

    @Test
    public void testResponse()  {
        IntStream.range(0,20).forEach(d ->{
            RequestEvent re = new RequestEvent("TT" + d, "message trying out " + d);

            try {
                ResponseEvent resp = processor.sendAndReceive(re, 3000);
                System.out.println(resp);
                assertEquals(re.getEventId(),resp.getEventId());
                assertEquals(new StringBuilder(re.getRequestBody()).reverse().toString(),resp.getResponseBody());
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }

        });


    }


}
