package com.iamiddy;

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
