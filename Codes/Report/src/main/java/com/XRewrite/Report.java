package com.XRewrite;

import java.util.ArrayList;
import java.util.List;

public class Report {

    public Report(String name) {
        this.name = name;
    }

    private String name;
    private int rewrittenQueries=0;
    private int TGDSize=0;
    private int querySize;
    private int eliminatedQuerySize;
    private long rewriteTime=0;
    private long factorizeTime=0;
    private long eliminateTime=0;
    private int atoms;
    private List<Integer> generatedQueries = new ArrayList<>();

    public int getAtoms() {
        return atoms;
    }

    public void setAtoms(int atoms) {
        this.atoms = atoms;
    }

    public void addGeneratedQueries(int num){
        generatedQueries.add(num);
    }

    public List<Integer> getGeneratedQueries() {
        return generatedQueries;
    }

    public void setGeneratedQueries(List<Integer> generatedQueries) {
        this.generatedQueries = generatedQueries;
    }

    public int getEliminatedQuerySize() {
        return eliminatedQuerySize;
    }

    public void setEliminatedQuerySize(int eliminatedQuerySize) {
        this.eliminatedQuerySize = eliminatedQuerySize;
    }

    public int getQuerySize() {
        return querySize;
    }

    public void setQuerySize(int querySize) {
        this.querySize = querySize;
    }

    public double getEliminateTime() {
        return eliminateTime;
    }

    public void addEliminateTime(double time){
        this.eliminateTime += time;
    }

    public void addRewriteTime(double time){
        this.rewriteTime += time;
    }

    public void addFactorizeTime(double time){
        this.factorizeTime += time;
    }

    public void setRewrittenQueries(int rewrittenQueries) {
        this.rewrittenQueries = rewrittenQueries;
    }

    public void setTGDSize(int TGDSize) {
        this.TGDSize = TGDSize;
    }

    public int getRewrittenQueries() {
        return rewrittenQueries;
    }

    public int getTGDSize() {
        return TGDSize;
    }




    public long getRewriteTime() {
        return rewriteTime;
    }

    public long getFactorizeTime() {
        return factorizeTime;
    }

    public static void outputReport(){

    }

    @Override
    public String toString() {
        return "Report{" +
                "name='" + name + '\'' +
                ", rewrittenQueries=" + rewrittenQueries +
                ", TGDSize=" + TGDSize +
                ", rewriteTime=" + rewriteTime +
                ", factorizeTime=" + factorizeTime +
                ", eliminateTime=" + eliminateTime +
                '}';
    }
}
