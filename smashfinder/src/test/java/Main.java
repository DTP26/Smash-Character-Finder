import com.smashfinder.FighterMap;
import java.util.HashMap;
import java.util.HashSet;
public class Main {
    public static void main(String[] args) {

        // initialize map of characters
        System.out.println("Loading fighter data...");
        FighterMap fighterMap = new FighterMap();
        /*for (Fighter fighter : fighterMap.nameMap.values()) {
            fighter.fighterInfo();
        }*/
        //fighterMap.get("Kirby").fighterInfo();
        HashMap<String, int[]> requirements = new HashMap<>();
        // test values
        int[] runList = new int[2], dashList = new int[2],
                airSpeedList = new int[2], airAccelList = new int[2], fallList = new int[2], weightList = new int[2],
                escapeList = new int[2], closeList = new int[2], midList = new int[2], longList = new int[2],
                multiJumpList = new int[2], antiProjectileList = new int[2];
        // spread seems fine
        runList[0] = 0;
        runList[1] = 0;
        requirements.put("Run Speed", runList);

        // spread seems fine
        dashList[0] = 0;
        dashList[1] = 0;
        requirements.put("Initial Dash", dashList);

        // somewhat high amount of characters in the middle, probably ideal actually
        airSpeedList[0] = 0;
        airSpeedList[1] = 0;
        requirements.put("Air Speed", airSpeedList);


        // very high amount of characters in the middle, fixed
        airAccelList[0] = 0;
        airAccelList[1] = 0;
        requirements.put("Air Acceleration", airAccelList);

        // middle is WAY too small, just removed gravity from equation
        fallList[0] = 0;
        fallList[1] = 0;
        requirements.put("Fall Speed", fallList);

        // spread seems fine
        weightList[0] = 0;
        weightList[1] = 0;
        requirements.put("Weight", weightList);

        escapeList[0] = 0;
        escapeList[1] = 0;
        requirements.put("Fast Escape", escapeList);

        closeList[0] = 0;
        closeList[1] = 0;
        requirements.put("Close Range", closeList);

        midList[0] = 0;
        midList[1] = 0;
        requirements.put("Mid Range", midList);

        longList[0] = 0;
        longList[1] = 0;
        requirements.put("Long Range", longList);

        multiJumpList[0] = 0;
        multiJumpList[1] = 0;
        requirements.put("Multiple Jumps", multiJumpList);

        antiProjectileList[0] = 0;
        antiProjectileList[1] = 0;
        requirements.put("Anti Projectile", antiProjectileList);

        HashSet<String> bestCharacters = fighterMap.bestFighter(requirements);
        System.out.println("These are the characters you should try!");
        int numCharacters = 0;
        for (String character : bestCharacters) {
            System.out.println(character);
            numCharacters++;
            fighterMap.get(character).fighterInfo();
        }
        System.out.println(numCharacters + " characters total!");
    }
}
