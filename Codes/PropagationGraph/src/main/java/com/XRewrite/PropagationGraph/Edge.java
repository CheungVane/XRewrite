package com.XRewrite.PropagationGraph;

import com.XRewrite.bean.IRule;
import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Edge {
    private IRule Lambda;
    private Node preNode;
    private Node postNode;

    public Edge(IRule lambda, Node preNode, Node postNode) {
        Lambda = lambda;
        this.preNode = preNode;
        this.postNode = postNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equal(Lambda, edge.Lambda) &&
                Objects.equal(preNode, edge.preNode) &&
                Objects.equal(postNode, edge.postNode);
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        builder.append(Lambda);
        builder.append(preNode);
        builder.append(postNode);
        return builder.toHashCode();
    }

    public IRule getLambda() {
        return Lambda;
    }

    public Node getPreNode() {
        return preNode;
    }

    public Node getPostNode() {
        return postNode;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "Lambda=" + Lambda +'}';
    }
}
