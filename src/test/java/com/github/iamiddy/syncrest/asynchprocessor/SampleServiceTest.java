/*
 * Copyright [2016] [Iddy Magohe]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.iamiddy.syncrest.asynchprocessor;

import com.github.iamiddy.syncrest.asynchprocessor.service.MessageConsumerImpl;
import com.github.iamiddy.syncrest.asynchprocessor.service.SyncAsyncProcessor;
import com.github.iamiddy.syncrest.asynchprocessor.domain.RequestEvent;
import com.github.iamiddy.syncrest.asynchprocessor.domain.ResponseEvent;
import com.github.iamiddy.syncrest.asynchprocessor.service.MessageConsumer;
import com.github.iamiddy.syncrest.asynchprocessor.service.SampleService;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * @author iddymagohe
 * Created by iddymagohe on 1/9/16.
 */
public class SampleServiceTest {

    private SyncAsyncProcessor processor;
    private MessageConsumer consumer;
    private SampleService service;
    ExecutorService ex = Executors.newFixedThreadPool(2);


    @Before
    public void setUp(){
        this.consumer = new MessageConsumerImpl();
        this.service = new SampleService(new LinkedBlockingQueue<>(),this.consumer);
        this.processor = SyncAsyncProcessor.getInstance(service,consumer);
        ex.execute(service);


    }

    @Test
    public void testResponse()  {
        IntStream.range(0,20).forEach(d -> {
            RequestEvent re = new RequestEvent("TT" + d, "message trying out " + d);

            try {
                ResponseEvent resp = processor.sendAndReceive(re, 3000);
                System.out.println(resp);
                assertEquals(re.getEventId(), resp.getEventId());
                assertEquals(new StringBuilder(re.getRequestBody()).reverse().toString(), resp.getResponseBody());
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }

        });


    }


}
