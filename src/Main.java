import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Color Converter App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JColorChooser colorChooser = new JColorChooser();
        colorChooser.getSelectionModel().addChangeListener(e -> {
            Color selectedColor = colorChooser.getColor();
            float[] hsv = rgbToHsv(selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue());
            float[] hsl = rgbToHsl(selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue());
            float[] cmyk = rgbToCmyk(selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue());

            mainPanel.removeAll();
            mainPanel.add(colorChooser, BorderLayout.WEST);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            infoPanel.add(new JLabel("RGB: " + selectedColor.getRed() + ", " + selectedColor.getGreen() + ", " + selectedColor.getBlue()));
            infoPanel.add(new JLabel("HSV: H=" + (int) (hsv[0] * 360) + " S=" + hsv[1] * 100 + " V=" + hsv[2] * 100));
            infoPanel.add(new JLabel("HSL: H=" + (int) (hsl[0] * 360) + " S=" + hsl[1] * 100 + " L=" + hsl[2] * 100));
            infoPanel.add(new JLabel("CMYK: C=" + cmyk[0] + " M=" + cmyk[1] + " Y=" + cmyk[2] + " K=" + cmyk[3]));

            mainPanel.add(infoPanel, BorderLayout.EAST);
            frame.revalidate();
            frame.repaint();
        });

        mainPanel.add(colorChooser, BorderLayout.WEST);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static float[] rgbToHsv(int r, int g, int b) {
        float rf = r / 255f;
        float gf = g / 255f;
        float bf = b / 255f;

        float max = Math.max(rf, Math.max(gf, bf));
        float min = Math.min(rf, Math.min(gf, bf));
        float h, s, v = max;

        float d = max - min;
        s = max == 0 ? 0 : d / max;

        if (max == min) {
            h = 0;
        } else {
            if (max == rf) h = (gf - bf) / d + (gf < bf ? 6 : 0);
            else if (max == gf) h = (bf - rf) / d + 2;
            else h = (rf - gf) / d + 4;
            h /= 6;
        }

        h = (float) (Math.round(h * 100.0) / 100.0);
        s = (float) (Math.round(s * 100.0) / 100.0);
        v = (float) (Math.round(v * 100.0) / 100.0);

        return new float[]{h, s, v};
    }

    private static float[] rgbToHsl(int r, int g, int b) {
        float rf = r / 255f;
        float gf = g / 255f;
        float bf = b / 255f;

        float max = Math.max(rf, Math.max(gf, bf));
        float min = Math.min(rf, Math.min(gf, bf));
        float h, s, l = (max + min) / 2;

        float d = max - min;
        s = l > 0.5f ? d / (2 - max - min) : d / (max + min);

        if (max == min) {
            h = 0;
        } else {
            if (max == rf) h = (gf - bf) / d + (gf < bf ? 6 : 0);
            else if (max == gf) h = (bf - rf) / d + 2;
            else h = (rf - gf) / d + 4;
            h /= 6;
        }

        h = (float) (Math.round(h * 100.0) / 100.0);
        s = (float) (Math.round(s * 100.0) / 100.0);
        l = (float) (Math.round(l * 100.0) / 100.0);

        return new float[]{h, s, l};
    }

    private static float[] rgbToCmyk(int r, int g, int b) {
        double rf = r / 255.0;
        double gf = g / 255.0;
        double bf = b / 255.0;

        double k = 1.0 - Math.max(Math.max(rf, gf), bf);
        if (k == 1.0) {
            return new float[]{0, 0, 0, 100}; // All components are 0% in this case
        }
        int c = (int) Math.round(((1.0 - rf - k) / (1 - k) * 100));
        int m = (int) Math.round(((1.0 - gf - k) / (1 - k) * 100));
        int y = (int) Math.round(((1.0 - bf - k) / (1 - k) * 100));
        int kValue = (int) Math.round(k * 100);

        return new float[]{c, m, y, kValue};
    }
}