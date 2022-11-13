package BaseMethodsForTesting;

public class BaseMethodEarlyReturn {
    public void MethodBefore(){
        if(1 < 2){
            System.out.println("A");
        }
        else {
            System.out.println("B");
        }
    }

    public void MethodAfter(){
        if (1 < 2) {
            System.out.println("A");
            return;
        }
        System.out.println("B");
    }


}
