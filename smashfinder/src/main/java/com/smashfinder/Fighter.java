package com.smashfinder;
import java.util.*;
// Int's values are either -1, 0, or 1, indicating below/around/above average respectively
// not doing oos for now cause site is inconsistent and certain moves are fast but not good oos for various reasons
// if I do add out of shield, it will be hardcoded
//public int fastestOOS;
    /*public int runSpeed;
    public int dashSpeed;
    public int airSpeed;
    public int airAccel;
    public int fallSpeed;
    public int weight;
    public int fastEscape;
    public boolean closeRange;
    public boolean midRange;
    public boolean longRange;
    public boolean multipleJumps;
    public boolean antiProjectile;*/
// TODO: add other hardcoded things such as tether grab, multiple jumps, etc
// TODO: maybe add character difficulty/complexity? kinda subjective though

public class Fighter {
    public String name;
    public HashMap<String, Integer> stats;

    // other fields are handled in fighterMap construction
    public Fighter(String name) {
        this.name = name;
        this.stats = new HashMap<>();
    }

    // prints out all the attributes of the character

    public void fighterInfo() {
        System.out.println("This character is " + name + "!");

        // custom message for each potential stat

        String runMessage =
                stats.get("Run Speed") == 1 ? "They have a high run speed, so it's not hard for them to move across the stage."
                        : (stats.get("Run Speed") == -1 ? "They have a slow run speed, so it can be difficult to move across the stage."
                        : "They have an average run speed, so moving along the stage isn't easy nor difficult generally.");
        System.out.println(runMessage);

        String dashMessage =
                stats.get("Initial Dash") == 1 ? "They have a fast initial dash, making it easy for them to make sudden movements, " +
                        "but potentially difficult to control."
                        : (stats.get("Initial Dash") == -1 ? "They have a slow initial dash, so sudden movements aren't as viable."
                        : "They have an average initial dash, " +
                        "so sudden movements aren't as fast, but still effective.");
        System.out.println(dashMessage);

        String airSpeedMessage =
                stats.get("Air Speed") == 1 ? "They have a high air speed, " +
                        "making it easier for them to cover distance in the air."
                        : (stats.get("Air Speed") == -1 ? "They have a slow air speed, making it harder for them " +
                        "to cover distance in the air."
                        : "They have an average air speed, so covering distance " +
                        "in the air isn't relatively easy nor difficult.");
        System.out.println(airSpeedMessage);

        String airAccelMessage =
                stats.get("Air Acceleration") == 1 ? "They have a high air acceleration, making it easy for them " +
                        "to change directions in the air."
                        : (stats.get("Air Acceleration") == -1 ? "They have a slow air acceleration, making it more difficult to " +
                        "change directions in the air."
                        : "They have an average air acceleration, so changing directions " +
                        "in the air isn't relatively easy or difficult for them.");
        System.out.println(airAccelMessage);

        String fallSpeedMessage =
                stats.get("Fall Speed") == 1 ? "They fall relatively quickly in the air, making it easier to " +
                        "land onstage but risker to go offstage."
                        : (stats.get("Fall Speed") == -1 ? "They are relatively floaty in the air, " +
                        "making it harder to land onstage but easier to remain offstage."
                : "They have an average fall speed, so it isn't particularly easy or " +
                        "difficult to land onstage or remain offstage.");
        System.out.println(fallSpeedMessage);

        String jumpMessage = stats.get("Multiple Jumps") == 1 ?
                "They have more than one double jump, allowing for more possible landing mixups."
                : "They only have one double jump.";
        System.out.println(jumpMessage);

        String weightMessage =
                stats.get("Weight") == 1 ? "They are a heavy character, so they often live to higher percents, " +
                        "but are more susceptible to juggles."
                        : (stats.get("Weight") == -1 ? "They are a light character, so they can be a bit harder to juggle, but " +
                        "will not survive to very high percents."
                        : "They have an average weight, so they won't survive " +
                        "noticeably longer or less than most characters.");
        System.out.println(weightMessage);

        String escapeMessage =
                stats.get("Fast Escape") == 1 ? "They have a noticeably quick escape option, making them less susceptible to combos."
                        : (stats.get("Fast Escape") == -1 ? "Their fastest escape option is rather slow, so " +
                        "they are more susceptible to combos."
                        : "They have an average frame 3 escape option, " +
                        "so combos aren't particularly easy or hard on them.");
        System.out.println(escapeMessage);

        String reflectMessage = stats.get("Anti Projectile") == 1 ?
                "They have at least one move that reflects / absorbs projectiles."
                : "They have no anti-projectile moves. Gonna have to maneuver around the hard way!";
        System.out.println(reflectMessage);

        String effectiveRange;
        boolean closeRange = stats.get("Close Range") == 1;
        boolean midRange = stats.get("Mid Range") == 1;
        boolean longRange = stats.get("Long Range") == 1;
        if (closeRange && !midRange && !longRange) {
            effectiveRange = "They are mostly only interested in fighting up close and personal, likely having " +
                    "many attacks hard to contest at close range.";
        } else if (!closeRange && midRange && !longRange) {
            effectiveRange = "They want to focus on fighting their opponent at midrange, " +
                    "likely with a sword/comparable disjoint.";
        } else if (!closeRange && !midRange && longRange) {
            effectiveRange = "They prefer to keep their opponent at long distance, likely having a large " +
                    "assortment of projectiles or just very long moves.";
        } else if (closeRange && midRange && !longRange) {
            effectiveRange = "They use a combination of up close combat and midrange neutral, likely sporting some " +
                    "safe up close options but also a strong poking tool/item.";
        } else if (closeRange && !midRange) {
            effectiveRange = "They want to be either at close range or long range, likely lacking strong disjoints but " +
                    "having both good up close options and at least one important projectile.";
        } else if (!closeRange && midRange) {
            effectiveRange = "They want to anywhere but up close, having effective disjoints and projectiles. " +
                    "but lacking particuarly safe up close options.";
        } else {
            effectiveRange = "This character has good up close options, relevant disjoints, and at least one " +
                    "important projectile, allowing them to play neutral just about anywhere.";
        }
        System.out.println(effectiveRange);

    }
}
