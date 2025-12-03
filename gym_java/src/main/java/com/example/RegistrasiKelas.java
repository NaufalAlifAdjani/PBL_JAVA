package com.example;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
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

public class RegistrasiKelas extends JFrame {

    // Komponen GUI
    private JTextField txtIdPendaftaran, txtIdMember, txtIdKelas, txtTanggal, txtCatatan;
    private JButton btnTambah, btnHapus, btnReset, btnKembali;
    private JTable tabelDaftarKelas;
    private DefaultTableModel tableModel;

    // Config DB
    private final String DB_URL = "jdbc:postgresql://localhost:5432/gym_db";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "awsome"; // Sesuaikan password
    private Connection conn;

    public RegistrasiKelas() {
        // Setup Frame
        setTitle("Form Pendaftaran Kelas Gym");
        setSize(850, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        koneksiDB();
        buatUI();
        tampilkanData();
    }

    private void koneksiDB() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Koneksi DB Berhasil");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal connect DB: " + e.getMessage());
        }
    }

    private void buatUI() {
        // --- ID Pendaftaran (AUTO) ---
        JLabel lblId = new JLabel("ID Pendaftaran:");
        lblId.setBounds(20, 20, 100, 25);
        add(lblId);

        txtIdPendaftaran = new JTextField("Auto"); // Default text
        txtIdPendaftaran.setBounds(130, 20, 200, 25);
        txtIdPendaftaran.setEditable(false); // User GABOLEH ngisi ini manual
        txtIdPendaftaran.setBackground(new Color(230, 230, 230)); // Warna abu-abu
        add(txtIdPendaftaran);

        // --- ID Member ---
        JLabel lblMem = new JLabel("ID Member:");
        lblMem.setBounds(20, 60, 100, 25);
        add(lblMem);
        txtIdMember = new JTextField();
        txtIdMember.setBounds(130, 60, 200, 25);
        add(txtIdMember);

        // --- ID Kelas ---
        JLabel lblKls = new JLabel("ID Kelas:");
        lblKls.setBounds(20, 100, 100, 25);
        add(lblKls);
        txtIdKelas = new JTextField();
        txtIdKelas.setBounds(130, 100, 200, 25);
        add(txtIdKelas);

        // --- Tanggal ---
        JLabel lblTgl = new JLabel("Tanggal:");
        lblTgl.setBounds(20, 140, 120, 25);
        add(lblTgl);
        txtTanggal = new JTextField();
        txtTanggal.setBounds(130, 140, 200, 25);
        add(txtTanggal);

        // --- Catatan ---
        JLabel lblCat = new JLabel("Catatan:");
        lblCat.setBounds(20, 180, 100, 25);
        add(lblCat);
        txtCatatan = new JTextField();
        txtCatatan.setBounds(130, 180, 200, 25);
        add(txtCatatan);

        // --- BUTTONS ---
        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 230, 90, 30);
        add(btnTambah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(130, 230, 90, 30);
        add(btnHapus);

        btnReset = new JButton("Reset");
        btnReset.setBounds(240, 230, 90, 30);
        add(btnReset);

        btnKembali = new JButton("Kembali");
        btnKembali.setBounds(20, 280, 310, 30);
        add(btnKembali);

        // --- TABLE ---
        String[] headers = {"ID Daftar", "ID Member", "ID Kelas", "Tanggal", "Catatan"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabelDaftarKelas = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(tabelDaftarKelas);
        sp.setBounds(350, 20, 460, 410);
        add(sp);

        // --- ACTION LISTENERS ---
        btnTambah.addActionListener(e -> simpanData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> resetForm());
        btnKembali.addActionListener(e -> dispose());

        // Klik tabel -> Isi form (Supaya tau ID mana yg mau dihapus)
        tabelDaftarKelas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelDaftarKelas.getSelectedRow();
                if (row != -1) {
                    txtIdPendaftaran.setText(tableModel.getValueAt(row, 0).toString()); // ID
                                                                                        // diambil
                                                                                        // dari sini
                    txtIdMember.setText(tableModel.getValueAt(row, 1).toString());
                    txtIdKelas.setText(tableModel.getValueAt(row, 2).toString());
                    txtTanggal.setText(tableModel.getValueAt(row, 3).toString());
                    txtCatatan.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });
    }

    // --- CRUD METHODS ---

    private void tampilkanData() {
        tableModel.setRowCount(0);
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM pendaftaran_kelas ORDER BY id_pendaftaran ASC";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new Object[] {rs.getInt("id_pendaftaran"), // ID tetap kita ambil
                                                                             // untuk ditampilkan
                        rs.getInt("id_member"), rs.getInt("id_kelas"), rs.getDate("tanggal_daftar"),
                        rs.getString("catatan")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Load Data: " + e.getMessage());
        }
    }

    private void simpanData() {
        if (txtIdMember.getText().isEmpty() || txtIdKelas.getText().isEmpty()
                || txtTanggal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Member, ID Kelas, dan Tanggal harus diisi!");
            return;
        }

        try {
            // PERBAIKAN DI SINI: Tidak memasukkan id_pendaftaran (karena auto increment)
            String sql =
                    "INSERT INTO pendaftaran_kelas (id_member, id_kelas, tanggal_daftar, catatan) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            // Parsing input (Mulai dari parameter 1 = id_member)
            ps.setInt(1, Integer.parseInt(txtIdMember.getText()));
            ps.setInt(2, Integer.parseInt(txtIdKelas.getText()));
            ps.setDate(3, Date.valueOf(txtTanggal.getText()));
            ps.setString(4, txtCatatan.getText());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            tampilkanData();
            resetForm();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID harus angka & Tanggal format YYYY-MM-DD");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah. Gunakan YYYY-MM-DD");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Database: " + e.getMessage());
        }
    }

    private void hapusData() {
        // Cek apakah ID Pendaftaran (yg auto/angka) sudah terpilih dari tabel
        if (txtIdPendaftaran.getText().equals("Auto") || txtIdPendaftaran.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data dari tabel dulu untuk menghapus!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin hapus data ID " + txtIdPendaftaran.getText() + "?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM pendaftaran_kelas WHERE id_pendaftaran = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(txtIdPendaftaran.getText()));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                tampilkanData();
                resetForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal Hapus: " + e.getMessage());
            }
        }
    }

    private void resetForm() {
        txtIdPendaftaran.setText("Auto"); // Kembalikan ke status Auto
        txtIdMember.setText("");
        txtIdKelas.setText("");
        txtTanggal.setText("");
        txtCatatan.setText("");
        tabelDaftarKelas.clearSelection();
    }

    // Main Method Testing
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new RegistrasiKelas().setVisible(true);
        });
    }
}
