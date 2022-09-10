import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class Parser {
    private String baseURL;
    private Document currentDoc;
    private List<Element> relElements;

    Parser() {
        //Original Google Maps
//        this.baseURL = "https://www.google.com/maps/dir/";
//        this.baseURL = "https://www.mapdevelopers.com/distance_from_to.php?&from=";
        this.baseURL = "https://distancecalculator.globefeed.com/US_Distance_Result.asp?vr=apes&fromplace=";
        relElements = new ArrayList<>();
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
        } catch (IOException e) {
            System.out.println("Could not get info");
        }
    }

    /**
     * A recursive helper method that add all relevant Elements to the relElements field
     * @param curr The current Element the method is running on
     * @param target The class name of the Elements that we want
     */
    public void getSpecificSection(Element curr, String target) {
        for (Element d : curr.children()) {
            if (d.className().equals(target)) {
                relElements.add(d);
            }
            else {
                getSpecificSection(d, target);
            }
        }
    }

    public int getShortestDistance(String start, String end) {
        // Original Code for Google Maps
//        start.replace(" ", "+");
//        end.replace(" ", "+");
////        String finalUrl = "https://www.google.com/maps/dir" + start + "/" + end + "/";
//        String specUrl = start + "/" + end + "/";

        start.replace(",", "");
        end.replace(",", "");
        start.replace(" ", "%20");
        end.replace(" ", "%20");
        String specUrl = start + "&toplace=" + end;
        try {
//            Document currCount = Jsoup.connect(baseURL + specUrl).get();
            currentDoc = Jsoup.connect(baseURL + specUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements divs = currentDoc.select("div");
        for (Element d : divs) {
//            System.out.println(d.text());
//            getSpecificSection(d, "m6QErb");
            getSpecificSection(d, "panel-body");
//            System.out.println(relElements);
            for (int i = 0; i < relElements.size(); i++) {
                if (relElements.get(i).text().contains(" minutes")) {
                    System.out.println(relElements.get(i).text());
                }
            }
        }

        return 0;
    }

    public int[][] createGraph(int numLocations, String[] locs) {
        int[][] graph = new int[numLocations][numLocations];
        int fin = numLocations - 1;
        for (int i = 1; i < numLocations - 1; i++) {
            graph[0][i] = getShortestDistance(locs[0], locs[i]);
            graph[i][0] = getShortestDistance(locs[i], locs[0]);
            graph[fin][i] = getShortestDistance(locs[fin], locs[i]);
            graph[i][fin] = getShortestDistance(locs[i], locs[fin]);
            for (int j = 1; j < numLocations -1; j++) {
                if (i != j) {
                    graph[i][j] = getShortestDistance(locs[i], locs[j]);
                }
            }
        }
        return graph;
    }

    public ArrayList<Integer> modifiedDijkstra(int[][] g) {
        HashMap<Pair<Integer, BitSet>, Integer> distances = new HashMap<>();
        HashMap<Pair<Integer, BitSet>, ArrayList<Integer>> paths = new HashMap<>();
        for (int i = 0; i < g.length; i++) {
            BitSet oneNode = new BitSet(g.length);
            oneNode.set(i);
            distances.put(new Pair<>(i, oneNode), 0);
            ArrayList<Integer> startingPoint = new ArrayList<>();
            startingPoint.add(i);
            paths.put(new Pair<>(i,oneNode), startingPoint);
        }

        Queue<Pair<Integer, BitSet>> q = new LinkedList<>();
        for (int i = 0; i < g.length; i++) {
            BitSet oneNode = new BitSet(g.length);
            oneNode.set(i);
            q.add(new Pair<>(i, oneNode));
        }

        int currentNode = 0;
        BitSet seenNodes = new BitSet(g.length);
        seenNodes.set(0);
        Pair<Integer, BitSet> currPair = new Pair<>(0, seenNodes);
        while (!q.isEmpty()) {
            currPair = q.poll();
            currentNode = currPair.getKey();
            seenNodes = currPair.getValue();
            for (int i = 0; i < g.length; i++) {
                if (g[currentNode][i] != 0) {
                    BitSet currBit = new BitSet(g.length);
                    currBit.set(i);
                    BitSet seenNodesClone = (BitSet) seenNodes.clone();
                    seenNodesClone.or(currBit);
                    if (distances.get(new Pair<>(i, seenNodesClone)) == null) {
                        distances.put(new Pair<>(i, seenNodesClone), Integer.MAX_VALUE);
                    }
                    if (distances.get(new Pair<>(i, seenNodesClone)) > distances.get(new Pair<>(currentNode, seenNodes)) + g[currentNode][i]) {
                        q.add(new Pair<>(i, seenNodesClone));
                        distances.put(new Pair<>(i, seenNodesClone), distances.get(new Pair<>(currentNode, seenNodes)) + g[currentNode][i]);
                        ArrayList currPath = paths.get(new Pair<>(currentNode, seenNodes));
                        currPath.add(i);
                        paths.put(new Pair<>(i, seenNodesClone), currPath);
                    }
                }
            }
        }
        BitSet everyVisited = new BitSet(g.length);
        everyVisited.set(0, g.length);
        return paths.get(new Pair<Integer, BitSet>(g.length-1, everyVisited));
    }

    public String getOrder() {
//        for (int i = 0; i < locations.length; i++) {
//
//        }
        return "";
    }
}
