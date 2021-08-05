package com.XRewrite.bean.impl;

import com.XRewrite.bean.IPosition;

import java.io.Serializable;

public class Position implements IPosition, Comparable<Position>, Serializable {

    private String pred;
    private int position;

    public Position(final String pred, final int position) {
        this.pred = pred;
        this.position = position;
    }

    public String getPredicate() {
        return (pred);
    }

    public void setPred(final String pred) {
        this.pred = pred;
    }

    @Override
    public int hashCode() {
        return (position * 37) + pred.hashCode();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(final int position) {
        this.position = position;
    }

    @Override
    public boolean equals(final Object o) {
        return this.pred.equals(((Position) o).getPredicate())&&
                this.position == ((Position) o).getPosition();
    }

    @Override
    public String toString() {
        return (getPredicate() + "[" + getPosition() + "]");
    }

    @Override
    public int compareTo(final Position o) {
        if (!(o instanceof Position))
            return -1;

        if (!(o.getPredicate().equals(getPredicate())))
            return -1;
        else {
            if (o.getPosition() < getPosition())
                return (-1);
            else if (o.getPosition() > getPosition())
                return (1);
            else
                return (0);
        }

    }

}
