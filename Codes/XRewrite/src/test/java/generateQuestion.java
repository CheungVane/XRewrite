import com.XRewrite.XRewriteMain;
import com.XRewrite.bean.*;
import com.XRewrite.bean.impl.BasicFactory;
import com.XRewrite.bean.impl.Query;
import com.XRewrite.bean.impl.Variable;
import com.XRewrite.utils.LiteralUtils;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class generateQuestion {
    @Test
    public void generateQuestions() throws IOException {
        String filePathin = "E:\\coursematerial\\Dissertation\\dataset\\dataset-clean";
        String filePathout = "E:\\coursematerial\\Dissertation\\dataset\\dataset-clean-questions";
        File dic = new File(filePathin);
        cleanData(filePathout);
        for(File f: dic.listFiles()){
            generate(f, filePathout);
        }
    }

    public static void cleanData(String filePathOut) throws IOException {
        File dicOut = new File(filePathOut);
        String filePathIn = "E:\\coursematerial\\Dissertation\\dataset\\dataset";
        filePathOut = filePathOut+"\\";
        File dic = new File(filePathIn);
        for(File f: dic.listFiles()){
            InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePathOut + f.getName()));
            BufferedWriter bw = new BufferedWriter(writer);
            while (true) {
                line = br.readLine();
                if(line==null)
                    break;
                if(skip(line))
                    continue;
                if(!line.equals("")&&!line.contains("==")&&!line.startsWith("/")&&!line.startsWith("%")){
                    bw.write(line+"\n");
                }
            }
            br.close();
            reader.close();
            bw.close();
            writer.close();
        }

    }

    public static boolean skip(String line){
        if(line.equals("")||line.contains("==")||line.startsWith("/")||line.startsWith("%")){
            return true;
        }

        if(line.contains(":-")){
            return line.substring(line.indexOf(":-")).replaceAll(" ","").contains("),");
        }

        return false;
    }

    public static List<ILiteral> getQueries(Set<IRule> rules){
        List<ILiteral> queries = new ArrayList<>();
        for(IRule r: rules){
            if(r.getHead().toString().contains("EX0")||r.getHead().toString().contains("aux"))
                continue;
            queries.addAll(r.getHead());
            //System.out.println(r.getHead().toString());
        }
        return queries;
    }

    public static void generate(File f, String filePathout) throws IOException {
        filePathout =filePathout+"\\";
        List<String> program = new ArrayList<>();
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(f));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        while (true) {
            line = br.readLine();
            if(line==null)
                break;
            program.add(line);
        }
        XRewriteMain main = new XRewriteMain(program);

        //HashSet temp = new HashSet();
        //temp.add(main.getParser().parseQuery("Q22222131(?A, ?B) "));
        main.setQueries(new ArrayList<>());

        Set<IRule> rules = main.getRules();

        List<ILiteral> queries = getQueries(rules);

        // num of queries
        int num = 6; //(int) (Math.log(rules.size())/Math.log(2)) + 1;
        List<IRule> Qs = new ArrayList<>();
        List<IQuery> Qs2 = new ArrayList<>();
        ITerm[] terms = new ITerm[5];
        terms[0] = new Variable("x");
        terms[1] = new Variable("y");
        terms[2] = new Variable("z");
        terms[3] = new Variable("w");
        terms[4] = new Variable("l");

        for(int i=0;i<num;i++){
            Set<ILiteral> tempBody = new HashSet<>();
            Set<ILiteral> tempHead = new HashSet<>();
            int numOfLiteral = (i+2)/2; //(int) Math.floor(Math.random()*4) + 1;
            for(int j =0;j<numOfLiteral;j++){
                int random = (int) Math.floor(Math.random() * queries.size());
                ILiteral tempLiteral = queries.get(random);
                ITerm[] tempTerms = new ITerm[tempLiteral.getAtom().getTuple().size()];
                for(int h=0;h<tempTerms.length;h++){
                    int random2 = (int) Math.floor(Math.random() * terms.length);
                    tempTerms[h] = terms[random2];
                }
                ITuple tempTuple = BasicFactory.getInstance().createTuple(tempTerms);
                IAtom tempAtom = BasicFactory.getInstance().createAtom(tempLiteral.getAtom().getPredicate(), tempTuple);
                boolean isDuplicate = false;
                for(ILiteral l: tempBody){
                    if(tempAtom.getPredicate().equals(l.getAtom().getPredicate())){
                        isDuplicate = true;
                    }
                }
                if(isDuplicate){
                    j--;
                    continue;
                }
                tempBody.add(BasicFactory.getInstance().createLiteral(true, tempAtom));
            }
            //System.out.println("temp: "+temp);
            ITuple tempTuple2 = BasicFactory.getInstance().createTuple(new ArrayList<>(LiteralUtils.getAllTerms(tempBody)));
            IPredicate tempPredicate = BasicFactory.getInstance().createPredicate("Q"+(i+1),tempTuple2.size());
            IAtom tempAtom2 = BasicFactory.getInstance().createAtom(tempPredicate, tempTuple2);
            tempHead.add(BasicFactory.getInstance().createLiteral(true, tempAtom2));

            IQuery queryHead = BasicFactory.getInstance().createQuery(tempHead);

            if(Qs.contains(BasicFactory.getInstance().createRule(tempHead,tempBody))){
                i--;
                continue;
            }

            Qs.add(BasicFactory.getInstance().createRule(tempHead,tempBody));
            Qs2.add(queryHead);
            //System.out.println(queryHead);
            //System.out.println(BasicFactory.getInstance().createRule(tempHead,tempBody));
        }







        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePathout+f.getName(),true));
        BufferedWriter bw = new BufferedWriter(writer);//指定以UTF-8格式写入文件
        for(IRule r: Qs){
            bw.write(r.toString()+"\n");
        }

        for(IQuery q: Qs2){
            bw.write("?- "+ q.toString()+"\n");

        }
        bw.close();
        writer.close();

    }
}
