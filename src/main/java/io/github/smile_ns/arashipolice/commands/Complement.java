package io.github.smile_ns.arashipolice.commands;

import java.util.*;

public class Complement {

    private final String[] args;

    public Complement(String[] args){
        this.args = args;
    }

    public List<String> police(){
        List<String> first = new ArrayList<>(Arrays.asList("forbid", "allow", "jail", "exempt-ip"));
        List<String> penalty = new ArrayList<>(Arrays.asList("move", "chat", "attack", "break", "place", "interact", "all"));
        List<String> jail = new ArrayList<>(Arrays.asList("release", "impose", "world"));
        List<String> exempt = new ArrayList<>(Arrays.asList("register", "delete"));

        if (args.length == 1 ) {
            if (args[0].length() == 0) return first;
            else return expansionArg(0, first);
        }

        if (args.length == 2 ) {

            switch (args[0]) {
                case "forbid":
                case "allow":
                    if (args[1].length() == 0) return penalty;
                    else return expansionArg(1, penalty);
                case "jail":
                    if (args[1].length() == 0) return jail;
                    else return expansionArg(1, jail);
                case "exempt-ip":
                    if (args[1].length() == 0) return exempt;
                    else return expansionArg(1, exempt);
            }
        }
        return null;
    }

    private List<String> expansionArg(int index, List<String> list){
        for (String str : list){
            if (str.startsWith(args[index])) return Collections.singletonList(str);
        }
        return null;
    }
}
