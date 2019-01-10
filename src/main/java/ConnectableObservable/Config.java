package ConnectableObservable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.observables.ConnectableObservable;
import twitter4j.*;

@Configuration
    public class Config implements ApplicationListener<ContextRefreshedEvent>{
    //the idea is that we are able to control the emission of values, this happens only after the connect method has been called
    //on a connectable observable
        private final ConnectableObservable<Status> observable =
        Observable.<Status>create(subscriber -> {
            System.out.println("Some subscriber has subscribed to the observable");
             TwitterStream twitterStream= TwitterExamples.configureStream();
             twitterStream.addListener(new StatusListener() {
                 @Override public void onStatus(Status status) {
                     subscriber.onNext(status);
                 }

                 @Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

                 }

                 @Override public void onTrackLimitationNotice(int i) {

                 }

                 @Override public void onScrubGeo(long l, long l1) {

                 }

                 @Override public void onStallWarning(StallWarning stallWarning) {

                 }

                 @Override public void onException(Exception e) {

                 }
             });
             twitterStream.sample();
        }).publish(); //this transforms the regular observable into a connectable observable

        @Override
        //this method is called when everything has been wired by spring, this means everyone has had a chance to subscribe to observable
        public void onApplicationEvent(ContextRefreshedEvent event){
            System.out.println("Connecting so that data can be emitted to the interested subscribers");
            observable.connect();
        }



        @Bean //the output of the method is registered as a bean within a bean factory, the bean name will be the same as the method name
        public Observable<Status> observable() {
            return observable;
        }



        @Component
        class Foo {
            @Autowired //spring automatically injects the observable we just created
            public Foo(Observable<Status> tweets) {
                tweets.subscribe(status -> {
                    System.out.println(status.getText());
                });
                System.out.println("Foo has Subscribed");
            }
        }
        @Component
        class Bar {
        @Autowired
            public Bar(Observable<Status> tweets) {
                tweets.subscribe(status -> {
                    System.out.println(status.getText());
                });
                System.out.println("Bar has Subscribed");
            }
        }
}
