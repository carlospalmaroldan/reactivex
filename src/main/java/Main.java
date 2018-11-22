import rx.Observable;
import rx.Observer;
import rx.subjects.PublishSubject;

public class Main {
    static Integer subscriber1 = 0;
    static Integer subscriber2 = 0;
    public static void main(String[] args){


        String[] letters = {"a","b","c"};

        Observable<String> observable=Observable.from(letters);
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
