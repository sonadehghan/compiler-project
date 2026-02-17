package scanner;

import java.io.*;



public class Scanner {
    private final StringReader reader;
    private int currentChar;
    private int line = 1;

    public Scanner(String input) {
        this.reader = new StringReader(input);
        advance();
    }

    private void advance() {
        try {
            currentChar = reader.read();
        } catch (IOException e) {
            currentChar = -1;
        }
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(currentChar)) {
            if (currentChar == '\n') {
                line++;
            }
            advance();
        }
    }

    public Token getNextToken() {
        while (currentChar != -1) {
            skipWhitespace();
            if (currentChar == '/') {
                advance();
                if (currentChar == '/') {
                    while (currentChar != '\n' && currentChar != -1) advance();
                    continue;
                } else {
                    continue;
                }
            }

            if (Character.isDigit(currentChar)) {
                return readNumberToken();
            }

            if (Character.isLetter(currentChar)) {
                return readIdentifierToken();
            }

            if (currentChar == '\'') {
                return readCharToken();
            }

            switch (currentChar) {
                case '+': advance(); return new Token(TokenType.PLUS, "+");
                case '*': advance(); return new Token(TokenType.MULTIPLY, "*");
                case '-': advance(); return new Token(TokenType.MINUS, "-");
                case '(': advance(); return new Token(TokenType.LPAREN, "(");
                case ')': advance(); return new Token(TokenType.RPAREN, ")");
                case '{': advance(); return new Token(TokenType.LBRACE, "{");
                case '}': advance(); return new Token(TokenType.RBRACE, "}");
                case ';': advance(); return new Token(TokenType.SEMICOLON, ";");
                case ',': advance(); return new Token(TokenType.COMMA, ",");

                case '<': advance(); return new Token(TokenType.LT, "<");
                case '>': advance(); return new Token(TokenType.GT, ">");

                case '=':
                    advance();
                    if (currentChar == '=') { // Check for == (Equality)
                        advance();
                        return new Token(TokenType.EQ, "==");
                    }
                    return new Token(TokenType.EQ, "=");

                case '&':
                    advance();
                    if (currentChar == '&') {
                        advance();
                        return new Token(TokenType.AND, "&&");
                    }
                    return new Token(TokenType.UNKNOWN, "&");

                case '|':
                    advance();
                    if (currentChar == '|') {
                        advance();
                        return new Token(TokenType.OR, "||");
                    }
                    return new Token(TokenType.UNKNOWN, "|");

                default:
                    char unknown = (char) currentChar;
                    advance();
                    return new Token(TokenType.UNKNOWN, String.valueOf(unknown));
            }
        }

        return new Token(TokenType.EOF, "<EOF>");
    }

    private Token readNumberToken() {
        StringBuilder buffer = new StringBuilder();
        boolean isFloat = false;

        while (Character.isDigit(currentChar)) {
            buffer.append((char) currentChar);
            advance();
        }

        if (currentChar == '.') {
            isFloat = true;
            buffer.append((char) currentChar);
            advance();
            while (Character.isDigit(currentChar)) {
                buffer.append((char) currentChar);
                advance();
            }
        }

        String value = buffer.toString();
        if (isFloat) {
            return new Token(TokenType.FLOAT_LITERAL, value);
        } else {
            return new Token(TokenType.INTEGER_LITERAL, value);
        }
    }

    private Token readIdentifierToken() {
        StringBuilder buffer = new StringBuilder();
        while (Character.isLetterOrDigit(currentChar)) {
            buffer.append((char) currentChar);
            advance();
        }

        String value = buffer.toString();

        switch (value) {
            case "int": return new Token(TokenType.INT, value);
            case "float": return new Token(TokenType.FLOAT, value);
            case "char": return new Token(TokenType.CHAR, value);
            case "if": return new Token(TokenType.IF, value);
            case "else": return new Token(TokenType.ELSE, value);
            case "while": return new Token(TokenType.WHILE, value);
            default: return new Token(TokenType.IDENTIFIER, value);
        }
    }

    private Token readCharToken() {
        advance();
        StringBuilder buffer = new StringBuilder();
        if (currentChar != '\'' && currentChar != -1) {
            buffer.append((char) currentChar);
            advance();
        }
        if (currentChar == '\'') {
            advance();
        }
        return new Token(TokenType.CHAR_LITERAL, buffer.toString());
    }
}
