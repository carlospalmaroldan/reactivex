import rx.Observable;
import twitter4j.*;

public final class TweetObservable {

    public static Observable<Status> tweetObservable(final String[] searchKeywords){
        return Observable.create(
                    subscriber->{
                            final TwitterStream twitterStream=new TwitterStreamFactory().getInstance();
                            twitterStream.addListener(new StatusAdapter(){
                            public void onStatus(Status status){
                                    subscriber.onNext(status);
                            }
                            public void onException(Exception ex){
                                    subscriber.onError(ex);
                            }
                        });
                            FilterQuery query=new FilterQuery();
                            query.track(searchKeywords);
                            twitterStream.filter(query);
                    }

        );
    }

}
