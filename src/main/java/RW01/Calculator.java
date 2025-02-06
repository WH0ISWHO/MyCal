package RW01;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Calculator extends JFrame {
    private final short width = 250;
    private final short height = 380;
    private final JTextField inputSpace = new JTextField();
    private final ArrayList<String> equation=new ArrayList<>();

    private final GridBagLayout grid = new GridBagLayout();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final Color BGC = new Color(246, 241, 235);// *내 전용색상!*
    private final Color Og=new Color(255, 132, 58);

    TitledBorder TB = new TitledBorder(new LineBorder(BGC, 1));
    String[] BNames = {"C", "±", "←", "+", "7", "8", "9", "-", "4", "5", "6", "x", "1", "2", "3",
            "÷", "0", ".", "="};
    String BString = "C±←+789-456x123÷0.=";
    JButton[] buttons = new JButton[BNames.length];
    MouseActionListener Mouse = new MouseActionListener();
    String num = "";
    String prev_operation = "";

    public Calculator() {
        setLayout(new GridLayout(2, 1));

        inputSpace.setEditable(false);
        inputSpace.setBackground(BGC);
        inputSpace.setHorizontalAlignment(JTextField.RIGHT);
        inputSpace.setFont(new Font("Dialog", Font.PLAIN, 30));
        inputSpace.setBounds(0, 0, width, 96);
        inputSpace.setBorder(new LineBorder(BGC, 0));// *입력창 색상!*
        inputSpace.setForeground(Color.black);

        JPanel BPanel = new JPanel();

        BPanel.setLayout(grid);
        BPanel.setBounds(0, 70, width, 284);
        BPanel.setBackground(BGC);// *밑면 색상!*

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        short x = 0;
        short y = 0;
        for (int i = 0; i < BNames.length; i++) {
            buttons[i] = new JButton(BNames[i]);
            buttons[i].setFont(new Font("Dialog", Font.BOLD, 20));
            buttons[i].setForeground(Color.black);// *숫자 색상!*

            // 버튼 색상
            if (BNames[i].matches("[-+x÷=]")) {
                buttons[i].setBackground(new Color(50, 50, 50));
                getForeColor(i);
            } else if (BNames[i].matches("[C]")) {
                buttons[i].setBackground(new Color(200, 0, 0));
                getForeColor(i);
            } else if (BNames[i].matches("[.±←]")) {
                buttons[i].setBackground(new Color(50, 50, 50));
                getForeColor(i);
            } else if (BNames[i].matches("[0123456789]")) {
                buttons[i].setBackground(new Color(100, 100, 100));
                buttons[i].setForeground(new Color(0, 0, 0));
            } else buttons[i].setBackground(new Color(123, 125, 127));
            getForeColor(i);

            // ---격자 형태---
            if (BNames[i].equals("0")) {
                BS(buttons[i], (short) 1, y, (short) 2, (short) 1);
                x++;
            } else if (BNames[i].equals(".")) {
                BS(buttons[i], (short) 0, y, (short) 1, (short) 1);
            }
            else {
                BS(buttons[i], x, y, (short) 1, (short) 1);
            }
            x++;
            if (x > 3) {
                x = 0;
                y++;
            }
            //      ------
            buttons[i].addActionListener(new ButtonActionListener());
            buttons[i].addMouseListener(Mouse);

            buttons[i].setBorder(TB);
            BPanel.add(buttons[i]);
            buttons[i].setOpaque(true);
        }
        add(inputSpace);
        add(BPanel);

        setTitle("Simple 계산기");
        setVisible(true);
        setSize(width, height);
        setBackground(BGC);// *색상!*
        setLocationRelativeTo(null);
        setResizable(true);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void getForeColor(int i) {
        buttons[i].setForeground(new Color(255, 255, 255));
    }

    public void BS (JButton c,short x, short y, short w, short h){
            gbc.gridx = x;
            gbc.gridy = y;
            gbc.gridwidth = w;
            gbc.gridheight = h;
            grid.setConstraints(c, gbc);
        }
        class ButtonActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oper=e.getActionCommand();
                if (oper.equals("C")) {
                    inputSpace.setText("");
                } else if (oper.equals("=")) {//%,.0f
                    String result = Double.toString(calculate(inputSpace.getText()));
                        inputSpace.setText(result);
                        num = "";
                } else if(oper.equals("←")){
                    delete();
                } else if (oper.equals("±")) {
                    // +- 버튼을 눌렀을 때 현재 숫자의 부호 변경 (하지만 아직 오류가 좀 있다.)
                    String text = inputSpace.getText();
                    if (!text.isEmpty()) {
                        if (text.charAt(0) == '-') {
                            inputSpace.setText(text.substring(1)); // 앞에 '-'가 있으면 제거
                        } else {
                            inputSpace.setText("-" + text); // 없으면 '-' 추가
                        }
                    }
                } else if(oper.equals("+") || oper.equals("-") || oper.equals("x") ||
                        oper.equals("÷")){
                    if(inputSpace.getText().isEmpty() && oper.equals("-")){
                        inputSpace.setText(inputSpace.getText()+e.getActionCommand());
                    } else if(!inputSpace.getText().isEmpty() && !prev_operation.equals("+") &&
                            !prev_operation.equals("-") && !prev_operation.equals("x") && !prev_operation.equals("÷")){
                        inputSpace.setText(inputSpace.getText()+e.getActionCommand());
                    }
                } else{
                    inputSpace.setText(inputSpace.getText()+e.getActionCommand());
                }
                prev_operation=e.getActionCommand();
            }
        }
    private void delete() {
        String currentText = inputSpace.getText();
        if (!currentText.isEmpty()) {
            inputSpace.setText(currentText.substring(0, currentText.length() - 1));
        }
    }
        private void fullTextParsing(String inputText){
            equation.clear();
            num = "";

            for (int i=0; i<inputText.length(); i++) {
                char c = inputText.charAt(i);

                if (c == '-' && (i == 0 || "+-x÷".indexOf(inputText.charAt(i - 1)) != -1)) {
                    num += c; // 음수로 인식
                } else if ("+-x÷".indexOf(c) != -1) {
                    equation.add(num);
                    num = "";
                    equation.add(c + "");
                } else {
                    num += c;
                }
            }
            equation.add(num);
            equation.remove("");
        }
        private double calculate (String inputText){
            fullTextParsing(inputText);
            double n1=0;
            double n2=0;
            String mode="";

            // 먼저 곱셈과 나눗셈을 처리
            for (int i = 0; i < equation.size(); i++) {
                String s = equation.get(i);
                if (s.equals("x")) {
                    mode = "mul";
                } else if (s.equals("÷")) {
                    mode = "div";
                } else {
                    if ((mode.equals("mul") || mode.equals("div")) &&
                        !s.equals("add") && !s.equals("sub") && !s.equals("mul") && !s.equals("div")) {
                        double one = Double.parseDouble(equation.get(i - 2));
                        double two = Double.parseDouble(equation.get(i));
                        double result = 0.0;
                        if (mode.equals("mul")) {
                            result = one * two;
                        }
                        if (mode.equals("div")) {
                            result = one / two;
                        }
                        equation.add(i + 1, Double.toString(result));
                        for (int j = 0; j < 3; j++) {
                            equation.remove(i - 2);
                        }
                        i -= 2;
                    }
                }
            }
            for (String s : equation) {
                if (s.equals("+")) {
                    mode = "add";
                } else if (s.equals("-")) {
                    mode = "sub";
                } else {
                    n2 = Double.parseDouble(s);
                    if (mode.equals("add")) {
                        n1 += n2;
                    } else if (mode.equals("sub")) {
                        n1 -= n2;
                    } else {
                        n1 = n2;
                    }
                }
            }
            n1 = Math.round(n1 * 10000) / 10000.0;
            return n1;
        }

        class MouseActionListener implements MouseListener {
            @Override
            public void mousePressed(MouseEvent e) {
                JButton jb = (JButton) e.getSource();
                int target = BString.indexOf(jb.getText());
                buttons[target].setBorder(new LineBorder(Color.black));

                if (jb.getText().matches("[-+x÷=]")) {
                    buttons[target].setBackground(Og);
                } else if (jb.getText().matches("[C]")) {
                    buttons[target].setBackground(Og);
                } else if (jb.getText().matches("[.±←]")) {
                    buttons[target].setBackground(Og);
                } else if (jb.getText().matches("[0123456789]")) {
                    buttons[target].setBackground(Og);
                } else
                    buttons[target].setBackground(Og);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JButton jb = (JButton) e.getSource();
                int target = BString.indexOf(jb.getText());
                buttons[target].setBorder(TB);
                if (jb.getText().matches("[-+x÷=]")) {
                    buttons[target].setBackground(new Color(50, 50, 50));
                } else if (jb.getText().matches("[C]")) {
                    buttons[target].setBackground(new Color(200, 0, 0));
                } else if (jb.getText().matches("[.±←]")) {
                    buttons[target].setBackground(new Color(50, 50, 50));
                } else if (jb.getText().matches("[0123456789]")) {
                    buttons[target].setBackground(new Color(100, 100, 100));
                } else
                    buttons[target].setBackground(new Color(123, 125, 127));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        }
        public static void main (String[]args){
            new Calculator();
        }
}