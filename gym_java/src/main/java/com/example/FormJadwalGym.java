package com.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FormJadwalGym extends JFrame {

    // Komponen GUI
    private JTextField txtNamaKelas;
    private JComboBox<String> cbHari, cbJamMulai, cbJamSelesai, cbInstruktur;
    private JTable tabelJadwal;
    private DefaultTableModel model;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear, btnRefresh, btnKembali;
    
    // Variabel bantu
    private String idSelected = ""; 

    // Konfigurasi DB
    private final String DB_URL = "jdbc:postgresql://localhost:5432/gym_db";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "secret"; // Sesuaikan passwordmu
    private Connection conn;

    public FormJadwalGym() {
        // Setup frame dasar
        setTitle("Kelola Data Jadwal Kelas");
        setSize(700, 550);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Biar muncul di tengah layar

        koneksiDB();
        buatUI();
        loadInstrukturKeDropdown(); // Isi dropdown instruktur
        loadDataJadwal();           // Isi tabel
    }

    private void koneksiDB() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Koneksi DB Berhasil di Form Jadwal");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal connect DB: " + e.getMessage());
        }
    }

    private void buatUI() {
        // A. Nama Kelas
        JLabel lblNama = new JLabel("Nama Kelas:");
        lblNama.setBounds(20, 20, 100, 25);
        add(lblNama);

        txtNamaKelas = new JTextField();
        txtNamaKelas.setBounds(120, 20, 200, 25);
        add(txtNamaKelas);

        // B. Hari (Dropdown)
        JLabel lblHari = new JLabel("Hari:");
        lblHari.setBounds(20, 60, 100, 25);
        add(lblHari);

        String[] hariOpsi = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
        cbHari = new JComboBox<>(hariOpsi);
        cbHari.setBounds(120, 60, 150, 25);
        add(cbHari);

        // C. Jam Mulai (Dropdown)
        JLabel lblMulai = new JLabel("Jam Mulai:");
        lblMulai.setBounds(20, 100, 100, 25);
        add(lblMulai);

        cbJamMulai = new JComboBox<>(generateJam());
        cbJamMulai.setBounds(120, 100, 90, 25);
        add(cbJamMulai);

        // D. Jam Selesai (Dropdown)
        JLabel lblSelesai = new JLabel("Jam Selesai:");
        lblSelesai.setBounds(230, 100, 100, 25);
        add(lblSelesai);

        cbJamSelesai = new JComboBox<>(generateJam());
        cbJamSelesai.setBounds(320, 100, 90, 25);
        add(cbJamSelesai);

        // E. Instruktur (Dropdown Database)
        JLabel lblIns = new JLabel("Instruktur:");
        lblIns.setBounds(20, 140, 100, 25);
        add(lblIns);

        cbInstruktur = new JComboBox<>();
        cbInstruktur.setBounds(120, 140, 290, 25);
        add(cbInstruktur);

        // --- TOMBOL ---
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(20, 190, 80, 30);
        add(btnSimpan);

        btnUbah = new JButton("Ubah");
        btnUbah.setBounds(110, 190, 80, 30);
        add(btnUbah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(200, 190, 80, 30);
        add(btnHapus);

        btnClear = new JButton("Clear");
        btnClear.setBounds(290, 190, 80, 30);
        add(btnClear);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(380, 190, 100, 30);
        add(btnRefresh);

        // Tombol Kembali (Penting agar user bisa balik ke menu)
        btnKembali = new JButton("Kembali");
        btnKembali.setBounds(500, 190, 100, 30);
        add(btnKembali);

        // --- TABEL ---
        String[] kolom = {"ID", "Kelas", "Hari", "Mulai", "Selesai", "Instruktur"};
        model = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabelJadwal = new JTable(model);
        JScrollPane sp = new JScrollPane(tabelJadwal);
        sp.setBounds(20, 240, 640, 250);
        add(sp);

        // --- ACTION LISTENER ---
        setupActions();
    }

    private void setupActions() {
        btnSimpan.addActionListener(e -> aksiSimpan());
        btnUbah.addActionListener(e -> aksiUbah());
        btnHapus.addActionListener(e -> aksiHapus());
        btnClear.addActionListener(e -> clearForm());
        
        btnRefresh.addActionListener(e -> {
            loadInstrukturKeDropdown();
            loadDataJadwal();
        });

        // Tombol kembali menutup form ini saja
        btnKembali.addActionListener(e -> dispose());

        // Klik Tabel
        tabelJadwal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelJadwal.getSelectedRow();
                if (row != -1) {
                    idSelected = model.getValueAt(row, 0).toString();
                    txtNamaKelas.setText(model.getValueAt(row, 1).toString());
                    cbHari.setSelectedItem(model.getValueAt(row, 2).toString());
                    cbJamMulai.setSelectedItem(model.getValueAt(row, 3).toString()); 
                    cbJamSelesai.setSelectedItem(model.getValueAt(row, 4).toString());
                    
                    String namaInstrukturTabel = model.getValueAt(row, 5).toString();
                    pilihInstrukturDiCombo(namaInstrukturTabel);
                }
            }
        });
    }

    // --- LOGIC UTAMA ---

    private void loadInstrukturKeDropdown() {
        cbInstruktur.removeAllItems();
        cbInstruktur.addItem("-- Pilih Instruktur --");
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_instruktur, nama_instruktur, keahlian FROM instruktur_gym");
            
            while (rs.next()) {
                String id = rs.getString("id_instruktur");
                String nama = rs.getString("nama_instruktur");
                String skill = rs.getString("keahlian");
                String item = id + " - " + nama + " [" + skill + "]";
                cbInstruktur.addItem(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataJadwal() {
        model.setRowCount(0);
        try {
            String sql = "SELECT j.id_kelas, j.nama_kelas, j.hari, " +
                         "TO_CHAR(j.jam_mulai, 'HH24:MI') as jam_mulai, " +
                         "TO_CHAR(j.jam_selesai, 'HH24:MI') as jam_selesai, " +
                         "i.nama_instruktur " +
                         "FROM jadwal_kelas j " +
                         "JOIN instruktur_gym i ON j.id_instruktur = i.id_instruktur " +
                         "ORDER BY j.hari, j.jam_mulai ASC";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_kelas"),
                    rs.getString("nama_kelas"),
                    rs.getString("hari"),
                    rs.getString("jam_mulai"),
                    rs.getString("jam_selesai"),
                    rs.getString("nama_instruktur")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Load Data: " + e.getMessage());
        }
    }

    private void aksiSimpan() {
        int idIns = getSelectedInstrukturID();
        if (idIns == -1) {
            JOptionPane.showMessageDialog(this, "Pilih Instruktur dulu!");
            return;
        }

        try {
            String sql = "INSERT INTO jadwal_kelas (nama_kelas, hari, jam_mulai, jam_selesai, id_instruktur) VALUES (?, ?, ?::time, ?::time, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, txtNamaKelas.getText());
            pstmt.setString(2, cbHari.getSelectedItem().toString());
            pstmt.setString(3, cbJamMulai.getSelectedItem().toString());
            pstmt.setString(4, cbJamSelesai.getSelectedItem().toString());
            pstmt.setInt(5, idIns);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Berhasil Simpan!");
            loadDataJadwal();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Simpan: " + e.getMessage());
        }
    }

    private void aksiUbah() {
        if(idSelected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data tabel dulu!");
            return;
        }
        int idIns = getSelectedInstrukturID();

        try {
            String sql = "UPDATE jadwal_kelas SET nama_kelas=?, hari=?, jam_mulai=?::time, jam_selesai=?::time, id_instruktur=? WHERE id_kelas=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, txtNamaKelas.getText());
            pstmt.setString(2, cbHari.getSelectedItem().toString());
            pstmt.setString(3, cbJamMulai.getSelectedItem().toString());
            pstmt.setString(4, cbJamSelesai.getSelectedItem().toString());
            pstmt.setInt(5, idIns);
            pstmt.setInt(6, Integer.parseInt(idSelected));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Berhasil Ubah!");
            loadDataJadwal();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Ubah: " + e.getMessage());
        }
    }

    private void aksiHapus() {
        if(idSelected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM jadwal_kelas WHERE id_kelas=?");
                pstmt.setInt(1, Integer.parseInt(idSelected));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                loadDataJadwal();
                clearForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal Hapus: " + e.getMessage());
            }
        }
    }

    private void clearForm() {
        txtNamaKelas.setText("");
        cbHari.setSelectedIndex(0);
        cbJamMulai.setSelectedIndex(0);
        cbJamSelesai.setSelectedIndex(0);
        cbInstruktur.setSelectedIndex(0);
        tabelJadwal.clearSelection();
        idSelected = "";
    }

    // --- HELPER METHODS ---

    private String[] generateJam() {
        String[] jam = new String[33]; 
        int index = 0;
        for (int h = 6; h <= 22; h++) {
            jam[index++] = String.format("%02d:00", h);
            if(h != 22) jam[index++] = String.format("%02d:30", h);
        }
        return jam;
    }

    private int getSelectedInstrukturID() {
        if (cbInstruktur.getSelectedIndex() <= 0) return -1;
        String selected = cbInstruktur.getSelectedItem().toString();
        String[] parts = selected.split(" - "); 
        return Integer.parseInt(parts[0]); 
    }

    private void pilihInstrukturDiCombo(String namaInstruktur) {
        for (int i = 0; i < cbInstruktur.getItemCount(); i++) {
            String item = cbInstruktur.getItemAt(i);
            if (item.contains(namaInstruktur)) {
                cbInstruktur.setSelectedIndex(i);
                break;
            }
        }
    }

    // Main method agar bisa ditest langsung (Run File)
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new FormJadwalGym().setVisible(true);
        });
    }
}