package com.XRewrite.graph;

import com.XRewrite.bean.IRule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class Graph {
    private DefaultDirectedGraph<IRule, DefaultEdge> queryGraph;

    public Graph(){
        queryGraph = new DefaultDirectedGraph(DefaultEdge.class);
    }

    public void addRule(final IRule father, final IRule child) {
        addRule(father);
        addRule(child);
        if (!father.equals(child)) {
            queryGraph.addEdge(father, child);
        }
    }

    public void addRule(final IRule rule) {
        queryGraph.addVertex(rule);
    }

    public void removeRule(final IRule rule) {
        queryGraph.removeVertex(rule);
    }

    public void removeAndBypass(final IRule rule) {
        final Collection<DefaultEdge> inEdges = ImmutableList.copyOf(queryGraph.incomingEdgesOf(rule));
        final Collection<DefaultEdge> outEdges = ImmutableList.copyOf(queryGraph.outgoingEdgesOf(rule));
        final Set<IRule> predecessors = Sets.newHashSet();
        for (final DefaultEdge e : inEdges) {
            final IRule predecessor = queryGraph.getEdgeSource(e);
            predecessors.add(predecessor);
        }
        final Set<IRule> successors = Sets.newHashSet();
        for (final DefaultEdge e : outEdges) {
            final IRule successor = queryGraph.getEdgeTarget(e);
            successors.add(successor);
        }
        queryGraph.removeVertex(rule);
        for (final IRule predecessor : predecessors) {
            for (final IRule successor : successors) {
                queryGraph.addEdge(predecessor, successor);
            }
        }
    }
    public void removeAndBypassSuccessors(final IRule subsumed, final IRule subsumee, final Set<IRule> explored,
                                          final Set<IRule> newQueries) {

        // remove and bypass Succ(subsumed)/Succ(subsumee)
        final Set<IRule> difference = Sets.difference(successorsOrSelf(subsumed), successorsOrSelf(subsumee));
        for (final IRule q : difference) {
            removeAndBypass(q);
            explored.remove(q);
            newQueries.remove(q);
        }
    }

    private Set<IRule> successorsOrSelf(final IRule query) {
        final Set<IRule> succOrSelf = new HashSet<IRule>();
        succOrSelf.add(query);

        final Collection<DefaultEdge> outEdges = ImmutableList.copyOf(queryGraph.outgoingEdgesOf(query));
        if (outEdges.size() > 0) {
            for (final DefaultEdge e : outEdges) {
                succOrSelf.addAll(successorsOrSelf(queryGraph.getEdgeTarget(e)));
            }
        }

        return succOrSelf;
    }

    public boolean contains(final IRule rule) {
        return queryGraph.containsVertex(rule);
    }

    public int size() {
        return queryGraph.vertexSet().size();
    }

    public Set<IRule> getRules() {
        return queryGraph.vertexSet();
    }

    @Override public String toString() {
        return queryGraph.toString();
    }

}
