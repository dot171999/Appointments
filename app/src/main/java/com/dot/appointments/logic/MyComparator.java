package com.dot.appointments.logic;

import java.util.Comparator;

public class MyComparator implements Comparator<stEvent> {

    @Override
    public int compare(stEvent o1, stEvent o2) {
        if(o1.y<o2.y)
            return -1;
        if(o1.y>o2.y)
            return 1;

        if(o1.type == 0 && o2.type ==1)
            return 1;
        if(o1.type == 1 && o2.type ==0)
            return -1;

        if(o1.rectID<o2.rectID)
            return -1;
        if(o1.rectID>o2.rectID)
            return 1;

        return 0;
    }
}