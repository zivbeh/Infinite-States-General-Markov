import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class generalMarkov {

    final String[] stateNames;

    public int currentState;
    public ArrayList<Integer> saveLastNData;

    public int numOrder;

    // the transitions array used to store all transitions a baby does
    public HashMap<String, Double[]> transitions = new HashMap<String, Double[]>();

    public generalMarkov(int currentState, ArrayList<Integer> trainingData, String[] stateNames1, int numberOfOrder, ArrayList<Integer> lastDataArr) {
        // initialize values and constants
        this.stateNames = stateNames1;
        this.currentState = currentState;
        this.saveLastNData = lastDataArr;
        this.numOrder = numberOfOrder;
        // train data
        train(trainingData);
    }

    public void nextState() {
        String str = "";
        for (int i = 0; i < saveLastNData.size(); i++) {
            str += saveLastNData.get(i);
        }
        Double[] arr = transitions.get(str);
        double ran = Math.random();
        double sum = 0;
        if (arr == null) {return;}
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                continue;
            }
            sum += arr[i];
            if (ran <= sum) {
                currentState = i;
                // update lastNStates data
                saveLastNData.add(i);
                if (saveLastNData.size() >= numOrder) {
                    saveLastNData.remove(0);
                }
                return;
            }
        }
    }
    public void displayStates(int n){
        System.out.print(stateNames[currentState] + " ");
        for (int i = 0; i < n; i++) {
            nextState();
            System.out.print(stateNames[currentState] + " ");
        }
    }

    public ArrayList<Integer> getNextNStates(int n) {
        ArrayList<Integer> nextNStates = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            nextState();
            nextNStates.add(currentState);
        }
        return nextNStates;
    }

    public HashMap<String, Integer[]> getRepetitionOfOrder(ArrayList<Integer> statesArr) {
        HashMap<String, Integer[]> Obj = new HashMap<String, Integer[]>();
        for (int i = 0; i < statesArr.size()-numOrder; i++) {
            int counter = 0;
            String str = "";
            while(counter < numOrder) {
                str += statesArr.get(i+counter);
                counter++;
            }
            Integer[] arr;
            if (Obj.get(str) != null) {
                arr = Obj.get(str);
                if (arr[statesArr.get(i+numOrder)] == null) {
                    arr[statesArr.get(i+numOrder)] = 0;
                }
                arr[statesArr.get(i+numOrder)]++;
            } else {
                arr = new Integer[stateNames.length];
                arr[statesArr.get(i+numOrder)] = 1;
            }
            Obj.put(str, arr);
        }
        return Obj;
    }


    public void train(ArrayList<Integer> statesArr) {
        // find Repetition Order
        HashMap<String, Integer[]> Obj = getRepetitionOfOrder(statesArr);
        // change transitions
        changeTransitionsArr(Obj);
    }

    private void changeTransitionsArr(HashMap<String, Integer[]> Obj) {
        for (Map.Entry<String, Integer[]> objPair : Obj.entrySet()) {
            String str = objPair.getKey();
            double sum = 0;
            for (int i = 0; i < objPair.getValue().length; i++) {
                if (objPair.getValue()[i] != null) {
                    sum += objPair.getValue()[i];
                }
            }
            for (int i = 0; i < objPair.getValue().length; i++) {
                Double[] arr;
                Double[] result = transitions.get(str);
                if (result != null) {
                    arr = transitions.get(str);
                } else {
                    arr = new Double[stateNames.length];
                }
                if (objPair.getValue()[i] != null) {
                    arr[i] = (objPair.getValue()[i])/sum;
                    transitions.put(str, arr);
                }
            }
        }
    }
}