import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.subscriptions.Subscriptions;
import twitter4j.*;

public class ConnectableObservableExample {



    public static void main(String[] args) throws InterruptedException{
        Observable<Status> observable=Observable.create(
            subscriber -> {

                TwitterStream twitterStream=TwitterExamples.configureStream();
                twitterStream.addListener(new StatusListener() {
                    @Override public void onStatus(Status status) {
                        subscriber.onNext(status);
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

                    }
                });
                subscriber.add(Subscriptions.create(() -> {
                    System.out.println("Disconnecting");
                    twitterStream.shutdown();
                }));
                twitterStream.sample();
            }
        );

        //by using publish and refcount we can avoid having more than one connection to the twitter stream
        /*Observable<Status> connectableObservable= observable.publish().refCount();
        System.out.println("Before subscribers");
        Subscription sub1 = connectableObservable.subscribe();
        System.out.println("Subscribed 1");
        Subscription sub2 = connectableObservable.subscribe();
        System.out.println("Subscribed 2");
        Thread.sleep(5000);
        sub1.unsubscribe();
        System.out.println("Unsubscribed 1");
        sub2.unsubscribe();
        System.out.println("Unsubscribed 2");*/


        //in the old way, just subscribing to the observable, we open two streams of data, which is wasteful
        observable.subscribe(new Subscriber<Status>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Status status) {
                System.out.println("status arrived");
            }
        });
        observable.subscribe(new Subscriber<Status>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Status status) {
                System.out.println("status arrived");
            }
        });

        //we can have
        ConnectableObservable<Status> published = observable.publish();
        published.connect();
    }

}
