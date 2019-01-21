package OperatorsAndTransformations;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import twitter4j.*;



public class DistinctExample {

    private static final Logger logger
        = LoggerFactory.getLogger(DistinctExample.class);

    public static void main(String[] args){
        /*CellPhone cellPhone=new CellPhone("juan",6456542L);
        String hola="hola";
        Samsung samsung=new Samsung();
        *//*System.out.println(cellPhone.equals(hola));*//*
        System.out.println(cellPhone.equals(samsung));*/

       /* Haceb  haceb=new Haceb();
        Mabe mabe=new Mabe();

        System.out.println(haceb.equals(mabe));
        System.out.println(haceb instanceof Refrigerator);
        System.out.println(mabe instanceof Refrigerator);
        System.out.println(mabe.equals(haceb));*/

       CellPhone cellPhone=new CellPhone("juan",6456542L);
       CellPhone cellPhone2=new CellPhone("juan",3324234L);
       System.out.println(cellPhone.hashCode());
       System.out.println(cellPhone2.hashCode());

       TwitterStream twitterStream=FilterExample.configureStream();
       Observable<Status> statusObservable=Observable.create(subscriber -> {
           twitterStream.addListener(new StatusListener() {
               @Override public void onStatus(Status status) {
                    subscriber.onNext(status);
               }

               @Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

               }

               @Override public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

               }

               @Override public void onScrubGeo(long userId, long upToStatusId) {

               }

               @Override public void onStallWarning(StallWarning warning) {

               }

               @Override public void onException(Exception ex) {

               }
           });
           twitterStream.sample();
       }

       );

       //distinct is dangerous because internally the ids of elements already emitted are stored, which means we can
       //run out of memory
       statusObservable.distinct(status->status.getId()).map(Status::getId).subscribe(System.out::println);
    }

    //in order to determine equality we can use getClass and it will distinguish between classes and subclasses
    //getClass also allows us to distinguish between two objects from classes that implement the same interface
    //instanceOf yields the same result for object belonging to classes implementing the same interface

    public static class CellPhone{
        String name;
        Long number;

        public CellPhone(String name,Long number){
            this.name=name;
            this.number=number;
        }


         public boolean equals(Object obj) {
            if(obj==null){
                return false;
            }
            else if(obj.getClass()!=this.getClass()){
                return false;
            }
            else if(((CellPhone)obj).number!=this.number || ((CellPhone)obj).name!=this.name){
                return false;
            }else {
                return true;
            }
        }
        //a desirable property is that unequal objects should have different hashcodes, however this does not mean that it is necessary
        /*public int hashCode(){
            int hash =7;
            hash=31*hash+name.hashCode();
            hash=31*hash+(int)(number>>>16);
            return hash;
        }*/

        //the IDE assigns them different hash codes too
    }

    public static class Samsung extends CellPhone{
        public Samsung(){super("juan",6456542L);}
    }


    public interface Refrigerator{
        public void freeze();
    }

    public static class Haceb implements Refrigerator{
        public void freeze(){}

        private Long height;
        private Long weight;

        public void Haceb(){ }

        public void Haceb(Long height,Long weight){
            this.weight=weight;
            this.height=height;
        }

        public boolean equals(Object obj) {
            if(obj==null){
                return false;
            }
            else if(obj.getClass()!=this.getClass()){
                return false;
            }
            else if(((Haceb)obj).height!=this.height || ((Haceb)obj).weight!=this.weight){
                return false;
            }else {
                return true;
            }
        }
    }

    public static class Mabe implements Refrigerator{
        public void freeze(){}

        private Long height;
        private Long weight;

         public boolean equals(Object obj) {
            if(obj == null){
                return false;
            }else if(!(obj instanceof Mabe)){
                return false;
            }else if(((Mabe)obj).height!=this.height || ((Mabe)obj).weight!=this.weight){
                return false;
             }else{return true;}
        }
    }

}
