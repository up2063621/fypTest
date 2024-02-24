import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class AdventureGame {

    private static final String JSON_FILE_PATH = "src/male-liquorst.txt.json";

    public static int getNumberForLabel(String label) throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(JSON_FILE_PATH)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject labels = (JSONObject) jsonObject.get("labels");
            return Math.toIntExact((Long) labels.get(label.toLowerCase()));
        }
    }

    public static void main(String[] args) {
        try {
            // Example usage
            String label = "a11";
            int correspondingNumber = getNumberForLabel(label);
            System.out.println("The corresponding number for label '" + label + "' is: " + correspondingNumber);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}
