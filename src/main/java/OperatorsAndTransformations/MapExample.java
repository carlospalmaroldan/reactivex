package OperatorsAndTransformations;

import rx.Observable;
import rx.Subscriber;
import twitter4j.*;

public class MapExample {


    public static void main(String[] args){
        TwitterStream twitterStream=FilterExample.configureStream();
        Observable<Status> observable=Observable.create(subscriber->{
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

        });
        //doOnNext can be used as a probe when composing operations on observables
        Observable<Integer> lengthObservable=observable.doOnNext(i -> System.out.println("Original : " + i))
            .map(status -> status.getText().length())
            .doOnNext(i -> System.out.println("mapped: " + i));
        lengthObservable.subscribe(new Subscriber<Integer>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Integer integer) {
                System.out.println(integer);
            }
        });

        twitterStream.sample();
    }
}
