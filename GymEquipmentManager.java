import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class GymEquipmentManager {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addNewButton;

    public GymEquipmentManager() {
        // Main frame
        frame = new JFrame("Gym Equipment Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 245, 250));

        // Table setup
        String[] columns = {"Name", "Category", "Qty", "Condition", "Active", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Table styling
        table.getTableHeader().setBackground(new Color(120, 80, 160));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(180, 150, 220));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(tableScroll, BorderLayout.CENTER);

        // Add new button
        addNewButton = new JButton("Add New Equipment");
        styleButton(addNewButton);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        topPanel.add(addNewButton);
        frame.add(topPanel, BorderLayout.NORTH);

        // Button action
        addNewButton.addActionListener(e -> openEquipmentDialog(null));

        // Example data
        tableModel.addRow(new Object[]{"Treadmill", "Cardio", 5, "Good", "Active", "Edit"});
        tableModel.addRow(new Object[]{"Dumbbells", "Strength", 20, "Needs Maintenance", "Active", "Edit"});

        frame.setVisible(true);
    }

    private void openEquipmentDialog(Object[] rowData) {
        JDialog dialog = new JDialog(frame, rowData == null ? "Add Equipment" : "Edit Equipment", true);
        dialog.setSize(450, 550);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(new Color(240, 235, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10); // increased spacing

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Equipment Name:");
        nameLabel.setFont(labelFont);
        dialog.add(nameLabel, gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        nameField.setMargin(new Insets(5,5,5,5));
        dialog.add(nameField, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy++;
        JLabel catLabel = new JLabel("Category:");
        catLabel.setFont(labelFont);
        dialog.add(catLabel, gbc);
        gbc.gridx = 1;
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
                "Cardio","Strength","Accessories","Flexibility","Balance","Recovery"
        });
        dialog.add(categoryCombo, gbc);

        // Quantity
        gbc.gridx = 0; gbc.gridy++;
        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setFont(labelFont);
        dialog.add(qtyLabel, gbc);
        gbc.gridx = 1;
        JTextField quantityField = new JTextField(5);
        quantityField.setMargin(new Insets(5,5,5,5));
        dialog.add(quantityField, gbc);

        // Condition
        gbc.gridx = 0; gbc.gridy++;
        JLabel condLabel = new JLabel("Condition:");
        condLabel.setFont(labelFont);
        dialog.add(condLabel, gbc);
        gbc.gridx = 1;
        JRadioButton goodBtn = new JRadioButton("Good");
        JRadioButton needsMaintBtn = new JRadioButton("Needs Maintenance");
        JRadioButton brokenBtn = new JRadioButton("Broken");
        goodBtn.setBackground(new Color(240,235,250));
        needsMaintBtn.setBackground(new Color(240,235,250));
        brokenBtn.setBackground(new Color(240,235,250));
        ButtonGroup conditionGroup = new ButtonGroup();
        conditionGroup.add(goodBtn); conditionGroup.add(needsMaintBtn); conditionGroup.add(brokenBtn);
        JPanel condPanel = new JPanel();
        condPanel.setBackground(new Color(240,235,250));
        condPanel.add(goodBtn); condPanel.add(needsMaintBtn); condPanel.add(brokenBtn);
        dialog.add(condPanel, gbc);

        // Purchase date
        gbc.gridx = 0; gbc.gridy++;
        JLabel dateLabel = new JLabel("Purchase Date:");
        dateLabel.setFont(labelFont);
        dialog.add(dateLabel, gbc);
        gbc.gridx = 1;
        JTextField purchaseDateField = new JTextField("YYYY-MM-DD",10);
        purchaseDateField.setMargin(new Insets(5,5,5,5));
        dialog.add(purchaseDateField, gbc);

        // Notes
        gbc.gridx = 0; gbc.gridy++;
        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setFont(labelFont);
        dialog.add(notesLabel, gbc);
        gbc.gridx = 1;
        JTextArea notesArea = new JTextArea(3,20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setMargin(new Insets(5,5,5,5));
        dialog.add(new JScrollPane(notesArea), gbc);

        // Active
        gbc.gridx = 0; gbc.gridy++;
        JLabel activeLabel = new JLabel("Active:");
        activeLabel.setFont(labelFont);
        dialog.add(activeLabel, gbc);
        gbc.gridx = 1;
        JCheckBox activeCheck = new JCheckBox("Active");
        activeCheck.setBackground(new Color(240,235,250));
        dialog.add(activeCheck, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(240,235,250));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        styleButton(saveBtn);
        styleButton(cancelBtn);
        cancelBtn.setBackground(Color.GRAY);
        btnPanel.add(saveBtn);
        btnPanel.add(Box.createRigidArea(new Dimension(10,0))); // spacing
        btnPanel.add(cancelBtn);
        dialog.add(btnPanel, gbc);

        // Pre-fill fields if editing
        if(rowData != null){
            nameField.setText((String)rowData[0]);
            categoryCombo.setSelectedItem(rowData[1]);
            quantityField.setText(rowData[2].toString());
            String cond = (String)rowData[3];
            goodBtn.setSelected(cond.equals("Good"));
            needsMaintBtn.setSelected(cond.equals("Needs Maintenance"));
            brokenBtn.setSelected(cond.equals("Broken"));
            activeCheck.setSelected(rowData[4].equals("Active"));
        }

        cancelBtn.addActionListener(e -> dialog.dispose());

        saveBtn.addActionListener(e -> {
            String name = nameField.getText();
            String category = (String)categoryCombo.getSelectedItem();
            String qty = quantityField.getText();
            String cond = goodBtn.isSelected()?"Good":needsMaintBtn.isSelected()?"Needs Maintenance":"Broken";
            String active = activeCheck.isSelected()?"Active":"Inactive";

            if(rowData != null){
                rowData[0] = name;
                rowData[1] = category;
                rowData[2] = qty;
                rowData[3] = cond;
                rowData[4] = active;
                table.repaint();
            } else {
                tableModel.addRow(new Object[]{name, category, qty, cond, active, "Edit"});
            }

            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private void styleButton(JButton button){
        button.setBackground(new Color(120, 80, 160));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(GymEquipmentManager::new);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer{
        public ButtonRenderer(){ setOpaque(true); setText("Edit"); styleButton(this);}
        public Component getTableCellRendererComponent(JTable table,Object value,
                                                       boolean isSelected, boolean hasFocus,int row,int column){ return this; }
    }

    class ButtonEditor extends DefaultCellEditor{
        protected JButton button;
        private Object[] rowData;
        public ButtonEditor(JCheckBox checkBox){
            super(checkBox);
            button = new JButton();
            button.setText("Edit");
            styleButton(button);
            button.addActionListener(e -> openEquipmentDialog(rowData));
        }
        public Component getTableCellEditorComponent(JTable table,Object value,
                                                     boolean isSelected,int row,int column){
            rowData = new Object[5];
            for(int i=0;i<5;i++){ rowData[i] = table.getValueAt(row,i); }
            return button;
        }
        public Object getCellEditorValue(){ return "Edit"; }
    }
}
