import javafx.animation.Animation;
import rx.Observable;
import rx.subjects.PublishSubject;
import twitter4j.*;

public class TwitterSubject {
    //We no longer need to manually close the twitter stream when no ones is interested in listening to it,
    //or open it when a subscriber subscribes.
    private final PublishSubject<Status> publishableSubject= PublishSubject.create();


    public Observable<Status> observe(){
        return publishableSubject;
    }

    public TwitterSubject(){
        TwitterStream twitterStream=TwitterExamples.configureStream();
        twitterStream.addListener(
            new StatusListener() {
                @Override public void onStatus(Status status) {
                    publishableSubject.onNext(status);
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
            }
        );
        //eagerly fetching data, which means that as soon as this class in instantiated we connect to the twitter stream
        twitterStream.sample();
    }



}
