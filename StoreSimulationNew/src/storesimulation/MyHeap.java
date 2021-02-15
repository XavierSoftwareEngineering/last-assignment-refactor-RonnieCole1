package storesimulation;

import java.util.ArrayList;

/*
 *MyHeap is a class which organizes the time of arrival of the customers as a heap.
 *
 *@author timmermank1, Ronnie Cole
 */
class MyHeap {
    Event[] myHeap = new Event[5000];
    int size = 0;

    Event remove() {
        Event removedEvent = myHeap[0];
        if (size != 0) {
            size--;
            swap(0, size);
            myHeap[size] = null;
            makeHeap();
        }
        return removedEvent;
    }

    
    void insert(Event item) {
        int indexOfItem = size;
        myHeap[indexOfItem] = item;
        size++;
        if (indexOfItem != 0) {
            while (item.isFirst((myHeap[(indexOfItem - 1) / 2])) && indexOfItem != 0) {
                swap(indexOfItem, (indexOfItem - 1) / 2);
                indexOfItem = (indexOfItem - 1) / 2;
            }
        }
    }

    private void makeHeap() {
        int i = 0;
        while (true) {
            if ( (2*i + 1) >= size ) {
                break;
            } else if (2 * i + 2 >= size) { //Only left child, no right child 
                if (!myHeap[i].isFirst(myHeap[(2 * i + 1)])) {
                    swap(i, 2 * i + 1);
                    i = 2 * i + 1;
                } else {
                    break;
                }
            } else if (myHeap[2 * i + 1].isFirst(myHeap[2 * i + 2])) { //Left child is smaller than right child
                if (!myHeap[i].isFirst(myHeap[(2 * i + 1)])) {
                    swap(i, 2 * i + 1);
                    i = 2 * i + 1;
                } else {
                    break;
                }
            } else { //Right child is smaller than left 
                if (!myHeap[i].isFirst(myHeap[(2 * i + 2)])) {
                    swap(i, 2 * i + 2);
                    i = 2 * i + 2;
                } else {
                    break;
                }
            }
        }
    }

    private void swap(int indexOfEvent1, int indexOfEvent2) {
        Event temp = new Event(null, 0, null);
        temp = myHeap[indexOfEvent1];
        myHeap[indexOfEvent1] = myHeap[indexOfEvent2];
        myHeap[indexOfEvent2] = temp;
    }

    public String toString() {
        String s = "[";
        for (int i = 0; i < size; i++) {
            s += myHeap[i].getEventTime() + " ";
        }
        return s + "]";
    }
    
    int getSize() {
        return size;
    }

}
