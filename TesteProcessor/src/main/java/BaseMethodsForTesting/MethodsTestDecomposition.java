package BaseMethodsForTesting;

public class MethodsTestDecomposition {

    public void teste01(){
        int a = 0;
        if(a > 1){
            a++;
            if(true){
                a -= 5;
            }
        } else if (a < -5) {
            a = 5;
        } else{
            a--;
        }
        a = a + 25;

    }
}
