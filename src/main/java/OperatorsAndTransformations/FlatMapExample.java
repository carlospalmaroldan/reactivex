package OperatorsAndTransformations;
import rx.Observable;
import rx.Subscriber;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class FlatMapExample {

    public enum DayOfWeek{SUNDAY,MONDAY}

    Observable<String> loadRecordsFor(DayOfWeek dow) {
            switch(dow) {
                case SUNDAY:
                    return Observable
                        .interval(90, MILLISECONDS)
                        .take(5)
                        .map(i -> "Sun-" + i);
                case MONDAY:
                    return Observable
                        .interval(65, MILLISECONDS)
                        .take(5)
                        .map(i -> "Mon-" + i);
                default:
                    return Observable
                        .interval(300, MILLISECONDS)
                        .take(5)
                        .map(i -> "Def-" + i);
        }
    };
    public static void main(String[] args) throws  InterruptedException{

        //for each one of the elements of the observable sequence we  generate a sequence of observables
        //thus the need to use flatMap arises. Problem is that the emissions are not ordered, we would
        //expect to see all emissions for the first element of the observable stream, but these get
        //intertwined with emissions for the second element
        //flatmap subscribes to the internal substreams
        FlatMapExample flatMapExample=new FlatMapExample();
        Observable<String> out= Observable
            .just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
            .flatMap(flatMapExample::loadRecordsFor);

        out.subscribe(new Subscriber<String>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(String s) {
                System.out.println(s);
            }
        });

        Thread.sleep(10000);

        //concatMap preserves the order, meaning that we first process the observables generated for the first element
        //in the sequence of observables
        Observable
            .just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
            .concatMap(flatMapExample::loadRecordsFor).subscribe(new Subscriber<String>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(String s) {
                System.out.println(s);
            }
        });

        Thread.sleep(10000);
    }
}
