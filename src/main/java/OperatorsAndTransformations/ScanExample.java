package OperatorsAndTransformations;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class ScanExample {

        public static void main(String[] args) throws InterruptedException{

            //we can accumulate values while they are being emitted
            /*Observable<Long> progress =Observable.from(Arrays.asList(10L, 14L, 12L, 13L, 14L, 16L));
            Observable<Long> totalProgress = progress
                .scan((total, chunk) -> total + chunk);
            totalProgress.subscribe(System.out::println);*/


            //we can even accumulate values coming from an infinite stream!
            Observable.interval(10,TimeUnit.MILLISECONDS).scan((total,chunk)->total+chunk).subscribe(System.out::println);


            Thread.sleep(20000);

        }
}
