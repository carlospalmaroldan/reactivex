import rx.Observable;
import rx.Subscriber;

import java.math.BigInteger;

public class InfiniteStreams {
    public static void main(String[] args){

        //We create observable on the main thread, but inside of it we make sure we are using a different thread to
        //execute the code, this guarantees that the main thread is not blocked
        Observable<BigInteger> naturalNumbers= Observable.create(new Observable.OnSubscribe<BigInteger>() {
            @Override public void call(Subscriber<? super BigInteger> subscriber) {
                      Runnable runnable=  new Runnable(){
                            @Override public void run() {
                                BigInteger i=BigInteger.ZERO;
                                while(true){
                                    subscriber.onNext(i);
                                    i=i.add(BigInteger.ONE);
                                }
                            }
                        };
                      new Thread(runnable).start();
            }
        });

        naturalNumbers.subscribe(System.out::println);
        while(true) {
            System.out.println("main thread did not stop");
        }
    }

}
