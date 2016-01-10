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

package com.github.iamiddy.syncrest.asynchprocessor.domain;

import com.github.iamiddy.syncrest.asynchprocessor.AbstractResponse;

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
