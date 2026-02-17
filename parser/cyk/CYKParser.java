package parser.cyk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CYKParser {
    Map<String, Set<String>> grammar;
    Set<String> nonTerminals;

    public CYKParser() {
        grammar = new HashMap<>();
        nonTerminals = new HashSet<>();
        loadGrammar();
    }

    private void loadGrammar() {
        addRule("S", "AB");
        addRule("S", "BC");
        addRule("A", "BA");
        addRule("A", "a");
        addRule("B", "CC");
        addRule("B", "b");
        addRule("C", "AB");
        addRule("C", "a");

    }

    private void addRule(String lhs, String rhs) {
        nonTerminals.add(lhs);
        grammar.computeIfAbsent(rhs, k -> new HashSet<>()).add(lhs);
    }
    public boolean parse(String input) {
        int n = input.length();
        Set<String>[][] table = new HashSet[n][n + 1];
for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                table[i][j] = new HashSet<>();
            }
        }
        for (int i = 0; i < n; i++) {
            String terminal = String.valueOf(input.charAt(i));
            if (grammar.containsKey(terminal)) {
                table[i][1].addAll(grammar.get(terminal));
            }
        }
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                for (int j = 1; j < l; j++) {

                    Set<String> leftPart = table[i][j];

                    Set<String> rightPart = table[i + j][l - j];

                    for (String b : leftPart) {
                        for (String c : rightPart) {
                            String productionKey = b + c;

                            if (grammar.containsKey(productionKey)) {
                                table[i][l].addAll(grammar.get(productionKey));
                            }
                        }
                    }
                }
            }
        }
        boolean accepted = table[0][n].contains("S");

        printTable(table, input);

        return accepted;
    }

    private void printTable(Set<String>[][] table, String input) {
        System.out.println("CYK Parse Table for: \"" + input + "\"");
        System.out.println("Format: table[row][col] where row=start index, col=length");
        System.out.println("--------------------------------------------------");

        int n = input.length();
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= n; j++) {
                Set<String> cell = table[i][j];
                if (!cell.isEmpty()) {
                    System.out.printf("table[%d][%d] (%s): %s%n", i, j, input.substring(i, i+j), cell);
                }
            }
        }
        System.out.println("--------------------------------------------------");
    }

    public static void main(String[] args) {
        CYKParser parser = new CYKParser();

        // Test strings
        String input1 = "baaba";
        String input2 = "bbbbb";

        System.out.println("Parsing: " + input1);
        boolean result1 = parser.parse(input1);
        System.out.println("Result: " + (result1 ? "ACCEPTED" : "REJECTED"));
        System.out.println();

        System.out.println("Parsing: " + input2);
        boolean result2 = parser.parse(input2);
        System.out.println("Result: " + (result2 ? "ACCEPTED" : "REJECTED"));
    }
}