package OperatorsAndTransformations;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.time.Instant;

public class MergeExample {

    public static void main(String[] args) throws InterruptedException{
        Long start=System.currentTimeMillis();
        /*fastAlgo("fast").subscribe(s->System.out.println(s));
        preciseAlgo("precise").subscribe(s->System.out.println(s));
        experimentalAlgo("experimental").subscribe(s->System.out.println(s));
        System.out.println("Elapsed time: "+(System.currentTimeMillis()-start));*/

        //merge does not improve the running time...
        //all processes are taking place in the main thread
        start=System.currentTimeMillis();
        Observable.merge(fastAlgo("fast"),
            preciseAlgo("precise"),
            experimentalAlgo("experimental")).subscribe(s->System.out.println(s));;
        System.out.println("Elapsed time: "+(System.currentTimeMillis()-start));

        //In order to execute each operation on a thread we use subscribeOn
        start=System.currentTimeMillis();
        Observable.merge(fastAlgo("fast").subscribeOn(Schedulers.io()),
            preciseAlgo("precise").subscribeOn(Schedulers.io()),
            experimentalAlgo("experimental").subscribeOn(Schedulers.io())).subscribe(s->System.out.println(s));;
        System.out.println("Elapsed time: "+(System.currentTimeMillis()-start));
        Thread.sleep(20000); //main thread waits for the spawned thread to finish execution
        //Total duration becomes just the duration of the longest running process, as expected
    }

    static Observable<String> fastAlgo(String string) throws InterruptedException{
        //Fast but poor quality
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                System.out.println("Start fast: "+Instant.now());
                try {
                    Thread.sleep(5000);
                }catch (InterruptedException e){}
                System.out.println(Thread.currentThread().getName());
                subscriber.onNext(string);
                System.out.println("End fast: "+Instant.now());
            }
        });
    }
    static Observable<String> preciseAlgo(String string) {
        //Precise but can be expensive
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                System.out.println("Start precise: "+Instant.now());
                try {
                    Thread.sleep(10000);
                }catch (InterruptedException e){}
                System.out.println(Thread.currentThread().getName());
                subscriber.onNext(string);
                System.out.println("End precise: "+Instant.now());
            }
        });
    }
    static Observable<String> experimentalAlgo(String string){
        //Unpredictable, running anyway
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                System.out.println("Start experimental: "+Instant.now());
                try {
                    Thread.sleep(15000);
                }catch (InterruptedException e){}
                System.out.println(Thread.currentThread().getName());
                subscriber.onNext(string);
                System.out.println("End experimental: "+Instant.now());
            }
        });
    }
}
