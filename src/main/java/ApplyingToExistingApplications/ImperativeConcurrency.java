package ApplyingToExistingApplications;

import rx.Observable;

import java.util.concurrent.TimeUnit;

public class ImperativeConcurrency {

    static Flight lookupFlight(String flightNo)  {
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch(InterruptedException e){
            System.out.println(e);
        }finally{
            return new Flight();
        }

    }
    static Passenger findPassenger(long id) {
        try {
            TimeUnit.SECONDS.sleep(2);
        }catch(InterruptedException e){
            System.out.println(e);
        }finally{
            return new Passenger();
        }

    }
    static Ticket bookTicket(Flight flight, Passenger passenger) {
        try {
            TimeUnit.SECONDS.sleep(2);
        }catch(InterruptedException e){
            System.out.println(e);
        }finally{
            return new Ticket();
        }

    }
   static  SmtpResponse sendEmail(Ticket ticket) {
       try {
           TimeUnit.SECONDS.sleep(2);
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


    public static void main(String[] args){
        //still blocking without the use of subscribeOn
        Observable<Flight> flight = rxLookupFlight("LOT 783");
        Observable<Passenger> passenger = rxFindPassenger(42);
        Observable<Ticket> ticket =
                flight.zipWith(passenger, (f, p) -> bookTicket(f, p));
        ticket.subscribe(t -> sendEmail(t));
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
