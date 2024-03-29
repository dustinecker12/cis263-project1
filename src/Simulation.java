package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Dustin Ecker dustin.ecker12@gmail.com
 * @version 1.0
 */
public class Simulation {

    /**
     * Private inner class used to log each customer that was served today.
     */
    private class Customer {
        private int customerID;
        private int waitTime;

        /**
         * Constructor method for private inner class used to log each customer served today.
         *
         * @param customerID    the customer ID, which is the position they were served in the simulation
         * @param waitTime      the amount of time the customer waited in the queue
         */
        public Customer(int customerID, int waitTime) {
            this.customerID = customerID;
            this.waitTime = waitTime;
        }
    }

    private ArrayList<Customer> customers;
    private Queue queue;
    private Random rand;
    private int[] customerQty;
    private int[] serviceTime;
    private int clock;
    private int ID;
    private int served;
    private int avgWait;
    private int maxWait;
    private int avgQueue;
    private int maxQueue;

    /**
     * Constructor method that creates a new simulation object and initializes every variable to its default value.
     *
     * @param qtyFile       path and filename for bank provided customer quantities
     * @param timeFile      path and filename for bank provided services times
     * @throws IOException  if file not found
     */
    public Simulation(String qtyFile, String timeFile) throws IOException {
        this.customers = new ArrayList<>();
        this.queue = new Queue();
        this.rand = new Random();
        this.clock = 0;
        this.ID = 0;
        this.served = 0;
        this.avgWait = 0;
        this.maxWait = 0;
        this.avgQueue = 0;
        this.maxQueue = 0;
        this.customerQty = readDataFiles(qtyFile);
        this.serviceTime = readDataFiles(timeFile);
    }

    /**
     * Main method that sets up four bank simulations for 3, 4, 5, and 6 tellers.
     *
     * @param args          optional arguments that may be passed in
     * @throws IOException  if file not found
     */
    public static void main(String[] args) throws IOException {
        Simulation sim = new Simulation("src/proj2a.dat", "src/proj2b.dat");

        System.out.println("Data logged for 3 tellers:");
        sim.simulation(3);
        sim.resetSimulation();
        System.out.println("----------------------------");

        System.out.println("Data logged for 4 tellers:");
        sim.simulation(4);
        sim.resetSimulation();
        System.out.println("----------------------------");

        System.out.println("Data logged for 5 tellers:");
        sim.simulation(5);
        sim.resetSimulation();
        System.out.println("----------------------------");

        System.out.println("Data logged for 6 tellers:");
        sim.simulation(6);
        sim.resetSimulation();
        System.out.println("----------------------------");
    }

    /**
     * Runs a simulation for an 8 hour (28800 second) bank day based off the given number of tellers.
     *
     * This method creates an array of tellers based off the user specified amount, then creates a loop for a full
     * bank day. In this loop, every minute new customers are added to the queue. Each second the array of tellers
     * is checked to see if a teller is available. If they are, a customer is pulled from the queue and the teller's
     * available time is updated. At the end of the loop the clock is incremented by one second. After the simulation
     * has finished, a log is generated and printed.
     *
     * @param numOfTellers  The number of tellers that the user specifies
     */
    private void simulation(int numOfTellers) {
        // Create array with given number of tellers
        Teller[] tellers = new Teller[numOfTellers];
        for (int i = 0; i < tellers.length; i++) {
            Teller teller = new Teller();
            tellers[i] = teller;
        }

        while (clock < 28800) {
            // Every 60 seconds add new customers to queue
            if (clock % 60 == 0) {
                int numCustomers = getCustomers(customerQty, rand.nextInt(99) + 1);

                for (int i = 0; i < numCustomers; i++) {
                    queue.enqueue(++ID, clock, getServiceTime(serviceTime, rand.nextInt(99) + 1));
                }

                logQueue();
            }

            // For each teller, determine if they are available.
            // If available and queue isn't empty, dequeue customer and assign to teller.
            // Update tellers available time
            for (Teller teller: tellers) {
                if (teller.getAvailableTime() <= clock && !queue.isEmpty()) {
                    // Add customerID and their wait time to a log
                    Customer c = new Customer(queue.getFirstID(), clock - queue.getFirstArrivalTime());
                    customers.add(c);
                    teller.setAvailableTime(clock, queue.dequeue());
                    served++;
                }
            }

            clock++;
        }

        // Add the end of simulation, generate and print log data
        generateLogData();
        printLogData();
    }

    /**
     * Generates the log data after the simulation has finished.
     *
     * This method calls other helpers methods to calculate the average wait time in the queue, the maximum wait time,
     * the average length of the queue, and the maximum length of the queue.
     */
    private void generateLogData() {
        calculateAvgWait();
        calculateMaxWait();

        // Divides queue size logged at each minute by 480 total minutes
        avgQueue = avgQueue / 480;
    }

    /**
     * Prints log data after simulation has finished.
     */
    private void printLogData() {
        System.out.println("Total customers served: " + served);
        System.out.println("Average wait time: " + avgWait / 60 + " minute(s) and " + avgWait % 60 + " second(s).");
        System.out.println("Maximum wait time: " + maxWait / 60 + " minute(s) and " + maxWait % 60 + " second(s).");
        System.out.println("Average queue size: " + avgQueue);
        System.out.println("Maximum queue size: " + maxQueue);
    }

    /**
     * Helper method to calculate the average wait time after the simulation has finished.
     */
    private void calculateAvgWait() {
        int tempWait = 0;

        for (int i = 0; i < customers.size(); i++) {
            tempWait += customers.get(i).waitTime;
        }

        avgWait = tempWait / served;
    }

    /**
     * Helper method to calculate the maximum wait time after the simulation has finished.
     */
    private void calculateMaxWait() {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).waitTime > maxWait)
                maxWait = customers.get(i).waitTime;
        }
    }

    /**
     * Used to log the queue size and check for new maximum queue size after each minute has passed.
     */
    private void logQueue() {
        avgQueue += queue.getSize();

        if (queue.getSize() > maxQueue)
            maxQueue = queue.getSize();
    }

    /**
     * Used to reset all variables back to zero to re-run a new simulation.
     */
    private void resetSimulation() {
        this.customers = new ArrayList<>();
        this.queue = new Queue();
        this.rand = new Random();
        this.clock = 0;
        this.ID = 0;
        this.served = 0;
        this.avgWait = 0;
        this.maxWait = 0;
        this.avgQueue = 0;
        this.maxQueue = 0;
    }

    /**
     * Returns a random number of customers generated from a random number.
     *
     * @param arr   the array of customer sizes provided by the bank
     * @param seed  the random number fed to method
     * @return      the number of customers returned based off criteria
     */
    private int getCustomers(int[] arr, int seed) {
        int total = 0;

        for (int i = 0; i < arr.length; i++) {
            total += arr[i];

            if (seed < total)
                return i;
        }

        return arr.length - 1;
    }

    /**
     * Returns a random predefined service time generated from a random number.
     *
     * @param arr   the array of customer service times provided by the bank
     * @param seed  the random number fed to the method
     * @return      the amount of service time in seconds
     */
    private int getServiceTime(int[] arr, int seed) {
        int total = 0;

        for (int i = 0; i < arr.length; i++) {
            total += arr[i];

            if (seed < total)
                return i * 10;
        }

        return (arr.length - 1) * 10;
    }

    /**
     * Reads customer provided data files into their arrays. These data files define the likelihood of
     * a number of customers arriving at any given minute, and their service time.
     *
     * @param fileName      the path and filename of the customer data file
     * @throws IOException  throws IO exception if file is not found
     */
    private int[] readDataFiles(String fileName) throws IOException {
        List<String> listOfStrings = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String line = br.readLine();

        while (line != null) {
            listOfStrings.add(line);
            line = br.readLine();
        }

        br.close();

        int[] arr = new int[listOfStrings.size()];

        for (int i = 0; i < listOfStrings.size(); i++) {
            arr[i] = Integer.parseInt(listOfStrings.get(i).split("\t")[1]);
        }

        return arr;
    }
}
