package com.github.iamiddy.syncrest.asynchprocessor.service;

import com.github.iamiddy.syncrest.asynchprocessor.AbstractResponse;

/**
 * Created by iddymagohe on 1/9/16.
 */
public interface MessageProducer {

    <R extends AbstractResponse> void produceMessage(R request);
}
