package com.XRewrite.PropagationGraph;

import com.XRewrite.Configuration;
import com.XRewrite.bean.ILiteral;
import com.XRewrite.bean.IRule;
import com.XRewrite.bean.ITerm;
import com.XRewrite.bean.impl.BasicFactory;
import com.XRewrite.bean.impl.Position;
import com.XRewrite.utils.CoverageUtils;
import com.XRewrite.utils.LiteralUtils;
import com.XRewrite.utils.ReportUtils;
import com.XRewrite.utils.RuleUtils;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;

public class PropagationGraph implements Serializable {
    private Set<IRule> rules;
    private HashMap<Position, Node> nodeMap = new HashMap<>();
    private HashMap<Pair<Node, Node>, Set<Pair<ILiteral, ILiteral>>> waysMap = new HashMap<>();

    public PropagationGraph(Set<IRule> rules) {
        this.rules = rules;
        long cur = System.currentTimeMillis();
        initMap();
        computePropagationGraph();
        ReportUtils.setComputingPGTime(System.currentTimeMillis()-cur);
        if(Configuration.getInstance().isEnableShowTime())
            System.out.println("computing pg used: "+ (System.currentTimeMillis()-cur));
    }

    public void initMap() {
        for (IRule rule : rules) {
            for (Position p : RuleUtils.getRulePositions(rule)) {
                Node node = getNode(p);
                if (waysMap.containsKey(new Pair<Node, Node>(node, node))) continue;
                waysMap.put(new Pair<Node, Node>(node, node), new HashSet<>());

            }
        }
    }

    public void computePropagationGraph() {

        System.out.println("computing PropagationGraph");
        for (IRule rule : rules) {
            Set<ITerm> frontierVs = RuleUtils.getFrontierVariables(rule);
            for (ITerm v : frontierVs) {
                final Set<Position> bodyPos = RuleUtils.getTermBodyPositions(rule, v);
                final Set<Position> headPos = RuleUtils.getTermHeadPositions(rule, v);
                for (Position bp : bodyPos) {
                    for (Position hp : headPos) {
                        Node node1 = getNode(bp);
                        Node node2 = getNode(hp);
                        Pair<Node, Node> posPair = new Pair<>(node1, node2);
                        Set<Pair<ILiteral, ILiteral>> ways = getWays(posPair);
                        ways.add(new Pair<ILiteral, ILiteral>(LiteralUtils.renaming(rule.getBody().iterator().next()), LiteralUtils.renaming(rule.getHead().iterator().next())));
                    }
                }
            }
        }


        Set<Node> changedNodes = new HashSet<>(nodeMap.values());
        Set<Node> tempChanged = new HashSet<>();
        Map<Pair<Node, Node>, Set<List<Edge>>> temp = new HashMap<>();
        while (!temp.equals(waysMap)) {
            tempChanged.clear();
            temp = new HashMap(waysMap);
            Set<Pair<Node, Node>> pairs = new HashSet<>(temp.keySet());

            for (Pair<Node, Node> nodePair1 : pairs) {
                if (!changedNodes.contains(nodePair1.getValue()) || nodePair1.getKey().equals(nodePair1.getValue()))
                    continue;
                for (Pair<Node, Node> nodePair2 : pairs) {
                    if (nodePair2.getKey().equals(nodePair2.getValue())) continue;

                    if (nodePair1.getValue().equals(nodePair2.getKey())) {

                        Set<Pair<ILiteral, ILiteral>> set1 = new HashSet(temp.get(nodePair1));
                        Set<Pair<ILiteral, ILiteral>> set2 = new HashSet(temp.get(nodePair2));
                        for (Pair<ILiteral, ILiteral> e1 : set1) {
                            for (Pair<ILiteral, ILiteral> e2 : set2) {

                                ILiteral l1 = e1.getValue();
                                ILiteral l2 = e2.getKey();


                                if (!l1.getAtom().getPredicate().getPredicateSymbol().equals(l2.getAtom().getPredicate().getPredicateSymbol()))
                                    continue;


                                if (CoverageUtils.isPropagatable(BasicFactory.getInstance().createLiteral(l1), BasicFactory.getInstance().createLiteral(l2))) {
                                    Pair<Node, Node> newPair = new Pair<>(nodePair1.getKey(), nodePair2.getValue());

                                    if (!waysMap.containsKey(newPair))
                                        waysMap.put(newPair, new HashSet<>());

                                    Pair<ILiteral, ILiteral> newWays = new Pair<>(e1.getKey(), e2.getValue());
                                    Set<Pair<ILiteral, ILiteral>> waysSet = waysMap.get(newPair);
                                    if (waysSet.contains(newWays)) continue;

                                    waysSet.add(newWays);
                                    tempChanged.add(nodePair1.getKey());
                                    tempChanged.add(nodePair2.getValue());

                                    //System.out.println("add nodes: "+nodePair1.getKey()+" : "+nodePair2.getValue());

                                }

                            }
                        }

                    }
                }
            }

            changedNodes.clear();
            changedNodes.addAll(tempChanged);
        }

        System.out.println("done computing PropagationGraph");

    }


    public Node getNode(Position pos) {
        if (nodeMap.containsKey(pos))
            return nodeMap.get(pos);
        Node node = new Node(pos);
        nodeMap.put(pos, node);
        return node;
    }

    public Set<Pair<ILiteral, ILiteral>> getWays(Pair<Node, Node> nodePair) {
        if (waysMap.containsKey(nodePair))
            return waysMap.get(nodePair);
        Set<Pair<ILiteral, ILiteral>> ways = new HashSet<>();
        waysMap.put(nodePair, ways);
        return ways;
    }

    public HashMap<Pair<Node, Node>, Set<Pair<ILiteral, ILiteral>>> getWaysMap() {
        return waysMap;
    }

    public boolean isLinked(Position pos1, Position pos2) {
        Node node1 = getNode(pos1);
        Node node2 = getNode(pos2);
        return waysMap.containsKey(new Pair<>(node1, node2));
    }


    @Override
    public String toString() {
        return "PropagationGraph{" +
                "rules=" + rules +
                ", nodeMap=" + nodeMap +
                ", waysMap=" + waysMap +
                '}';
    }
}
