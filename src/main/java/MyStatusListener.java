import rx.Subscriber;
import twitter4j.*;

public class MyStatusListener implements StatusListener{
        Subscriber<? super  Status> subscriber;
        TwitterStream twitterStream;

        public MyStatusListener(Subscriber<? super Status> subscriber, TwitterStream twitterStream){
            this.subscriber=subscriber;
            this.twitterStream=twitterStream;
        }

        @Override public void onException(Exception e) {
            subscriber.onError(e);
        }

        @Override public void onStatus(Status status) {
            //manually allowing the subscriber to unsubscribe, has to wait until a new message is passed
            /*if(!subscriber.isUnsubscribed()) {
                subscriber.onNext(status);
            }else{
                twitterStream.shutdown();
            }*/
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


}
