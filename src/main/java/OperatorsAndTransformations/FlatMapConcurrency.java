package OperatorsAndTransformations;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import rx.Subscriber;
import sun.misc.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class FlatMapConcurrency {

    public static void main(String[] args) throws MalformedURLException,IOException,ProtocolException {
        List<String> list=new ArrayList<>();
        for(int i=0;i<2000;i++){
            list.add("String");
        }

        /*flatMap(
            bytes -> Observable.empty(),
            e -> Observable.error(e),
            () -> rate(id)
        );*/
        //I was expecting the process to crash due to rx java sending way too many concurrent requests to the url, according to the book:
        /*Therefore,
        if we have, say 10,000 Users, we suddenly triggered 10,000 concurrent HTTP connections.
            If all of them hit the same server, we can expect any of the following:
        • Rejected connections
        • Long wait time and timeouts
        • Crashing the server
        • Hitting rate-limit or blacklisting
        • Overall latency increase
        • Issues on the client, including too many open sockets, threads, excessive memory
            usage*/
        Observable<String> observableList=Observable.from(list);
        Observable<String> observableWebPagesContent= observableList.flatMap(
            string->{
                Observable<String> output=Observable.empty();
                try {
                   output=  executeHttpRequest();
                }catch(IOException e){
                    System.out.println(e);
                }
               return output;
            });
        observableWebPagesContent.subscribe(string->System.out.println(string),exception->exception.printStackTrace());

        Observable<String> observableWebPagesContentLimited= observableList.flatMap(
            string->{
                Observable<String> output=Observable.empty();
                try {
                    output=  executeHttpRequest();
                }catch(IOException e){
                    System.out.println(e);
                }
                return output;
            },10);
    }



    public static String inputStreamToString(InputStream inputStream) throws IOException {
        try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString();
        }
    }

    public static Observable<String> executeHttpRequest() throws MalformedURLException,IOException,ProtocolException {
        URL url = new URL("http://www.elcolombiano.com");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return Observable.just(inputStreamToString(con.getInputStream()));
    }
}
