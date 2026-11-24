
public class Pasien {
    public static final int UMUR_MIN = 0;
    public static final int UMUR_MAX = 120;

    private String id;
    private String nama;
    private int umur;
    private boolean dalamAntrean;   

    
    public Pasien() {
        this.id = "P000";
        this.nama = "Tanpa Nama";
        this.umur = 0;
        this.dalamAntrean = false;
    }

   
    public Pasien(String id, String nama, int umur) {
        this.setId(id);
        this.setNama(nama);
        this.setUmur(umur);
        this.dalamAntrean = false;
    }

   
    public String getId() { return id; }
    public String getNama() { return nama; }
    public int getUmur() { return umur; }
    public boolean isDalamAntrean() { return dalamAntrean; }

    
    public void setId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID pasien tidak boleh kosong");
        }
        this.id = id.trim();
    }

    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama pasien tidak boleh kosong");
        }
        this.nama = nama.trim();
    }

    public void setUmur(int umur) {
        if (umur < UMUR_MIN || umur > UMUR_MAX) {
            throw new IllegalArgumentException(
                "Umur harus " + UMUR_MIN + "-" + UMUR_MAX + " tahun (diisi: " + umur + ")");
        }
        this.umur = umur;
    }

    
    void setDalamAntrean(boolean dalamAntrean) {
        this.dalamAntrean = dalamAntrean;
    }

    
    public String getKategoriUsia() {
        if (umur < 2) return "Bayi";
        if (umur < 12) return "Anak";
        if (umur < 18) return "Remaja";
        if (umur < 60) return "Dewasa";
        return "Lansia";
    }

    
    public boolean perluPendamping() {
        return umur < 12 || umur >= 60;
    }

    
    public String getStatusAntrean() {
        if (dalamAntrean) {
            return "Sedang menunggu";
        }
        return "Tidak dalam antrean";
    }

    @Override
    public String toString() {
        return id + " - " + nama + " (" + umur + " th)";
    }
}
