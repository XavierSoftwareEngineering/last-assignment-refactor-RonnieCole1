/**
 * Store Simulation Project
 * This file controls the flow of the store simulation.
 */
package storesimulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Katie Timmerman 
 */
public class StoreSimulation {

    private static final int NUMBER_STANDARD_CHECKOUT = 8; // number of cashier registers
    private static final int NUMBER_SELF_CHECKOUTS = 0; // number of self-scan registers
    private static double simClock = 0; // elapsed time (minutes)
    private static MyHeap events = new MyHeap(); // events that occur in the store
    private static ArrayList<Register> registers = new ArrayList(); // registers used in the store

    public static void main(String[] args) {
        //startSimulation();
    }
    
    public static void testHeap(){
        
    }
    
    public static void testRegister(){
        
    }
    
    public static void startSimulation(){
        
        loadRegisters();
        loadCustomerData();

        // Events are stored in a priority queue, so they will always be returned in order.
        while (events.getSize() > 0) {
            Event e = events.remove();
            simClock = e.getEventTime(); // Always set the clock to the time of the new event.
            if (e.getEventType() == EventType.ARRIVAL) {
                handleArrival(e);
            } else if (e.getEventType() == EventType.END_SHOPPING) {
                handleEndShopping(e);
            } else {
                handleEndCheckout(e);
            }
        }
       
    }
    
    /**
     * The arguments for the Register class are (canTime, payTime)
     * They may need to be updated.
     */
    private static void loadRegisters() {
        for (int i = 0; i < NUMBER_STANDARD_CHECKOUT; i++) {
            Register r = new Register(0.02, 2.3);
            registers.add(r);
        }
        for (int i = 0; i < NUMBER_SELF_CHECKOUTS; i++) {
            Register r = new Register(0.08, 3.7);
            registers.add(r);
        }
    }

    private static void loadCustomerData() {
        double arriveTime, avgSelectionTime;
        int items;

        try {
            File myFile = new File("arrival.txt");
            Scanner inputFile = new Scanner(myFile);
            while (inputFile.hasNext()) {
                arriveTime = inputFile.nextDouble();
                items = inputFile.nextInt();
                avgSelectionTime = inputFile.nextDouble();
                Customer customer = new Customer(arriveTime, items, avgSelectionTime);
                Event event = new Event(customer, arriveTime, EventType.ARRIVAL);
                events.insert(event);
            }//end while
            inputFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("File was not found");
            System.exit(0);
        }
    }

    private static void handleArrival(Event e) {
        Customer c = e.getCustomer();
        double endShoppingTime = c.getArriveTime() + c.getNumItems() * c.getAvgSelectionTime();
        Event endShopping = new Event(c, endShoppingTime, EventType.END_SHOPPING);
        events.insert(endShopping);
    }

    private static void handleEndShopping(Event e) {
        int shortest = getShortestLine();
        Customer customer = e.getCustomer();
        customer.setCheckoutLine(shortest); // Customer will always enter shortest checkout line.
        registers.get(shortest).enqueue(customer); // Even if line is empty, customer must be enqueued and dequeued so that the customer gets included in the stats for the register
        if (registers.get(shortest).getLineLength() == 1) { // If new customer is the only one in line, begin checkout.
            startCheckout(customer);
        }
    }

    private static void handleEndCheckout(Event e) {
        int line = e.getCustomer().getCheckoutLine();
        Customer c = registers.get(line).dequeue();
        if (registers.get(line).isEmpty()) {
            return;
        } else {
            Customer customer = registers.get(line).peek();
            startCheckout(customer);
        }
    }

    private static void startCheckout(Customer customer) {
        int line = customer.getCheckoutLine();
        double checkoutLength = customer.getNumItems() * registers.get(line).getScanTime() + registers.get(line).getPayTime();
        Event endCheckout = new Event(customer, checkoutLength + simClock, EventType.END_CHECKOUT);
        events.insert(endCheckout);
    }

    private static void printCollectedStatistics() {
        System.out.println("NOT IMPLEMENTED YET!");
    }

    private static int getShortestLine() {
        System.out.println("NOT IMPLEMENTED YET");
        return 0;
    }

}
