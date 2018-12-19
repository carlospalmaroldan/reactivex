import java.math.BigInteger;
import java.util.Iterator;

public class NaturalNumbersIterator  {



    private static class NaturalIterator implements Iterator<BigInteger> {
        private BigInteger current= BigInteger.ZERO;

        @Override public boolean hasNext() {
            return true;
        }

        @Override public BigInteger next() {
            current= current.add(BigInteger.ONE);
            return current;
        }
    }


    public static void main(String[] args){
            NaturalIterator naturalIterator=new NaturalIterator();
            while(naturalIterator.hasNext()){
                System.out.println(naturalIterator.next());
            }
    }
}


