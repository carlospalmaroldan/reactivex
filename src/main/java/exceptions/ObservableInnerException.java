package main.java.exceptions;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObservableInnerException {

    private static final List<String> SUBJECTS = new ArrayList<>();

    static {
        SUBJECTS.add("Java");
        SUBJECTS.add("Db");
        SUBJECTS.add("Spring");
    }

    private static final Map<String, List<String>> TOPICS_BY_SUBJECT = new HashMap<>();

    static {
        TOPICS_BY_SUBJECT.put("Java", Arrays.asList("Concurrency", "Generics"));
        TOPICS_BY_SUBJECT.put("Db", Arrays.asList("Performance", "SQL"));
        TOPICS_BY_SUBJECT.put("Spring", Arrays.asList("IOC", "proxy", "beanFactory"));
    }

    //onErrorReturn allows us to capture any exceptions during processing and emit them to the subscribers, so that
    //they know what happened
    public static void main(String[] args) {
        final Map<String, Integer> topicCountBySubject = new HashMap<>();
        final List<String> allTopics = new ArrayList<>();
        Observable<List<String>> stringObservable=Observable.from(SUBJECTS)
            .map(subj -> {

                    topicCountBySubject.put(subj, TOPICS_BY_SUBJECT.get(subj).size());

                    if(topicCountBySubject.size()<0){
                        throw new RuntimeException("This is the inner exception");
                    }

                    return TOPICS_BY_SUBJECT.get(subj);

            })
            .map(list->{
                Car car=null;
                car.getEngine();
                return list;
            })
            .onErrorReturn(throwable -> {
                List<String> errorList=new ArrayList<>();
                errorList.add(throwable.getClass().getName());
                return errorList;
                });

        stringObservable.subscribe((list -> System.out.println(list)));

    }

    private static class Car{
        private String engine;
        public Car(){

        }
        public String getEngine(){
            return this.engine;
        }

    }

}
