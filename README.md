# Rest-Async-Processing

A simple interface that handles synchronous requests that block for a response or times-out, a request is then handed to the async processor, that encapsulate application layer services from
the underlying complex event-driven processing architecture which might includes a number microservices in process , and  the coordination between the components (apps) is decoupled by a message bus infrastructure.

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
**SyncAsyncProcessor** exposes a `sendAndReceive` for sending in a request and waiting for a response, both request-in and response-out classes must extend `AbstractResponse` for **correlation** purposes, nothing will work without it
```java
public <R extends AbstractResponse, T extends AbstractResponse> T sendAndReceive(R request, long timeout) 
``` 
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

### DEMO
For demonstration and testing purposes, I've included [SampleService](https://github.com/iamiddy/rest-async-processing/blob/master/src/main/java/com/iamiddy/service/SampleService.java) MessageProducer , which takes in a `BlockingQueue` and a `MessageConsumer`

 - `produceMessage` implementation takes in a request, processes it (by reversing the request body), construct a response and puts it into the BlockingQueue for Consumption <br/>
 - [SampleService](https://github.com/iamiddy/rest-async-processing/blob/master/src/main/java/com/iamiddy/service/SampleService.java)  also implements Runnable's `run` method, for consuming messages from the `BlockingQueue` which internally calls the MessageConsumer's `onMessage` method.
 
SampleTest that you can run
```java
public class SampleServiceTest {

    private SyncAsyncProcessor processor;
    private MessageConsumer consumer;
    private SampleService service;
    ExecutorService ex = Executors.newFixedThreadPool(2);


    @Before
    public void setUp(){
        this.consumer = new MessageConsumerImpl();
        this.service = new SampleService(new LinkedBlockingQueue<>(),this.consumer);
        this.processor = SyncAsyncProcessor.getInstance(service,consumer);
        ex.execute(service); //run() will call onMessage() for any new message on a queue
    }

    @Test
    public void testResponse()  {
        IntStream.range(0,20).forEach(d ->{
            RequestEvent re = new RequestEvent("TT" + d, "message trying out " + d);

            try {
                ResponseEvent resp = processor.sendAndReceive(re, 3000); 
                System.out.println(resp);
                assertEquals(re.getEventId(),resp.getEventId());
                assertEquals(new StringBuilder(re.getRequestBody()).reverse().toString(),resp.getResponseBody());
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }

        });


    }


}
```
