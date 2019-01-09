import rx.Subscriber;
import twitter4j.Status;

public class MainSubject {


    public static void main(String[] args){
        TwitterSubject twitterSubject=new TwitterSubject();
        twitterSubject.observe().subscribe(new Subscriber<Status>() {
           @Override public void onCompleted() {

           }

           @Override public void onError(Throwable e) {

           }

           @Override public void onNext(Status status) {
               System.out.println(status);
           }
       });
    }
}
