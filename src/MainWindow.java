import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        add(createRightPanel(), BorderLayout.CENTER);
        JPanel ModelDataPanel = createLeftPanelsContainer(createModelsList(), createDataList());
        add(createLeftPanel(createTitle(), ModelDataPanel, createRunButton()), BorderLayout.WEST);
    }

   public JPanel createRightPanel(){
       JPanel rightPanel = new JPanel();
       rightPanel.setLayout(new BorderLayout());
       rightPanel.setBackground(new Color(50, 50, 50));
       rightPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
       return rightPanel;
   }

   public JPanel createLeftPanel(JLabel title, JPanel container, JButton runButton){
       JPanel leftPanel = new JPanel();
       leftPanel.setLayout(new BorderLayout());
       leftPanel.setBackground(new Color(30, 30, 30));
       leftPanel.setPreferredSize(new Dimension(300, 0));
       leftPanel.add(title, BorderLayout.NORTH);
       leftPanel.add(container, BorderLayout.CENTER);
       leftPanel.add(runButton, BorderLayout.SOUTH);
       return leftPanel;
   }

   public JLabel createTitle(){
       JLabel titleLabel = new JLabel("Select model and data", JLabel.CENTER);
       titleLabel.setForeground(Color.WHITE); // Белый текст
       titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
       return titleLabel;
   }

   public JPanel createLeftPanelsContainer(JPanel leftScrollPane, JPanel rightScrollPane){
       JPanel middlePanel = new JPanel();
       middlePanel.setLayout(new GridLayout(1, 2, 10, 0));
       middlePanel.setBackground(new Color(30, 30, 30));

       middlePanel.add(leftScrollPane);
       middlePanel.add(rightScrollPane);

       return middlePanel;
   }

   public JPanel createModelsList(){
       JPanel leftInnerPanel = new JPanel();
       leftInnerPanel.setLayout(new BoxLayout(leftInnerPanel, BoxLayout.Y_AXIS));
       leftInnerPanel.setBackground(new Color(40, 40, 40));
       leftInnerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
       for (String modelName : Service.findModels()) {
           JButton modelButton = new JButton(modelName);
           modelButton.setFont(new Font("Arial", Font.BOLD, 14));
           modelButton.setBackground(new Color(60, 60, 60));
           modelButton.setForeground(Color.WHITE);
           modelButton.setFocusPainted(false);
           modelButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            modelButton.addActionListener(e -> {
                Service.setSelectedModelName(modelButton.getText());
            });
           modelButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
           modelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
           leftInnerPanel.add(modelButton);
           leftInnerPanel.add(Box.createVerticalStrut(5));
       }
       JScrollPane leftScrollPane = new JScrollPane(leftInnerPanel);

       //refactor later
       leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
       leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
       leftScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

       return leftInnerPanel;
   }

    public JPanel createDataList(){
        JPanel rightInnerPanel = new JPanel();
        rightInnerPanel.setLayout(new BoxLayout(rightInnerPanel, BoxLayout.Y_AXIS));
        rightInnerPanel.setBackground(new Color(40, 40, 40));
        rightInnerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (String dataName : Service.findData()) {
            JButton dataButton = new JButton(dataName);
            dataButton.setFont(new Font("Arial", Font.BOLD, 14));
            dataButton.setBackground(new Color(60, 60, 60));
            dataButton.setForeground(Color.WHITE);
            dataButton.setFocusPainted(false);
            dataButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            dataButton.addActionListener(e -> {
                Service.setSelectedDataName(dataButton.getText());
            });
            dataButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            dataButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            rightInnerPanel.add(dataButton);
            rightInnerPanel.add(Box.createVerticalStrut(5));
        }
        //refactor later
        JScrollPane rightScrollPane = new JScrollPane(rightInnerPanel);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        return rightInnerPanel;
    }

    public JButton createRunButton(){
        JButton runButton = new JButton("Run model");
        runButton.setBackground(new Color(70, 70, 70));
        runButton.setForeground(Color.WHITE);
        runButton.setFocusPainted(false);
        runButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        runButton.addActionListener(e -> {
            if (Service.getSelectedModelName() != null && Service.getSelectedDataName() != null){
                Service.runController();
            }
        });
        return runButton;
    }

}
