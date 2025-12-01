package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; // untuk JTable
import java.awt.event.*;
import java.sql.*;

public class FormInstruktur extends JFrame implements Iinstruktur {

    // komponen GUI
    private JTextField txtId, txtNama, txtUsia, txtKeahlian, txtTelp;
    private JButton btnSimpan, btnUpdate, btnHapus, btnReset, btnKembali;
    private JTable tableInstruktur;
    private DefaultTableModel tableModel;

    // config db
    private final String DB_URL = "jdbc:postgresql://localhost:5432/gym_db";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "awsome";

    public FormInstruktur() {
        // setup frame
        setTitle("Form Data Instruktur Gym");
        setSize(800, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // judul
        JLabel lblJudul = new JLabel("kelola data instruktur");
        lblJudul.setBounds(20, 10, 300, 30);
        add(lblJudul);

        // input ID
        JLabel lblId = new JLabel("ID Instruktur: ");
        lblId.setBounds(20, 50, 100, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(130, 50, 200, 25);
        txtId.setEditable(false); // id otomatis
        txtId.setText("Auto");
        add(txtId);

        // input nama
        JLabel lblNama = new JLabel("Nama: ");
        lblNama.setBounds(20, 90, 100, 25);
        add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(130, 90, 200, 25);
        add(txtNama);

        // input usia
        JLabel lblUsia = new JLabel("Usia: ");
        lblUsia.setBounds(20, 130, 100, 25);
        add(lblUsia);

        txtUsia = new JTextField();
        txtUsia.setBounds(130, 130, 200, 25);
        add(txtUsia);

        // input keahlian
        JLabel lblKeahlian = new JLabel("Keahlian: ");
        lblKeahlian.setBounds(20, 170, 100, 25);
        add(lblKeahlian);

        txtKeahlian = new JTextField();
        txtKeahlian.setBounds(130, 170, 200, 25);
        add(txtKeahlian);

        // input telp
        JLabel lblTelp = new JLabel("No Telepon: ");
        lblTelp.setBounds(20, 210, 100, 25);
        add(lblTelp);

        txtTelp = new JTextField();
        txtTelp.setBounds(130, 210, 200, 25);
        add(txtTelp);

        // button
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(20, 260, 80, 30);
        add(btnSimpan);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(110, 260, 80, 30);
        add(btnUpdate);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(200, 260, 80, 30);
        add(btnHapus);

        btnReset = new JButton("Reset");
        btnReset.setBounds(290, 260, 80, 30);
        add(btnReset);

        btnKembali = new JButton("Kembali");
        btnKembali.setBounds(20, 300, 350, 30);
        add(btnKembali);

        // JTable
        String[] columns = {"ID", "Nama", "Usia", "Keahlian", "Telepon"};
        tableModel = new DefaultTableModel(columns, 0);
        tableInstruktur = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(tableInstruktur);
        sp.setBounds(400, 50, 400, 400);
        add(sp);

        // handle event
        // untuk click table
        tableInstruktur.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableInstruktur.getSelectedRow();
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtNama.setText(tableModel.getValueAt(row, 1).toString());
                txtUsia.setText(tableModel.getValueAt(row, 2).toString());
                txtKeahlian.setText(tableModel.getValueAt(row, 3).toString());
                txtTelp.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        // implementasi interface untuk simpan, update, delete, reset form
        @Override
        public void loadData() {}

        @Override
        public void simpanData() {}

        @Override
        public void updateData() {}

        @Override
        public void hapusData() {}

        @Override
        public void resetForm() {
            // reset form
            txtId.setText("");
            txtNama.setText("");
            txtUsia.setText("");
            txtKeahlian.setText("");
            txtTelp.setText("");
            tableInstruktur.clearSelection();
        }

        // btn action
        // simpan
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simpanData();
            }
        });

        // update
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData();
            }
        });

        // hspus
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusData();
            }
        });

        // reset
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });

        // back
        btnKembali.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        loadData();
    }
}
