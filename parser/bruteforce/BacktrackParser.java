package parser.bruteforce;

import scanner.Token;
import scanner.TokenType;
import java.util.ArrayList;
import java.util.List;

public class BacktrackParser {

    private final List<Token> tokens;
    private int pos = 0;

    public BacktrackParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (!peek(TokenType.EOF)) {
            if (!parseStatementOrDecl()) {
                throw new RuntimeException("Syntax Error: Unable to parse at token " + peek());
            }
        }
        System.out.println("\n[SUCCESS]: Input parsed successfully!");
    }

    private boolean tryParse(Runnable parserRule) {
        int snapshot = pos;
        try {
            parserRule.run();
            return true;
        } catch (RuntimeException e) {
            // Backtrack: Restore position if the rule failed
            pos = snapshot;
            return false;
        }
    }

    private boolean parseStatementOrDecl() {
        // Try Declaration first
        if (tryParse(this::parseDeclaration)) return true;
        // If not decl, try Statement
        if (tryParse(this::parseStatement)) return true;
        return false;
    }

    private void parseDeclaration() {
        parseType();
        match(TokenType.IDENTIFIER);

        if (peek(TokenType.EQ)) {
            consume(TokenType.EQ);
            parseExpression();
        }
        match(TokenType.SEMICOLON);
    }

    private void parseStatement() {
        if (peek(TokenType.IF)) {
            parseIfStmt();
        } else if (peek(TokenType.WHILE)) {
            parseWhileStmt();
        } else if (peek(TokenType.LBRACE)) {
            parseBlock();
        } else {
            parseAssignment();
            match(TokenType.SEMICOLON);
        }
    }
    private void parseIfStmt() {
        consume(TokenType.IF);
        match(TokenType.LPAREN);
        parseExpression();
        match(TokenType.RPAREN);
        parseStatement();

        // Optional Else part
        if (peek(TokenType.ELSE)) {
            consume(TokenType.ELSE);
            parseStatement();
        }
    }
    private void parseWhileStmt() {
        consume(TokenType.WHILE);
        match(TokenType.LPAREN);
        parseExpression();
        match(TokenType.RPAREN);
        parseStatement();
    }

    private void parseBlock() {
        match(TokenType.LBRACE);
        while (!peek(TokenType.RBRACE) && !peek(TokenType.EOF)) {
            if (!parseStatementOrDecl()) {
                throw new RuntimeException("Error inside block");
            }
        }
        match(TokenType.RBRACE);
    }

    private void parseAssignment() {
        match(TokenType.IDENTIFIER);
        match(TokenType.EQ);
        parseExpression();
    }


    private void parseExpression() {
        parseLogicalExpr();
    }

    private void parseLogicalExpr() {
        parseRelationalExpr();
        while (peek(TokenType.AND) || peek(TokenType.OR)) {
            consume(); // consume operator
            parseRelationalExpr();
        }
    }

    private void parseRelationalExpr() {
        parseArithmeticExpr();
        if (peek(TokenType.LT) || peek(TokenType.GT) || peek(TokenType.EQ)) {
            consume(); // consume operator
            parseArithmeticExpr();
        }
    }

    private void parseArithmeticExpr() {
        parseTerm();
        while (peek(TokenType.PLUS) || peek(TokenType.MINUS)) {
            consume();
            parseTerm();
        }
    }

    private void parseTerm() {
        parseFactor();
        while (peek(TokenType.MULTIPLY)) {
            consume();
            parseFactor();
        }
    }

    private void parseFactor() {
        if (peek(TokenType.LPAREN)) {
            consume(TokenType.LPAREN);
            parseExpression();
            match(TokenType.RPAREN);
        } else if (peek(TokenType.IDENTIFIER)) {
            consume(TokenType.IDENTIFIER);
        } else if (peek(TokenType.INTEGER_LITERAL)) {
            consume(TokenType.INTEGER_LITERAL);
        } else if (peek(TokenType.FLOAT_LITERAL)) {
            consume(TokenType.FLOAT_LITERAL);
        } else if (peek(TokenType.CHAR_LITERAL)) {
            consume(TokenType.CHAR_LITERAL);
        } else {
            throw new RuntimeException("Syntax Error: Unexpected token " + peek());
        }
    }


    private void parseType() {
        if (peek(TokenType.INT) || peek(TokenType.FLOAT) || peek(TokenType.CHAR)) {
            consume();
        } else {
            throw new RuntimeException("Expected Type");
        }
    }

    private void match(TokenType type) {
        if (peek(type)) {
            consume();
        } else {
            throw new RuntimeException("Expected " + type + " but found " + peek());
        }
    }

    private void consume() {
        if (pos < tokens.size()) pos++;
    }

    private void consume(TokenType type) {
        match(type);
    }

    private Token peek() {
        if (pos < tokens.size()) return tokens.get(pos);
        return new Token(TokenType.EOF, "");
    }

    private boolean peek(TokenType type) {
        return peek().type == type;
    }
}