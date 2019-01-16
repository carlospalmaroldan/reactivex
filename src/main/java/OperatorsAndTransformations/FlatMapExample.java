package OperatorsAndTransformations;
import org.omg.CORBA.OBJ_ADAPTER;
import rx.Observable;
import rx.Subscriber;

import java.util.Arrays;

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
        /*FlatMapExample flatMapExample=new FlatMapExample();
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

        Thread.sleep(10000);*/

        Observable<String> numbers=Observable.from(Arrays.asList("1","2","3","4","5","6","7","8"));
        Observable<String> letters=Observable.from(Arrays.asList("h","i","j","k","l","m","n","o"));


        Observable<String> letterArgument=Observable.create(string->System.out.println(string));

        /*letters.flatMap(l->letters).map(l->l).subscribe(System.out::println);*/
       /* numbers.flatMap(n->methodThatPassesArgumentToObservable(n)).subscribe(System.out::println);*/
        //nested maps behave like nested for loops
        numbers.flatMap(n->letters.map(l->l+n)).subscribe(System.out::println);
        numbers.flatMap(n1->numbers.flatMap(n2->numbers.map(n3->n1+n2+n3))).subscribe(System.out::println);
    }

    public static Observable<String> methodThatPassesArgumentToObservable(String argument){
        Observable<String> letterArgument=Observable.from(Arrays.asList("h","i","j","k","l","m","n","o")).map(e->e+argument);
        return letterArgument;
    }
}
