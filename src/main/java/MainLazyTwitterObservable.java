import rx.Subscriber;
import twitter4j.Status;

public class MainLazyTwitterObservable {



        public static void main(String[] args) throws InterruptedException{
            LazyTwitterObservable lazyTwitterObservable=new LazyTwitterObservable();
            Subscriber<Status> subscriber=new Subscriber<Status>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(Status status) {
                    //the processing of the message by the subscriber happens on another thread
                    System.out.println("THREAD IN WHICH SUBSCRIBER PROCESSES: "+Thread.currentThread().getName());
                    System.out.println(status);
                }
            };
            Subscriber<Status> subscriber2=new Subscriber<Status>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(Status status) {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println(status);
                }
            };

            long start=System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName()); //subscription happens  on the main thread
            lazyTwitterObservable.observe().subscribe(subscriber);
            lazyTwitterObservable.observe().subscribe(subscriber2);

            Thread.sleep(5000);//Main thread is not blocked
            subscriber.unsubscribe();
            subscriber2.unsubscribe();
            System.out.println(System.currentTimeMillis()-start);

        }
}
