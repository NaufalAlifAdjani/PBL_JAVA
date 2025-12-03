package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class RegistrasiKelas {
    // Deklarasi atribut
    private JTextField idPendaftaran, idMember, idKelas, tanggalDaftar, catatan;
    private JButton tambah, delete, reset;
    private JTable tabelDaftarKelas;

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/gym_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "passwordbaru";

    public RegistrasiKelas() {
        // Setup atribut
        idPendaftaran = new JTextField();
        idMember = new JTextField();
        idKelas = new JTextField();
        tanggalDaftar = new JTextField();
        catatan = new JTextField();

        String[] columnNames = {"ID Pendaftaran", "ID Member", "ID Kelas", "Tanggal Daftar", "Catatan"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tabelDaftarKelas = new JTable(model);

        tambah = new JButton("Tambah");
        delete = new JButton("Hapus");
        reset = new JButton("Reset");

        // Aksi untuk tombol "Tambah"
        tambah.addActionListener(e -> {
            String idPendStr = idPendaftaran.getText();
            String idMemStr  = idMember.getText();
            String idKelStr  = idKelas.getText();
            String tgl       = tanggalDaftar.getText();
            String cat       = catatan.getText();

            if (idPendStr.trim().isEmpty() || idMemStr.trim().isEmpty() || idKelStr.trim().isEmpty() || tgl.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Semua field harus diisi.");
                return;  
            }

            try {
                int idPend = Integer.parseInt(idPendStr.trim());
                int idMem  = Integer.parseInt(idMemStr.trim());
                int idKel  = Integer.parseInt(idKelStr.trim());

                try {
                    java.sql.Date sqlDate = java.sql.Date.valueOf(tgl.trim());

                    saveData(idPend, idMem, idKel, sqlDate, cat);

                    DefaultTableModel model1 = (DefaultTableModel) tabelDaftarKelas.getModel();
                    model1.addRow(new Object[]{idPendStr, idMemStr, idKelStr, tgl, cat});
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Format tanggal salah. Gunakan format yyyy-MM-dd.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID Pendaftaran, ID Member, dan ID Kelas harus berupa angka.");
            }
        });
    }

    // Method untuk menyimpan data ke database
    public static void saveData(int idPendaftaran, int idMember, int idKelas, java.sql.Date tanggalDaftar, String catatan) {
        String sql = "INSERT INTO pendaftaran_kelas (id_pendaftaran, id_member, id_kelas, tanggal_daftar, catatan) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPendaftaran);
            ps.setInt(2, idMember);
            ps.setInt(3, idKelas);
            ps.setDate(4, tanggalDaftar);
            ps.setString(5, catatan);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menyimpan ke database: " + e.getMessage());
        }
    }

    // Method untuk menghapus data berdasarkan ID Pendaftaran
    public static void deleteFromDatabase(String idPendaftaran) {
        String sql = "DELETE FROM pendaftaran_kelas WHERE id_pendaftaran = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPendaftaran);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menghapus data dari database: " + e.getMessage());
        }
    }

    // Getter untuk setiap atribut
    public JTextField getIdPendaftaran() { 
        return idPendaftaran; 
    }
    public JTextField getIdMember() { 
        return idMember; 
    }
    public JTextField getIdKelas() { 
        return idKelas; 
    }
    public JTextField getTanggalDaftar() { 
        return tanggalDaftar; 
    }
    public JTextField getCatatan() { 
        return catatan; 
    }

    public JButton getTambah() { 
        return tambah; 
    }
    public JButton getDelete() { 
        return delete; 
    }
    public JButton getReset() { 
        return reset; 
    }

    public JTable getTabel() { 
        return tabelDaftarKelas; 
    }
}



