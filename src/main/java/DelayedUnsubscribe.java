import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

import java.util.concurrent.TimeUnit;

public class DelayedUnsubscribe {

    static void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException ignored) {
            //intentionally ignored
        }
    }

    static Observable<Integer> delay(Integer integer){
            return Observable.create(new Observable.OnSubscribe<Integer>() {
                @Override public void call(Subscriber<? super Integer> subscriber) {
                    Runnable runnable=new Runnable() {
                        @Override public void run() {
                            sleep(10,TimeUnit.SECONDS);
                            if(!subscriber.isUnsubscribed()){
                                subscriber.onNext(integer);
                                subscriber.onCompleted();
                            }
                        }
                    };
                    Thread thread=new Thread(runnable);
                    thread.start();
                    //This line allows the subscriber to unsubscribe before the message is sent
                    //we no longer wait to contact the subscriber only to realize he has unsubscribed
                    subscriber.add(Subscriptions.create(thread::interrupt));
                }
            });
    }

    public static void main(String[] args){
       Observable<Integer> delayedInteger= delay(13);
       Subscriber<Integer> subscriber= new Subscriber<Integer>() {
           @Override public void onCompleted() {
                System.out.println("completed");
           }

           @Override public void onError(Throwable e) {
               System.out.println("error");
           }

           @Override public void onNext(Integer integer) {
               System.out.println(integer);
           }
       };
       delayedInteger.subscribe(subscriber);
       //subscriber sends a signal to the observable that it is no longer interested in its data
        //all resources are released and program terminates, the thread is gracefully destroyed
       subscriber.unsubscribe();
       while(true){
           try {
               Thread.sleep(1000);
               System.out.println("main not blocked");
           }catch (InterruptedException e){}
       }
    }
}
