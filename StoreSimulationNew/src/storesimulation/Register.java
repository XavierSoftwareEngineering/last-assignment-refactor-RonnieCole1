package storesimulation;
import java.util.ArrayList;

/**
 * @author timmermank1 and Ronnie Cole
 */
class Register {

    private ArrayList<Customer> line = new ArrayList();
    private int maxSize;
    private double scanTime, payTimel;
    int size;
    
    Register(double d, double d0) {
        scanTime = d;
        payTimel = d0;
        size = 0;
        maxSize = 0;
    }

    int getLineLength() {
        return line.size();
    }

    void enqueue(Customer customer) {
        line.add(customer);
        size++;
        maxSize++;
    }

    Customer dequeue() {
        size--;
        return line.remove(0);
    }

    boolean isEmpty() {
        if (line.size() == 0 ) {
            return true;
        } else {
            return false;
        }
    }

    Customer peek() {
        return line.get(0);
    }

    double getScanTime() { 
        return scanTime;
    }

    double getPayTime() {
        return payTimel;
    }
//    
    int getMaxSize() {
        return maxSize;
    }
    
    public String toStringRegister() {
        String s = "[";
        for (int i = 0; i < size; i++) {
            s += line.get(i).getArriveTime()+ " ";
        }
        return s + "]";
    }
}
