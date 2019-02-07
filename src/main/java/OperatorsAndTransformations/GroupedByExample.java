package OperatorsAndTransformations;

import org.apache.commons.lang3.tuple.Pair;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import rx.Observable;
import rx.Subscriber;
import rx.observables.GroupedObservable;
import twitter4j.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GroupedByExample {

    public static void main(String[] args) throws InterruptedException{
        TwitterStream twitterStream=FilterExample.configureStream();
        Observable<Status> observable=Observable.create(subscriber->{
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
        });


        //TAKE 100 STATUSES, THEN SEPARATE THEM INTO GROUPS BASED ON LANGUAGE, PRINT THE COUNT OF STATUSES PER
        //LANGUAGE, PRINT A MAP SHOWING THE LANGUAGE AND THE NUMBER OF STATUSES

        /*Observable<GroupedObservable<String,Status>> groupedByLanguage=observable.take(100).groupBy(Status::getLang);
        //we need an external collection to hold the data from the observable stream
        HashMap<String, List<String>> output=new HashMap<>();
        //the idea is for the observable stream to emit a single value which happens to be a map of
        //language name and the text of the statuses written in that language
        groupedByLanguage.flatMap(status->status).reduce(new HashMap<String,List<String>>(),
                (map,element)->{
                       List<String> list= map.get(element.getLang());
                       if(list==null) {
                           ArrayList<String> newList=new ArrayList<>();
                           newList.add(element.getText());
                           map.put(element.getLang(), newList);
                       }else{
                           list.add(element.getText());
                       }
                        return map;
                }
                ).subscribe(new Subscriber<HashMap<String, List<String>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(HashMap<String, List<String>> stringStringHashMap) {
                //data is passed to the collection that is supposed to hold it
                output.putAll(stringStringHashMap);
            }
        });


        Thread.sleep(30000);
        System.out.println(output);
        System.out.println(output.entrySet().stream().map(x->x.getValue().size()).reduce((x,y)->x+y));
*/

        //GENERATE 10 TWEETS PER LANGUAGE FROM THE FOLLOWING LANGUAGE list:
        //ja, es en,pt, ar,und,tr,fa,in,fr,pl,tl
        //The most important part here is that we can use an external data structure to hold the values that we found
        //from the source, the other thing is that we can use rather complex logic inside take while with a condition
        //related to said data structure
        Map<String,List<String>>  outputMap=new HashMap<>();
        List<String> approvedLanguages=Arrays.asList("ja","es","en","pt","ar","und","tr","fa","in","fr","pl","tl");
        observable.filter(status->approvedLanguages.contains(status.getLang()))
                .takeWhile(status->{

                    List<String> list= outputMap.get(status.getLang());
                    if(list==null) {
                        ArrayList<String> newList=new ArrayList<>();
                        newList.add(status.getText());
                        outputMap.put(status.getLang(), newList);
                    }else{
                        if(list.size()<10) {
                            list.add(status.getText());
                        }
                    }
                    return outputMap.entrySet().stream().map(x->x.getValue().size()).reduce((x,y)->x+y).get()<120;

                }).map(status -> status).subscribe(new Subscriber<Status>() {
            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable);
            }

            @Override
            public void onNext(Status status) {
                System.out.println(outputMap);
            }
        });












        /*Thread.sleep(300000);*/






        //PRINT 100 TWEETS ON THE FIRST 10  LANGUAGES FOUND THEN DISCONNECT







        //take the first geographical location found in the stream, then wait for the next 99 statuses
        //coming from that location



    }


}
