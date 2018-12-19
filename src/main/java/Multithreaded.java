import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.util.SynchronizedQueue;
import rx.observables.BlockingObservable;
import rx.schedulers.Schedulers;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class Multithreaded {

    static Queue<ObjectWrapper> resultsQueue = new SynchronizedQueue<>();
    static Queue<List<String>> queue=new SynchronizedQueue<>();
    static Queue<Integer> priorityQueue = new PriorityQueue<>();

    public static void main(String[] args) throws InterruptedException, IOException {
       /* Observable.fromCallable(thatReturnsNumberOne())
            .map(numberToString())
            .subscribe(printResult());

        Observable.fromCallable(thatReturnsNumberOne())
            .map(numberToString())
            .subscribe(new Observer<String>() {
                @Override public void onCompleted() {
                    System.out.println("completed");
                }

                @Override public void onError(Throwable e) {
                    System.out.println("error");
                }

                @Override public void onNext(String s) {
                    System.out.println("Subscriber thread: "+Thread.currentThread().getName());
                    System.out.println(s);
                }
            });

        Observable.fromCallable(thatReturnsNumberOne())
            .map(numberToString())
            .observeOn(Schedulers.newThread())      // subscriber on different thread
            .subscribe(printResult());*/
/*
        Observable.fromCallable(thatReturnsNumberOne())
            .observeOn(Schedulers.newThread())      // operator on different thread
            .map(numberToString())
            .subscribe(printResult());*/

   /*     Observable.fromCallable(thatReturnsNumberOne())
            .subscribeOn(Schedulers.newThread())
            .map(numberToString())
            .observeOn(Schedulers.newThread())
            .subscribe(printResult());

        Thread.sleep(5000);*/

        //toBlocking() seems to affect the main thread, making it wait until the observable completes, but it doesn't
        // affect the other observables

        Observable<List<String>> observable1 = Observable.fromCallable(getTextFromFile("C:\\Users\\carlos.palma\\ReactiveX\\file.txt"))
            .subscribeOn(Schedulers.io());

        Observable<List<String>> observable2 = Observable.fromCallable(getTextFromFile("C:\\Users\\carlos.palma\\ReactiveX\\file2.txt"))
            .subscribeOn(Schedulers.io());


        /*Observable.zip(observable1, observable2, (o1, o2) -> {return null;}).toBlocking().single();
*/
        /*observable1.mergeWith(observable2).toBlocking().subscribe(item->System.out.println());*/


        BlockingObservable<List<String>> result=Observable.merge(observable1,observable2).toBlocking();
        result.subscribe(elem->queue.add(elem));
        Iterator iterator = queue.iterator();
        System.out.println("printing results");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }

    private static void enqueueText(List<String> text) throws InterruptedException {
        final String concatString = text.stream()
            .collect(Collectors.joining(" "));
        ObjectWrapper objectWrapper = new ObjectWrapper();
        objectWrapper.setString(concatString);
        Thread.sleep(10000);
        resultsQueue.add(objectWrapper);
    }

    private static void enqueueNumber(Integer number) {
        ObjectWrapper objectWrapper = new ObjectWrapper();
        objectWrapper.setInteger(number);
        resultsQueue.add(objectWrapper);
    }

    private static Callable<Integer> thatReturnsNumberOne() {
        return () -> {
            System.out.println("Observable thread: " + Thread.currentThread().getName());
            return 1;
        };
    }

    private static Func1<Integer, String> numberToString() {
        return new Func1<Integer, String>() {
            @Override public String call(Integer integer) {
                System.out.println("Operator thread: " + Thread.currentThread().getName());
                return String.valueOf(integer);
            }
        };
    }

    private static Action1<String> printResult() {
        return new Action1<String>() {
            @Override public void call(String s) {
                System.out.println("Subscriber thread: " + Thread.currentThread().getName());
                System.out.println("Result: " + s);
            }
        };
    }

    private static Callable<List<String>> getTextFromFile(String path) throws IOException {
        return new Callable<List<String>>() {
            @Override public List<String> call() throws Exception {
                Thread.sleep(5000);
                List<String> fileContents = Files.readAllLines(Paths.get(path));
                return fileContents;
            }
        };
    }

}
