import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SceneReader {

    //public static final String filePath = "src/male-liquorst.txt.json";
    public static final String filePath = "src/male-animal.txt.json";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        try {
            JSONArray lines = readLines(filePath);

            // Displaying the lines
            scenePresent(lines, 0);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /*private static void scenePresent(JSONArray lines, int lineNumber) throws IOException, ParseException {
        boolean lineNumberReached = false;
        int currentLineNumber = 0;

        for (Object lineObj : lines) {
            String line = (String) lineObj;
            currentLineNumber = currentLineNumber + 1;

            // Skip lines until reaching the specified lineNumber
            if (!lineNumberReached) {
                if (currentLineNumber > lineNumber) {
                    lineNumberReached = true;
                }
            }

            // Start displaying lines only after lineNumberReached is true
            if (lineNumberReached) {
                // Stop displaying lines if one begins with a *
                if (line.startsWith("*choice mood action")) {
                    decisionMAMaker(lines);
                    break;
                } else if (line.startsWith("*choice action")) {
                    break;
                } else if (line.startsWith("*temp")){
                    decisionTempMaker(lines);
                    break;
                }
                System.out.println(line);
            }
        }
    }*/
    private static void scenePresent(JSONArray lines, int lineNumber) throws IOException, ParseException {
        boolean lineNumberReached = false;
        int currentLineNumber = 0;
        List<String> processedLines = new ArrayList<>();

        for (Object lineObj : lines) {
            String line = (String) lineObj;
            currentLineNumber++;

            // Skip lines until reaching the specified lineNumber
            if (!lineNumberReached) {
                if (currentLineNumber > lineNumber) {
                    lineNumberReached = true;
                }
            }

            // Start displaying lines only after lineNumberReached is true
            if (lineNumberReached) {
                if (line.startsWith("*choice mood action")) {
                    decisionMAMaker(convertListToJSONArray(lines.subList(currentLineNumber, lines.size())));
                    break;
                } else if (line.startsWith("*choice")) {
                    decisionAMaker(convertListToJSONArray(lines.subList(currentLineNumber, lines.size())));
                    break;
                } else if (line.startsWith("*temp")) {
                    decisionTempMaker(convertListToJSONArray(lines.subList(currentLineNumber, lines.size())));
                    break;
                } else if (line.startsWith("*finish")){
                    System.out.println("This scenario is finished. Move on to the next");
                    break;
                } else if (line.startsWith("*set")){
                    statUpdater(convertListToJSONArray(lines.subList(currentLineNumber, lines.size())), line);
                } else if (line.startsWith("*goto")){
                    //System.out.println(line.substring(6));
                    int labelLine = labelFinder(line.substring(1)); //substring removes a character so "*goto AFTER" is accepted, might need tweaking for other cases
                    scenePresent(lines, labelLine);
                    break;
                }


                System.out.println(line);
                processedLines.add(line);
            }
        }
    }

    private static void statUpdater(JSONArray remainingLines, String currentLine) {
        String statToUpdate = currentLine.substring(5, 7);

        if (statToUpdate == "F?"){
            statToUpdate = "familial";
        }else if (statToUpdate == "IN"){
            statToUpdate = "intellectual";
        }else if (statToUpdate == "PH"){
            statToUpdate = "physical";
        }else if (statToUpdate == "s?"){
            statToUpdate = "social";
        }else if (statToUpdate == "v?"){
            statToUpdate = "vocational";
        }else if (statToUpdate == ""){
            statToUpdate = "intellectual";
        }


    }

    private static JSONArray convertListToJSONArray(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(list);
        return jsonArray;
    }

    private static void decisionTempMaker(JSONArray remainingLines) throws IOException, ParseException{
        Scanner input = new Scanner(System.in);
        // Array to store the labels of the different outcomes.
        ArrayList<String> labelResults = new ArrayList<>();
        int decisionMade = 1;
        System.out.println("This decision is made by chance");
        for (Object lineObj : remainingLines) {
            String line = (String) lineObj;
            if (line.startsWith("*label")){
                break;
            }
            if (line.startsWith("*rand")){
                int rangeVal = Integer.parseInt(line.substring(line.length() - 1));
                decisionMade = tempValConditionHandler(1,rangeVal);
            }
            if (line.startsWith("  *go")){
                labelResults.add(line.substring(8));
                System.out.println(line.substring(8));
            }
        }
        String label = labelResults.get(2);
        System.out.println("Loop Ended");
        int goToLine = getNumberForLabel(label);
        JSONArray lines = readLines(filePath);
        scenePresent(lines, goToLine);
    }

    private static void decisionAMaker(JSONArray remainingLines) throws IOException, ParseException {
        Scanner input = new Scanner(System.in);
        // Array to store actions
        ArrayList<String> Actions = new ArrayList<>();
        ArrayList<String> gotoArray = new ArrayList<>();

        System.out.println("\nTime to choose an action");

        for (Object lineObj : remainingLines) {
            String line = (String) lineObj;

            // If the line starts with *label, end the loop
            if (line.startsWith("*label")) {
                break;
            }
            // If the line starts with "    #", add it to the actActions array
            if (line.startsWith("  #")) {
                Actions.add(line.substring(3)); // Remove the leading spaces
            }
            // If the line starts with "      *goto" add it to the gotoArray array
            if (line.startsWith("    *goto")) {
                gotoArray.add(line.substring(5));
            }
        }

        boolean decisionMade = false;
        String gotoLine = "";
        // Display the action options
        while (!decisionMade) {
            System.out.println("\nActions:");
            for (String action : Actions) {
                System.out.println("  " + action);
            }
            System.out.println("Choose an action");
            int act = input.nextInt();
            //Checks the right combination of actions are chosen
            //Needs replacing with proper input checking system

            if (act <= Actions.size()) {
                System.out.println("Valid Action chosen");
                gotoLine = gotoArray.get(act - 1);
                decisionMade = true;
            }


        }
        System.out.println(gotoLine);
        JSONArray lines = readLines(filePath);
        int newLineBegin = labelFinder(gotoLine);
        System.out.println(newLineBegin);
        scenePresent(lines, newLineBegin);
    }

    private static void decisionMAMaker(JSONArray remainingLines) throws IOException, ParseException {
        Scanner input = new Scanner(System.in);
        // Arrays to store moods and actions
        ArrayList<String> moodActions = new ArrayList<>();
        ArrayList<String> actActions = new ArrayList<>();
        ArrayList<String> gotoArray = new ArrayList<>();

        System.out.println("\nTime to choose a mood and an action");

        for (Object lineObj : remainingLines) {
            String line = (String) lineObj;

            // If the line starts with *label, end the loop
            if (line.startsWith("*label")) {
                break;
            }
            //Currently, only works with two mood, two action scenarios
            // If the line starts with "  #", add it to the moodActions array
            if (line.startsWith("  #")) {
                moodActions.add(line.substring(3)); // Remove the leading spaces
            }
            // If the line starts with "    #", add it to the actActions array
            if (line.startsWith("    #")) {
                actActions.add(line.substring(5)); // Remove the leading spaces
            }
            // If the line starts with "      *goto" add it to the gotoArray array
            if (line.startsWith("      *goto")){
                gotoArray.add(line.substring(7));
            }
        }
        boolean decisionMade = false;
        String gotoLine = "";
        // Display the collected mood and action options
        while (!decisionMade){
            System.out.println("Mood Actions:");
            for (String mood : moodActions) {
                System.out.println("  " + mood);
            }
            System.out.println("\nAct Actions:");
            for (String action : actActions) {
                System.out.println("  " + action);
            }
            System.out.println("Choose a mood");
            int mood = input.nextInt();
            System.out.println("Choose an action");
            int act = input.nextInt();
            //Checks the right combination of actions are chosen
            //Needs replacing with proper input checking system

            if (mood == act){
                System.out.println("Correct combination chosen");
                gotoLine = gotoArray.get(mood-1);
                decisionMade = true;
            }
        }
        System.out.println(gotoLine);
        JSONArray lines = readLines(filePath);
        int newLineBegin = labelFinder(gotoLine);
        System.out.println(newLineBegin);
        scenePresent(lines, newLineBegin);
    }

    private static int labelFinder(String gotoLine){
        String label = gotoLine.substring(5);
        label = label.toLowerCase(Locale.ROOT);
        System.out.println(label);
        try {
            //JSONObject labels = readLabels(filePath);

            // Use the labels object as needed
            //System.out.println(labels);
            int lineWithLabel = getNumberForLabel(label);
            System.out.println(lineWithLabel);
            return lineWithLabel;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static JSONArray readLines(String filePath) throws IOException, ParseException {
        // Create a JSON parser
        JSONParser parser = new JSONParser();
        // Parse the JSON file
        Object obj = parser.parse(new FileReader(filePath));
        // Cast the parsed object to a JSONObject
        JSONObject jsonObject = (JSONObject) obj;
        // Get the lines array under the "lines" category
        JSONArray lines = (JSONArray) jsonObject.get("lines");
        return lines;
    }

    public static JSONObject readLabels(String filePath) throws IOException, ParseException {
        // Create a JSON parser
        JSONParser parser = new JSONParser();
        // Parse the JSON file
        Object obj = parser.parse(new FileReader(filePath));
        // Cast the parsed object to a JSONObject
        JSONObject jsonObject = (JSONObject) obj;
        // Get the labels object
        JSONObject labels = (JSONObject) jsonObject.get("labels");

        return labels;
    }

    public static int getNumberForLabel(String label) throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject labels = (JSONObject) jsonObject.get("labels");
            return Math.toIntExact((Long) labels.get(label.toLowerCase()));
        }
    }

    public static int tempValConditionHandler(int low, int high){
        Random random = new Random();
        int range = high - low + 1;
        int randomNumber = random.nextInt(range) + low;
        return randomNumber;
    }

    public static void removeLine(JSONArray lines, int numEntriesToRemove) {
        int size = lines.size();

        if (numEntriesToRemove >= size) {
            // If numEntriesToRemove is greater than or equal to the size, clear the JSONArray
            lines.clear();
        } else {
            // Remove the specified number of entries from the start of the JSONArray
            for (int i = 0; i < numEntriesToRemove; i++) {
                lines.remove(0);
            }

            // Shift the remaining entries forward to the start of the JSONArray
            for (int i = numEntriesToRemove; i < size -1; i++) {
                lines.set(i - numEntriesToRemove, lines.get(i));
            }

            // Remove the redundant entries at the end of the JSONArray
            for (int i = size - numEntriesToRemove; i < size -1; i++) {
                lines.remove(size - numEntriesToRemove);
            }
        }
    }


}


