public class ObjectWrapper implements Comparable{
    String string;

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    Integer integer;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public int compareTo(Object o) {
        ObjectWrapper anotherWrapper=(ObjectWrapper)o;
        if(anotherWrapper.getInteger()==null || getInteger()==null){return 0;}
        if(anotherWrapper.getInteger()>this.getInteger()){
            return -1;
        }else if(anotherWrapper.getInteger()<this.getInteger()){
            return 1;
        }else{
            return 0;
        }
    }

    public String toString(){
        return this.getString();
    }
}
