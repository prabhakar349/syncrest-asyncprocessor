# Rest-Async-Processing

Synchronous request block for a response or timeout, a request is handed to the async processor, that encapsulate application layer services from
the underlying complex processing logic, on the event-driven architecture which includes one or many microservices , it's coordination is decoupled by a message bus infrastructure.

##Use case

Let's say you want to expose rest end-points of anything (PUT,GET,POST etc) to your clients, and the rest-endpoints app's responsibility is to drop the incoming requests to a message bus 
infrastructure being a queue or topic(pub/sub) model, and hang-on for a response from a different queue or topic.
This implies the rest-endpoints app is completely decoupled from the actual request processors apps(microservices) and these could be one or many.
In this architecture the only clue to the rest-endpoints app is basically where to drop a request and from where a response will be delivered, details about how many hops being queues or topics this request will move between for processing, is not known by rest-endpoints app.

###Few things to note:
 1. Rest-endpoints app can receive lot's of requests per second before responding to the first request.
 2. Depending on the incoming request rate, it is more likely that, at any point in time the rest-endpoints app will have received many requests and few responses to some of requests from the message bus, all held in memory at the same time.
 3. The response should only be returned to the correct request it originated from and not to any other random requests in memory.(correlation mechanism should be in place)
 4. For load balancing (LB) and high availability(HA) requirements there could be up **N** number of rest-endpoints app instances up and running at the same time.
 5. Because of the LB and HA , a response should never be returned to any instance that it's associated request didn't come from.
 6. Regardless of the message bus architecture or technology stack the rest-endpoints app service layer experience/interaction should remain the same 

The **Rest-Async-Processing** has taken note 1 through 6 into consideration, and exposes a very simple interface to the rest-endpoints app or any other app with a similar interaction architecture 
 while encapsulating these apps from all the handling details pointed out in (1 - 6), and it can be used in any Java application employing the async requests fulfillment architecture.
```java
  /**
  * @param <R> request
  * @return <T> response
  */
   public <R extends AbstractResponse, T extends AbstractResponse> T sendAndReceive(R request, long timeout)
```
### How it works ?
At it's core **Rest-Async-Processing** implements a famous [Observer design pattern](https://en.wikipedia.org/wiki/Observer_pattern) in such a way that,
a new `ResponseObserver` (Observer) is created for every incoming request, and whenever a response , is available the `ResponseObservable` (Subject) notifies it's associated `ResponseObserver`, and 
also leveraged the `Callable<V>` and `Future` 's capabilities to asynchronously wait for a response in a form of `Future` browse through the code for more details.




## USAGE
Create an instance of `SyncAsyncProcessor` which requires `MessageProducer` and `MessageConsumer` implementations <br/>

1. MessageProducer interface , implement `produceMessage`  with specific details to write to your message bus
2.  MessageConsumer abstract class, extends  it with specific consumer details, if you choose to override then `onMessage` , you must call `super.onMessage` at the end with a desired `Response` 

```java
/**
* SyncAsyncProcessor#getInstance method signature
*/
public static SyncAsyncProcessor getInstance(MessageProducer producer, MessageConsumer messageConsumer);
/**
* MessageConsumer
*/
public interface MessageProducer {
    <R extends AbstractResponse> void produceMessage(R request);
}
/**
* MessageConsumer
*/
public abstract class MessageConsumer extends ResponseObservable {
    public <R extends AbstractResponse> void onMessage(R response) {
        setChanged();
        notifyObservers(response);
    }
}
```
- Observer
- Singleton
- Callable & Future