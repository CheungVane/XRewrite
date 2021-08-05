package com.XRewrite.PropagationGraph;

import com.XRewrite.bean.impl.Position;
import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class Node implements Serializable {
    private Position pos;

    Node(Position pos){
        this.pos = pos;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equal(pos, node.pos);
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        builder.append(this.pos);
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        return pos.toString();
    }
}