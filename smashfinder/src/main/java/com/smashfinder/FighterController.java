package com.smashfinder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
 @RestController
@RequestMapping("/api")

public class FighterController {
   
    private final FighterMap fighterMap = new FighterMap();

    @PostMapping("/match")
    public Set<String> getBestFighters(@RequestBody Map<String, int[]> requirements) {
        return fighterMap.bestFighter(new HashMap<>(requirements));
    }
    

}
