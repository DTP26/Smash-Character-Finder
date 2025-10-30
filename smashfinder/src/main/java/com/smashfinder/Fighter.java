package com.smashfinder;
import java.util.*;


public class Fighter {
    /* 
    Int's values are either -1, 0, or 1 
        1. For boolean attributes (such as a character having multiple jumps 
           or not, 0 or 1 refer to false and true respectively)
        2. Other attributes (such as run speed, weight, etc) ate are either 
           low (-1) average (0) or high (1)
    */
    
    public String name;
    public HashMap<String, Integer> stats;

    // other fields are handled in fighterMap construction
    public Fighter(String name) {
        this.name = name;
        this.stats = new HashMap<>();
    }        
}
