package com.iamiddy.domain;

import com.iamiddy.AbstractResponse;

/**
 * Created by iddymagohe on 1/9/16.
 */
public class RequestEvent extends AbstractResponse {

    private String requestId;

    private  String requestBody;

    public RequestEvent(String requestId, String requestBody) {
        this.requestId = requestId;
        this.requestBody = requestBody;
    }

    @Override
    public String getEventId() {
        return requestId;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}
