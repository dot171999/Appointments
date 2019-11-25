package com.dot.appointments.logic;

public class stEvent {
    public int y;          // this is the 'topY' or 'bottomY' of a rectangle
    public int type;       // either EVENT_START or EVENT_STOP
    public int rectID;

    public stEvent(int y, int type, int rectID) {
        this.y = y;
        this.type = type;
        this.rectID = rectID;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRectID() {
        return rectID;
    }

    public void setRectID(int rectID) {
        this.rectID = rectID;
    }
}
