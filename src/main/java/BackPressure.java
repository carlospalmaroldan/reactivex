import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import java.util.stream.IntStream;

public class BackPressure {

    public static  void main(String[] args) {
       /* Observable.range(1, 1_000_000)
        .subscribe(ComputeFunction::compute);
*/

        PublishSubject<Integer> source=PublishSubject.create();
        source.observeOn(Schedulers.computation()).subscribe(ComputeFunction::compute,Throwable::printStackTrace);

        IntStream.range(1,1_000_000).forEach(source::onNext);

    }




}
