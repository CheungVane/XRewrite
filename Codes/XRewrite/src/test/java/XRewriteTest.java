import com.XRewrite.Configuration;
import com.XRewrite.Report;
import com.XRewrite.XRewriteMain;
import com.XRewrite.bean.IRule;
import com.XRewrite.utils.ReportUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class XRewriteTest {

    @Test
    public void test(){

    }

    @Test
    public void testWithE() throws IOException {
        String filePath = "E:\\coursematerial\\Dissertation\\dataset\\design data\\datasets2";
        String outputPath = "E:\\coursematerial\\Dissertation\\dataset\\design data\\resultWithE";
        String inExcel = "E:\\coursematerial\\Dissertation\\dataset\\design data\\resultWithE\\reportwithE.xls";
        File dic = new File(filePath);

        InputStream myxls = new FileInputStream(inExcel);
        HSSFWorkbook book = new HSSFWorkbook(myxls);
        Sheet sheet = book.getSheetAt(0);



        for(File f: Objects.requireNonNull(dic.listFiles())){

            int fileNum = Integer.parseInt(f.getName().substring(0, 5));


            System.out.println(f.getName());

            List<Set<IRule>> rewrittenQueries = rewrite(f, sheet);
            if(rewrittenQueries==null) throw new RuntimeException("fail getting rewritten queries");
            String tempDic = outputPath+"\\"+f.getName().replace(".txt","");
            new File(tempDic).mkdir();
            for(Set<IRule> rs: rewrittenQueries){
                String qName = rs.iterator().next().getHead().iterator().next().getAtom().getPredicate().getPredicateSymbol();
                String newPath = tempDic + "\\"+qName+".txt";
                File newFile = new File(newPath);
                //System.out.println(newFile+": "+ReportUtils.getReport(qName).toString());
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(newFile));
                BufferedWriter bw = new BufferedWriter(writer);
                for(IRule r:rs){
                    bw.write(r.toString()+"\n");
                }
                bw.close();
                writer.close();
            }

        }

        FileOutputStream fos = new FileOutputStream(outputPath+"\\reportwithE.xls");
        book.write(fos);//写文件
        fos.close();

    }


    @Test
    public void testWithoutE() throws IOException {
        String filePath = "E:\\coursematerial\\Dissertation\\dataset\\design data\\datasets2";
        String outputPath = "E:\\coursematerial\\Dissertation\\dataset\\design data\\resultWithoutE";
        String inExcel = "E:\\coursematerial\\Dissertation\\dataset\\design data\\resultWithoutE\\reportwithoutE.xls";
        File dic = new File(filePath);
        Configuration.getInstance().setElimination(false);

        for(File f: Objects.requireNonNull(dic.listFiles())){

            InputStream myxls = new FileInputStream(inExcel);
            HSSFWorkbook book = new HSSFWorkbook(myxls);
            Sheet sheet = book.getSheetAt(0);

            System.out.println(f.getName());

            List<Set<IRule>> rewrittenQueries = rewrite(f, sheet);
            if(rewrittenQueries==null) throw new RuntimeException("fail getting rewritten queries");
            String tempDic = outputPath+"\\"+f.getName().replace(".txt","");
            new File(tempDic).mkdir();
            for(Set<IRule> rs: rewrittenQueries){
                String qName = rs.iterator().next().getHead().iterator().next().getAtom().getPredicate().getPredicateSymbol();
                String newPath = tempDic + "\\"+qName+".txt";
                File newFile = new File(newPath);
                //System.out.println(newFile+": "+ReportUtils.getReport(qName).toString());
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(newFile));
                BufferedWriter bw = new BufferedWriter(writer);
                for(IRule r:rs){
                    bw.write(r.toString()+"\n");
                }
                bw.close();
                writer.close();
            }

            FileOutputStream fos = new FileOutputStream(outputPath+"\\reportwithoutE.xls");
            book.write(fos);//写文件
            fos.close();

        }



    }
    public static List<Set<IRule>> rewrite(File f , Sheet sheet){

        List<String> program = new ArrayList<>();
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(f));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            while (true) {
                line = br.readLine();
                if (line == null)
                    break;
                program.add(line);
            }
            XRewriteMain main = new XRewriteMain(program);
            String pgPath = "E:\\coursematerial\\Dissertation\\dataset\\PropagationGraph\\" + f.getName();
            //boolean hasPG = main.getPGfromDisk(pgPath);
            main.execute();
            //if(!hasPG){
            //    main.savePGtoDist(pgPath);
            //}
            writeReport(f, sheet);
            return main.getOutput();
        }catch (IOException e){e.printStackTrace();}
        return null;
    }

    public static void writeReport(File f, Sheet sheet){
        for(int i=0;i<6;i++){
            Row row = sheet.createRow(sheet.getLastRowNum()+1);
            String queryName = "Q"+(i+1);
            Report report = ReportUtils.getReport(queryName);
            int index = 0;
            row.createCell(index).setCellValue(f.getName().replace(".txt",""));
            index++;
            row.createCell(index).setCellValue(queryName);
            index++;
            row.createCell(index).setCellValue(report.getQuerySize());
            index++;
            row.createCell(index).setCellValue(report.getEliminatedQuerySize());
            index++;
            row.createCell(index).setCellValue(report.getRewrittenQueries());
            index++;
            row.createCell(index).setCellValue(report.getAtoms());
            index++;
            row.createCell(index).setCellValue(report.getTGDSize());
            index++;
            row.createCell(index).setCellValue(report.getRewriteTime()+report.getFactorizeTime()+report.getEliminateTime()+ReportUtils.getComputingPGTime());
            index++;
            row.createCell(index).setCellValue(report.getRewriteTime());
            index++;
            row.createCell(index).setCellValue(report.getFactorizeTime());
            index++;
            row.createCell(index).setCellValue(report.getEliminateTime());
            index++;
            row.createCell(index).setCellValue(ReportUtils.getComputingPGTime());
            index++;

            List<Integer> generatedQueries = report.getGeneratedQueries();
            StringBuilder sb = new StringBuilder();
            for(int k = 0;k<generatedQueries.size();k++){
                sb.append(k).append(":").append(generatedQueries.get(k)).append(";\n");
            }
            row.createCell(index).setCellValue(sb.toString());

        }


    }


}
