// File: com/godscape/system/themes/sandbox/scenes/SandboxForeground.java
package com.godscape.system.themes.sandbox.scenes;

import java.awt.*;

public class SandboxForeground {
    private int width;
    private int height;

    public SandboxForeground(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(30, 30, 30, 200));
        for (int i = 0; i < width; i += 50) {
            g2d.fillRect(i, height - 30, 30, 30);  // Random foreground objects like rocks
        }
    }
}
