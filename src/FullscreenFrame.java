import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FullscreenFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private ImageIcon icon = new ImageIcon("./assets/italkx.jpg");
    private JLabel iconLab = new JLabel(icon);

    private JPanel panel = new JPanel();
    public JLabel label = new JLabel(Controller.MessageField.getText());
    public JLabel timeUpLabel = new JLabel("Time's up");

    public FullscreenFrame() {
        super("Second Screen");
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        List<String> AVAILABLE_FONT_FAMILY_NAMES = Arrays.asList(ge.getAvailableFontFamilyNames());
        try {
            List<File> LIST = Arrays.asList(
                    new File("./assets/static/Montserrat-Black.ttf"),
                    new File("./assets/static/Montserrat-BlackItalic.ttf"),
                    new File("./assets/static/Montserrat-Bold.ttf"),
                    new File("./assets/static/Montserrat-BoldItalic.ttf"),
                    new File("./assets/static/Montserrat-ExtraBold.ttf"),
                    new File("./assets/static/Montserrat-ExtraBoldItalic.ttf"),
                    new File("./assets/static/Montserrat-ExtraLight.ttf"),
                    new File("./assets/static/Montserrat-ExtraLightItalic.ttf"),
                    new File("./assets/static/Montserrat-Italic.ttf"),
                    new File("./assets/static/Montserrat-Light.ttf"),
                    new File("./assets/static/Montserrat-LightItalic.ttf"),
                    new File("./assets/static/Montserrat-Medium.ttf"),
                    new File("./assets/static/Montserrat-MediumItalic.ttf"),
                    new File("./assets/static/Montserrat-Regular.ttf"),
                    new File("./assets/static/Montserrat-SemiBold.ttf"),
                    new File("./assets/static/Montserrat-SemiBoldItalic.ttf"),
                    new File("./assets/static/Montserrat-Thin.ttf"),
                    new File("./assets/static/Montserrat-ThinItalic.ttf")

            );
            for (File LIST_ITEM : LIST) {
                if (LIST_ITEM.exists()) {
                    Font FONT = Font.createFont(Font.TRUETYPE_FONT, LIST_ITEM);
                    if (!AVAILABLE_FONT_FAMILY_NAMES.contains(FONT.getFontName())) {
                        ge.registerFont(FONT);
                    }
                }
            }
        } catch (FontFormatException | IOException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        GraphicsDevice[] gs = ge.getScreenDevices();
        if (gs.length <= 1) {
            throw new RuntimeException("There are no screens !");
        }
        Rectangle bounds = gs[1].getDefaultConfiguration().getBounds();

        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);

        label.setFont(new Font("Montserrat", Font.BOLD, 50));
        label.setBounds(0, 0, 100, 50);
        label.setForeground(new Color(0xfe0002));

        timeUpLabel.setFont(new Font("Montserrat", Font.BOLD, 30));
        timeUpLabel.setForeground(new Color(0xfe0002));
        timeUpLabel.setVisible(false);

        panel.setBounds(0, 0, bounds.width, bounds.height - 150);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.anchor = GridBagConstraints.CENTER;
        panel.add(label, labelConstraints);

        // Set constraints for timeUpLabel (centered in the second row)
        GridBagConstraints timeUpLabelConstraints = new GridBagConstraints();
        timeUpLabelConstraints.gridx = 0;
        timeUpLabelConstraints.gridy = 1;
        timeUpLabelConstraints.anchor = GridBagConstraints.CENTER;
        panel.add(timeUpLabel, timeUpLabelConstraints);




        setLayout(null);
        iconLab.setBounds(0, 0, bounds.width, bounds.height);
        add(panel);
        add(iconLab);

        setSize(bounds.width, bounds.height);
        setLocation(bounds.x, bounds.y);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
    }

    public void timesUp()   {
        label.setVisible(false);
        timeUpLabel.setVisible(true);
    }

    public void reset(){
        label.setVisible(true);
        timeUpLabel.setVisible(false);
    }

    public void close() {
        dispose();
    }
}