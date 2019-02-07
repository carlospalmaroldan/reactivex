package OperatorsAndTransformations;
import rx.Observable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Random;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ConcatExample {

    static Observable<String> speak(String quote, long millisPerChar) {
        String[] tokens = quote.replaceAll("[:,]", "").split(" ");
        Observable<String> words = Observable.from(tokens);
        Observable<Long> absoluteDelay = words
            .map(String::length)
            .map(len -> len * millisPerChar)
            .scan((total, current) -> total + current);
        return words
            .zipWith(absoluteDelay.startWith(0L), Pair::of)
            .flatMap(pair -> Observable.just(pair.getLeft()).delay( pair.getRight(),MILLISECONDS));
    }

    public static void main(String[] args) throws InterruptedException{
        /*Observable<String> words = Observable.from(Arrays.asList("Don't","ask","what","your","country"));
        Observable<Long> absoluteDelay = words
            .map(String::length)
            .map(len -> len * 10).map(Integer::longValue)
            .scan((total, current) -> total + current);
         words.zipWith(absoluteDelay.startWith(0L), Pair::of)
             .subscribe(System.out::println);*/





        Observable<String> alice = speak(
            "To be, or not to be: that is the question", 110);
        Observable<String> bob = speak(
            "Though this be madness, yet there is method in't", 90);
        Observable<String> jane = speak(
                        "There are more things in Heaven and Earth, " +
                "Horatio, than are dreamt of in your philosophy", 100);


        //merge subscribes right away to all the observables
       /* Observable
            .merge(
                alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob: " + w),
                jane.map(w -> "Jane: " + w)
            )
            .subscribe(System.out::println);;
*/

       //concat starts emitting from an observable only when the previous one has finished its emissions
        /*Observable
            .concat(
                alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob: " + w),
                jane.map(w -> "Jane: " + w)
            )
            .subscribe(System.out::println);*/



        //we use just to create an observable from another one but with a delay
        //the speak method allowed us to introduce a delay to every word spoken by a person, thus
        //guaranteeing that they are emitted in the correct order
        //Now on top of that we are adding a general delay to each one of the observables that model the speech
        //of each person (the outer ones).
        Random rnd = new Random();
        Observable<Observable<String>> quotes = Observable.just(
            alice.map(w -> "Alice: " + w),
            bob.map(w -> "Bob: " + w),
            jane.map(w -> "Jane: " + w))
            .flatMap(innerObs -> Observable.just(innerObs)
                .delay(rnd.nextInt(5), SECONDS));

        //switchonNext subscribes to the first observable but when another one starts emitting it switches and abandons
        //the former for the new
        Observable
            .switchOnNext(quotes)
            .subscribe(System.out::println);


        //this is important because when using delay, the action is performed in a different thread
        Thread.sleep(20000);
    }

}
