import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.observables.ConnectableObservable;
import rx.subjects.PublishSubject;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    static Integer subscriber1 = 0;
    static Integer subscriber2 = 0;
    public static void main(String[] args) throws Exception{

        Observable<Integer> obs1=naturalNumbers(10);
        obs1.subscribe(System.out::println);

        String[] letters = {"a","b","c"};

        Observable<String> observable=Observable.from(letters);
        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });
        observable.subscribe(getStringObserver());
        observable.subscribe(getStringObserver());

        Observable<String> observable2=Observable.from(letters).map(string->string.toUpperCase());
        observable2.subscribe(getStringObserver());

        Observable<Observable<String>> nested=Observable.from(letters).map(s->Observable.from(letters));
        Observable<String> flat=Observable.from(letters).flatMap(s->Observable.from(letters));

        Observable.from(letters).scan(new StringBuffer(),StringBuffer::append).subscribe(total->{
                   System.out.println(total);
                }
        );

        Integer[] numbers={1,2,3,4,5,6};
        Observable.from(numbers).groupBy(i->0==(i%2)?"EVEN":"ODD").subscribe(group->
                    group.subscribe(number-> {
                               if(group.getKey().toString().equals("EVEN")){
                                   System.out.println(number);
                               }
                            }
                    )
        );


     /*   ConnectableObservable<Long> hotObservable=Observable.interval(1,TimeUnit.SECONDS).publish();
        hotObservable.subscribe(val->System.out.println("Subscriber1>>"+val));
        hotObservable.connect();
        Thread.sleep(5000);  //this proves that observables run on different threads
        hotObservable.subscribe(val->System.out.println("Subscriber2>>"+val));
        Thread.sleep(5000);*/

        Observable<String> tweets=Observable.just("learningRxJava","Writing blog about RxJava","RxJava Rocks!!");
        tweets.map(tweet->tweet.length()).subscribe(n->System.out.println(n));

       // tweets.flatMap(tweet->Observable.from(tweet.split(""))).subscribe(System.out::println);
        tweets.count().subscribe(System.out::println);
        tweets.map(tweet->tweet.length()).reduce(0,(acc,el)->acc+el).subscribe(System.out::println);


    /*    PublishSubject<Integer> subject = PublishSubject.create();
        subject.subscribe(getFirstObserver());
        subject.onNext(1);
        subject.onNext(2);
        subject.onNext(3);
        subject.subscribe(getSecondObserver());
        subject.onNext(4);
        subject.onCompleted();
        System.out.println(subscriber1);
        System.out.println(subscriber1+subscriber2);*/



        Observable<String> createdObservable= Observable.create(
                //the lambda expression we were using before was just implementing the call method on this functional
                //interface
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        for(int i=0;i<5;i++) {
                            subscriber.onNext("Hello World "+i);
                        }
                        subscriber.onCompleted();
                    }
                }
        );

        Subscriber<String> mySubscriber= new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("MySubscriber completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(String s) {
                System.out.println("MySubscriber onNext():"+s);
            }
        };


        createdObservable.subscribe(mySubscriber);


        Observable<Integer> rangeObservable=Observable.range(2,5);
        rangeObservable.subscribe(integer -> System.out.println(integer));

    }


    public static Observable<Integer> naturalNumbers(int n){
        return Observable.create(subscriber -> {
            IntStream.range(1,n).forEach(number->subscriber.onNext(number));
            subscriber.onCompleted();
        });
    }


    static Observer<String> getStringObserver(){
        return new Observer<String>(){
            String result;
            public void onCompleted(){
                System.out.println("result is "+result);
            }

            public void onError(Throwable e){
                System.out.println(e);
            }

            public void onNext(String string){
                result=result+string;
            }
        };
    }


    static Observer<Integer> getFirstObserver(){
            return new Observer<Integer>(){

                public void onCompleted(){
                    System.out.println("First Observer completed");
                }

                public void onError(Throwable e){
                    System.out.println(e);
                }

                public void onNext(Integer number){
                    subscriber1+=number;
                }

        };
    }

    static Observer<Integer> getSecondObserver(){
        return  new Observer<Integer>() {
            public void onCompleted() {
                System.out.println("Second Observer completed");
            }

            public void onError(Throwable e) {
                System.out.println(e);
            }

            public void onNext(Integer number) {
                subscriber2+=number;
            }
        };
    }
}
