package gr.thundercats.distrmapper;

import gr.thundercats.distrmapper.common.Directions;
import gr.thundercats.distrmapper.common.LatLng;
import gr.thundercats.distrmapper.mapper.MapWorker;
import gr.thundercats.distrmapper.master.Master;
import gr.thundercats.distrmapper.reducer.ReduceWorker;

import java.util.Scanner;

public class Bootstrap {

    public static String MASTER_HOST;
    public static Integer MASTER_PORT;
    public static Integer THIS_PORT;
    public static Integer TYPE;

    public static void main(String[] args) {

        MASTER_HOST = args[0];
        MASTER_PORT = Integer.parseInt(args[1]);
        THIS_PORT = Integer.parseInt(args[2]);
        TYPE = Integer.parseInt(args[3]);

        switch (TYPE) {
            case 0: { //Master
                System.out.println("Starting Master on port " + MASTER_PORT);

                Master master = new Master(MASTER_PORT);
                Scanner scanner = new Scanner(System.in);

                while (true) {
                    switch (scanner.next().toLowerCase()){
                        case "request": {
                            LatLng start = new LatLng(scanner.nextDouble(), scanner.nextDouble());
                            LatLng end = new LatLng(scanner.nextDouble(), scanner.nextDouble());
                            scanner.nextLine();

                            Directions directions = new Directions(start, end, false);
                            System.out.println("Sending request...");
                            master.requestDirections(directions);
                            break;
                        }
                        case "exit": {
                            return;
                        }
                    }
                }
            }
            case 1: { //MapWorker
                System.out.println("Starting MapWorker on port " + THIS_PORT);

                MapWorker mapWorker1 = new MapWorker(THIS_PORT);
                mapWorker1.sendBind(MASTER_HOST, MASTER_PORT);
                break;
            }
            case 2: { //MapReducer
                System.out.println("Starting MapReducer on port " + THIS_PORT);

                ReduceWorker reduceWorker = new ReduceWorker(THIS_PORT);
                reduceWorker.sendBind(MASTER_HOST, MASTER_PORT);
                break;
            }
        }

    }

}
