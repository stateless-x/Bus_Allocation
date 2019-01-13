
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class TicketCounter extends Thread {

    private BusLine airport;
    private BusLine city;
    private int MaxSeats;
    
    private int checkID;
    private CyclicBarrier barrier;
    
    public void setCyclicBarrier(CyclicBarrier bar) {
        barrier = bar;
    }
    TicketCounter(String n, int m, BusLine a, BusLine c, int cID) {
        super(n);
        MaxSeats = m;
        airport = a;
        city = c;
        checkID = cID;

    }

    @Override
    public void run() {
        try {
            int transaction;
            int people;
            String name;
            String destination;
            Scanner sc = new Scanner(new File(this.getName() + ".txt"));
            do {  
                String line = sc.nextLine();
                String[] buf = line.split(",");
                transaction = Integer.parseInt(buf[0].trim());
                name = buf[1].trim();
                people = Integer.parseInt(buf[2].trim());
                destination = buf[3].trim();    
                try {
                    if (transaction == checkID) {
                        barrier.await();
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
                if (destination.equals("A") || destination.equals("a")) {
                    airport.allocateBus(transaction, name, people, destination, this.getName());
                } else if (destination.equals("C") || destination.equals("c")) {
                    city.allocateBus(transaction, name, people, destination, this.getName());
                          
                }
            } while (sc.hasNext());
        } catch (FileNotFoundException | NumberFormatException e) {
            System.err.println(e);
        }

    }
   
}
