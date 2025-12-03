package com.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent; // untuk JTable
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FormInstrukturGym extends JFrame {

    // komponen GUI
    private JTextField txtId, txtNama, txtUsia, txtKeahlian, txtTelp;
    private JButton btnSimpan, btnUpdate, btnHapus, btnReset, btnKembali;
    private JTable tableInstruktur;
    private DefaultTableModel tableModel;

    // config db
    private final String DB_URL = "jdbc:postgresql://localhost:5432/gym_db";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "secret";
    private Connection conn;

    private void koneksiDB() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Berhadil connect db");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal connect ke db: " + e.getMessage());
        }
    }

    public FormInstrukturGym() {
        // setup frame utk form instruktur
        setTitle("Form data instruktur gym");
        setSize(800, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // menutup form

        buatUI(); // tampilan user
        buatBtn(); // fungsi tombol
        koneksiDB();
        tampilkanData(); // munculkan data atbel
    }

    // helpel utk label dan field
    private void buatInput(String label, int y, JTextField field) {
        JLabel lbl = new JLabel(label + ":");
        lbl.setBounds(20, y, 100, 25);
        add(lbl);
        field.setBounds(130, y, 240, 25);
        add(field);
    }

    // helper untuk button
    private JButton buatBtn(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 80, 30);
        add(btn);
        return btn;
    }

    // helper
    private void ambilDataTabel() {
        int row = tableInstruktur.getSelectedRow();
        if (row != -1) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtNama.setText(tableModel.getValueAt(row, 1).toString());
            txtUsia.setText(tableModel.getValueAt(row, 2).toString());
            txtKeahlian.setText(tableModel.getValueAt(row, 3).toString());
            txtTelp.setText(tableModel.getValueAt(row, 4).toString());
        }
    }

    // logic button
    private void buatBtn() {
        btnSimpan.addActionListener(e -> simpanData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> resetForm());
        btnKembali.addActionListener(e -> dispose());

        // mengambil data dari tabel ke input saat baris diklik
        tableInstruktur.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ambilDataTabel();
            }
        });
    }

    // CRUD
    // create
    private void simpanData() {
        if (!cekInputValid())
            return;

        try {
            String sql = "INSERT INTO instruktur_gym (nama_instruktur, usia, keahlian, nomor_telepon) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtNama.getText());
            ps.setInt(2, Integer.parseInt(txtUsia.getText()));
            ps.setString(3, txtKeahlian.getText());
            ps.setString(4, txtTelp.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            tampilkanData();
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error Simpan: " + e.getMessage());
        }
    }

    // edit
    private void updateData() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data dari tabel dulu");
            return;
        }
        if (!cekInputValid())
            return;

        try {
            String sql = "UPDATE instruktur_gym SET nama_instruktur=?, usia=?, keahlian=?, nomor_telepon=? WHERE id_instruktur=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtNama.getText());
            ps.setInt(2, Integer.parseInt(txtUsia.getText()));
            ps.setString(3, txtKeahlian.getText());
            ps.setString(4, txtTelp.getText());
            ps.setInt(5, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil diupdate");
            tampilkanData();
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error update: " + e.getMessage());
        }
    }

    // delete
    private void hapusData() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM instruktur_gym WHERE id_instruktur=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(txtId.getText()));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                tampilkanData();
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error hapus: " + e.getMessage());
            }
        }
    }

    // read data
    private void tampilkanData() {
        tableModel.setRowCount(0);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs =
                    stmt.executeQuery("SELECT * FROM instruktur_gym ORDER BY id_instruktur ASC");

            while (rs.next()) {
                tableModel.addRow(new Object[] {rs.getInt("id_instruktur"),
                        rs.getString("nama_instruktur"), rs.getInt("usia"),
                        rs.getString("keahlian"), rs.getString("nomor_telepon")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Load Data: " + e.getMessage());
        }
    }

    private void resetForm() {
        // reset form
        txtId.setText("");
        txtNama.setText("");
        txtUsia.setText("");
        txtKeahlian.setText("");
        txtTelp.setText("");
        tableInstruktur.clearSelection();
    }

    // validasi input
    private boolean cekInputValid() {
        if (txtNama.getText().isEmpty() || txtUsia.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan usia wajib diisi!");
            return false;
        }
        try {
            Integer.parseInt(txtUsia.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Usia harus berupa angka");
            return false;
        }
        return true;
    }

    // setup ui
    private void buatUI() {
        // judul
        JLabel lblJudul = new JLabel("Kelola data instruktur");
        lblJudul.setBounds(20, 10, 300, 30);
        add(lblJudul);

        // input
        buatInput("ID Instruktur", 50, txtId = new JTextField());
        txtId.setEditable(false);

        buatInput("Nama Lengkap", 90, txtNama = new JTextField());
        buatInput("Usia", 130, txtUsia = new JTextField());
        buatInput("Keahlian", 170, txtKeahlian = new JTextField());
        buatInput("No. Telepon", 210, txtTelp = new JTextField());

        // button
        btnSimpan = buatBtn("Simpan", 20, 250);
        btnUpdate = buatBtn("Update", 110, 250);
        btnHapus = buatBtn("Hapus", 200, 250);
        btnReset = buatBtn("Reset", 290, 250);

        btnKembali = new JButton("Kembali ke menu");
        btnKembali.setBounds(20, 300, 350, 30);
        add(btnKembali);

        // set JTabel
        String[] headers = {"ID", "Nama", "Usia", "Keahlian", "Telepon"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableInstruktur = new JTable(tableModel);
        tableInstruktur.setRowHeight(30);
        JScrollPane sp = new JScrollPane(tableInstruktur); // bungkurl table dgn scroll pane
        sp.setBounds(400, 50, 400, 400);
        add(sp); // nemambahkan scroll pane
    }

    public static void main(String[] args) {
        // EventQueue/invokeLater adalah best practice swing agar thread aman
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormInstrukturGym().setVisible(true);
            }
        });
    }
}