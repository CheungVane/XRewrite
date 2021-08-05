import com.XRewrite.Configuration;
import com.XRewrite.Parser.impl.Parser;
import com.XRewrite.Report;
import com.XRewrite.XRewriteExecutor;
import com.XRewrite.XRewriteMain;
import com.XRewrite.bean.IRule;
import com.XRewrite.utils.ReportUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParserTest {
    NumberFormat numberFormat = NumberFormat.getInstance();

    @Before
    public void init() {
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(5);
        numberFormat.setMaximumIntegerDigits(5);
    }

    @Test
    public void testParser(){
        List<String> program = new ArrayList<>();
        program.add("acgt:HumanBeing(X) :- acgt:clinicalTrialLeaderOf(X,Y)");
        program.add("acgt:SystemicVein(X) :- acgt:SubdivisionOfInferiorVenaCavalTree(X)\n");
        program.add("acgt:Identifier(X) :- acgt:PatientIdentifier(X)\n");
        program.add("%Deterministic dependencies\n");
        Parser parser = new Parser();
        parser.parse(program);
        System.out.println(parser.getRules());
    }

    @Test
    public void test1() throws IOException {

        //Configuration.getInstance().setElimination(false);

        List<String> program = new ArrayList<>();



        String fileFullName = "U.txt";
        URL fileURL = this.getClass().getResource("/testset/"+fileFullName);
        if(fileURL==null){
            System.out.println(fileFullName);
            return;
        }
        System.out.println(fileURL);
        String filePath = fileURL.getPath();
        File filename = new File(filePath);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        while (true) {
            line = br.readLine();
            if(line==null)
                break;
            program.add(line);
        }



        System.out.println("program: "+program.toString());
        XRewriteMain main = new XRewriteMain(program);

        List temp = new ArrayList();
        temp.add(main.getParser().parseQuery("Q6(?X0,?X1)"));
        main.setQueries(temp);


        main.execute();
        XRewriteExecutor executor = main.getExecutor();

        System.out.println("output:" );
        for(Set<IRule> rs: main.getOutput()){
            for(IRule r: rs){
                System.out.println(r);
            }
        }
        //main.getOutput();
        System.out.println("output size:" + executor.getOutput().get(0).size());

        for(Report report: ReportUtils.getAllReport()){
            System.out.println(report.toString());
        }
    }
}
