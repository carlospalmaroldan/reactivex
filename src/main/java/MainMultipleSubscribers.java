import rx.Subscriber;
import twitter4j.Status;

public class MainMultipleSubscribers {


    public static void main(String[] args) throws InterruptedException{
        MultipleSubscribersTwitter multipleSubscribersTwitter=new MultipleSubscribersTwitter();
        multipleSubscribersTwitter.observe().subscribe(new Subscriber<Status>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Status status) {
                //another thread takes charge of reading the twitter messages and processing them
                System.out.println("Thread "+Thread.currentThread().getName());
                System.out.println(status);
            }
        });

        while(true){
            Thread.sleep(1000);
            System.out.println("Thread "+Thread.currentThread().getName());
            System.out.println("main not blocked");
        }
    }

}
