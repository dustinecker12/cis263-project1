package src;

import java.util.NoSuchElementException;

/**
 * @author Dustin Ecker dustin.ecker12@gmail.com
 * @version 1.0
 */
public class Queue {

    /**
     * Private inner class that defines the node object for this Queue.
     */
    private class Customer {
        private int customerID;
        private int arrivalTime;
        private int serviceTime;
        private Customer next;

        /**
         * Constructor method for private inner class that defines the node object for this Queue.
         *
         * @param customerID    customer ID, which is the position they were served in the simulation
         * @param arrivalTime   the time the customer joined the queue, in seconds
         * @param serviceTime   the pre-determined time this customer requires for their service, in seconds
         */
        public Customer(int customerID, int arrivalTime, int serviceTime) {
            this.customerID = customerID;
            this.arrivalTime = arrivalTime;
            this.serviceTime = serviceTime;
            this.next = null;
        }
    }

    private Customer head;
    private Customer tail;
    private int size;

    /**
     * Constructor method for this queue.
     * Initializes the head and tail to null and queue size to 0.
     */
    public Queue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Method for adding a new customer to the queue.
     *
     * @param customerID    customer ID, which is the position they were served in the simulation
     * @param arrivalTime   the time the customer joined the queue, in seconds
     * @param serviceTime   the pre-determined time this customer requires for their service, in seconds
     */
    public void enqueue(int customerID, int arrivalTime, int serviceTime) {
        Customer newCustomer = new Customer(customerID, arrivalTime, serviceTime);
        if (isEmpty()) {
            head = newCustomer;
        } else {
            tail.next = newCustomer;
        }
        tail = newCustomer;

        size++;
    }

    /**
     * Method for removing a customer from the queue. Returns the time their services requires.
     *
     * @return  amount of time the customer's services requires, in seconds
     */
    public int dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();

        int data = head.serviceTime;
        head = head.next;
        if (head == null)
            tail = null;

        size--;

        return data;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return  true or false depending on queue being empty
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Checks the size of the queue.
     *
     * @return  current size of queue
     */
    public int getSize() {
        return size;
    }

    /**
     * Used to find the ID of the current customer at the front of the queue.
     *
     * @return  ID of customer at front of queue.
     */
    public int getFirstID() {
        return head.customerID;
    }

    /**
     * Used to check the arrival time of the current customer at the front of the queue.
     *
     * @return  arrival time of customer at front of queue.
     */
    public int getFirstArrivalTime() {
        return head.arrivalTime;
    }
}
