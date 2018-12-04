import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class MultithreadedObservablesClashing {

    static Queue<ObjectWrapper> resultsQueue=new PriorityQueue<>();

    public static void main(String[] args) throws IOException{
        //toBlocking() seems to affect the main thread, making it wait until the observable completes, but it doesn't
        // affect the other observables, meaning that the other observables tries to access the queue while the first
        //one is still busy using it, raising a java.util.ConcurrentModificationException on the priority Queue

        Observable.fromCallable(getTextFromFile("C:\\Users\\carlos.palma\\ReactiveX\\file.txt"))
            .subscribeOn(Schedulers.newThread())
            .toBlocking()
            .subscribe(text -> {
                System.out.println("new thread to read file " + Thread.currentThread().getName());
                try {
                    enqueueText(text);
                } catch (InterruptedException e) {
                }
            });

        Observable.just("some String")
            .map(str -> {
                System.out.println("thread to map string " + Thread.currentThread().getName());
                return str.length();
            })
            .observeOn(Schedulers.computation())
            .map(length -> {
                System.out.println("computation thread " + Thread.currentThread().getName());
                return 2 * length;
            })
            .subscribe(number -> {
                System.out.println("subscriber for computation thread " + Thread.currentThread().getName());
                System.out.println(number);
                enqueueNumber(number);
            });




        /*  Thread.sleep(5000);*/

        Iterator iterator = resultsQueue.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    private static void enqueueText(List<String> text) throws InterruptedException{
        final String concatString= text.stream()
            .collect(Collectors.joining(" "));
        ObjectWrapper objectWrapper=new ObjectWrapper();
        objectWrapper.setString(concatString);
        Thread.sleep(5000);
        resultsQueue.add(objectWrapper);
    }

    private static void enqueueNumber(Integer number){
        ObjectWrapper objectWrapper=new ObjectWrapper();
        objectWrapper.setInteger(number);
        resultsQueue.add(objectWrapper);
    }

    private static Callable<List<String>> getTextFromFile(String path) throws IOException {
        return new Callable<List<String>>() {
            @Override public List<String> call() throws Exception {
                List<String> fileContents=Files.readAllLines(Paths.get(path));
                return fileContents;
            }
        };
    }
}
