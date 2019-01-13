
import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Simulation {

    public static int inputMaxSeats() {
        int max = 0;
        boolean ok = false;
        Scanner sc = new Scanner(System.in);
        while (ok != true) {
            try {
                System.out.printf("%s >> Enter Maximum seats = ", Thread.currentThread().getName());
                max = sc.nextInt();
                if (max > 0) {
                    ok = true;
                } else {
                    System.out.println("Invalid Input");
                }
            } catch (Exception e) {
                System.err.println("Wrong Format! Please Enter a Number");
                sc.next();
            }
        }

        return max;
    }

    public static int inputCheckPoint() {
        int chk = 0;
        Scanner sc = new Scanner(System.in);
        boolean ok = false;
        while (ok != true) {
            try {
                System.out.printf("%s >> Enter Checkpoint =  ", Thread.currentThread().getName());
                chk = sc.nextInt();
                if (chk >= 0) {
                    ok = true;
                } else {
                    System.out.println("Invalid Input");
                }
            } catch (Exception e) {
                System.out.println("Wrong Format!, Please Enter a Number");
                sc.next();
            }
        }
        return chk;
    }

    public static void main(String[] args) {
        int maxSeats;
        int checkPoint;
        maxSeats = inputMaxSeats();
        checkPoint = inputCheckPoint();

        ArrayList<TicketCounter> t_counter = new ArrayList<TicketCounter>();
        BusLine airportBus = new BusLine(maxSeats);
        BusLine cityBus = new BusLine(maxSeats);

        for (int i = 1; i < 4; i++) {
            String name = "T" + i;
            TicketCounter temp = new TicketCounter(name, maxSeats, airportBus, cityBus, checkPoint);
            t_counter.add(temp);
        }
        
        //start the threads
        for (int i = 0; i < 3; i++) {
            t_counter.get(i).start();
        }

     
        CyclicBarrier checkPt = new CyclicBarrier(4);
        for (int i = 0; i < 3; i++) {
            t_counter.get(i).setCyclicBarrier(checkPt);
        }
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        } while (checkPt.getNumberWaiting() < 3);
      
        //print Check Point
        System.out.printf("\n%s >>  ==================== Check Point ====================\n", Thread.currentThread().getName());
        System.out.printf("%s >> %d airport-bound bus have been allocated\n", Thread.currentThread().getName(),airportBus.bus_list.size());
        System.out.printf("%s >> %d city-bound bus have been allocated\n", Thread.currentThread().getName(),cityBus.bus_list.size());
        System.out.printf("%s >>  ======================================================\n\n", Thread.currentThread().getName());
        
        try {
            checkPt.await();
        } catch (Exception ex) {
        }

        for (int i = 0; i < 3; i++) {
            try {
                t_counter.get(i).join();
            } catch (Exception e) {
                System.err.println(e);
            }

        }
        //print Summary
        System.out.printf("\n%s >> ==================== Airport Bound =====================", Thread.currentThread().getName());
        airportBus.printBusLine("A");
        System.out.printf("\n%s >> ==================== City Bound ====================", Thread.currentThread().getName());
        cityBus.printBusLine("C");

    }
}
