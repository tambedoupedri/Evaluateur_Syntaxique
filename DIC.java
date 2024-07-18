import java.util.Scanner;

public class DIC {
    // Variable globale pour récupérer le caractère saisi par l'utilisateur
    private static char calu;
    // Variable pour suivre l'état de la syntaxe de l'expression
    private static int correct = 1;
    private static Scanner scanner = new Scanner(System.in);
    private static int index = 0;
    private static String input;

    public static void main(String[] args) {
        while (true) {
            System.out.print("A toi : ");
            input = scanner.nextLine();
            index = 0;
            lireUtile();
            if (calu == '.') {
                System.out.println("Au revoir...");
                break;
            }
            int result = expression();
            if (calu == '=') {
                lireUtile();
                if (!Character.isWhitespace(calu) && calu != '\0') {
                    correct = 0;
                }
                if (correct == 1) {
                    System.out.println("La syntaxe de l'expression est correcte");
                    System.out.println("La valeur est " + result);
                } else {
                    System.out.println("La syntaxe de l'expression est erronée");
                    correct = 1;
                }
            }
        }
    }

    private static boolean operateurMultiplicatif(char c) {
        return (c == '*' || c == '/');
    }

    private static boolean operateurAdditif(char c) {
        return (c == '+' || c == '-');
    }

    private static boolean chiffre(char c) {
        return (c >= '0' && c <= '9');
    }

    private static void lireUtile() {
        do {
            calu = lireCaractere();
        } while (Character.isWhitespace(calu));
    }

    private static char lireCaractere() {
        if (index < input.length()) {
            return input.charAt(index++);
        } else {
            return '\0';
        }
    }

    private static int nombre() {
        int num = 0;
        while (chiffre(calu)) {
            num = num * 10 + (calu - '0');
            calu = lireCaractere();
        }
        if (Character.isAlphabetic(calu) || calu == '(' || calu == '.') {
            correct = 0;
            while (calu != '=') {
                lireUtile();
            }
        } else if (Character.isWhitespace(calu)) {
            lireUtile();
            if (chiffre(calu) || Character.isAlphabetic(calu)) {
                correct = 0;
                while (calu != '=') {
                    lireUtile();
                }
            }
        }
        return num;
    }

    private static int facteur() {
        int facteur = 0;  // Initialisation par défaut
        if (chiffre(calu)) {
            facteur = nombre();
        } else if (calu == '(') {
            lireUtile();
            facteur = expression();
            if (calu == ')') {
                lireUtile();
            } else {
                correct = 0;
            }
        } else {
            correct = 0;
            while (calu != '=') {
                lireUtile();
            }
        }
        return facteur;
    }

    private static int terme() {
        int term = facteur();
        while (operateurMultiplicatif(calu)) {
            char op = calu;
            lireUtile();
            int nextFacteur = terme();
            if (op == '*') {
                term *= nextFacteur;
            } else if (op == '/') {
                if (nextFacteur == 0) {
                    System.out.println("Division par zero impossible");
                    correct = 0;
                } else {
                    term /= nextFacteur;
                }
            } else {
                correct = 0;
                while (calu != '=') {
                    lireUtile();
                }
            }
        }
        return term;
    }

    private static int expression() {
        int exp = terme();
        while (operateurAdditif(calu)) {
            char op = calu;
            lireUtile();
            int nextTerme = expression();
            if (op == '+') {
                exp += nextTerme;
            } else {
                exp -= nextTerme;
            }
        }
        return exp;
    }
}
