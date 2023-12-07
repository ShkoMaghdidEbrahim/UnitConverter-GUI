import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlContrastIJTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.StringJoiner;

public class Frame {
    public static void main(String[] args) {
        FlatNightOwlContrastIJTheme.install(new FlatNightOwlContrastIJTheme());
        
        UIManager.put("Button.arc", 10);
        UIManager.put("ProgressBar.arc", 5);
        UIManager.put("Component.arc", 10);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("ScrollBar.trackArc", 5);
        UIManager.put("ScrollBar.thumbArc", 5);
        UIManager.put("ScrollBar.width", 11);
        UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("ScrollBar.track", new Color(0x0B253A) );
        UIManager.put("ScrollBar.thumb", new Color(0x103657) );
        
        String[] units = {"Meters", "Centimeters", "Kilograms", "Grams", "MPH", "KPH", "Decimal", "Binary", "Hexadecimal", "Celsius", "Fahrenheit", "Kelvin"};
        String[] abv = {"M", "CM", "KG", "G", "MPH", "KPH", "\u2081\u2080", "\u2082", "\u2081\u2086", "°C", "°F", "°K"};
        double[] denominators = {100, 0, 1000, 0, 1.6092};
    
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        format.setMaximumFractionDigits(4);
        format.setGroupingUsed(true);
        
        char[] keys = new char[95];
        for (int i = 32; i < 127; i++) {
            if (i == 48) {
                i = 58;
            }
            else if (i == 46) {
                i = 47;
            }
            keys[i - 32] = (char) i;
        }
        //48 - 57
        
        Icon backIcon = new ImageIcon("src/LOGO.ico");
        JFrame frame = frame();
        JPanel panel = panel(frame);
        
        JComboBox<String> choicesFrom = choicesFrom(panel, units);
        JComboBox<String> choicesTo = choicesTo(panel);
        
        JTextField inputToConv = inputToConv(panel);
        JTextField result = result(panel);
        
        JLabel dialogueSuffix = dialogueSuffix(panel, choicesFrom, abv);
        dialogueResult(panel);
        
        JButton[] buttons = new JButton[13];
        {
            buttons[10] = backSpace(panel, backIcon, inputToConv);
            buttons[11] = dot(panel, inputToConv);
            buttons[12] = convert(panel);
            buttons[0] = number0(panel, inputToConv);
            buttons[1] = number1(panel, inputToConv);
            buttons[2] = number2(panel, inputToConv);
            buttons[3] = number3(panel, inputToConv);
            buttons[4] = number4(panel, inputToConv);
            buttons[5] = number5(panel, inputToConv);
            buttons[6] = number6(panel, inputToConv);
            buttons[7] = number7(panel, inputToConv);
            buttons[8] = number8(panel, inputToConv);
            buttons[9] = number9(panel, inputToConv);
        }
        
        ActionListener[] buttonChangers = new ActionListener[3];
        {
            buttonChangers[2] = dotAction(buttons, inputToConv);
            buttonChangers[1] = changeBack(buttonChangers, buttons, inputToConv);
            buttonChangers[0] = changer(buttonChangers, buttons, inputToConv);
        }
        
        keyDisabler(inputToConv, keys);
        
        choicesFrom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choicesTo.removeAllItems();
                inputToConv.setText("");
                result.setText("");
                buttons[11].setText(".");
                String selected = String.valueOf(choicesFrom.getSelectedItem());
                //Add items to the second menu accordingly
                if (selected.equals(units[0])) {
                    choicesTo.addItem(units[1]);
                }
                else if (selected.equals(units[1])) {
                    choicesTo.addItem(units[0]);
                }
                else if (selected.equals(units[2])) {
                    choicesTo.addItem(units[3]);
                }
                else if (selected.equals(units[3])) {
                    choicesTo.addItem(units[2]);
                }
                else if (selected.equals(units[4])) {
                    choicesTo.addItem(units[5]);
                }
                else if (selected.equals(units[5])) {
                    choicesTo.addItem(units[4]);
                }
                //Number Systems
                for (int i = 9; i < 12; i++) {
                    if (selected.equals(units[i])) {
                        for (int j = 9; j < 12; j++) {
                            if (!selected.equals(units[j])) {
                                choicesTo.addItem(units[j]);
                            }
                        }
                    }
                }
                //Temperatures
                for (int i = 6; i < 9; i++) {
                    if (selected.equals(units[i])) {
                        for (int j = 6; j < 9; j++) {
                            if (!selected.equals(units[j])) {
                                choicesTo.addItem(units[j]);
                            }
                        }
                    }
                }
                
                //Disable unnecessary buttons in certain conversions
                if (selected.equals(units[6])) {
                    for (int i = 2; i < buttons.length; i++) {
                        buttons[i].setEnabled(true);
                    }
                }
                else if (selected.equals(units[7])) {
                    for (int i = 2; i < 12; i++) {
                        if (i == 10) {
                            i = 11;
                        }
                        buttons[i].setEnabled(false);
                    }
                }
                else if (selected.equals(units[8])) {
                    for (int i = 2; i < 12; i++) {
                        buttons[i].setEnabled(true);
                    }
                    buttons[11].removeActionListener(buttons[11].getActionListeners()[0]);
                    buttons[11].setText("L");
                    buttons[11].addActionListener(buttonChangers[0]);
                }
            }
        });
        choicesFrom.addActionListener(buttonChangers[2]);
        choicesTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result.setText("");
                for (int i = 0; i < units.length; i++) {
                    if (String.valueOf(choicesTo.getSelectedItem()).equals(units[i])) {
                        dialogueSuffix.setText(abv[i]);
                    }
                }
                
            }
        });
        
        buttons[12].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputToConv.getText().length() > 0) {
                    bigToSmall(units, denominators, choicesFrom, choicesTo, result, inputToConv, format);
                    smallToBig(units, denominators, choicesFrom, choicesTo, result, inputToConv, format);
                    
                    decToE(units, choicesFrom, choicesTo, result, inputToConv);
                    binToE(units, choicesFrom, choicesTo, result, inputToConv);
                    hexToE(units, choicesFrom, choicesTo, result, inputToConv);
                    
                    celToE(units, choicesFrom, choicesTo, result, inputToConv, format);
                    fahToE(units, choicesFrom, choicesTo, result, inputToConv, format);
                    kelToE(units, choicesFrom, choicesTo, result, inputToConv, format);
                    
                }
            }
        });
        
        inputToConv.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                for (int i = 48; i < 58; i++) {
                    if (e.getKeyCode() == i) {
                        buttons[i - 48].setBackground(new Color(0x103657));
                    }
                }
                if (e.getKeyCode() == 46) {
                    buttons[11].setBackground(new Color(0x103657));
                }
                if (e.getKeyCode() == 8) {
                    buttons[10].setBackground(new Color(0x103657));
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                
                for (int i = 48; i < 58; i++) {
                    if (e.getKeyCode() == i) {
                        buttons[i - 48].setBackground(new Color(0x0B253A));
                    }
                }
                if (e.getKeyCode() == 46) {
                    buttons[11].setBackground(new Color(0x0B253A));
                }
                if (e.getKeyCode() == 8) {
                    buttons[10].setBackground(new Color(0x0B253A));
                }
                buttons[11].setBackground(new Color(0x0B253A));
                buttons[10].setBackground(new Color(0x0B253A));
            }
        });
        
        frame.setVisible(true);
    }
    
    private static void keyDisabler(JTextField inputToConv, char[] keys) {
        for (char disableThis : keys) {
            inputToConv.getInputMap().put(KeyStroke.getKeyStroke(disableThis), "none");
        }
    }
    
    static JFrame frame() {
        JFrame frame = new JFrame("Unit Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 345);
        frame.setLocation(430, 100);
        frame.setResizable(false);
        return frame;
    }
    static JPanel panel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setVisible(true);
        frame.add(panel);
        return panel;
    }
    
    static void bigToSmall(String[] units, double[] denominators, JComboBox<String> choicesFrom, JComboBox<String> choicesTo, JTextField result, JTextField inputToConv, DecimalFormat format) {
        for (int i = 0; i < units.length - 6; i++) {
            if (String.valueOf(choicesFrom.getSelectedItem()).equals(units[i]) && String.valueOf(choicesTo.getSelectedItem()).equals(units[i + 1])) {
                result.setText(String.valueOf(format.format(Double.parseDouble(inputToConv.getText()) * denominators[i])));
            }
        }
    }
    static void smallToBig(String[] units, double[] denominators, JComboBox<String> choicesFrom, JComboBox<String> choicesTo, JTextField result, JTextField inputToConv, DecimalFormat format) {
        for (int i = 0; i < units.length - 6; i++) {
            if (i > 0) {
                if (String.valueOf(choicesFrom.getSelectedItem()).equals(units[i]) && String.valueOf(choicesTo.getSelectedItem()).equals(units[i - 1])) {
                    result.setText(String.valueOf(format.format(Double.parseDouble(inputToConv.getText()) / denominators[i - 1])));
                }
            }
        }
    }
    
    static void decToE(String[] units, JComboBox<String> choicesFrom, JComboBox<String> choicesTo, JTextField result, JTextField inputToConv) {
        if (String.valueOf(choicesFrom.getSelectedItem()).equals(units[6])) {
            if (inputToConv.getText().length() < 11) {
                if (String.valueOf(choicesTo.getSelectedItem()).equals(units[7])) {
                    result.setText(Integer.toBinaryString(Integer.parseInt(inputToConv.getText())));
                }
                if (String.valueOf(choicesTo.getSelectedItem()).equals(units[8])) {
                    result.setText(Integer.toHexString(Integer.parseInt(inputToConv.getText())).toUpperCase(Locale.ROOT));
                }
            }
            else {
                result.setText("Input should be < 11");
            }
        }
    }
    static void binToE(String[] units, JComboBox<String> choicesFrom, JComboBox<String> choicesTo, JTextField result, JTextField inputToConv) {
        try {
            if (String.valueOf(choicesFrom.getSelectedItem()).equals(units[7])) {
                if (inputToConv.getText().length() < 32) {
                    if (String.valueOf(choicesTo.getSelectedItem()).equals(units[6])) {
                        result.setText(String.valueOf(Integer.parseInt(inputToConv.getText(), 2)));
                    }
                    if (String.valueOf(choicesTo.getSelectedItem()).equals(units[8])) {
                        int input = Integer.parseInt(inputToConv.getText(), 2);
                        result.setText(Integer.toHexString(input).toUpperCase());
                    }
                }
                else {
                    result.setText("Input should be < 32");
                }
        
            }
        }
        catch (NumberFormatException e) {
            result.setText("Enter a Valid Number");
        }
    }
    static void hexToE(String[] units, JComboBox<String> choicesFrom, JComboBox<String> choicesTo, JTextField result, JTextField inputToConv) {
        if (String.valueOf(choicesFrom.getSelectedItem()).equals(units[8])) {
            if (inputToConv.getText().length() < 8) {
                if (String.valueOf(choicesTo.getSelectedItem()).equals(units[7])) {
                    int input = Integer.parseInt(inputToConv.getText(), 16);
                    result.setText(Integer.toBinaryString(input));
                }
                if (String.valueOf(choicesTo.getSelectedItem()).equals(units[6])) {
                    result.setText(String.valueOf(Integer.parseInt(inputToConv.getText(), 16)));
                }
            }
            else {
                result.setText("Input should be < 8");
            }
        }
    }
    
    static void celToE(String[] units, JComboBox<String> choicesFrom, JComboBox<String> choicesTo, JTextField result, JTextField inputToConv, DecimalFormat format) {
        if (String.valueOf(choicesFrom.getSelectedItem()).equals(units[9])) {
            double input = Double.parseDouble(inputToConv.getText());
            if (String.valueOf(choicesTo.getSelectedItem()).equals(units[10])) {
                double results = (input * 9/5) + 32;
                result.setText(format.format(results));
            }
            else if (String.valueOf(choicesTo.getSelectedItem()).equals(units[11])) {
               double results = input + 273.15;
               result.setText(format.format(results));
            }
        }
    }
    static void fahToE(String[] units, JComboBox<String> choicesFrom, JComboBox<String> choicesTo, JTextField result, JTextField inputToConv, DecimalFormat format) {
        if (String.valueOf(choicesFrom.getSelectedItem()).equals(units[10])) {
            double input = Double.parseDouble(inputToConv.getText());
            if (String.valueOf(choicesTo.getSelectedItem()).equals(units[9])) {
                double results = (input - 32) * 5/9;
                result.setText(format.format(results));
            }
            else if (String.valueOf(choicesTo.getSelectedItem()).equals(units[11])) {
                double results = (input - 32) * 5/9 + 273.15;
                result.setText(format.format(results));
            }
        }
    }
    static void kelToE(String[] units, JComboBox<String> choicesFrom, JComboBox<String> choicesTo, JTextField result, JTextField inputToConv, DecimalFormat format) {
        if (String.valueOf(choicesFrom.getSelectedItem()).equals(units[11])) {
            double input = Double.parseDouble(inputToConv.getText());
            if (String.valueOf(choicesTo.getSelectedItem()).equals(units[9])) {
                double results = input - 273.15;
                result.setText(format.format(results));
            }
            else if (String.valueOf(choicesTo.getSelectedItem()).equals(units[10])) {
                double results = (input - 273.15) * 9/5 + 32;
                result.setText(format.format(results));
            }
        }
    }
    
    static void dialogueResult(JPanel panel) {
        JLabel dialogueResult = new JLabel(" = ");
        dialogueResult.setBounds(205, 54, 100, 25);
        panel.add(dialogueResult);
    }
    static JLabel dialogueSuffix(JPanel panel, JComboBox<String> choicesTo, String[] abv) {
        JLabel dialogueSuffix = new JLabel(abv[choicesTo.getSelectedIndex() + 1]);
        dialogueSuffix.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        dialogueSuffix.setBounds(390, 58, 100, 25);
        panel.add(dialogueSuffix);
        return dialogueSuffix;
    }
    
    static JComboBox<String> choicesFrom(JPanel panel, String[] Units) {
        final JComboBox<String> choicesFrom = new JComboBox<>(Units);
        choicesFrom.setBounds(15, 5, 175, 30);
        choicesFrom.setFocusable(false);
        choicesFrom.setVisible(true);
        choicesFrom.setMaximumRowCount(4);
        panel.add(choicesFrom);
        return choicesFrom;
    }
    static JComboBox<String> choicesTo(JPanel panel) {
        final JComboBox<String> choicesTo = new JComboBox<>(new String[]{"Centimeters"});
        choicesTo.setBounds(235, 5, 175, 30);
        choicesTo.setMaximumRowCount(4);
        choicesTo.setFocusable(false);
        choicesTo.setVisible(true);
        choicesTo.setEnabled(true);
        panel.add(choicesTo);
        return choicesTo;
    }
    
    static JTextField inputToConv(JPanel panel) {
        JTextField inputToConv = new JTextField();
        inputToConv.setBounds(15, 52, 175, 30);
        panel.add(inputToConv);
        return inputToConv;
    }
    static JTextField result(JPanel panel) {
        JTextField result = new JTextField();
        result.setBounds(235, 52, 150, 30);
        result.setEditable(false);
        result.setFocusable(false);
        panel.add(result);
        return result;
    }
    
    static JButton number3(JPanel panel, JTextField inputToConv) {
        JButton number = new JButton("3");
        number.setBounds(275, 200, 140, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "3"));
        panel.add(number);
        return number;
    }
    static JButton number6(JPanel panel, JTextField inputToConv) {
        JButton number = new JButton("6");
        number.setBounds(275, 150, 140, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "6"));
        panel.add(number);
        return number;
    }
    static JButton number9(JPanel panel, JTextField inputToConv) {
        JButton number = new JButton("9");
        number.setBounds(275, 100, 140, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "9"));
        panel.add(number);
        return number;
    }
    static JButton convert(JPanel panel) {
        JButton convert = new JButton("CONVERT");
        convert.setBounds(275, 250, 140, 40);
        convert.setFont(new Font("TimesNew", Font.BOLD, 14));
        convert.setFocusable(false);
        convert.setMnemonic((char) 10);
        convert.setForeground(Color.white);
        panel.add(convert);
        return convert;
    }
    
    static JButton number1(JPanel panel, JTextField inputToConv) {
        JButton number = new JButton("1");
        number.setBounds(15, 200, 140, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "1"));
        panel.add(number);
        return number;
    }
    static JButton number4(JPanel panel, JTextField inputToConv) {
        JButton number = new JButton("4");
        number.setBounds(15, 150, 140, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "4"));
        panel.add(number);
        return number;
    }
    static JButton number7(JPanel panel, JTextField inputToConv) {
        JButton number = new JButton("7");
        number.setBounds(15, 100, 140, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "7"));
        panel.add(number);
        return number;
    }
    static JButton number0(JPanel panel, JTextField inputToConv) {
        JButton number = new JButton("0");
        number.setBounds(15, 250, 65, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "0"));
        panel.add(number);
        return number;
    }
    static JButton dot(JPanel panel, JTextField inputToConv) {
        JButton dot = new JButton(".");
        dot.setBounds(90, 250, 65, 40);
        dot.setFont(new Font("TimesNew", Font.BOLD, 22));
        dot.setFocusable(false);
        dot.setForeground(Color.white);
        dot.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "."));
        panel.add(dot);
        return dot;
    }
    
    static JButton number2(JPanel panel, JTextField inputToConv) {
        JButton number = new JButton("2");
        number.setBounds(165, 200, 100, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "2"));
        panel.add(number);
        return number;
    }
    static JButton number5(JPanel panel, JTextField inputToConv) {
        JButton number = new JButton("5");
        number.setBounds(165, 150, 100, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "5"));
        panel.add(number);
        return number;
    }
    static JButton number8(JPanel panel, JTextField inputToConv) {
        JButton number = new JButton("8");
        number.setBounds(165, 100, 100, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> inputToConv.setText(inputToConv.getText() + "8"));
        panel.add(number);
        return number;
    }
    static JButton backSpace(JPanel panel, Icon backIcon, JTextField inputToConv) {
        JButton number = new JButton(backIcon);
        number.setBounds(165, 250, 100, 40);
        number.setFont(new Font("TimesNew", Font.BOLD, 22));
        number.setFocusable(false);
        number.setForeground(Color.white);
        number.addActionListener(e -> {
            if (inputToConv.getText().length() > 0) {
                StringJoiner joiner = new StringJoiner("");
                char[] text = inputToConv.getText().toCharArray();
                String[] textString = new String[text.length];
                for (int i = 0; i < text.length; i++) {
                    textString[i] = String.valueOf(text[i]);
                }
                textString[inputToConv.getText().length() - 1] = "";
                for (String value : textString) {
                    joiner.add(value);
                }
                inputToConv.setText(joiner.toString());
            }
        });
        panel.add(number);
        return number;
    }
    
    static ActionListener dotAction(JButton[] buttons, JTextField inputToConv) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                for (int i = 0; i < 10; i++) {
                    buttons[i].setText(String.valueOf(i));
                    buttons[i].setEnabled(true);
                }
                
                for (int i = 4; i < 10; i++) {
                    int finalI = i;
                    buttons[i].setText(String.valueOf(i));
                    buttons[i].removeActionListener(buttons[i].getActionListeners()[0]);
                    buttons[i].addActionListener(e1 -> inputToConv.setText(inputToConv.getText() + finalI));
                }
                
                buttons[11].setText(".");
                buttons[11].setEnabled(true);
                buttons[11].removeActionListener(buttons[11].getActionListeners()[0]);
                buttons[11].addActionListener(e17 -> inputToConv.setText(inputToConv.getText() + "."));
            }
        };
    }
    static ActionListener changeBack(ActionListener[] buttonChangers, JButton[] buttons, JTextField inputToConv) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                for (int i = 0; i < 4; i++) {
                    buttons[i].setText(String.valueOf(i));
                    buttons[i].setEnabled(true);
                }
                
                for (int i = 4; i < 10; i++) {
                    int finalI = i;
                    buttons[i].setText(String.valueOf(i));
                    buttons[i].removeActionListener(buttons[i].getActionListeners()[0]);
                    buttons[i].addActionListener(e1 -> inputToConv.setText(inputToConv.getText() + finalI));
                }
                
                buttons[11].setText("L");
                buttons[11].removeActionListener(buttons[11].getActionListeners()[0]);
                buttons[11].addActionListener(buttonChangers[0]);
                
            }
        };
    }
    static ActionListener changer(ActionListener[] buttonChangers, JButton[] buttons, JTextField inputToConv) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //disable buttons
                for (int i = 0; i < 4; i++) {
                    buttons[i].setText("");
                    buttons[i].setEnabled(false);
                }
                //set new text and functions
                for (int i = 65; i < 71; i++) {
                    int finalI = i;
                    buttons[i - 61].setText(String.valueOf((char) i));
                    buttons[i - 61].removeActionListener(buttons[i - 61].getActionListeners()[0]);
                    buttons[i - 61].addActionListener(e1 -> inputToConv.setText(inputToConv.getText() + (char) finalI));
                }
                //rename and add action
                buttons[11].setText("N");
                buttons[11].removeActionListener(buttons[11].getActionListeners()[0]);
                buttons[11].addActionListener(buttonChangers[1]);
            }
        };
    }
}