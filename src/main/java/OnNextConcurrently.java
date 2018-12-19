import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MICROSECONDS;

public class OnNextConcurrently {
    //OnNext of any observer must never be called concurrently! this is by design
    public static void main(String[] args){
        Observable<Integer> integerObservable=Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override public void call(Subscriber<? super Integer> subscriber) {
                ExecutorService executorService=Executors.newFixedThreadPool(10);

                for(int i=0;i<1400;i++) {
                    executorService.execute(new Runnable() {
                        @Override public void run() {
                            subscriber.onNext(10);
                        }
                    });
                }

            }
        });

        integerObservable.subscribe(System.out::println);


        //timer uses a different thread to generate a zero value after some delay
        Observable
            .timer(1, TimeUnit.SECONDS)
            .subscribe((Long zero) -> log(zero));

        //uses another thread to generate values periodically
        Observable
            .interval(1_000_000 / 60, MICROSECONDS)
            .subscribe((Long i) -> log(i));
    }


    private static void log(Object msg) {
        System.out.println(
            Thread.currentThread().getName() +
                ": " + msg);
    }
}
