package OperatorsAndTransformations;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public class ReduceExample {


    public static void main(String[] args){

        Observable<Integer> sequence=Observable.range(10,20).reduce((init,value)->value+init);
        Observable.range(10,20).subscribe(System.out::println);
        Observable.range(10,20).scan((init,value)->value+init).subscribe(System.out::println);
        sequence.subscribe(System.out::println);
        //we can use reduce to transform a stream of observables into an observable that emits a single value, like a collection

        Observable<List<Integer>> integerList=Observable.range(10,20).reduce(new ArrayList<>(),(list,value)->{list.add(value);return list;});
        integerList.subscribe(System.out::println);
    }
}
