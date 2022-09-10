import java.util.Scanner;

public class Program {
    static String start;
    static String finish;
    static int stops;
    static String[] locations;
    static String[] allStops;

    public static void main(String[] args) {

//        Parser tester = new Parser();
//        int[][] g = {{0, 1, 2, 0},
//                    {1, 0, 5, 3},
//                    {2, 5, 0, 4},
//                    {0, 3, 4, 0}};
//        System.out.println(tester.modifiedDijkstra(g));

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter starting location: ");

        start = sc.next();

        System.out.println("Enter destination: ");

        finish = sc.next();

        System.out.println("Enter number of stops: ");

        stops = sc.nextInt();
        locations = new String[stops];
        allStops = new String[stops+2];
        allStops[0] = start;
        allStops[stops+1] = finish;

        for (int i = 0; i < stops; i++) {
            System.out.println("Enter addresses of location #" + (i+1) + ": ");
            locations[i] = sc.next();
            allStops[i+1] = locations[i];
        }

//        System.out.println(getOrder());
        sc.close();
    }
}
