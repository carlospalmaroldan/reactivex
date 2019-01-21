package OperatorsAndTransformations;

import rx.Observable;
import twitter4j.*;

public class TakeExample {

    public static void main(String[] args) {
        TwitterStream twitterStream = FilterExample.configureStream();
        Observable<Status> statusObservable = Observable.create(subscriber -> {
                twitterStream.addListener(new StatusListener() {
                    @Override public void onStatus(Status status) {
                        subscriber.onNext(status);
                    }

                    @Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

                    }

                    @Override public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

                    }

                    @Override public void onScrubGeo(long userId, long upToStatusId) {

                    }

                    @Override public void onStallWarning(StallWarning warning) {

                    }

                    @Override public void onException(Exception ex) {

                    }
                });
                twitterStream.sample();
            }

        );
        //print the first status that is written in a certain language
      /*  statusObservable.filter(status -> status.getLang().equalsIgnoreCase("en")).take(1).subscribe(System.out::println);
        statusObservable.takeFirst(status -> status.getLang().equalsIgnoreCase("en")).subscribe(System.out::println);*/
        statusObservable.takeWhile(status -> status.getLang().equalsIgnoreCase("en")).subscribe(System.out::println);
    }


}
