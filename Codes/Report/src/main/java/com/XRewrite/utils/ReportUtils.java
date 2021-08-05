package com.XRewrite.utils;

import com.XRewrite.Report;

import java.util.Collection;
import java.util.HashMap;

public class ReportUtils {
    private static HashMap<String, Report> reportMap = new HashMap<>();
    private static Report currentReport;
    private static long computingPGTime = 0;

    public static void clean(){
        reportMap.clear();
        currentReport = null;
        computingPGTime = 0;
    }

    public static void doReport(){
        System.out.println("PG time: "+computingPGTime);
        System.out.println(getAllReport());
    }

    public static long getComputingPGTime() {
        return computingPGTime;
    }

    public static void setComputingPGTime(long computingPGTime) {
        ReportUtils.computingPGTime = computingPGTime;
    }

    public static Report getReport(String name){
        if(reportMap.containsKey(name)) return reportMap.get(name);
        Report report = new Report(name);
        reportMap.put(name, report);
        return report;
    }

    public static Collection<Report> getAllReport(){
        return reportMap.values();
    }

    public static Report getCurrentReport() {
        return currentReport;
    }

    public static void setCurrentReport(Report currentReport) {
        ReportUtils.currentReport = currentReport;
    }
}
