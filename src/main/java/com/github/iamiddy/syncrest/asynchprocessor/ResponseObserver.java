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

import java.util.concurrent.Callable;

/**
 * Created by iddymagohe on 1/9/16.
 */
public interface ResponseObserver<T extends AbstractResponse> extends Callable<T>{

    /**
     *
     * @return currentRequestId
     */
    String getEventId();

    /**
     * Applies a response to ResponseObserver and delete this Observer from responseObservable
     * @see ResponseObservable#deleteObserver(ResponseObserver)
     * @see ResponseObservable#notifyObservers(AbstractResponse)
     *
     * @param responseObservable observable
     * @param response to notify an observer with
     *
     */
    void applyResponse(ResponseObservable responseObservable, T response);
}
