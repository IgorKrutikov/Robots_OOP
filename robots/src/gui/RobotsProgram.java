package gui;

import java.awt.Frame;
import java.util.Locale;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {
    public static void main(String[] args) {

        Locale locale = new Locale("ru", "RU");
        var bundle = java.util.ResourceBundle.getBundle("resources/default_components", locale);

        for (var word: bundle.keySet()){
            UIManager.put(word, bundle.getString(word));
        }

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
