package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormRegistrasiKelas {
    
    public FormRegistrasiKelas(RegistrasiKelas form) {
        JFrame frame = new JFrame("Form Registrasi Kelas Gym");
        frame.setSize(400, 350);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Label dan Text Fields
        JLabel judul = new JLabel("DAFTAR KELAS GYM");
        judul.setBounds(145, 10, 200, 25);
        frame.add(judul);

        JLabel lblidDaftar = new JLabel("Id Pendaftaran  :");
        lblidDaftar.setBounds(10, 60, 100, 25);
        frame.add(lblidDaftar);
        form.getIdPendaftaran().setBounds(115, 60, 200, 25);
        frame.add(form.getIdPendaftaran());

        JLabel lblidMember = new JLabel("Id Member        :");
        lblidMember.setBounds(10, 90, 100, 25);
        frame.add(lblidMember);
        form.getIdMember().setBounds(115, 90, 200, 25);
        frame.add(form.getIdMember());

        JLabel lblidKelas = new JLabel("Id Kelas             :");
        lblidKelas.setBounds(10, 120, 100, 25);
        frame.add(lblidKelas);
        form.getIdKelas().setBounds(115, 120, 200, 25);
        frame.add(form.getIdKelas());

        JLabel lbltglDaftar = new JLabel("Tanggal Daftar  :");
        lbltglDaftar.setBounds(10, 150, 100, 25);
        frame.add(lbltglDaftar);
        form.getTanggalDaftar().setBounds(115, 150, 200, 25);
        frame.add(form.getTanggalDaftar());

        JLabel lblcatatan = new JLabel("Catatan              :");
        lblcatatan.setBounds(10, 180, 100, 25);
        frame.add(lblcatatan);
        form.getCatatan().setBounds(115, 180, 200, 25);
        frame.add(form.getCatatan());

        // Tombol
        form.getTambah().setBounds(20, 215, 100, 30);
        frame.add(form.getTambah());

        form.getReset().setBounds(130, 215, 100, 30);
        frame.add(form.getReset());

        form.getDelete().setBounds(240, 215, 100, 30);
        frame.add(form.getDelete());

        // Tabel untuk menampilkan data
        JScrollPane scroll = new JScrollPane(form.getTabel());
        scroll.setBounds(20, 250, 400, 130);
        frame.add(scroll);

        // Set frame visible
        frame.setVisible(true);

        // Reset button action
        form.getReset().addActionListener(e -> {
            form.getIdPendaftaran().setText("");
            form.getIdMember().setText("");
            form.getIdKelas().setText("");
            form.getTanggalDaftar().setText("");
            form.getCatatan().setText("");
        });

        // Delete button action
        form.getDelete().addActionListener(e -> {
            int selectedRow = form.getTabel().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Pilih baris yang ingin dihapus!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) form.getTabel().getModel();
            model.removeRow(selectedRow);

            String idPendaftaran = model.getValueAt(selectedRow, 0).toString();  // ID Pendaftaran ada di kolom 0
            RegistrasiKelas.deleteFromDatabase(idPendaftaran);
        });
    }
}

