
import twitter4j.*;

import java.util.concurrent.TimeUnit;

public class Twitter {

    private static  void listen() throws InterruptedException{
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new twitter4j.StatusListener() {

            public void onStatus(Status status) {
                System.out.println("Status: {}"+ status);
            }

            public void onException(Exception ex) {
                System.out.println("Error callback"+ ex);
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            public void onTrackLimitationNotice(int i) {

            }

            public void onScrubGeo(long l, long l1) {

            }

            public void onStallWarning(StallWarning stallWarning) {

            }

        });
        twitterStream.sample();
        TimeUnit.SECONDS.sleep(10);
        twitterStream.shutdown();
    }

    public static void main(String[] args) throws InterruptedException{
        listen();
    }
}
