public class Dokter {
    private String id;
    private String nama;
    private String spesialis;
    private boolean aktif;


    public Dokter() {
        this.id = "D000";
        this.nama = "Tanpa Nama";
        this.spesialis = "Umum";
        this.aktif = true;
    }

    
    public Dokter(String id, String nama, String spesialis) {
        this.setId(id);
        this.setNama(nama);
        this.setSpesialis(spesialis);
        this.aktif = true;     
    }

    
    public Dokter(String id, String nama) {
        this(id, nama, "Umum");
    }

   
    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getSpesialis() { return spesialis; }
    public boolean isAktif() { return aktif; }

    
    public void setId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID dokter tidak boleh kosong");
        }
        this.id = id.trim();
    }

    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama dokter tidak boleh kosong");
        }
        this.nama = nama.trim();
    }

    public void setSpesialis(String spesialis) {
        if (spesialis == null || spesialis.isBlank()) {
            throw new IllegalArgumentException("Spesialis dokter tidak boleh kosong");
        }
        this.spesialis = spesialis.trim();
    }

   

    public boolean cekTersedia(int kunjunganHariIni, int batas) {
        return aktif && kunjunganHariIni < batas;
    }

    
    public void aktifkan() {
        if (aktif) {
            throw new IllegalStateException(nama + " sudah aktif");
        }
        this.aktif = true;
    }

   
    public void nonaktifkan() {
        if (!aktif) {
            throw new IllegalStateException(nama + " sudah nonaktif");
        }
        this.aktif = false;
    }

   
    public boolean sesuaiSpesialis(String cari) {
        return cari != null && spesialis.equalsIgnoreCase(cari.trim());
    }

    @Override
    public String toString() {
        return id + " - " + nama + " (" + spesialis + ")" + (aktif ? "" : " [nonaktif]");
    }
}
