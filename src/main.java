import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        int[] trainingData2 = {0,1,2,3,0,1,2,2,0,1,2,1,0,1,2,0,0,1,2,3};
        String[] StateNames = {
                "sleeping",
                "eating","crying", "running"
        };
        ArrayList<Integer> lastNdata = new ArrayList<Integer>();
        lastNdata.add(2);
        lastNdata.add(3);
        generalMarkov test = new generalMarkov(0, trainingData2, StateNames, 2, lastNdata);

        test.displayStates(100);
    }
}
