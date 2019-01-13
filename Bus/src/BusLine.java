
import java.util.*;

class Bus {

    private int seat;
    ArrayList<String> destination = new ArrayList<String>();
    ArrayList<Integer> passenger = new ArrayList<Integer>();

    Bus() {
    }

    public int getSeat() {
        return seat;
    }

    public void insert_people(int people, String tn) {
        seat += people;
        destination.add(tn);
        passenger.add(people);
    }

    public void printBus() {
        for (int i = 0; i < destination.size(); i++) {
            if (i == 0) {
                System.out.printf("%s (%d)", destination.get(i), passenger.get(i));
            } else {
                System.out.printf(", %s (%d)", destination.get(i), passenger.get(i));
            }
        }

    }
}

public class BusLine {

    private int people;
    private int maxSeats;
    private int temp_people;
    private boolean have_bus;
    private Bus bus;
    protected ArrayList<Bus> bus_list;

    public BusLine(int m) {
        maxSeats = m;
        bus_list = new ArrayList<Bus>();
        have_bus = false;
    }

    public void printBusLine(String s) {
        for (int i = 0; i < bus_list.size(); i++) {
            System.out.printf("\n%s >> %s%d: ", Thread.currentThread().getName(), s, i);
            bus_list.get(i).printBus();
        }
        System.out.println();
    }

    public synchronized void allocateBus(int transaction, String tour_name, int people, String bus_type, String tName) {
        if (!have_bus) { // if no bus exist

            while (people >= maxSeats) {
                bus = new Bus();
                bus.insert_people(maxSeats, tour_name);
                bus_list.add(bus);
                people -= maxSeats;
                System.out.printf("%s >> Transaction %2d : %-8s \t(%-3d Seats) bus  %s%d\r\n", tName, transaction, tour_name, maxSeats, bus_type, bus_list.size() - 1);
                have_bus = false;
            }
            if (people > 0) {
                bus = new Bus();
                bus.insert_people(people, tour_name);
                bus_list.add(bus);
                System.out.printf("%s >> Transaction %2d : %-8s \t(%-3d Seats) bus  %s%d\r\n", tName, transaction, tour_name, people, bus_type, bus_list.size() - 1);
                have_bus = true;
                people = 0;
            }

        } else if (have_bus) { // now if bust already exist
            //(>=)
            if (maxSeats - bus_list.get(bus_list.size() - 1).getSeat() > people) { //remaining seat > people
                bus_list.get(bus_list.size() - 1).insert_people(people, tour_name); // add people to the bus
                System.out.printf("%s >> Transaction %2d : %-8s \t(%-3d Seats) bus  %s%d\r\n", tName, transaction, tour_name, people, bus_type, bus_list.size() - 1); // print
                if (maxSeats - bus_list.get(bus_list.size() - 1).getSeat() == people) {
                    have_bus = false;
                } else {
                    have_bus = true;
                }

            } else if (maxSeats - bus_list.get(bus_list.size() - 1).getSeat() < people) { // if remaing seat < people
                temp_people = maxSeats - bus_list.get(bus_list.size() - 1).getSeat(); //get remaining people
                bus_list.get(bus_list.size() - 1).insert_people(temp_people, tour_name); //store remaining people
                have_bus = false;
                System.out.printf("%s >> Transaction %2d : %-8s \t(%-3d Seats) bus  %s%d\r\n", tName, transaction, tour_name, temp_people, bus_type, bus_list.size() - 1);
                people -= temp_people;

                while (people >= maxSeats) {

                    bus = new Bus(); // create bus for remaining people
                    bus.insert_people(maxSeats, tour_name);
                    bus_list.add(bus); // add the bus to the list
                    System.out.printf("%s >> Transaction %2d : %-8s \t(%-3d Seats) bus  %s%d\r\n", tName, transaction, tour_name, maxSeats, bus_type, bus_list.size() - 1);
                    people -= maxSeats;
                    have_bus = false;
                }
                if (people > 0) {
                    bus = new Bus();
                    bus.insert_people(people, tour_name);
                    bus_list.add(bus);
                    System.out.printf("%s >> Transaction %2d : %-8s \t(%-3d Seats) bus  %s%d\r\n", tName, transaction, tour_name, people, bus_type, bus_list.size() - 1);
                    have_bus = true;
                    people = 0;
                }
            }
        }
    }
}
