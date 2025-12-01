package com.example;

public class InstrukturGym {
    private int idInstruktur;
    private String namaInstruktur;
    private int usia;
    private String keahlian;
    private String noTelp;

    public InstrukturGym(int idInstruktur, String namaInstruktur, int usia, String keahlian, String noTelp) {
        this.idInstruktur = idInstruktur;
        this.namaInstruktur = namaInstruktur;
        this.usia = usia;
        this.keahlian = keahlian;
        this.noTelp = noTelp;
    }

    // getter
    public int getId() {
        return idInstruktur;
    }

    public String getNama() {
        return namaInstruktur;
    }

    public int getUsia() {
        return usia;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public String getNoTelp() {
        return noTelp;
    }
}
