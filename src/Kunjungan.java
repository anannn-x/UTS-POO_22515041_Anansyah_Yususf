import java.time.LocalDate;


public class Kunjungan {
    public static final String DIPROSES = "DIPROSES";
    public static final String SELESAI = "SELESAI";

    private String nomor;
    private Pasien pasien;
    private Dokter dokter;
    private LocalDate tanggal;
    private String keluhan;
    private String diagnosa;
    private String status;

    
    public Kunjungan() {
        this.nomor = "K000";
        this.pasien = new Pasien();
        this.dokter = new Dokter();
        this.tanggal = LocalDate.now();
        this.keluhan = "-";
        this.diagnosa = "-";
        this.status = DIPROSES;
    }

    
    public Kunjungan(String nomor, Pasien pasien, Dokter dokter,
                     LocalDate tanggal, String keluhan, String diagnosa) {
        this.setNomor(nomor);
        this.setPasien(pasien);
        this.setDokter(dokter);
        this.setTanggal(tanggal);
        this.setKeluhan(keluhan);
        if (diagnosa == null || diagnosa.isBlank()) {
            this.diagnosa = "-";
            this.status = DIPROSES;
        } else {
            this.diagnosa = diagnosa.trim();
            this.status = SELESAI;
        }
    }

   
    public Kunjungan(String nomor, Pasien pasien, Dokter dokter,
                     LocalDate tanggal, String keluhan) {
        this(nomor, pasien, dokter, tanggal, keluhan, "-");
    }

   
    public String getNomor() { return nomor; }
    public Pasien getPasien() { return pasien; }
    public Dokter getDokter() { return dokter; }
    public LocalDate getTanggal() { return tanggal; }
    public String getKeluhan() { return keluhan; }
    public String getDiagnosa() { return diagnosa; }
    public String getStatus() { return status; }

    
    public void setNomor(String nomor) {
        if (nomor == null || nomor.isBlank()) {
            throw new IllegalArgumentException("Nomor kunjungan tidak boleh kosong");
        }
        this.nomor = nomor.trim();
    }

    public void setPasien(Pasien pasien) {
        if (pasien == null) {
            throw new IllegalArgumentException("Pasien wajib diisi");
        }
        this.pasien = pasien;
    }

    public void setDokter(Dokter dokter) {
        if (dokter == null) {
            throw new IllegalArgumentException("Dokter wajib diisi");
        }
        this.dokter = dokter;
    }

    public void setTanggal(LocalDate tanggal) {
        if (tanggal == null) {
            throw new IllegalArgumentException("Tanggal wajib diisi");
        }
        this.tanggal = tanggal;
    }

    public void setKeluhan(String keluhan) {
        if (keluhan == null || keluhan.isBlank()) {
            throw new IllegalArgumentException("Keluhan tidak boleh kosong");
        }
        this.keluhan = keluhan.trim();
    }

    public void setDiagnosa(String diagnosa) {
        if (diagnosa == null || diagnosa.isBlank()) {
            throw new IllegalArgumentException("Diagnosa tidak boleh kosong");
        }
        this.diagnosa = diagnosa.trim();
    }

   
    public boolean sudahDidiagnosa() {
        return !diagnosa.equals("-");
    }

   
    public void tandaiSelesai() {
        if (status.equals(SELESAI)) {
            throw new IllegalStateException("Kunjungan " + nomor + " sudah selesai");
        }
        if (!sudahDidiagnosa()) {
            throw new IllegalStateException("Diagnosa belum diisi, kunjungan belum bisa diselesaikan");
        }
        this.status = SELESAI;
    }

   
    public boolean isHariIni() {
        return tanggal.equals(LocalDate.now());
    }

   
    public String ringkasan() {
        return nomor + " | " + tanggal + " | " + pasien.getNama()
             + " -> " + dokter.getNama() + " | " + status;
    }

    @Override
    public String toString() {
        return nomor + " | " + tanggal + " | " + pasien.getNama()
             + " -> " + dokter.getNama()
             + " | keluhan: " + keluhan + " | diagnosa: " + diagnosa
             + " | " + status;
    }
}
