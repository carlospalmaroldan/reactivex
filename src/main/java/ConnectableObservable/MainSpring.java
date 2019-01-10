package ConnectableObservable;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
@ComponentScan
public class MainSpring implements CommandLineRunner{

    //the important part here is that both subscribers to the observable get sent the same messages,
    //and we achieve that thanks to a connectable observable, otherwise we would have a situation in which
    //the first one to subscribe would receive messages that the second one will never receive.
    public static void main(String[] args){
        SpringApplication.run(MainSpring.class, args);
    }

    @Override
    public void run(final String...s) {}
}
