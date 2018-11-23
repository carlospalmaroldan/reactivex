import rx.Observable;
import rx.observables.ConnectableObservable;
import twitter4j.Status;

public class SentimentAnalyzer {
    public static void main(String[] args) throws Exception{
        String[] searchTerms={"Justing Bieber"};
        ConnectableObservable<Status> observable=TweetObservable.tweetObservable(searchTerms).publish();
        observable.connect();
        Observable<String> tweetStream=observable.map(Status::getText);
        tweetStream.subscribe(System.out::println);
    }
}
