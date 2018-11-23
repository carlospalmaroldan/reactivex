import rx.Observable;
import rx.Observer;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RxFibonacci {
        public static Observable<Long> fibs(){
            return Observable.create(
              subscriber -> {
                    Long prev=0L;
                    Long current=1L;

                    subscriber.onNext(0L);
                    subscriber.onNext(1L);
                    for(int i=0;i<50;i++){
                        Long oldPrev=prev;
                        prev=current;
                        current+=oldPrev;
                        subscriber.onNext(current);
                    }
              }
            );
        }

        public static Observable<Integer> fakeUserInput(){
            Random random=new Random();
           /* return Observable.interval(500,TimeUnit.MILLISECONDS).
                    map(number->random.nextInt(20)).delay(random.nextInt(1000),TimeUnit.MILLISECONDS);*/
           return Observable.interval(500,TimeUnit.MILLISECONDS)
                   .concatMap(number->Observable.just(random.nextInt(20)).delay(1000,TimeUnit.MILLISECONDS));
        }

        public static void main(String[] args) throws Exception{
            fakeUserInput().subscribe(new Observer<Integer>() {
                @Override
                public void onCompleted() {
                    System.out.println("completed");
                }

                @Override
                public void onError(Throwable e) {
                    System.out.println(e);
                }

                @Override
                public void onNext(Integer integer) {
                    System.out.println(integer);
                }
            });
            Thread.sleep(50000);
          //  fibs().subscribe(System.out::println);
        }

}
