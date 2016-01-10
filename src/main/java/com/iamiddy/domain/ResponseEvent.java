package com.iamiddy.domain;

import com.iamiddy.AbstractResponse;

/**
 * Created by iddymagohe on 1/9/16.
 */
public class ResponseEvent extends AbstractResponse {

    private String eventId;

    private  String responseBody;


    public ResponseEvent(String eventId, String responseBody) {
        this.eventId = eventId;
        this.responseBody = responseBody;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "ResponseEvent{" +
                "eventId='" + eventId + '\'' +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
