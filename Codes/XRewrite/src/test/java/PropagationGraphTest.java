import com.XRewrite.Parser.impl.Parser;
import com.XRewrite.XRewriteMain;
import com.XRewrite.bean.ILiteral;
import com.XRewrite.PropagationGraph.Node;
import com.XRewrite.PropagationGraph.PropagationGraph;
import com.XRewrite.utils.CoverageUtils;
import javafx.util.Pair;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PropagationGraphTest {

    @Test
    public void test1() throws IOException {
        List<String> program = new ArrayList<>();
        String filePath = "E:\\coursematerial\\Dissertation\\dataset\\test\\00057.txt";
        String outPath = "E:\\coursematerial\\Dissertation\\dataset\\PropagationGraph\\00057.txt";
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filePath));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        while (true) {
            line = br.readLine();
            if (line == null)
                break;
            program.add(line);
        }
        XRewriteMain main = new XRewriteMain(program);
        main.getExecutor().distinguishQueriesAndTGDs();
        try{
            FileOutputStream fileOut =
                    new FileOutputStream(outPath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(main.getExecutor().getPg());
            out.close();
            fileOut.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
