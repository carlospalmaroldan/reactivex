import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.TimeUnit;

public class Delayed {

    public static <T> Observable<T> delay(T x){

        //basicament la tarea del subscriber es crear un hilo que es en el que se ejecuta todo el codigo
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override public void call(Subscriber<? super T> subscriber) {
                Runnable runnable=new Runnable() {
                    @Override public void run() {
                        sleep(10,TimeUnit.SECONDS);
                        if(!subscriber.isUnsubscribed()){
                            subscriber.onNext(x);
                            subscriber.onCompleted();
                        }
                    }
                };
                new Thread(runnable).start();
            }
        });
    }

    static void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException ignored) {
            //intentionally ignored
        }
    }

    //this implementation forces us to wait 10 seconds even if during that time the subscriber already
    //unsubscribed
    public static void main(String[] args){
        Observable<Integer> delayedInteger=delay(50);
        delayedInteger.subscribe(System.out::println);
        while(true){
            System.out.println("main not interrupted");
            try {
                Thread.sleep(1000);
            }catch(InterruptedException e){

            }
        }

    }
}
