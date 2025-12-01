package com.example; // Sesuai dengan folder di gambar

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Main {
    public static void main(String[] args) {
        // 1. Setup Frame Utama (Dashboard)
        JFrame frame = new JFrame("Dashboard Admin - Gym Management");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null); // Layout manual agar mudah diatur posisinya
        frame.setLocationRelativeTo(null); // Agar muncul di tengah layar

        // 2. Judul Menu
        JLabel lblJudul = new JLabel("SISTEM MANAJEMEN GYM", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setBounds(50, 20, 300, 30);
        frame.add(lblJudul);

        // 3. Tombol-tombol Navigasi

        // Tombol ke Form 1: Registrasi Member
        JButton btnMember = new JButton("1. Registrasi Member");
        btnMember.setBounds(75, 80, 250, 40);
        frame.add(btnMember);

        // Tombol ke Form 2: Data Instruktur
        JButton btnInstruktur = new JButton("2. Data Instruktur");
        btnInstruktur.setBounds(75, 140, 250, 40);
        frame.add(btnInstruktur);

        // Tombol ke Form 3: Jadwal Kelas
        JButton btnJadwal = new JButton("3. Jadwal Kelas");
        btnJadwal.setBounds(75, 200, 250, 40);
        frame.add(btnJadwal);

        // Tombol ke Form 4: Pendaftaran Kelas
        JButton btnDaftar = new JButton("4. Pendaftaran Kelas Gym");
        btnDaftar.setBounds(75, 260, 250, 40);
        frame.add(btnDaftar);

        // 4. Logika Action Listener (Menghubungkan ke file lain)

        // Klik Tombol Member -> Buka File RegistrasiMember.java
        btnMember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Memanggil method main() milik kelas lain
                // RegistrasiMember.main(null);
            }
        });

        // Klik Tombol Instruktur -> Buka File InstrukturGym.java
        btnInstruktur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FormInstruktur().setVisible(true); // membuka form
            }
        });

        // Klik Tombol Jadwal -> Buka File JadwalGym.java
        btnJadwal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // JadwalGym.main(null);
            }
        });

        // Klik Tombol Daftar -> Buka File RegistrasiKelas.java
        btnDaftar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // RegistrasiKelas.main(null);
            }
        });

        // Tampilkan Frame
        frame.setVisible(true);
    }
}
