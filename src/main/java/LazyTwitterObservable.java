import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;
import twitter4j.*;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


//How to handle multiple subscribers without creating a connection each time someone subscribes to the observable stream
public class LazyTwitterObservable {
    private final Set<Subscriber<? super Status>> subscribers =
        new CopyOnWriteArraySet<>();
    private final TwitterStream twitterStream;


    private final Observable<Status> observable = Observable.create(
        subscriber -> {
            register(subscriber);
            //somehow this line allows the subscriber to be removed from the array when calling the unsubscribe method
            subscriber.add(Subscriptions.create(() ->this.deregister(subscriber)));
        });

    //configures the twitter stream and sends the status to each and every subscriber in the array
    public LazyTwitterObservable(){
        this.twitterStream=TwitterExamples.configureStream();
        this.twitterStream.addListener(new StatusListener() {
            @Override public void onStatus(Status status) {
                System.out.println(Thread.currentThread().getName()); //the statuses are passed to the subscriber on another thread
                subscribers.forEach(s -> s.onNext(status));
            }

            @Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override public void onTrackLimitationNotice(int i) {

            }

            @Override public void onScrubGeo(long l, long l1) {

            }

            @Override public void onStallWarning(StallWarning stallWarning) {

            }

            @Override public void onException(Exception e) {
                subscribers.forEach(s -> s.onError(e));
            }
        });
    }

    Observable<Status> observe() {
        return observable;
    }

    private synchronized void register(Subscriber<? super Status> subscriber) {
        if (subscribers.isEmpty()) {
            System.out.println("REGISTERING SUBSCRIBER:"+Thread.currentThread().getName()); //subscriber is added to the array on the main thread
            subscribers.add(subscriber);
            twitterStream.sample();
        } else {
            subscribers.add(subscriber);
        }
    }

    private synchronized void deregister(Subscriber<? super Status> subscriber) {
        subscribers.remove(subscriber);
        if (subscribers.isEmpty()) {
            twitterStream.shutdown();
        }
    }
}
