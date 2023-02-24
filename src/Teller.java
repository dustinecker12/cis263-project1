package src;

/**
 * @author Dustin Ecker dustin.ecker12@gmail.com
 * @version 1.0
 */
public class Teller {

    /**
     * The teller's available time in seconds.
     */
    private int availableTime;

    /**
     * Constructor method for teller.
     * Initializes teller's available time to 0th second.
     */
    public Teller() {
        this.availableTime = 0;
    }

    /**
     * Getter method for teller's available time.
     *
     * @return  available time the teller is available in seconds
     */
    public int getAvailableTime() {
        return availableTime;
    }

    /**
     * Setter method for changing teller's new available time.
     *
     * @param currTime      passes in the current time in seconds
     * @param serviceTime   passes in the pre-determined service time for the customer being helped
     */
    public void setAvailableTime(int currTime, int serviceTime) {
        this.availableTime = currTime + serviceTime;
    }
}
