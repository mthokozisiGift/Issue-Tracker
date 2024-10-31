import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Index extends JFrame {
    private JPanel IndexPanel;
    private JTextArea txtDescription;
    private JComboBox txtSeverity;
    private JTextField txtAgent;
    private JButton ADDISSUEButton;
    private JTextField txtSearch;
    private JButton SEARCHButton;
    private JScrollPane txtResults;
    private JTextPane txtResults2;
    public String agentName, severity,description, search, results;

    public static void main(String[] args) {
        new Index();
    }

    public Index(){
        setTitle("ISSUE TRACKER");
        setContentPane(IndexPanel);
        setMinimumSize(new Dimension(700,600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        ADDISSUEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agentName = txtAgent.getText();
                severity = String.valueOf(txtSeverity.getSelectedItem());
                description = txtDescription.getText();
                saveInfo();

            }
        });
        SEARCHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search = txtSearch.getText();
                ConductSearch();
            }
        });
    }

    private void ConductSearch() {
        String url = "jdbc:mysql://localhost:3306/issue_tracker";
        String user = "root";
        String pass = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pass);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String query = "SELECT * FROM information WHERE agent_name = '"+agentName+"' OR severity = '"+severity+"'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                String agent = rs.getString("agent_name");
                String sev = rs.getString("severity");
                String desc = rs.getString("description");
                Timestamp t = rs.getTimestamp("date_issued");

                if (txtSearch.getText().equals(" ")){
                    JOptionPane.showMessageDialog(this, "Please Enter something to search for", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    txtResults2.setText(txtResults2.getText() + "\n");
                    txtResults2.setText(txtResults2.getText() +"Agent Name: " + agent +"\n");
                    txtResults2.setText(txtResults2.getText() +"Severity: " +sev + "\n");
                    txtResults2.setText(txtResults2.getText() +"Description: " + desc +"\n");
                    txtResults2.setText(txtResults2.getText() +"Date Issued: " + t + "\n");
                    txtResults2.setText(txtResults2.getText() +"Date today: " + timestamp + "\n");
                    txtResults2.setText(txtResults2.getText() +"------------------------------------------------------"+ "\n");
                }
            }
            rs.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    private void saveInfo() {
        String url = "jdbc:mysql://localhost:3306/issue_tracker";
        String user = "root";
        String pass = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pass);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String query = "INSERT INTO information(agent_name,severity,description,date_issued) "+
                    "VALUES('"+agentName+"', '"+severity+"', '"+description+"', '"+timestamp+"')";
            PreparedStatement pstmt = con.prepareStatement(query);
            if (!agentName.equals(" ")){
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "ISSUE ADDED", "success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please Enter Agent Name", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
