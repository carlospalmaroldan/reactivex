package ApplyingToExistingApplications;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import rx.Observable;
import rx.Subscriber;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class ImperativeConcurrency {

    static Flight lookupFlight(String flightNo)  {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("lookupFlight "+Thread.currentThread().getName());
        }catch(InterruptedException e){
            System.out.println(e);
        }finally{
            return new Flight();
        }

    }
    static Passenger findPassenger(long id) {
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("findPassenger "+Thread.currentThread().getName());
        }catch(InterruptedException e){
            System.out.println(e);
        }finally{
            return new Passenger();
        }

    }
    static Ticket bookTicket(Flight flight, Passenger passenger) {
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("bookTicket " +Thread.currentThread().getName());
            System.out.println("ticket has been booked");
        }catch(InterruptedException e){
            System.out.println(e);
        }finally{
            return new Ticket();
        }

    }
   static  SmtpResponse sendEmail(Ticket ticket) {
       try {
           TimeUnit.SECONDS.sleep(2);
           System.out.println("sendEmail "+Thread.currentThread().getName());
       }catch(InterruptedException e){
           System.out.println(e);
       }finally{
           return new SmtpResponse();
       }

    }

    static Observable<Flight> rxLookupFlight(String flightNo){
        return Observable.defer(() ->
                Observable.just(lookupFlight(flightNo)));
    }
    static Observable<Passenger> rxFindPassenger(long id){
        return Observable.defer(() ->
                Observable.just(findPassenger(id)));
    }


    public static void main(String[] args) throws InterruptedException{
        //still blocking without the use of subscribeOn
        /*Observable<Flight> flight = rxLookupFlight("LOT 783");
        Observable<Passenger> passenger = rxFindPassenger(42);
        Observable<Ticket> ticket =
                flight.zipWith(passenger, (f, p) -> bookTicket(f, p));
        ticket.subscribe(t -> sendEmail(t));*/
        //Once we choose to move to other threads to execute the code, we never return to the main thread
        Observable<Flight> flight = rxLookupFlight("LOT 783").subscribeOn(Schedulers.io());
        Observable<Passenger> passenger = rxFindPassenger(42).subscribeOn(Schedulers.io());
        Observable<Ticket> ticket =
                flight.zipWith(passenger, (f, p) -> bookTicket(f, p));
        ticket.subscribe(t -> sendEmail(t));

        Thread.sleep(30000);
    }


    private static class Flight{

    }

    private static class Ticket{

    }

    private static class Passenger{

    }

    private static class SmtpResponse{

    }
}
