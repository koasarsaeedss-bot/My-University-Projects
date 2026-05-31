import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

// ============================================================
//   HAYAAT BLOOD BANK MANAGEMENT SYSTEM
//   Semester-2 | DBS-OOP Project
// ============================================================

// ─── 1. DASHBOARD ───────────────────────────────────────────
class Dashboard extends JFrame {

    Color bgBeige  = new Color(245, 245, 240);
    Color darkSlate = new Color(60, 60, 60);

    public Dashboard() {
        setTitle("Hayaat Blood Bank Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(bgBeige);
        setLayout(new GridBagLayout());

        JLabel header = new JLabel("HAYAAT BLOOD BANK");
        header.setFont(new Font("Serif", Font.BOLD, 32));
        header.setForeground(darkSlate);

        JButton btnReg    = new JButton("Register New Donor");
        JButton btnSearch = new JButton("Emergency Search");
        JButton btnDonate = new JButton("Record Donation");

        styleButton(btnReg);
        styleButton(btnSearch);
        styleButton(btnDonate);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(15, 20, 15, 20);

        gbc.gridy = 0; add(header,    gbc);
        gbc.gridy = 1; add(btnReg,    gbc);
        gbc.gridy = 2; add(btnSearch, gbc);
        gbc.gridy = 3; add(btnDonate, gbc);

        btnReg.addActionListener(e -> new RegistrationForm());
        btnSearch.addActionListener(e -> new SearchDonor());
        btnDonate.addActionListener(e -> new RecordDonations());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleButton(JButton b) {
        b.setPreferredSize(new Dimension(250, 50));
        b.setBackground(darkSlate);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Serif", Font.PLAIN, 18));
    }
}

// ─── 2. REGISTRATION FORM ───────────────────────────────────
class RegistrationForm extends JFrame {

    Color bgBeige   = new Color(245, 245, 240);
    Color darkSlate = new Color(60, 60, 60);
    Font serifFont  = new Font("Serif", Font.PLAIN, 16);

    JTextField nameField, cnicField, phoneField;
    JComboBox<String> bloodGroupBox, cityBox;

    public RegistrationForm() {
        setTitle("Hayaat Blood Bank - Register Donor");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        getContentPane().setBackground(bgBeige);

        JLabel title = new JLabel("New Donor Registration");
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(darkSlate);
        add(title);

        nameField  = createStyledField("Full Name");
        cnicField  = createStyledField("CNIC (e.g. 42201-XXXXXXX-X)");
        phoneField = createStyledField("Phone Number");

        add(nameField);
        add(cnicField);
        add(phoneField);

        bloodGroupBox = new JComboBox<>(new String[]{"A+","A-","B+","B-","AB+","AB-","O+","O-"});
        cityBox       = new JComboBox<>(new String[]{"Karachi","Hyderabad","Larkana","Jamshoro"});

        styleComboBox(bloodGroupBox);
        styleComboBox(cityBox);
        add(bloodGroupBox);
        add(cityBox);

        JButton saveButton = new JButton("Save Donor Details");
        saveButton.setPreferredSize(new Dimension(300, 40));
        saveButton.setBackground(darkSlate);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> saveToDatabase());
        add(saveButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JTextField createStyledField(String placeholder) {
        JTextField field = new JTextField(placeholder, 25);
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(serifFont);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, darkSlate));
        field.setBackground(bgBeige);
        return field;
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setPreferredSize(new Dimension(300, 35));
        box.setFont(serifFont);
        box.setBackground(bgBeige);
    }

    private void saveToDatabase() {
        String name  = nameField.getText();
        String cnic  = cnicField.getText();
        String phone = phoneField.getText();
        int bloodGroupId = bloodGroupBox.getSelectedIndex() + 1;
        int cityId       = cityBox.getSelectedIndex() + 1;

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/BloodDonorDB", "root", "kosar123@");

            String query = "INSERT INTO Donor (full_name, cnic, phone, blood_group_id, city_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, cnic);
            pstmt.setString(3, phone);
            pstmt.setInt(4, bloodGroupId);
            pstmt.setInt(5, cityId);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Donor Registered Successfully!");
            nameField.setText("");
            cnicField.setText("");
            phoneField.setText("");
            bloodGroupBox.setSelectedIndex(0);
            cityBox.setSelectedIndex(0);
            conn.close();

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "CNIC already exists!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}

// ─── 3. SEARCH DONOR ────────────────────────────────────────
class SearchDonor extends JFrame {

    Color bgBeige   = new Color(245, 245, 240);
    Color darkSlate = new Color(60, 60, 60);

    JComboBox<String> bloodGroupCombo;
    JTable resultTable;
    DefaultTableModel tableModel;

    public SearchDonor() {
        setTitle("Hayaat - Emergency Search");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(bgBeige);
        setLayout(new BorderLayout(20, 20));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(bgBeige);
        JLabel label = new JLabel("Select Blood Group: ");
        label.setFont(new Font("Serif", Font.PLAIN, 18));

        bloodGroupCombo = new JComboBox<>(new String[]{"A+","A-","B+","B-","AB+","AB-","O+","O-"});

        JButton searchBtn = new JButton("Find Donors");
        searchBtn.setBackground(darkSlate);
        searchBtn.setForeground(Color.WHITE);

        topPanel.add(label);
        topPanel.add(bloodGroupCombo);
        topPanel.add(searchBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Name", "Phone", "CNIC", "Eligible"};
        tableModel  = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("Serif", Font.PLAIN, 14));
        resultTable.setRowHeight(25);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> performSearch());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void performSearch() {
        tableModel.setRowCount(0);
        String selectedGroup = (String) bloodGroupCombo.getSelectedItem();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/BloodDonorDB", "root", "kosar123@")) {

            String sql = "SELECT full_name, phone, cnic, is_eligible FROM Donor "
                       + "JOIN BloodGroup ON Donor.blood_group_id = BloodGroup.blood_group_id "
                       + "WHERE BloodGroup.blood_type = ? "
                       + "AND is_eligible = 'YES' "
                       + "AND (last_donation <= DATE_SUB(CURDATE(), INTERVAL 90 DAY) OR last_donation IS NULL)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, selectedGroup);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("full_name"),
                    rs.getString("phone"),
                    rs.getString("cnic"),
                    rs.getString("is_eligible")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}

// ─── 4. RECORD DONATIONS ────────────────────────────────────
class RecordDonations extends JFrame {

    Color bgBeige   = new Color(245, 245, 240);
    Color darkSlate = new Color(60, 60, 60);

    JTextField donorIdField, unitsField;

    public RecordDonations() {
        setTitle("Log New Donation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(bgBeige);
        setLayout(new GridLayout(5, 1, 10, 10));

        add(new JLabel("  Enter Donor ID:", SwingConstants.LEFT));
        donorIdField = new JTextField();
        add(donorIdField);

        add(new JLabel("  Units Donated:", SwingConstants.LEFT));
        unitsField = new JTextField("1");
        add(unitsField);

        JButton saveBtn = new JButton("Confirm Donation");
        saveBtn.setBackground(darkSlate);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> processDonation());
        add(saveBtn);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void processDonation() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/BloodDonorDB", "root", "kosar123@")) {

            conn.setAutoCommit(false);

            // Insert into donation history
            String sql1 = "INSERT INTO DonationHistory (donor_id, donation_date, units_donated) VALUES (?, CURDATE(), ?)";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, Integer.parseInt(donorIdField.getText()));
            ps1.setInt(2, Integer.parseInt(unitsField.getText()));
            ps1.executeUpdate();

            // Mark donor ineligible for 90 days
            String sql2 = "UPDATE Donor SET last_donation = CURDATE(), is_eligible = 'NO' WHERE donor_id = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, Integer.parseInt(donorIdField.getText()));
            ps2.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(this, "Success! Donor marked ineligible for 90 days.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID and Units.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}

// ─── 5. MAIN ENTRY POINT ────────────────────────────────────
public class BloodDonorSystem {
    public static void main(String[] args) {
        // Test DB connection first
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/BloodDonorDB", "root", "kosar123@");
            conn.close();
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Database connection failed!\n" + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Launch Dashboard
        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}
