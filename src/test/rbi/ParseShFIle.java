package test.rbi;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParseShFIle {
    static Map<String, String> shmap = new HashMap<>(); // define a hashmap to save the variables, "key" is variable's name (left to "="), "value" is the value (right = "=")


    /**
 * readFile
 * Read in the file and parse the text, save to the variables and values into shmap
 * */
    static private void readFile(String fileName) {
        List<String> list;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {

            // read the lines in the file, and ignore those unnecessary lines

            list = br.lines()
                    .filter(line -> !line.startsWith("#") && !line.startsWith("echo") && !line.isEmpty() && !line.startsWith("java"))
                    .collect(Collectors.toList());

            // Stream the list and parse the String, for each String in the list, find the "$*_*" replace with the value which has put into the map

            list.stream().forEach(line -> {

                String[] l = line.split("=");
                String key = "", value = "";
                if (!l[0].isEmpty()) key = l[0].trim();
                if (l[1]!=null && !l[1].isEmpty()) value = l[1].trim();

                Pattern pattern = Pattern.compile("\\$([A-Za-z]\\w*)_(\\w+)"); //define a regex indicate start with $, followed by a valid character, a "_" in the middle, stopped by special character,
                Matcher matcher = pattern.matcher(value);
                while (matcher.find()) {
                    String replaceFrom = matcher.group();
                    String replaceTo = shmap.get(replaceFrom.substring(1, replaceFrom.length()));
                    value = value.replace(replaceFrom, replaceTo);
                }

                shmap.put(key, value);


            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // to be noted, this file is located relative path to project classpath, if move it to same folder with this java file, it should be changed to "src/test/rbi/script.sh"
        ParseShFIle.readFile("script.sh");
        System.out.println(shmap.get("CLASSPATH"));


    }

}
