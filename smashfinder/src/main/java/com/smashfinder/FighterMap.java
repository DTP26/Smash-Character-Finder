package com.smashfinder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class FighterMap {
    HashMap<String, Fighter> nameMap;

    // initialize FighterMap by adding all characters on https://ultimateframedata.com/smash
    public FighterMap() {
        this.nameMap = new HashMap<>();
        String url = "http://ultimateframedata.com/smash";
        String statsUrl = "http://ultimateframedata.com/stats";
        Document homePage;
        Document statsPage;
        try {
            homePage = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Failed to fetch page: " + e.getMessage());
            return;
        }

        try {
            statsPage = Jsoup.connect(statsUrl).get();
        } catch (IOException e) {
            System.out.println("Failed to fetch page: " + e.getMessage());
            return;
        }
        Elements fighters =
                homePage.selectFirst("div#charList").children();
        // ignore non character pages my removing until banjo is reached
        // figure out a way to not hard code this
        fighters.remove(0);
        fighters.remove(0);
        fighters.remove(0);
        fighters.remove(0);
        // first, we add characters to the fightermap
        for (Element fighter : fighters) {
            String fighterName = fighter.selectFirst("a").attr("title").trim()
                    .replaceAll("&", "and")
                    .replaceAll("[.]", "")
                    .replaceAll("-", " ");
            nameMap.put(fighterName, new Fighter(fighterName));
        }

        // then, we go through tables of the stats page

        // get airdodge speed from dodges table
        Elements dodges =
                statsPage.selectFirst("table#neutralairdodgestable > tbody").children();
        for (Element character : dodges) {
            String name = character.selectFirst("td:eq(1)")
                    .text().trim()
                    .replaceAll("&", "and")
                    .replaceAll("[.]", "")
                    .replaceAll("-", " ");

            int airdodgeSpeed = Integer.parseInt(character.selectFirst("td:eq(2)").text()
                    .replaceAll("[()]", "")
                    .substring(0, 1));
            // account for characters with frame 1 escapes that aren't airdodges
            if (("Bayonetta Duck Hunt Jigglypuff Ken Little Mac Lucina Luigi Mii Brawler" +
                    "Marth Olimar Pac Man Squritle Ivysaur Charizard Ryu Shulk Snake Yoshi").contains(name)) {
                //this.nameMap.get(name).fastEscape = 1;
                this.nameMap.get(name).stats.put("Fast Escape", 1);
            } else {
                this.nameMap.get(name).stats.put("Fast Escape", Integer.compare(3, airdodgeSpeed));
            }
            //System.out.println("Fast escape of " + name + " is " + this.nameMap.get(name).fastEscape);
        }
        //System.out.println("The airdodge speed of " + this.name + " is " + airdodgeSpeed);*/

        // first, the weight table
        Elements weights = statsPage.selectFirst("table#weighttable > tbody").children();
        for (Element character : weights) {
            String name = character.selectFirst("td:eq(1)")
                    .text().trim()
                    .replaceAll("&", "and")
                    .replaceAll("[.]", "")
                    .replaceAll("-", " ");

            int weight = Integer.parseInt(character.selectFirst("td:eq(2)").text());
            weight = weight > 101 ? 1 : (weight < 90 ? -1 : 0);
            // special case for rosa due to luma not being in the stats table name
            if (name.contains("Rosalina")) {
                name = "Rosalina and Luma";
            }
            this.nameMap.get(name).stats.put("Weight", weight);
            //System.out.println("Weight of " + name + " is " + this.nameMap.get(name).weight);
        }

        // TODO: handle edge cases with characters that undergo stat changes

        // initial dash and run table
        Elements runs = statsPage.selectFirst("table#dashandruntable > tbody").children();
        for (Element character : runs) {
            String name = character.selectFirst("td:eq(0)")
                    .text().trim()
                    .replaceAll("&", "and")
                    .replaceAll("[.]", "")
                    .replaceAll("-", " ");
            double initialDash = Double.parseDouble(character.selectFirst("td:eq(1)").text());
            double runSpeed = Double.parseDouble(character.selectFirst("td:eq(2)").text());

            int runInt = runSpeed > 1.85 ? 1 : (runSpeed < 1.6 ? -1 : 0);
            int dashInt = initialDash > 2.05 ? 1 : (initialDash < 1.82 ? -1 : 0);
            // special case for ice climbers due to partner being separate
            if (name.contains("Ice Climbers")) {
                if (name.contains("leader")) {
                    this.nameMap.get("Ice Climbers").stats.put("Run Speed", runInt);
                    this.nameMap.get("Ice Climbers").stats.put("Initial Dash", dashInt);
                    // System.out.println("Groundspeed of " + "Ice Climbers" + " is " + this.nameMap.get("Ice Climbers").groundSpeed);
                }
            } else {
                this.nameMap.get(name).stats.put("Run Speed", runInt);
                this.nameMap.get(name).stats.put("Initial Dash", dashInt);
                //System.out.println("Ground Speed of " + name + " is " + this.nameMap.get(name).groundSpeed);
            }
            //System.out.println("Ground speed is " + groundSpeed);
        }

        // airspeed table


        Elements airSpeeds = statsPage.selectFirst("table#airspeedtable > tbody").children();
        for (Element character : airSpeeds) {
            String name = character.selectFirst("td:eq(1)")
                    .text().trim()
                    .replaceAll("&", "and")
                    .replaceAll("[.]", "")
                    .replaceAll("-", " ");

            double airSpeed = Double.parseDouble(character.selectFirst("td:eq(2)").text());
            // special case for rosa due to luma not being in the stats table name
            if (name.contains("Rosalina")) {
                name = "Rosalina and Luma";
            }
            int airInt = airSpeed > 1.14 ? 1 : (airSpeed < 0.97 ? -1 : 0);
            this.nameMap.get(name).stats.put("Air Speed", airInt);
            // System.out.println("Air speed of " + name + " is " + this.nameMap.get(name).airSpeed);
        }
        // hardcode sephiroth due to not being in the table
        this.nameMap.get("Sephiroth").stats.put("Air Speed", -1);

        // air accel table
        Elements airAccels = statsPage.selectFirst("table#airaccelerationtable > tbody").children();
        for (Element character : airAccels) {
            String name = character.selectFirst("td:eq(1)")
                    .text().trim()
                    .replaceAll("&", "and")
                    .replaceAll("[.]", "")
                    .replaceAll("-", " ");

            double airAccel;
            // hero only has additional accel listed for some reason
            if (name.equals("Hero")) {
                airAccel = Double.parseDouble(character.selectFirst("td:eq(3)").text());
            } else {
                airAccel = Double.parseDouble(character.selectFirst("td:eq(4)").text());
            }
            // edge cases needed for characters named differently
            if (name.contains("Dedede")) {
                name = "King Dedede";
            } else if (name.contains("Popo")) {
                name = "Ice Climbers";
            } else if (name.contains("Rosalina")) {
                name = "Rosalina and Luma";
            }
            int accelInt = airAccel > 0.085 ? 1 : (airAccel < 0.06 ? -1 : 0);
            if (!name.equals("Nana")) {
                this.nameMap.get(name).stats.put("Air Acceleration", accelInt);
                //System.out.println("Air accel of " + name + " is " + this.nameMap.get(name).airAccel);
            }
        }
        // some characters aren't on stats page, so we hardcode to avoid computation time of loading fighter pages
        this.nameMap.get("Kazuya").stats.put("Air Acceleration", -1);
        this.nameMap.get("Joker").stats.put("Air Acceleration", 0);
        this.nameMap.get("Pyra").stats.put("Air Acceleration", 0);
        this.nameMap.get("Mythra").stats.put("Air Acceleration", 0);
        this.nameMap.get("Terry").stats.put("Air Acceleration", 0);
        this.nameMap.get("Sephiroth").stats.put("Air Acceleration", 0);

        // fall speed + gravity table to determine floaties / fast fallers
        Elements fallSpeeds = statsPage.selectFirst("table#fallspeedtable > tbody").children();

        // then we combine gravity with results from the fall speed table
        for (Element character : fallSpeeds) {
            String name = character.selectFirst("td:eq(1)")
                    .text().trim()
                    .replaceAll("&", "and")
                    .replaceAll("[.]", "")
                    .replaceAll("-", " ");

            if (name.contains("Rosalina")) {
                name = "Rosalina and Luma";
            }
            double fallSpeed = Double.parseDouble(character.selectFirst("td:eq(2)").text());
            double fastFallSpeed = Double.parseDouble(character.selectFirst("td:eq(3)").text());

            int fallInt = fallSpeed > 1.7 ? 1 : (fallSpeed < 1.5 ? -1 : 0);
            //int fallGravAverage = Math.max(-1, Math.min(1, fallInt + gravTableTemp.get(name)));
            int fastFallInt = fastFallSpeed > 2.7 ? 1 : (fastFallSpeed < 2.4 ? -1 : 0);
            int floatOrFast = Math.max(-1, Math.min(1, fallInt + fastFallInt));
            this.nameMap.get(name).stats.put("Fall Speed", floatOrFast);
            //String fallStatus = floatOrFast < 0 ? "Floaty" : (floatOrFast > 0 ? "Fast Faller" : "Average");
            //System.out.println(name + " is a " + fallStatus);
        }

        // hardcode byleth, not in fall speed or gravity table
        this.nameMap.get("Byleth").stats.put("Fall Speed", 0);


        // find characters with reflectors / antiprojectile properties
        Elements reflectors = statsPage.selectFirst("table#reflectorstable > tbody").children();
        HashSet<String> charsWithReflectors = new HashSet<>();
        // edge case cause pit and dark pit are listed together
        charsWithReflectors.add("Pit");
        charsWithReflectors.add("Dark Pit");
        for (Element character : reflectors) {
            charsWithReflectors.add(
                    character.selectFirst("td:eq(1)")
                            .text().trim()
                            .replaceAll("&", "and")
                            .replaceAll("[.]", "")
                            .replaceAll("-", " ")
            );
        }
        for (String name : nameMap.keySet()) {
            nameMap.get(name).stats.put("Anti Projectile", charsWithReflectors.contains(name) ? 1 : 0);
        }

        // the rest of this is hard coded due to
        // being either more subjective in nature or not on the website
        for (Fighter character : this.nameMap.values()) {
            character.stats.put("Multiple Jumps", ("Banjo and Kazooie Dark Pit Jigglypuff King Dedede Kirby Meta Knight" +
                    "Pit Charizard Ridley Sephiroth").contains(character.name) ? 1 : 0);

            // in general, I need to decide how burst moves (spindash, after burner kick, clown kart dash,
            // etc. factor into these ranges.
            // characters I may need further opinions on:
            // King Dedede, Palutena, Duck Hunt
            boolean closeRange = ("Mario Donkey Kong Link Samus Dark Samus Yoshi Kirby Fox Pikachu Luigi Ness " +
                    "Captain Falcon Jigglypuff Peach Daisy Bowser Ice Climbers Sheik Zelda Dr Mario Pichu Falco " +
                    "Young Link Ganondorf Mewtwo Roy Mr Game and Watch Meta Knight Zero Suit Samus Wario Snake " +
                    "Squirtle Charizard Diddy Kong Lucas Sonic Lucario ROB Wolf Wii Fit Trainer" +
                    "Rosalina and Luma Little Mac Greninja Mii Brawler Palutena Pac Man Ryu Ken Bayonetta " +
                    "Inkling Ridley King K Rool Isabelle Incineroar Piranha Plant Joker Banjo and Kazooie " +
                    "Terry Steve Kazuya").contains(character.name);
            character.stats.put("Close Range", closeRange ? 1 : 0);
            // characters I may need further opinions on:
            // Peach Young Link Diddy Kong Rosalina Palutena Belmonts Banjo mewtwo
            boolean midRange = ("Donkey Kong Link Ice CLimbers Marth Lucina Young Link Roy Chrom " +
                    "Pit Dark Pit Charizard Zero Suit Samus Ike Diddy Kong King Dedede Olimar Toon Link Mii Swordfighter " +
                    "Palutena Robin Shulk Bowser Jr Cloud Corrin Ridley Hero Byleth Steve " +
                    "Sephiroth Pyra Mythra Sora Rosalina and Luma Peach Daisy ROB").contains(character.name);
            character.stats.put("Mid Range", midRange ? 1 : 0);
            // characters I may need further opinions on:
            // Mario Dr Mario Falco Wolf Pit Sonic Greninja Bowser Jr Ryu Inkling Sephiroth Pyra
            boolean longRange = ("Link Samus Dark Samus Pikachu Ness Sheik Zelda Dr Mario Pichu " +
                    "Young Link Mewtwo Pit Dark Pit Snake Ivysaur Lucas Sonic King Dedede Olimar " +
                    "Lucario ROB Toon Link Wolf Villager Mega Man Wii Fit Trainer Greninja " +
                    "Mii Swordfighter Mii Gunner Palutena Pac Man Robin Bowser Jr Duck Hunt " +
                    "Ryu Simon Richter King K Rool Isabelle Piranha Plant Joker Hero " +
                    "Banjo and Kazooie Min Min Sephiroth Pyra Sora").contains(character.name);
            character.stats.put("Long Range", longRange ? 1 : 0);
        }
        // hardcode samus, mario, and bowser due to being included in zero suit samus / dr. mario / bowser jr.
        this.nameMap.get("Samus").stats.put("Mid Range", 0);
        this.nameMap.get("Mario").stats.put("Long Range", 0);
        this.nameMap.get("Bowser").stats.put("Mid Range", 0);
        this.nameMap.get("Bowser").stats.put("Long Range", 0);
    }

    /* takes in a map of user's preferred stats and computes the best fighter for them:
    the first int in the array is whether the user wants the stat to be high, low, etc
    The second int in the array is whether matching that stat is mandatory, preferred, or irrelevant
     if the second value for a stat is 1, a point is added to a character's score if the stat matches
     if the second value for a stat is 0, the stat is ignored entirely in the scoring
     if the second value for a stat is -1, a character is removed from the pool entirely if the stat doesn't match
     We return a Hashset just in case there are multiple characters tied for highest score
     */
    public HashSet<String> bestFighter(
             HashMap<String, int[]> requirements) {
        // map to link characters with their scores
        int highestScore = -1;
        HashSet<String> bestMatches = new HashSet<>();
        for (String currCharacter : nameMap.keySet()) {
            int score = 0;
            boolean disqualified = false;
            HashMap<String, Integer> currStats = nameMap.get(currCharacter).stats;
            for (String stat : requirements.keySet()) {
                int value = requirements.get(stat)[0];
                int requested = requirements.get(stat)[1];
                // only tally requirement if it was actually requested
                if (requested != -1) {
                    boolean reqMet = value == currStats.get(stat);
                    if (reqMet) {
                        score++;
                    } else if (requested == 1) {
                        disqualified = true;
                        break;
                    }
                }
                // ignore if the user doesn't care about the stat
            }
            // add the character if their score is the highest
            // reset the returned set of characters if new high score is reached
            if (!disqualified && score >= highestScore) {
                if (score > highestScore) {
                    highestScore = score;
                    bestMatches.clear();
                }
                bestMatches.add(currCharacter);
            }
        }
        return bestMatches;
    }


    // returns a fighter from the figherMap

    public Fighter get(String name) {
        return nameMap.get(name);
    }
}
