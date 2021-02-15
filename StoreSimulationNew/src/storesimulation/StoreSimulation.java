/**
 * Store Simulation Project
 * This file controls the flow of the store simulation.
 */
package storesimulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author Katie Timmerman, Ronnie Cole
 */
public class StoreSimulation {

    private static final int NUMBER_STANDARD_CHECKOUT = 8;
    private static final int NUMBER_SELF_CHECKOUTS = 0;
    private static double simClock = 0; // elapsed time (minutes)
    private static int timesDequeued = 0;
    private static int timesEventRemoved = 0;
    private static MyHeap events = new MyHeap();
    private static ArrayList<Register> registers = new ArrayList();

    public static void main(String[] args) {
        //startSimulation();
        testRegister(6,0);
        testHeap(6,0);
    }
    
    public static void testHeap( int customerCount, int atRegister){
        createCustomer(customerCount,atRegister);
        printHeap();
        testRemovingEventUntilOneEventLeft(customerCount);
            
    }
    
    public static void testRegister( int customerCount, int atRegister){
        createRegister(1);
        createCustomer(customerCount,atRegister);
        printCustomerArrivalTimes(atRegister);
        testRegisterPeek(atRegister);
        testRegisterDequeue(atRegister);
        testRegisterDequeueUntilOnlyOneCustomer(customerCount, atRegister);
        testRegisterDequeueWhenEmpty(atRegister);
    }
    
    public static void createRegister( int registerCount ) {
        for (int i = 0; i < registerCount; i++ ) {
            Register standardRegister = new Register(Math.random(), Math.random());
            registers.add(standardRegister);
        }
    }   
    
    public static void createCustomer( int customerCount, int atRegister) {
        for (int i = 0; i < customerCount; i++ ) {
            Customer customer = new Customer( Math.random()*10, (int)Math.random()*100 , Math.random()*100);
            registers.get(atRegister).enqueue(customer);
            events.insert(new Event(customer, Math.random()*100, EventType.ARRIVAL));
        }
    }
    
    public static void printHeap() {
        System.out.println("Heap: " + events);
    }
    
    public static void testRemovingEvent() {
        events.remove();
        timesEventRemoved++;
        System.out.println("Queue after a removal: " + events);
    }
    
    public static void testRemovingEventUntilOneEventLeft(int customerCount) {
        for (int i = timesEventRemoved ; i < customerCount - 1; i++ ) {
            events.remove();
            timesEventRemoved++;
        }
        System.out.println("Heap after " + timesEventRemoved + " removals: " + events);
    }
    
    public static void testRegisterPeek(int atRegister){
        System.out.println("Our peek: " + registers.get(atRegister).peek().getArriveTime());
    }
    
    public static void testRegisterDequeue(int atRegister){
        registers.get(atRegister).dequeue();
        timesDequeued++;
        System.out.println("Customer Arrival times after dequeue: " + registers.get(atRegister).toStringRegister());
    }
    
    public static void testRegisterDequeueUntilOnlyOneCustomer( int customerCount, int atRegister) {
        for (int i = timesDequeued ; i < customerCount - 1; i++ ) {
            registers.get(atRegister).dequeue();
            timesDequeued++;
        }
        System.out.println("Customer Arrival times after " + timesDequeued + " dequeues:" + registers.get(atRegister).toStringRegister());
    }
    
    public static void testRegisterDequeueWhenEmpty(int atRegister) {
        registers.get(atRegister).peek();
        registers.get(atRegister).dequeue();
        System.out.println("If Queue empty, expect True. Our test result: " + registers.get(0).isEmpty());
    }
    
    public static void printCustomerArrivalTimes(int atRegister) {
        System.out.println("Customer Arrival times: " + registers.get(atRegister).toStringRegister());
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
            } else if (e.getEventType() != EventType.END_SHOPPING) {
                handleEndCheckout(e);
            }
        }
       
    }
    
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
            }
            inputFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("File was not found");
            System.exit(0);
        }
    }

    private static void handleArrival(Event event) {
        Customer customer = event.getCustomer();
        double endShoppingTime = customer.getArriveTime() + customer.getNumItems() * customer.getAvgSelectionTime();
        Event endShopping = new Event(customer, endShoppingTime, EventType.END_SHOPPING);
        events.insert(endShopping);
    }

    private static void handleEndCheckout(Event event) {
        int line = event.getCustomer().getCheckoutLine();
        Customer customer = registers.get(line).dequeue();
        if (registers.get(line).isEmpty() == false) {
            Customer customer2 = registers.get(line).peek();
            startCheckout(customer2);
        }
    }

    private static void startCheckout(Customer customer) {
        int line = customer.getCheckoutLine();
        double checkoutLength = customer.getNumItems() * registers.get(line).getScanTime() + registers.get(line).getPayTime();
        Event endCheckout = new Event(customer, checkoutLength + simClock, EventType.END_CHECKOUT);
        events.insert(endCheckout);
        }
    }