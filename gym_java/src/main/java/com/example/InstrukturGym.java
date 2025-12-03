package com.example;

public class InstrukturGym {
    private int id_instruktur;
    private String nama_instruktur;
    private int usia;
    private String keahlian;
    private String nomor_telepon;

    // konstruktor
    public InstrukturGym(int id, String nama, int usia, String keahlian, String noTelp) {
        this.id_instruktur = id;
        this.nama_instruktur = nama;
        this.usia = usia;
        this.keahlian = keahlian;
        this.nomor_telepon = noTelp;
    }

    // getter
    public int getId() {
        return id_instruktur;
    }

    public String getNama() {
        return nama_instruktur;
    }

    public int getUsia() {
        return usia;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public String getNoTelp() {
        return nomor_telepon;
    }
}
