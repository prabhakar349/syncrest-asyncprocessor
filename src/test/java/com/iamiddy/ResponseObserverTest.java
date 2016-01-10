package com.iamiddy;

import com.iamiddy.domain.RequestEvent;
import com.iamiddy.domain.ResponseEvent;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by iddymagohe on 1/9/16.
 */
public class ResponseObserverTest {

    @Test
    public void testSubscription(){
        ResponseObservable observable = new ResponseObservable();
        RequestEvent requestEvent = new RequestEvent("123456","Sample request");
        ResponseEvent responseEvent = new ResponseEvent(requestEvent.getEventId(),"Sample Response");
        ResponseObserverImpl ro = new ResponseObserverImpl<RequestEvent>(requestEvent.getEventId());
        assertNull(ro.getResponse());
        observable.addObservers(ro);
        assertTrue(observable.countObservers() > 0);
        observable.setChanged();
        observable.notifyObservers(responseEvent);
        assertNotNull(ro.getResponse());
        assertTrue(responseEvent.getEventId().equals(ro.getResponse().getEventId()));
        assertFalse(observable.countObservers() >0);
    }
}
