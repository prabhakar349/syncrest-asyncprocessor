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

import java.util.Optional;

/**
 * Created by iddymagohe on 1/9/16.
 */
public class ResponseObserverImpl<T extends AbstractResponse>  implements ResponseObserver<T> {

    private String eventId;
    private T response;

    public ResponseObserverImpl(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public void applyResponse(ResponseObservable responseObservable, T response) {
        this.response = response;
        //Self deleting responseObservable.notify()
    }

    @Override
    public T call() throws Exception {
        Optional<T> responseOptional;
        do{
            responseOptional = Optional.ofNullable(this.response);
        }while(!responseOptional.isPresent());
        return response;
    }

    public T getResponse() {
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseObserverImpl<?> that = (ResponseObserverImpl<?>) o;

        return !(eventId != null ? !eventId.equals(that.eventId) : that.eventId != null);

    }

    @Override
    public int hashCode() {
        return eventId != null ? eventId.hashCode() : 0;
    }
}
