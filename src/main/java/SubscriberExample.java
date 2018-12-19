import rx.Observable;
import rx.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class SubscriberExample {


    Subscriber<String> subscriber=new Subscriber<String>() {

        Integer n=0;

        @Override public void onCompleted() {

        }

        @Override public void onError(Throwable e) {
            System.out.println(e);
        }

        @Override public void onNext(String s) {
            System.out.println(s);
            n=n+1;
            if(n==3){
                unsubscribe();
            }
        }
    };



    static Observable<Integer> ints=Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override public void call(Subscriber<? super Integer> subscriber) {
            subscriber.onNext(5);
            subscriber.onNext(6);
            subscriber.onNext(7);
            subscriber.onCompleted();
        }
    });

    private static void log(Object msg) {
        System.out.println(
            Thread.currentThread().getName() +
                ": " + msg);
    }




    //Implementing range using create
    private static  Observable<Integer> range(Integer start,Integer number){
        return Observable.create(new Observable.OnSubscribe<Integer>(){
            int count=0;
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
               while(count<number+1){
                   subscriber.onNext(start+count);
                   count=count+1;
               }
            }
        });
    }





    public static void main(String[] args){
        SubscriberExample subscriberExample=new SubscriberExample();
        //only the three first elements are printed since the subscriber has the ability to unsubscribe itself
        List<String> list=new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        list.add("four");
        Observable<String> sequence=Observable.from(list);
        sequence.subscribe(subscriberExample.subscriber);
        //only the main thread is used
        log("Starting");
        ints.subscribe(i -> log("Element: " + i));
        ints.subscribe(new Subscriber<Integer>() {
            @Override public void onCompleted() {
                System.out.println("completed");
            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Integer integer) {
                log("Element: " + integer);
            }
        });
        log("Exit");
        range(5,3).subscribe(System.out::println);


    }
}
