import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ProfileSorter {
    TreeMap < String, String > profileMap = new TreeMap < > ();

    public void sortProfiles() {
        try {
            File file = new File("profiles.cfg");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                profileMap.put(scanner.nextLine(), scanner.nextLine());
            }

            FileWriter printer = new FileWriter("profiles.cfg");
            for (Map.Entry < String, String > profile: profileMap.entrySet()) {
                printer.write(profile.getKey() + "\n" + profile.getValue() + "\n");
            }
            printer.close();
        } catch (IOException e) {
            System.exit(1);
        }
    }

}