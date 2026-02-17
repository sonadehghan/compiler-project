package parser.ll1;

import java.util.ArrayList;
import java.util.List;

enum TokenType {
    NUMBER, ID, PLUS, MINUS, MULTIPLY, DIVIDE, LPAREN, RPAREN, EOF
}

class Token {
    public final TokenType type;
    public final String lexeme;

    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return type + "(" + lexeme + ")";
    }
}

class Lexer {
    private final String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input.replaceAll("\\s+", ""); // Remove whitespace
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char c = input.charAt(pos);

            if (Character.isDigit(c)) {
                // Handle Numbers
                StringBuilder sb = new StringBuilder();
                while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                    sb.append(input.charAt(pos));
                    pos++;
                }
                tokens.add(new Token(TokenType.NUMBER, sb.toString()));
            } else if (Character.isLetter(c)) {
                // Handle IDs (Variables)
                StringBuilder sb = new StringBuilder();
                while (pos < input.length() && Character.isLetter(input.charAt(pos))) {
                    sb.append(input.charAt(pos));
                    pos++;
                }
                tokens.add(new Token(TokenType.ID, sb.toString()));
            } else {
                switch (c) {
                    case '+': tokens.add(new Token(TokenType.PLUS, "+")); break;
                    case '-': tokens.add(new Token(TokenType.MINUS, "-")); break;
                    case '*': tokens.add(new Token(TokenType.MULTIPLY, "*")); break;
                    case '/': tokens.add(new Token(TokenType.DIVIDE, "/")); break;
                    case '(': tokens.add(new Token(TokenType.LPAREN, "(")); break;
                    case ')': tokens.add(new Token(TokenType.RPAREN, ")")); break;
                    default: throw new RuntimeException("Unknown character: " + c);
                }
                pos++;
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}

class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token consume() {
        if (!isAtEnd()) current++;
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    public void parse() {
        E();
        if (!isAtEnd()) {
            throw new RuntimeException("Error: Unexpected input at end. Expected EOF.");
        }
        System.out.println("Parsing Successful! Input accepted by grammar.");
    }

    // E -> T E'
    private void E() {
        System.out.println("E -> T E'");
        T();
        EPrime();
    }

    // E' -> + T E' | - T E' | epsilon
    private void EPrime() {
        if (peek().type == TokenType.PLUS) {
            System.out.println("E' -> + T E'");
            consume();
            T();
            EPrime();
        } else if (peek().type == TokenType.MINUS) {
            System.out.println("E' -> - T E'");
            consume();
            T();
            EPrime();
        } else {
            System.out.println("E' -> epsilon");
        }
    }

    // T -> F T'
    private void T() {
        System.out.println("T -> F T'");
        F();
        TPrime();
    }

    // T' -> * F T' | / F T' | epsilon
    private void TPrime() {
        if (peek().type == TokenType.MULTIPLY) {
            System.out.println("T' -> * F T'");
            consume();
            F();
            TPrime();
        } else if (peek().type == TokenType.DIVIDE) {
            System.out.println("T' -> / F T'");
            consume();
            F();
            TPrime();
        } else {
            System.out.println("T' -> epsilon");
        }
    }

    // F -> ( E ) | id | num
    private void F() {
        if (peek().type == TokenType.LPAREN) {
            System.out.println("F -> ( E )");
            consume();
            E();
            if (peek().type != TokenType.RPAREN) {
                throw new RuntimeException("Error: Expected ')'");
            }
            consume();
        } else if (peek().type == TokenType.ID) {
            System.out.println("F -> id (" + peek().lexeme + ")");
            consume();
        } else if (peek().type == TokenType.NUMBER) {
            System.out.println("F -> num (" + peek().lexeme + ")");
            consume();
        } else {
            throw new RuntimeException("Error: Unexpected token " + peek().type + " in Factor");
        }
    }
}

public class LL1Parser {
    public static void main(String[] args) {
        String[] testCases = {
                "1 + 2",
                "a * (b + c) / 4",
                "(1 + 2) * 3",
                "id"
        };

        for (String input : testCases) {
            System.out.println("\n--- Parsing: " + input + " ---");
            try {
                Lexer lexer = new Lexer(input);
                List<Token> tokens = lexer.tokenize();
                Parser parser = new Parser(tokens);
                parser.parse();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
