import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;
import twitter4j.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TwitterObservable {

        public static void main(String[] args) throws InterruptedException{
           Observable<Status> statusObservable=Observable.create(subscriber -> {
                //if we don't do this the main thread is used for processing the observable and we want the subscriber to unsubscribe
                Runnable runnable=new Runnable() {
                        @Override public void run() {
                            TwitterStream twitterStream = TwitterExamples.configureStream();
                            MyStatusListener listener=new MyStatusListener(subscriber,twitterStream);
                            twitterStream.addListener(listener);
                            //magical line to allow the subscriber to unsubscribe, immediately disconnects subscriber
                            subscriber.add(Subscriptions.create(twitterStream::shutdown));
                            twitterStream.sample();//this creates another thread that gets the tweets for us
                            try {
                                TimeUnit.SECONDS.sleep(10);
                            }catch (InterruptedException e){

                            }
                            twitterStream.shutdown();
                        }
                    };
                new Thread(runnable).start();
               }
           );
           Subscriber<Status> subscriber=new Subscriber<Status>() {
               @Override public void onCompleted() {

               }

               @Override public void onError(Throwable e) {
                    System.out.println(e);
               }

               @Override public void onNext(Status status) {
                    System.out.println(status);
               }
           };
            statusObservable.subscribe(subscriber);
            Long start=System.currentTimeMillis();
            //Even though we were supposed to receive 10 seconds of updates, we choose to unsubscribe
            TimeUnit.SECONDS.sleep(5);
            System.out.println(System.currentTimeMillis()-start);
            subscriber.unsubscribe();
        }
}
