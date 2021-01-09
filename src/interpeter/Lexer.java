package interpeter;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Lexer {
    public List<String> lexer(String line) {

        List<String> tokens = new LinkedList<String>();
        Scanner in = new Scanner(line);

        while (in.hasNext()){
            tokens.add(in.next().trim());
        }

        in.close();
        return tokens;
    }
}
