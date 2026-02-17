package parser.recursiveDescent;

import java.util.ArrayList;
import java.util.List;

public class Recursive_Descent {

    static class Token {
        public enum Type { NUMBER, PLUS, MINUS, MUL, DIV, LPAREN, RPAREN, EOF }

        public final Type type;
        public final String lexeme;

        public Token(Type type, String lexeme) {
            this.type = type;
            this.lexeme = lexeme;
        }

        @Override
        public String toString() {
            return String.format("Token(%s, '%s')", type, lexeme);
        }
    }


    static class Lexer {
        private final String input;
        private int pos = 0;

        public Lexer(String input) {
            this.input = input.replaceAll("\\s+", ""); // Remove all whitespace
        }

        public List<Token> tokenize() {
            List<Token> tokens = new ArrayList<>();
            while (pos < input.length()) {
                char current = peek();

                if (Character.isDigit(current)) {
                    tokens.add(readNumber());
                } else if (current == '+') {
                    tokens.add(new Token(Token.Type.PLUS, "+"));
                    consume();
                } else if (current == '-') {
                    tokens.add(new Token(Token.Type.MINUS, "-"));
                    consume();
                } else if (current == '*') {
                    tokens.add(new Token(Token.Type.MUL, "*"));
                    consume();
                } else if (current == '/') {
                    tokens.add(new Token(Token.Type.DIV, "/"));
                    consume();
                } else if (current == '(') {
                    tokens.add(new Token(Token.Type.LPAREN, "("));
                    consume();
                } else if (current == ')') {
                    tokens.add(new Token(Token.Type.RPAREN, ")"));
                    consume();
                } else {
                    throw new RuntimeException("Unknown character: " + current);
                }
            }
            tokens.add(new Token(Token.Type.EOF, ""));
            return tokens;
        }

        private Token readNumber() {
            StringBuilder sb = new StringBuilder();
            while (pos < input.length() && Character.isDigit(peek())) {
                sb.append(peek());
                consume();
            }
            return new Token(Token.Type.NUMBER, sb.toString());
        }

        private char peek() {
            return input.charAt(pos);
        }

        private void consume() {
            pos++;
        }
    }

    static class Parser {
        private final List<Token> tokens;
        private int current = 0;

        public Parser(List<Token> tokens) {
            this.tokens = tokens;
        }

        public double parse() {
            return expr();
        }

        // expr   -> term ( ( '+' | '-' ) term )*
        private double expr() {
            double result = term();

            while (match(Token.Type.PLUS, Token.Type.MINUS)) {
                Token operator = previous();
                double right = term();
                if (operator.type == Token.Type.PLUS) {
                    result += right;
                } else if (operator.type == Token.Type.MINUS) {
                    result -= right;
                }
            }

            return result;
        }

        // term   -> factor ( ( '*' | '/' ) factor )*
        private double term() {
            double result = factor();

            while (match(Token.Type.MUL, Token.Type.DIV)) {
                Token operator = previous();
                double right = factor();
                if (operator.type == Token.Type.MUL) {
                    result *= right;
                } else if (operator.type == Token.Type.DIV) {
                    result /= right;
                }
            }

            return result;
        }

        // factor -> NUMBER | '(' expr ')'
        private double factor() {
            if (match(Token.Type.NUMBER)) {
                return Double.parseDouble(previous().lexeme);
            }

            if (match(Token.Type.LPAREN)) {
                double result = expr();
                consume(Token.Type.RPAREN, "Expect ')' after expression.");
                return result;
            }

            throw new RuntimeException("Unexpected token: " + peek().type);
        }

        private boolean match(Token.Type... types) {
            for (Token.Type type : types) {
                if (check(type)) {
                    advance();
                    return true;
                }
            }
            return false;
        }

        private boolean check(Token.Type type) {
            if (isAtEnd()) return false;
            return peek().type == type;
        }

        private Token advance() {
            if (!isAtEnd()) current++;
            return previous();
        }

        private boolean isAtEnd() {
            return peek().type == Token.Type.EOF;
        }

        private Token peek() {
            return tokens.get(current);
        }

        private Token previous() {
            return tokens.get(current - 1);
        }

        private Token consume(Token.Type type, String message) {
            if (check(type)) return advance();
            throw new RuntimeException(message);
        }
    }

    public static void main(String[] args) {
        String expression = "10 + 3 * (2 - 1)"; // = 13

        System.out.println("Input: " + expression);

        Lexer lexer = new Lexer(expression);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        double result = parser.parse();

        System.out.println("Result: " + result);
    }
}