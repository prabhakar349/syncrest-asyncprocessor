package com.iamiddy.service;

import com.iamiddy.AbstractResponse;

/**
 * Created by iddymagohe on 1/9/16.
 */
public interface MessageProducer {

    <R extends AbstractResponse> void produceMessage(R request);
}
