import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class UserStats {
    public static void main(String[] args) {
        getVariableValues(userStatsFilePath);
        System.out.println(familial);
        //changeVariableValue("familial", 55);
        System.out.println(familial);
        writeVariableValues(userStatsFilePath);
        System.out.println(familial);
    }


    public static final String userStatsFilePath = "src/userStatsFile";
    private static String characterName;

    private static int familial;
    private static int intellectual;
    private static int physical;
    private static int social;
    private static int vocational;
    private static int calmness;
    private static int confidence;
    private static int expressiveness;
    private static int gentleness;
    private static int happiness;
    private static int thoughtfulness;
    private static int trustworthiness;

    private static String occupation;
    private static String relationshipStatus;
    private static int money;
    private static String acquisitions;

    public static void getVariableValues(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read each line from the file and assign values to variables
            characterName = br.readLine();
            familial = Integer.parseInt(br.readLine());
            intellectual = Integer.parseInt(br.readLine());
            physical = Integer.parseInt(br.readLine());
            social = Integer.parseInt(br.readLine());
            vocational = Integer.parseInt(br.readLine());
            calmness = Integer.parseInt(br.readLine());
            confidence = Integer.parseInt(br.readLine());
            expressiveness = Integer.parseInt(br.readLine());
            gentleness = Integer.parseInt(br.readLine());
            happiness = Integer.parseInt(br.readLine());
            thoughtfulness = Integer.parseInt(br.readLine());
            trustworthiness = Integer.parseInt(br.readLine());
            occupation = br.readLine();
            relationshipStatus = br.readLine();
            money = Integer.parseInt(br.readLine());
            acquisitions = br.readLine();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            // Handle exceptions as needed (e.g., log or throw a custom exception)
        }
    }
    public static void changeVariableValue(String variableName, Object newValue) {
        try {
            // Use reflection to get the field based on the variable name
            Field field = UserStats.class.getDeclaredField(variableName);

            // Set the accessibility of the field to true to modify private fields
            field.setAccessible(true);

            // Update the value of the field with the new value
            field.set(null, newValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            // Handle exceptions as needed (e.g., log or throw a custom exception)
        }
    }


    public static void writeVariableValues(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write each variable value to the file
            bw.write(characterName + "\n");
            bw.write(familial + "\n");
            bw.write(intellectual + "\n");
            bw.write(physical + "\n");
            bw.write(social + "\n");
            bw.write(vocational + "\n");
            bw.write(calmness + "\n");
            bw.write(confidence + "\n");
            bw.write(expressiveness + "\n");
            bw.write(gentleness + "\n");
            bw.write(happiness + "\n");
            bw.write(thoughtfulness + "\n");
            bw.write(trustworthiness + "\n");
            bw.write(occupation + "\n");
            bw.write(relationshipStatus + "\n");
            bw.write(money + "\n");
            bw.write(acquisitions + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions as needed (e.g., log or throw a custom exception)
        }
    }


}
