package com.dot.appointments.logic;

public class stRect {
    public int topY;
    public int bottomY;
    public int leftX;      // output parameter, must be initially -1
    public int rightX;
    public int id;
    public String name;

    public stRect(int topY, int bottomY, int leftX, int rightX,int id,String name) {
        this.topY = topY;
        this.bottomY = bottomY;
        this.leftX = leftX;
        this.rightX = rightX;
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopY() {
        return topY;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    public void setBottomY(int bottomY) {
        this.bottomY = bottomY;
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }
}
