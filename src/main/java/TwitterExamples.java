
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import rx.Subscriber;
import rx.functions.Action2;
import sun.nio.ch.ThreadPool;
import twitter4j.*;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.math.BigInteger;
import rx.Observable;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TwitterExamples {


    public static TwitterStream configureStream(){ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(false)
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


    private static void countTweets(TwitterStream twitterStream)throws Exception{
        Callable<BigInteger> callable=new Callable<BigInteger>(){
            BigInteger number=BigInteger.ZERO;
            public  BigInteger call(){
                twitterStream.addListener(new StatusListener() {

                    /* BigInteger count=count;*/

                    @Override public void onException(Exception e) {
                        System.out.println("Error callback"+ e);
                    }

                    @Override public void onStatus(Status status) {
                        number=number.add(BigInteger.ONE);
                        /*System.out.println(number);*/
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
                try {
                    TimeUnit.SECONDS.sleep(100);
                }catch(InterruptedException e){}
                twitterStream.shutdown();
                return number;
            }

        };
        ExecutorService executorService=Executors.newFixedThreadPool(5);
        Future<BigInteger> future=executorService.submit(callable);

        System.out.println(future.get()); //future.get is blocking
    }

    private static Observable<BigInteger> nonBlockingCountTweets(TwitterStream twitterStream){
             ArrayList<BigInteger> phraseIDs = new ArrayList<>();
            Observable<BigInteger> observable=Observable.create(subscriber-> {
                Runnable runnable = new Runnable() { //default behavior of observables is use the main thread
                    @Override public void run() {
                        twitterStream.addListener(new StatusListener() {
                            BigInteger count = BigInteger.ZERO;

                            @Override public void onStatus(Status status) {
                                count = count.add(BigInteger.ONE);
                                subscriber.onNext(count);
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
                                subscriber.onError(e);
                            }
                        });
                        twitterStream.sample();//this creates another thread that gets the tweets for us
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                        }
                        twitterStream.shutdown();

                    }
                };
                ExecutorService executorService=Executors.newFixedThreadPool(5);
                executorService.execute(runnable);
            });

           /*BigInteger[] integers={BigInteger.ONE,BigInteger.ONE.add(BigInteger.ONE)};
            Observable<BigInteger> observable= Observable.from(integers).reduce((seed, value) -> {
                // sum all values from the sequence
                System.out.println("reduce-seed = " + seed);
                System.out.println("reduce-value = " + value);
                return value;
            });*/

            return  observable;
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
       // consume(twitterStream,x->System.out.println(x),x->System.out.println(x)); //this blocks the main thread
        try {
            //this is blocking
            // countTweets(twitterStream);
            //this one is not blocking, we can process message while the main thread is still doing its thing
            nonBlockingCountTweets(twitterStream).subscribe(System.out::println);
            while(true){
                System.out.println("MAIN THREAD NOT DEAD");
                Thread.sleep(1000);
            }
        }catch(Exception e){

        }



    }
}
