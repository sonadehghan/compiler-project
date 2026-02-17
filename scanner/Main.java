package scanner;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String inputCode = ""
                + "int count = 0;\n"
                + "float price = 10.5;\n"
                + "char grade = 'A';\n"
                + "\n"
                + "if (count < 5 && price > 10.0) {\n"
                + "   count = count + 1;\n"
                + "   if (grade == 'A' || count == 1) {\n"
                + "      price = price * 1.5;\n"
                + "   } else {\n"
                + "      price = price - 1.0;\n"
                + "   }\n"
                + "}\n"
                + "while (count < 10) {\n"
                + "   count = count + 1;\n"
                + "}";

        Scanner scanner = new Scanner(inputCode);
        Token token;

        System.out.println(String.format("%-20s %-15s %s", "TYPE", "VALUE", "LINE"));
        System.out.println("---------------------------------------------");

        do {
            token = scanner.getNextToken();
            if (token.type != TokenType.EOF && token.type != TokenType.UNKNOWN) {
                System.out.println(String.format("%-20s %-15s", token.type, token.value));
            } else if (token.type == TokenType.EOF) {
                System.out.println(token);
            }
        } while (token.type != TokenType.EOF);
    }
}
