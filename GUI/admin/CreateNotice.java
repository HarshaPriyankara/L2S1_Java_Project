package GUI.admin;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateNotice extends JFrame {

    private JTextField titleField;
    private JTextArea contentArea;

    public  CreateNotice(){
        setTitle("Notice Management - Create Notice");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        String[] navItems = {"Create Notice", "Delete Notice", "Update Notice", "View Notice", "Back"};

        for (String item : navItems) {
            JButton navBtn = createSidebarButton(item);
            sidebar.add(navBtn);
        }
        add(sidebar, BorderLayout.WEST);

        add(CreateMainCon(),BorderLayout.CENTER);



    }

    private JPanel CreateMainCon() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(null);
        mainContent.setBackground(new Color(236, 240, 241));

        JLabel lblTitle = new JLabel("Notice Title:");
        lblTitle.setBounds(50, 50, 100, 30);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        mainContent.add(lblTitle);

         titleField = new JTextField();
        titleField.setBounds(50, 80, 650, 35);
        titleField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mainContent.add(titleField);

        JLabel lblContent = new JLabel("Notice Content:");
        lblContent.setBounds(50, 140, 150, 30);
        lblContent.setFont(new Font("SansSerif", Font.BOLD, 14));
        mainContent.add(lblContent);

         contentArea = new JTextArea();
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        contentArea.setLineWrap(true);  //when typed sentence go to right end of the typed line
        contentArea.setWrapStyleWord(true);//it is automatically pass to the next line

        JScrollPane scrollPane = new JScrollPane(contentArea); //sometimes textarea can be exceeds that handle at that time
        scrollPane.setBounds(50, 170, 650, 250);
        mainContent.add(scrollPane);


        JButton btnSubmit = new JButton("Submit & Save");
        btnSubmit.setBounds(50, 450, 150, 40);
        btnSubmit.setBackground(new Color(39, 174, 96));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("SansSerif", Font.BOLD, 14));

        mainContent.add(btnSubmit);

        btnSubmit.addActionListener(e->{saveNoticeToFile();});

        return mainContent;
    }

    private void saveNoticeToFile(){

        String title =titleField.getText();
        String content =contentArea.getText();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        String sanitizedTitle = title.replaceAll("[^a-zA-Z0-9\\s]", "_");
        String folderPath = "notices/" + sanitizedTitle;
        String fileName = sanitizedTitle + ".txt";

        try{
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();

            // 3. Database ekata danna (Oyaata database logic eka methanata danna puluwan)
            // String sqlPath = file.getAbsolutePath();

            JOptionPane.showMessageDialog(this, "Notice saved successfully in: " + folderPath);
            String finalPathForDB = folderPath + "/" + fileName;
            titleField.setText("");
            contentArea.setText("");
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving notice: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        return btn;
    }

    public static  void  main(String[] args){
        javax.swing.SwingUtilities.invokeLater(()->{
            new CreateNotice().setVisible(true);
                }
                );
    }
}
