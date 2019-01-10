import rx.Subscriber;
import twitter4j.Status;

import java.util.ArrayList;

public class MainSubject {


    public static void main(String[] args){
        TwitterSubject twitterSubject=new TwitterSubject();
        //Externally a subject can be treated like just another observable, multiple subscribers
        //can subscribe to it, we no longer have to keep track of the individual subscribers
        //in order to turn on/off the stream of data
       /* Subscriber<Status> subscriber1= new Subscriber<Status>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Status status) {
                System.out.println(status);
            }
        };
        Subscriber<Status> subscriber2= new Subscriber<Status>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Status status) {
                System.out.println(status);
            }
        };
        twitterSubject.observe().subscribe(subscriber1);
        twitterSubject.observe().subscribe(subscriber2);*/
        ArrayList<Thread> threadArrayList=new ArrayList<Thread>();
        //Theoretically it is possible to break a subject by calling its onNext() method simultaneously from
        //different threads.
        for(int i=0;i<10000;i++){
            Runnable runnable=new Runnable() {
                @Override public void run() {
                    Subscriber<Status> subscriber=new Subscriber<Status>() {
                        @Override public void onCompleted() {

                        }

                        @Override public void onError(Throwable e) {

                        }

                        @Override public void onNext(Status status) {
                            System.out.println(status);
                        }
                    };
                    twitterSubject.observe().subscribe(subscriber);
                }
            };
            Thread thread=new Thread(runnable);
            threadArrayList.add(thread);

        }
        threadArrayList.stream().forEach(t->t.start());

    }
}
