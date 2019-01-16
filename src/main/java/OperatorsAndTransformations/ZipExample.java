package OperatorsAndTransformations;

import rx.Observable;
import rx.Subscriber;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class ZipExample {

    enum City{Warsaw,London,Paris,NewYork};

    public static void main(String[] args){
        //zip allows us to combine emissions from several observables by taking corresponding elements
        //from the observables and applying a function to them
       /* List<String> names= Arrays.asList("Carlos","Jose","Marlon","Mateo","Javier");
        List<String> surnames= Arrays.asList("Palma","Munoz","Pineda","Lopez","Gomez");
        List<String> age= Arrays.asList("32","35","30","22","31");
        Observable<String> nameObservable=Observable.from(names);
        Observable<String> surnameObservable=Observable.from(surnames);
        Observable<String> ageObservable=Observable.from(age);
        Observable fullname=Observable.zip(nameObservable,surnameObservable,ageObservable,(n,s,a)->n+" "+s+" "+a);
        fullname.subscribe(n->System.out.println(n));*/

        Observable<LocalDate> nextTenDays =
            Observable
                .range(1, 10)
                .map(i -> LocalDate.now().plusDays(i));

        //we can filter based on several criteria applied to different observables at the same time
        Observable<Vacation> possibleVacations = Observable
            .just(City.Warsaw, City.London, City.Paris)
            .flatMap(city -> nextTenDays.map(date -> new Vacation(city, date)))
            .flatMap(vacation ->
                Observable.zip(
                    vacation.weather().filter(Weather::isSunny),
                    vacation.cheapFlightFrom(City.NewYork).filter(flight -> flight.cheap==true),
                    vacation.cheapHotel().filter(hotel -> hotel.cheap==true),
                    (w, f, h) -> vacation
                ));

        possibleVacations.subscribe(v->System.out.println(v));

    }

    static Observable<String> Name(String string) throws InterruptedException{
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(5000);
                }catch (InterruptedException e){}
                subscriber.onNext(string);
            }
        });
    }

    public static class Vacation{
        private final City where;
        private final LocalDate when;
        Weather weather;
        Flight flight;
        Hotel hotel;
        Vacation(City where, LocalDate when) {
            this.where = where;
            this.when = when;
            Random r = new Random();
            weather=new Weather(r.nextBoolean());
            r = new Random();
            flight=new Flight(r.nextBoolean());
            r = new Random();
            hotel=new Hotel(r.nextBoolean());
        }

        public Observable<Weather> weather() {

            return Observable.just(weather) ;
        }
        public Observable<Flight> cheapFlightFrom(City from) {

            return Observable.just(flight);
        }
        public Observable<Hotel> cheapHotel() {

            return Observable.just(hotel);
        }

        public String toString(){
            return "Vacation: where "+where+" when: "+when +" Weather: "+weather.isSunny()+" cheapFlightFrom "+flight+" cheap hotel: "+hotel;
        }
    }


    public static class Weather{

        public boolean sunny;
        public Weather(boolean sunny){
            this.sunny=sunny;
        }
        public boolean isSunny(){
            return sunny;
        }
    }
    public static class Flight{
        boolean cheap;
        public Flight(boolean cheap){
            this.cheap=cheap;
        }

        public String toString(){
            return String.valueOf(cheap);
        }
    }
    public static class Hotel{
        boolean cheap;
        public Hotel(boolean cheap){
            this.cheap=cheap;
        }
        public String toString(){
            return String.valueOf(cheap);
        }
    }
}
