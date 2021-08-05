import com.XRewrite.XRewriteMain;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OneTest {
    @Test
    public void test() throws IOException {
        String filePath = "E:\\coursematerial\\Dissertation\\dataset\\design data\\datasets2\\00279.txt";
        File f = new File(filePath);
        List<String> program = new ArrayList<>();

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
        main.execute();
    }
}
