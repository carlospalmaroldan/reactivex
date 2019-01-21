package OperatorsAndTransformations;


import rx.Subscriber;
import twitter4j.*;

import rx.Observable;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterExample {
    private static final Logger logger
        = LoggerFactory.getLogger(FilterExample.class);

    public static TwitterStream configureStream(){ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(false)
            .setPrettyDebugEnabled(false)
            .setDaemonEnabled(false)
            .setOAuthConsumerKey("vkmERHUXKaUJwL1g3lWamaiyM")
            .setOAuthConsumerSecret("Kz0hkVeDqafSoTunQmLqTqfTDDprrhRBA8kyvrRSQhe2dcUHA4")
            .setOAuthAccessToken("941447676176097280-DyXqo2VQm1Kna5Ex6iBIsxIggtr7Ibm")
            .setOAuthAccessTokenSecret("ibx9FjwmrnIesbpXzsWyWBRxbgCrNdxMrGvYzAKVqnZbz");

        Configuration configuration= configurationBuilder.build();
        TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();
        /*TwitterStream twitterStream = new TwitterStreamFactory().getInstance();*/
        return twitterStream;
    }

    public static void main(String[] args) throws InterruptedException{
        /*System.setProperty ("twitter4j.loggerFactory",
        "twitter4j.internal.logging.NullLoggerFactory");

        System.out.println(args[0]);*/
        TwitterStream twitterStream=configureStream();
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

        Observable<Status> spanishTweets=observable.filter(status -> status.getLang().equalsIgnoreCase("es"));
        spanishTweets.subscribe(new Subscriber<Status>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Status status) {
                System.out.println(status.getText());
            }
        });
        twitterStream.sample();
       /* while(true){
            System.out.println("main not dead");
            Thread.sleep(1000);
        }*/

    }

}
