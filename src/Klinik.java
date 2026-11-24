import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


public class Klinik {
    public static final int MAKS_KUNJUNGAN_PER_DOKTER_PER_HARI = 5;

    private String nama;
    private final Dokter[] daftarDokter;              
    private int jumlahDokter;
    private final ArrayList<Pasien> daftarPasien;
    private final ArrayList<Kunjungan> riwayat;
    private final Queue<Pasien> antrean;
    private final int kapasitasAntrean;
    private LocalDate tanggalPraktik;
    private int counterPasien;
    private int counterKunjungan;

    
    public Klinik() {
        this("Klinik Umum", 5, 20);
    }

    
    public Klinik(String nama) {
        this(nama, 5, 20);
    }

   
    public Klinik(String nama, int kapasitasDokter, int kapasitasAntrean) {
        this.setNama(nama);
        if (kapasitasDokter <= 0) {
            throw new IllegalArgumentException("Kapasitas dokter harus > 0");
        }
        this.daftarDokter = new Dokter[kapasitasDokter];
        this.jumlahDokter = 0;
        this.daftarPasien = new ArrayList<>();
        this.riwayat = new ArrayList<>();
        this.antrean = new Queue<>(kapasitasAntrean);   
        this.kapasitasAntrean = kapasitasAntrean;
        this.tanggalPraktik = LocalDate.now();
        this.counterPasien = 0;
        this.counterKunjungan = 0;
    }

    
    public String getNama() { return nama; }
    public LocalDate getTanggalPraktik() { return tanggalPraktik; }
    public int getKapasitasAntrean() { return kapasitasAntrean; }

    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama klinik tidak boleh kosong");
        }
        this.nama = nama.trim();
    }

    public void setTanggalPraktik(LocalDate tanggal) {
        if (tanggal == null) {
            throw new IllegalArgumentException("Tanggal tidak boleh kosong");
        }
        this.tanggalPraktik = tanggal;
    }

   
    public Dokter tambahDokter(String namaDokter, String spesialis) {
        if (jumlahDokter >= daftarDokter.length) {
            throw new IllegalStateException("Kapasitas dokter penuh (" + daftarDokter.length + ")");
        }
        String id = String.format("D%03d", jumlahDokter + 1);
        Dokter d = new Dokter(id, namaDokter, spesialis);
        daftarDokter[jumlahDokter] = d;
        jumlahDokter++;
        return d;
    }

   
    public Dokter cariDokter(String id) {
        if (id == null) return null;
        for (int i = 0; i < jumlahDokter; i++) {
            if (daftarDokter[i].getId().equalsIgnoreCase(id.trim())) {
                return daftarDokter[i];
            }
        }
        return null;
    }

    
    public Dokter[] getDaftarDokter() {
        return Arrays.copyOf(daftarDokter, jumlahDokter);
    }

    public int getJumlahDokter() {
        return jumlahDokter;
    }

    
    public Pasien daftarPasienBaru(String namaPasien, int umur) {
        String id = String.format("P%03d", counterPasien + 1);
        Pasien p = new Pasien(id, namaPasien, umur);   
        counterPasien++;                               
        daftarPasien.add(p);
        return p;
    }

    
    public Pasien cariPasien(String id) {
        if (id == null) return null;
        for (Pasien p : daftarPasien) {
            if (p.getId().equalsIgnoreCase(id.trim())) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Pasien> getDaftarPasien() {
        return new ArrayList<>(daftarPasien);
    }

    public int getJumlahPasien() {
        return daftarPasien.size();
    }



    public void masukAntrean(String idPasien) {
        Pasien p = cariPasien(idPasien);
        if (p == null) {
            throw new IllegalArgumentException("Pasien dengan ID '" + idPasien + "' tidak ditemukan");
        }
        if (p.isDalamAntrean()) {
            throw new IllegalStateException(p.getNama() + " sudah berada di antrean");
        }
        antrean.enqueue(p);            
        p.setDalamAntrean(true);
    }

    public boolean antreanKosong() { return antrean.isEmpty(); }
    public boolean antreanPenuh() { return antrean.isFull(); }
    public int ukuranAntrean() { return antrean.size(); }
    public Pasien pasienTerdepan() { return antrean.front(); }
    public String infoAntrean() { return antrean.toString(); }

    

    public int hitungKunjungan(Dokter dokter, LocalDate tanggal) {
        int total = 0;
        for (Kunjungan k : riwayat) {
            if (k.getDokter() == dokter && k.getTanggal().equals(tanggal)) {
                total++;
            }
        }
        return total;
    }

   
    public int totalKunjungan(Dokter dokter) {
        int total = 0;
        for (Kunjungan k : riwayat) {
            if (k.getDokter() == dokter) {
                total++;
            }
        }
        return total;
    }

    public int sisaKuota(Dokter dokter) {
        return MAKS_KUNJUNGAN_PER_DOKTER_PER_HARI - hitungKunjungan(dokter, tanggalPraktik);
    }

    
    public Kunjungan cariKunjungan(String nomor) {
        if (nomor == null) return null;
        for (Kunjungan k : riwayat) {
            if (k.getNomor().equalsIgnoreCase(nomor.trim())) {
                return k;
            }
        }
        return null;
    }

   
    public Kunjungan layani(String idDokter, String keluhan, String diagnosa) {
        if (antrean.isEmpty()) {
            throw new IllegalStateException("Antrean kosong, tidak ada pasien untuk dilayani");
        }
        Dokter dokter = cariDokter(idDokter);
        if (dokter == null) {
            throw new IllegalArgumentException("Dokter dengan ID '" + idDokter + "' tidak ditemukan");
        }
        if (!dokter.isAktif()) {
            throw new IllegalStateException(dokter.getNama() + " sedang nonaktif");
        }
        if (sisaKuota(dokter) <= 0) {
            throw new IllegalStateException(dokter.getNama() + " sudah mencapai batas "
                + MAKS_KUNJUNGAN_PER_DOKTER_PER_HARI + " kunjungan pada " + tanggalPraktik);
        }
        Pasien pasien = antrean.front();
        Kunjungan k = new Kunjungan(String.format("K%03d", counterKunjungan + 1),
                                    pasien, dokter, tanggalPraktik, keluhan, diagnosa);
        antrean.dequeue();
        pasien.setDalamAntrean(false);
        counterKunjungan++;
        riwayat.add(k);
        return k;
    }

    public ArrayList<Kunjungan> getRiwayat() {
        return new ArrayList<>(riwayat);
    }

    public int getJumlahKunjungan() {
        return riwayat.size();
    }
}
