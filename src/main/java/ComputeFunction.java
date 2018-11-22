public class ComputeFunction {

    public static void compute(Integer v){
        try{
            System.out.println("compute integer : " + v);
            Thread.sleep(1000);
        }catch(InterruptedException e){}
    }
}
