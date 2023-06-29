package save;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;

public class Saver {
    static private final File configDir = new File(System.getProperty("user.home"), "Robots");
    static private final File fileName = new File("robotsFrames.properties");
    static private final Properties property = new Properties();

    public static void save(Container container, String frameName) {

        var bounds = container.getBounds();

        var wrap = getStringWrapper(frameName);


        if (container instanceof JFrame) {
            property.setProperty(wrap.apply("state"), String.valueOf(((JFrame) container).getExtendedState()));
        }
        else if(container instanceof JInternalFrame frame) {
            String state = "";
            if (frame.isIcon()){
                state = "Icon";
            } else if (frame.isMaximum()) {
                state = "Maximised";
            }
            property.setProperty(wrap.apply("state"), state);
        }
        property.setProperty(wrap.apply("xPos"), String.valueOf(bounds.x));
        property.setProperty(wrap.apply("yPos"), String.valueOf(bounds.y));
        property.setProperty(wrap.apply("widthPos"), String.valueOf(bounds.width));
        property.setProperty(wrap.apply("heightPos"), String.valueOf(bounds.height));

    }

    public static void flush() {

        if (!configDir.exists()) {
            configDir.mkdir();
        }

        try (var fileOutputStream = new FileOutputStream((new File(configDir, fileName.toString()).getAbsolutePath()))) {
            property.store(fileOutputStream, "Stored windows disposition");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static Function<String, String> getStringWrapper(String wrapper) {
        return (label) -> wrapper + "_" + label;
    }

    private static void load() {
        try (var fileInputStream = new FileInputStream((new File(configDir, fileName.toString()).getAbsolutePath()))) {
            property.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fillFrame(Container container, String frameName) {
        if (property.isEmpty()) load();
        var wrap = getStringWrapper(frameName);

        String state = property.getProperty(wrap.apply("state"));

        int x = Integer.parseInt(property.getProperty(wrap.apply("xPos")));
        int y = Integer.parseInt(property.getProperty(wrap.apply("yPos")));
        int width = Integer.parseInt(property.getProperty(wrap.apply("widthPos")));
        int height = Integer.parseInt(property.getProperty(wrap.apply("heightPos")));

        container.setBounds(x, y, width, height);

        if (container instanceof JFrame){
            ((JFrame)container).setExtendedState(Integer.parseInt(state));
        }
        else if(container instanceof JInternalFrame frame){
            if (state.equals("Icon")){
                if (frame.isIconifiable()) {
                    try {
                        frame.setIcon(true);
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                }
            } else if (state.equals("Maximised")){
                try {
                    frame.setMaximum(true);
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
