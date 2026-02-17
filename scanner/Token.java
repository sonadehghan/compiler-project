package scanner;

import java.util.regex.Pattern;

public class Token {
    public final TokenType type;
    final String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

//    @Override
    public String toString() {
        return String.format("Token[%s, '%s']", type, value);
    }
}
