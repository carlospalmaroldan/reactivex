package OperatorsAndTransformations;

import rx.Observable;

import org.apache.commons.lang3.tuple.Pair;
import rx.Subscriber;

import java.util.Arrays;

public class ComposeExample {

    static <T> Observable<T> odd(Observable<T> upstream) {
        Observable<Boolean> trueFalse = Observable.just(true, false).repeat();
        return upstream
                .zipWith(trueFalse, Pair::of)
                .filter(Pair::getRight)
                .map(Pair::getLeft);
    }


    static <T> Observable.Transformer<T, T> odd() {
        Observable<Boolean> trueFalse = Observable.just(true, false).repeat();
        return upstream -> upstream
                .zipWith(trueFalse, Pair::of)
                .filter(Pair::getRight)
                .map(Pair::getLeft);

    }

    public static void main(String[] args){

        Observable<String> persons=Observable.from(Arrays.asList("Carlos","Marlon","Javier","Jose","Jaime"));
        odd(persons).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });


        //compose allows us to create our own function in the pipeline that can be chained with the other functions of the API
        persons.compose(odd())
                .forEach(System.out::println);



    }

}
