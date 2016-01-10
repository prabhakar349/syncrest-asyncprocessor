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

import com.github.iamiddy.syncrest.asynchprocessor.domain.ResponseEvent;
import com.github.iamiddy.syncrest.asynchprocessor.domain.RequestEvent;
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
