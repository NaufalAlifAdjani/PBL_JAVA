package com.example; // Pastikan ini sama dengan folder Anda

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane; // Tambahan untuk pesan error jika file belum ada

public class Main {
    public static void main(String[] args) {
        // 1. Setup Frame Utama (Dashboard)
        JFrame frame = new JFrame("Dashboard Admin - Gym Management");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null); 
        frame.setLocationRelativeTo(null); 

        // 2. Judul Menu
        JLabel lblJudul = new JLabel("SISTEM MANAJEMEN GYM", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setBounds(50, 20, 300, 30);
        frame.add(lblJudul);

        // 3. Tombol-tombol Navigasi
        JButton btnMember = new JButton("1. Registrasi Member");
        btnMember.setBounds(75, 80, 250, 40);
        frame.add(btnMember);

        JButton btnInstruktur = new JButton("2. Data Instruktur");
        btnInstruktur.setBounds(75, 140, 250, 40);
        frame.add(btnInstruktur);

        JButton btnJadwal = new JButton("3. Jadwal Kelas");
        btnJadwal.setBounds(75, 200, 250, 40);
        frame.add(btnJadwal);

        JButton btnDaftar = new JButton("4. Pendaftaran Kelas Gym");
        btnDaftar.setBounds(75, 260, 250, 40);
        frame.add(btnDaftar);

        // 4. Logika Action Listener (SUDAH DIPERBAIKI)
        
        // --- TOMBOL 1: REGISTRASI MEMBER ---
        btnMember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // KODE LAMA (Salah/Mati):
                // // RegistrasiMember.main(null); 
                
                // KODE BARU (Benar):
                // Membuka jendela RegistrasiMember
                new RegistrasiMember().setVisible(true); 
                
                // Opsional: frame.dispose(); // Jika ingin menutup menu utama saat form terbuka
            }
        });

        // --- TOMBOL 2: INSTRUKTUR (Placeholder) ---
        btnInstruktur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Form Instruktur belum dibuat!");
                // Nanti diganti: new InstrukturGym().setVisible(true);
            }
        });

        // --- TOMBOL 3: JADWAL (Placeholder) ---
        btnJadwal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Form Jadwal belum dibuat!");
                // Nanti diganti: new JadwalGym().setVisible(true);
            }
        });

        // --- TOMBOL 4: PENDAFTARAN (Placeholder) ---
        btnDaftar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Form Pendaftaran belum dibuat!");
                // Nanti diganti: new RegistrasiKelas().setVisible(true);
            }
        });

        // Tampilkan Frame
        frame.setVisible(true);
    }
}