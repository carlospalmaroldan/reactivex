import java.math.BigInteger;
import rx.Observable;
import rx.Subscriber;

public class InfiniteStreamsBlocking {
    public static void main(String[] args){
        Observable<BigInteger> naturalNumbers=Observable.create(new Observable.OnSubscribe<BigInteger>() {
            @Override public void call(Subscriber<? super BigInteger> subscriber) {
                BigInteger i=BigInteger.ZERO;
                while(true){
                    subscriber.onNext(i);
                    i=i.add(BigInteger.ONE);
                }
            }
        });
        // the observable is created on the main thread and it is subscribed to in the same thread, this means
        // that we are blocking this thread when generating the sequence of all natural numbers
        naturalNumbers.subscribe(System.out::println);
    }
}
