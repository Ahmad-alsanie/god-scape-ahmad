package com.godscape.system.templates;
public class WelcomeScreen {
    public static void display() {
        String asciiArt =
                        "   ██████╗  ██████╗ ██████╗  ███████╗███████╗ █████╗ ██████╗ ███████╗\n" +
                        "  ██╔════╝ ██╔═══██╗██╔═══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝\n" +
                        "  ██║ ████╗██║   ██║██║   ██║███████╗██║     ███████║██████╔╝███████╗\n" +
                        "  ██║   ██║██║   ██║██║   ██║╚════██║██║     ██╔══██║██╔═══╝ ██╔════╝\n" +
                        "  ╚██████╔╝╚██████║╚██████╔╝╚███████║███████╗██║  ██║██║     ███████╗\n" +
                        "   ╚═════╝  ╚═════╝ ╚═════╝  ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝     ╚══════╝\n" +
                        "                                                                     \n" +
                        "         \"Conquer the grind, rule the realm. Automate your legacy.\"\n";

        System.out.println(asciiArt);
    }

    public static void main(String[] args) {
        WelcomeScreen.display();
    }
}
