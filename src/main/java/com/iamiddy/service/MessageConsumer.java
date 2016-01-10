package com.iamiddy.service;

import com.iamiddy.AbstractResponse;
import com.iamiddy.ResponseObservable;

/**
 * Created by iddymagohe on 1/9/16.
 */
public interface MessageConsumer  {


    <R extends AbstractResponse> void onMessage(R response);
}

