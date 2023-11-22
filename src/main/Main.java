package main;

import org.lwjgl.LWJGLException;
// KSPortalcraft
// 22/11/2023
// 00:51
// A simple program that let's you draw polygons and see how many degrees the polygon has.
// We use 180(n-2) and that's pretty much it.
// It uses LWGJL 2.9.3
// Min JRE: 1.2
// Requires OPENGL 1.1 Support and that's pretty much it.

// Credits: Me, LWGJL Team, Google



import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private static int sides = 3;
    
    // Pretty much removed as it didn't turn out well, but still has to be there to avoid errors I am too lazy to fix
    private static double estimatedAngle = 60.0;
    
    private static double exactAngle = 60.0;
    private static Color polygonColor = Color.WHITE;

    private static JLabel angleLabel;
    private static JLabel exactAngleLabel;

    public static void main(String[] args) {
        initDisplay();
        setupUI();

        while (!Display.isCloseRequested()) {
            render();
            Display.update();
            Display.sync(60);
        }

        Display.destroy();
    }

    private static void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600)); // 800x600 is best
            Display.create();
            Display.setTitle("PolyTool");
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 0, 600, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    private static void setupUI() {
        final JFrame frame = new JFrame("Polygon Drawer");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 1));

        JPanel inputPanel = new JPanel();
        JLabel label = new JLabel("Enter number of sides:");
        final JTextField textField = new JTextField(10);
        JButton button = new JButton("Draw Polygon");

        angleLabel = new JLabel("Version 0.0.1"); // This is because I tried removing the angleLabel and broke it 

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sides = Integer.parseInt(textField.getText());
                    estimatedAngle = Math.round(180.0 * (sides - 2) / sides);
                    exactAngle = 180.0 * (sides - 2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a number.");
                }

                exactAngleLabel.setText("Exact Angle: " + exactAngle + " degrees");
            }
        });

        inputPanel.add(label);
        inputPanel.add(textField);
        inputPanel.add(button);

        JPanel anglePanel = new JPanel();
        anglePanel.add(angleLabel);

        frame.add(inputPanel);
        frame.add(anglePanel);

        JFrame angleFrame = new JFrame("Angle Information");
        angleFrame.setSize(300, 150);
        angleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        angleFrame.setLayout(new GridLayout(2, 1));

        JPanel exactAnglePanel = new JPanel();
        exactAngleLabel = new JLabel("Exact Angle: " + exactAngle + " degrees");

        exactAnglePanel.add(exactAngleLabel);

        JPanel colorPanel = new JPanel();
        JButton colorButton = new JButton("Change Color");

        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(frame, "Choose a color", polygonColor); // cool built in swing dialogue
                if (newColor != null) {
                    polygonColor = newColor;
                }
            }
        });

        colorPanel.add(colorButton);

        angleFrame.add(exactAnglePanel);
        angleFrame.add(colorPanel);

        frame.setVisible(true);
        angleFrame.setVisible(true);
    }

    private static void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        drawPolygon(sides);
    }

    private static void drawPolygon(int sides) {
        double angle = 360.0 / sides;

        GL11.glColor3ub((byte) polygonColor.getRed(), (byte) polygonColor.getGreen(), (byte) polygonColor.getBlue());
        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 0; i < sides; i++) {
            double x = 400 + 200 * Math.cos(Math.toRadians(i * angle));
            double y = 300 + 200 * Math.sin(Math.toRadians(i * angle));
            GL11.glVertex2d(x, y);
        }
        GL11.glEnd();
    }
}
