
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import twitter4j.*;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TwitterExamples {


    public static TwitterStream configureStream(){ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
            .setOAuthConsumerKey("vkmERHUXKaUJwL1g3lWamaiyM")
            .setOAuthConsumerSecret("Kz0hkVeDqafSoTunQmLqTqfTDDprrhRBA8kyvrRSQhe2dcUHA4")
            .setOAuthAccessToken("941447676176097280-DyXqo2VQm1Kna5Ex6iBIsxIggtr7Ibm")
            .setOAuthAccessTokenSecret("ibx9FjwmrnIesbpXzsWyWBRxbgCrNdxMrGvYzAKVqnZbz");

        Configuration configuration= configurationBuilder.build();
        TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();
        return twitterStream;
    }

    private static  void print(TwitterStream twitterStream) throws InterruptedException,TwitterException{
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
        twitterStream.sample();//this creates another thread that gets the tweets for us
        TimeUnit.SECONDS.sleep(10);
        twitterStream.shutdown();
    }


    private static void countTweets(TwitterStream twitterStream)throws InterruptedException{
        twitterStream.addListener(new StatusListener() {

            BigInteger count=BigInteger.ZERO;

            @Override public void onException(Exception e) {
                System.out.println("Error callback"+ e);
            }

            @Override public void onStatus(Status status) {
                count=count.add(BigInteger.ONE);
                System.out.println(count);
            }

            @Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override public void onTrackLimitationNotice(int i) {

            }

            @Override public void onScrubGeo(long l, long l1) {

            }

            @Override public void onStallWarning(StallWarning stallWarning) {

            }
        });
        twitterStream.sample();//this creates another thread that gets the tweets for us
        TimeUnit.SECONDS.sleep(10);
        twitterStream.shutdown();
    }

    //we can have a more general method that takes as parameters what we want to do with the information on the
    //stream, this approach is different from using only variables as parameters. This allows us to separate reading from
    //the HTTP connection from actually processing the data

    private static void consume(TwitterStream twitterStream,Consumer<Status> action,Consumer<Exception> exception) throws InterruptedException{
            twitterStream.addListener(new StatusListener() {
                @Override public void onStatus(Status status) {
                    action.accept(status);
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
                    exception.accept(e);
                }
            });
        //this creates another thread that gets the tweets for us, that means creating a HTTP connection
        twitterStream.sample();
        TimeUnit.SECONDS.sleep(10);
        twitterStream.shutdown();
    }

    public static void main(String[] args) throws InterruptedException,TwitterException{
        TwitterStream twitterStream=configureStream();
        BigInteger count = BigInteger.ZERO;
        consume(twitterStream,x->System.out.println(x),x->System.out.println(x));

    }
}
