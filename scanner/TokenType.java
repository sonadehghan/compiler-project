package scanner;

public enum TokenType {
    INT, FLOAT, CHAR, IF, ELSE, WHILE,
    PLUS, MINUS, MULTIPLY,
    LT, GT, EQ, AND, OR, // <, >, =, &&, ||
    LPAREN, RPAREN, LBRACE, RBRACE, SEMICOLON, COMMA,
    INTEGER_LITERAL, FLOAT_LITERAL, CHAR_LITERAL, IDENTIFIER,
    EOF, UNKNOWN
}
