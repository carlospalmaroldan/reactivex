import rx.Observable;

public class Caching {


    public static void main(String[] args){
        Observable<Integer> ints =
            Observable.create(subscriber -> {
                    log("Create");
                    subscriber.onNext(42);
                    subscriber.onCompleted();
                }
            );
        log("Starting");
        //for each subscriber all the computations within the observable need to be performed
        ints.subscribe(i -> log("Element A: " + i));
        ints.subscribe(i -> log("Element B: " + i));
        log("Exit");
        //we can cache the observable results so that a new subscriber uses the cached values instead of asking
        //for the computation
        Observable<Object> ints1 =
            Observable.create(subscriber -> {
                    log("Create");
                    subscriber.onNext(42);
                    subscriber.onCompleted();
                }
            ).cache();
        ints1.subscribe(i -> log("Element C: " + i));
        ints1.subscribe(i -> log("Element D: " + i));
    }


    private static void log(Object msg) {
        System.out.println(
            Thread.currentThread().getName() +
                ": " + msg);
    }

}
