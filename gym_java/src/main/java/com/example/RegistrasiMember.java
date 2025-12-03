package com.example; // SESUAIKAN package Anda

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrasiMember extends JFrame {
    // Komponen GUI
    private JTextField txtNama, txtNoTelp, txtTanggal;
    private JTextArea txtAlamat;
    private JTable tableMember;
    private DefaultTableModel tableModel;
    private JButton btnSimpan, btnHapus, btnReset;

    // --- KONFIGURASI POSTGRESQL ---
    // Port default PostgreSQL adalah 5432
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/gym_db";
    
    // User & Pass default Laragon/Postgres biasanya 'postgres'
    private static final String DB_USER = "postgres"; 
    private static final String DB_PASS = "12345678"; // Coba kosongkan "" jika gagal login

    public RegistrasiMember() {
        setTitle("Form Registrasi Member Gym (PostgreSQL)");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        initUI();
        loadData();
    }

    private void initUI() {
        // --- FORM INPUT ---

        // Nama Member
        JLabel lblNama = new JLabel("Nama Member:");
        lblNama.setBounds(20, 20, 120, 25);
        add(lblNama);
        txtNama = new JTextField();
        txtNama.setBounds(150, 20, 200, 25);
        add(txtNama);

        // Nomor Telepon
        JLabel lblTelp = new JLabel("No. Telepon:");
        lblTelp.setBounds(20, 60, 120, 25);
        add(lblTelp);
        txtNoTelp = new JTextField();
        txtNoTelp.setBounds(150, 60, 200, 25);
        add(txtNoTelp);

        // Tanggal Bergabung
        JLabel lblTgl = new JLabel("Tgl Gabung (YYYY-MM-DD):");
        lblTgl.setBounds(20, 100, 180, 25);
        add(lblTgl);
        txtTanggal = new JTextField();
        txtTanggal.setBounds(180, 100, 170, 25);
        txtTanggal.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        add(txtTanggal);

        // Alamat
        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setBounds(20, 140, 120, 25);
        add(lblAlamat);
        txtAlamat = new JTextArea();
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        scrollAlamat.setBounds(150, 140, 200, 80);
        add(scrollAlamat);

        // --- TOMBOL ---
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(150, 240, 80, 30);
        add(btnSimpan);

        btnReset = new JButton("Reset");
        btnReset.setBounds(240, 240, 80, 30);
        add(btnReset);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(330, 240, 80, 30);
        add(btnHapus);

        // --- TABEL ---
        String[] columns = {"ID", "Nama Member", "No. Telepon", "Alamat", "Tgl Gabung"};
        tableModel = new DefaultTableModel(columns, 0);
        tableMember = new JTable(tableModel);
        JScrollPane scrollTable = new JScrollPane(tableMember);
        scrollTable.setBounds(420, 20, 400, 400); 
        add(scrollTable);

        // --- EVENT HANDLING ---
        btnSimpan.addActionListener(e -> simpanData());
        btnReset.addActionListener(e -> resetForm());
        btnHapus.addActionListener(e -> hapusData());

        tableMember.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableMember.getSelectedRow();
                txtNama.setText(tableModel.getValueAt(row, 1).toString());
                txtNoTelp.setText(tableModel.getValueAt(row, 2).toString());
                txtAlamat.setText(tableModel.getValueAt(row, 3).toString());
                txtTanggal.setText(tableModel.getValueAt(row, 4).toString());
            }
        });
    }

    // --- KONEKSI DATABASE ---
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // --- READ (TAMPIL DATA) ---
    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM members ORDER BY id_member ASC")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_member"),
                    rs.getString("nama_member"),
                    rs.getString("nomor_telepon"),
                    rs.getString("alamat"),
                    rs.getDate("tanggal_bergabung")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Konek ke PostgreSQL: " + e.getMessage());
        }
    }

    // --- CREATE (SIMPAN DATA) ---
    private void simpanData() {
        String nama = txtNama.getText();
        String telp = txtNoTelp.getText();
        String alamat = txtAlamat.getText();
        String tgl = txtTanggal.getText();

        if (nama.isEmpty() || telp.isEmpty() || tgl.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO members (nama_member, nomor_telepon, alamat, tanggal_bergabung) VALUES (?, ?, ?, ?::date)")) {
            
            // Note: ?::date adalah casting eksplisit PostgreSQL (opsional tapi aman)
            ps.setString(1, nama);
            ps.setString(2, telp);
            ps.setString(3, alamat);
            ps.setString(4, tgl); 
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Berhasil Disimpan!");
            loadData();
            resetForm();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Simpan: " + e.getMessage());
        }
    }

    // --- DELETE (HAPUS DATA) ---
    private void hapusData() {
        int selectedRow = tableMember.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris dulu!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM members WHERE id_member = ?")) {
                
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Berhasil Dihapus!");
                loadData();
                resetForm();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal Hapus: " + e.getMessage());
            }
        }
    }

    private void resetForm() {
        txtNama.setText("");
        txtNoTelp.setText("");
        txtAlamat.setText("");
        txtTanggal.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        tableMember.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistrasiMember().setVisible(true);
        });
    }
}