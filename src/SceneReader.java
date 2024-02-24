import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class SceneReader {

    public static final String filePath = "src/male-liquorst.txt.json";

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

    private static void scenePresent(JSONArray lines, int lineNumber) throws IOException, ParseException {
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
                } else if (line.startsWith("*temp random")){
                    decisionTempMaker(lines);
                    break;
                }
                System.out.println(line);
            }
        }
    }




    private static void decisionTempMaker(JSONArray remainingLines) throws IOException, ParseException{
        int optionAmount;
        for (Object lineObj : remainingLines) {
            String line = (String) lineObj;
            //System.out.println("Checking if line is included. decisionTempMaker starts here");
            System.out.println(line);

        }

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
    private static final String JSON_DATA = "{\"crc\":-1299289936,\"lines\":[\"You are still much too young to buy liquor, but your friends want to \\\"get wasted\\\" tonight. They plan on waiting outside of the liquor store and asking older customers to buy it for them.\",\"*choice mood action\",\" #EXCITED/INTERESTED\",\"   #JOIN THEM/GET READY TO PARTY\",\"     *goto A11\",\" #NOT INTERESTED\",\"   #FIND SOMETHING ELSE TO DO\",\"     *goto A22\",\"*label A11\",\"*temp randomB\",\"*rand randomB 1 3\",\"*if randomB=1\",\"  *goto B1\",\"*elseif randomB=2\",\"  *goto B2\",\"*else\",\"  *goto B3\",\"*label B1\",\"*set PH%-10\",\"*set IN%-10\",\"A young adult about four years older than you agrees to buy the liquor. He picks out a cherry flavored cordial that you and your friends drink from the bottle. You get \\\"buzzed\\\" and a little rowdy. Later that evening, you lie\",\"down in bed and everything starts to spin.\",\"*page_break\",\"Suddenly, you feel ill. You head for the bathroom, but the door is locked. Someone's in there. BLEAAAAAACH! You throw up red liquid all over the floor. At that moment, your dad comes out of the bathroom. He lets you sleep it off\",\"until morning.\",\"*finish\",\"*label B2\",\"Almost everyone refuses to make the purchase for you. Eventually you give up and find something else to do.\",\"*finish\",\"*label B3\",\"*set TH%-20\",\"*set IN%-10\",\"A middle-aged man with a square jaw looks at you sternly, then asks you what kind of liquor you want to drink. Please choose:\",\"\",\"\",\"Select an action:\",\"\",\"*choice\",\" #BEER\",\"   *goto C1\",\" #WINE\",\"   *goto C2\",\" #HARD LIQUOR\",\"   *goto C3\",\"*label C1\",\"*set OH+1\",\"*goto AFTER\",\"*label C2\",\"*set OH+1\",\"*goto AFTER\",\"*label C3\",\"*set OH+3\",\"*set FM%-20\",\"*set CA%-30\",\"*set TH%-20\",\"*label AFTER\",\"After a long time, the man emerges from the store and reaches into his jacket. He retrieves a small, leather pouch and flips it open. \\\"I am a police officer, boys, and you are violating the law by asking me to purchase liquor for you.\\\"\",\"*page_break\",\"Within minutes a squad car arrives. You are all driven home. Your parents are embarrassed and angry. Familial relationships decline.\",\"*finish\",\"*label A22\",\"*set IN%+20\",\"*set PH%+30\",\"You don't seem to be interested in this kind of experience right now. Intellectual and Physical spheres rise.\",\"*finish\",\"\",\"\"],\"labels\":{\"a11\":12,\"b1\":21,\"b2\":30,\"b3\":33,\"c1\":48,\"c2\":51,\"c3\":54,\"after\":59,\"a22\":64}}";


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


