package parser.bruteforce;

import java.util.Arrays;
import java.util.List;
import scanner.Token;
import scanner.TokenType;

public class Main {
    public static void main(String[] args) {
        // Code: int a = 5; if (a < 10) { a = a + 1; }
        List<Token> tokens = Arrays.asList(
                new Token(TokenType.INT, "int"),
                new Token(TokenType.IDENTIFIER, "a"),
                new Token(TokenType.EQ, "="),
                new Token(TokenType.INTEGER_LITERAL, "5"),
                new Token(TokenType.SEMICOLON, ";"),

                new Token(TokenType.IF, "if"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.IDENTIFIER, "a"),
                new Token(TokenType.LT, "<"),
                new Token(TokenType.INTEGER_LITERAL, "10"),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.LBRACE, "{"),

                new Token(TokenType.IDENTIFIER, "a"),
                new Token(TokenType.EQ, "="),
                new Token(TokenType.IDENTIFIER, "a"),
                new Token(TokenType.PLUS, "+"),
                new Token(TokenType.INTEGER_LITERAL, "1"),
                new Token(TokenType.SEMICOLON, ";"),

                new Token(TokenType.RBRACE, "}"),
                new Token(TokenType.EOF, "")
        );

        BacktrackParser parser = new BacktrackParser(tokens);
        try {
            parser.parse();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}
