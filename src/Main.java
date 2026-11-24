import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        Klinik klinik = new Klinik("Klinik Sehat Sentosa");
        siapkanDataAwal(klinik);

        boolean jalan = true;
        while (jalan) {
            tampilkanMenu(klinik);
            String pilih = bacaTeks("Pilih menu");
            try {
                switch (pilih) {
                    case "1": menuDaftarPasien(klinik); break;
                    case "2": menuTambahDokter(klinik); break;
                    case "3": menuMasukAntrean(klinik); break;
                    case "4": menuCari(klinik); break;
                    case "5": menuLayani(klinik); break;
                    case "6": menuLaporan(klinik); break;
                    case "7": menuLihatAntrean(klinik); break;
                    case "8": menuLihatDokter(klinik); break;
                    case "9": menuRiwayat(klinik); break;
                    case "10": menuLihatPasien(klinik); break;
                    case "11": menuUbahTanggal(klinik); break;
                    case "12": menuUbahData(klinik); break;
                    case "0": jalan = false; System.out.println("Terima kasih."); break;
                    default: System.out.println("Menu tidak dikenal.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("[GAGAL] " + e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println("[GAGAL] Format tanggal harus yyyy-MM-dd, contoh 2026-11-24");
            }
        }
    }

    
    private static void siapkanDataAwal(Klinik klinik) {
        klinik.tambahDokter("dr. zulhaji", "Umum");
        klinik.tambahDokter("dr. qharin", "Anak");
        klinik.daftarPasienBaru("Zulfahmi", 21);
        klinik.daftarPasienBaru("Anisa", 10);
        klinik.daftarPasienBaru("Rahyono", 61);
    }

    private static void tampilkanMenu(Klinik klinik) {
        System.out.println();
        System.out.println("=== " + klinik.getNama().toUpperCase() + " ===");
        System.out.println("Tanggal praktik : " + klinik.getTanggalPraktik());
        System.out.println("Antrean         : " + klinik.ukuranAntrean() + "/" + klinik.getKapasitasAntrean());
        System.out.println("1.  Daftarkan pasien baru");
        System.out.println("2.  Tambah dokter");
        System.out.println("3.  Masukkan pasien ke antrean (enqueue)");
        System.out.println("4.  Cari data (berdasarkan ID)");
        System.out.println("5.  Layani pasien terdepan (dequeue)");
        System.out.println("6.  Laporan ringkasan");
        System.out.println("7.  Lihat antrean (front, size, isEmpty, isFull)");
        System.out.println("8.  Lihat dokter & sisa kuota hari ini");
        System.out.println("9.  Riwayat kunjungan");
        System.out.println("10. Daftar pasien terdaftar");
        System.out.println("11. Ubah tanggal praktik");
        System.out.println("12. Ubah data (pasien/dokter/kunjungan)");
        System.out.println("0.  Keluar");
    }

    private static void menuDaftarPasien(Klinik klinik) {
        String nama = bacaTeks("Nama pasien");
        int umur = bacaInt("Umur (0-120)");
        Pasien p = klinik.daftarPasienBaru(nama, umur);
        System.out.println("Pasien terdaftar: " + p);
    }

    private static void menuTambahDokter(Klinik klinik) {
        String nama = bacaTeks("Nama dokter");
        String spesialis = bacaTeks("Spesialis");
        Dokter d = klinik.tambahDokter(nama, spesialis);
        System.out.println("Dokter ditambahkan: " + d);
    }

    private static void menuMasukAntrean(Klinik klinik) {
        String id = bacaTeks("ID pasien (mis. P001)");
        klinik.masukAntrean(id);
        System.out.println("Pasien masuk antrean. Antrean: " + klinik.infoAntrean());
    }

    private static void menuCari(Klinik klinik) {
        String jenis = bacaTeks("Cari apa? (1 = pasien, 2 = dokter)");
        String id = bacaTeks("Masukkan ID");
        if (jenis.equals("1")) {
            Pasien p = klinik.cariPasien(id);
            if (p == null) {
                System.out.println("Pasien dengan ID " + id + " tidak ditemukan.");
            } else {
                System.out.println("Ditemukan : " + p);
                System.out.println("Kategori  : " + p.getKategoriUsia()
                    + (p.perluPendamping() ? " (perlu pendamping)" : ""));
                System.out.println("Antrean   : " + p.getStatusAntrean());
            }
        } else if (jenis.equals("2")) {
            Dokter d = klinik.cariDokter(id);
            if (d == null) {
                System.out.println("Dokter dengan ID " + id + " tidak ditemukan.");
            } else {
                System.out.println("Ditemukan : " + d);
                boolean ada = d.cekTersedia(
                    klinik.hitungKunjungan(d, klinik.getTanggalPraktik()),
                    Klinik.MAKS_KUNJUNGAN_PER_DOKTER_PER_HARI);
                System.out.println("Tersedia hari ini : " + (ada ? "ya" : "tidak"));
            }
        } else {
            System.out.println("Pilihan harus 1 atau 2.");
        }
    }

    private static void menuLayani(Klinik klinik) {
        if (klinik.antreanKosong()) {
            System.out.println("Antrean kosong.");
            return;
        }
        System.out.println("Pasien terdepan: " + klinik.pasienTerdepan());
        menuLihatDokter(klinik);
        String idDokter = bacaTeks("ID dokter (mis. D001)");
        String keluhan = bacaTeks("Keluhan");
        String diagnosa = bacaTeks("Diagnosa (boleh kosong)");
        Kunjungan k = klinik.layani(idDokter, keluhan, diagnosa);
        System.out.println("Kunjungan dicatat: " + k);
    }

    private static void menuLaporan(Klinik klinik) {
        ArrayList<Kunjungan> riwayat = klinik.getRiwayat();
        int selesai = 0;
        for (Kunjungan k : riwayat) {
            if (k.getStatus().equals(Kunjungan.SELESAI)) selesai++;
        }

        System.out.println("=== LAPORAN " + klinik.getNama().toUpperCase() + " ===");
        System.out.println("Jumlah pasien      : " + klinik.getJumlahPasien());
        System.out.println("Jumlah dokter      : " + klinik.getJumlahDokter());
        System.out.println("Jumlah kunjungan   : " + klinik.getJumlahKunjungan()
            + " (selesai: " + selesai + ", diproses: " + (riwayat.size() - selesai) + ")");
        System.out.println("Pasien di antrean  : " + klinik.ukuranAntrean());
        System.out.println("Kunjungan per dokter:");
        for (Dokter d : klinik.getDaftarDokter()) {
            System.out.println("  " + d.getId() + " " + d.getNama()
                + " : " + klinik.totalKunjungan(d) + " kunjungan");
        }
    }

    private static void menuLihatAntrean(Klinik klinik) {
        System.out.println("Isi antrean : " + klinik.infoAntrean());
        System.out.println("size        : " + klinik.ukuranAntrean() + " / " + klinik.getKapasitasAntrean());
        System.out.println("isEmpty     : " + klinik.antreanKosong());
        System.out.println("isFull      : " + klinik.antreanPenuh());
        if (!klinik.antreanKosong()) {
            System.out.println("front       : " + klinik.pasienTerdepan());
        }
    }

    private static void menuLihatDokter(Klinik klinik) {
        Dokter[] dokter = klinik.getDaftarDokter();
        System.out.println("Dokter (maks " + Klinik.MAKS_KUNJUNGAN_PER_DOKTER_PER_HARI
            + " kunjungan/hari, tanggal " + klinik.getTanggalPraktik() + "):");
        for (Dokter d : dokter) {
            int terpakai = klinik.hitungKunjungan(d, klinik.getTanggalPraktik());
            System.out.println("  " + d + " | kunjungan: " + terpakai
                + " | sisa kuota: " + klinik.sisaKuota(d));
        }
    }

    private static void menuRiwayat(Klinik klinik) {
        ArrayList<Kunjungan> riwayat = klinik.getRiwayat();
        if (riwayat.isEmpty()) {
            System.out.println("Belum ada kunjungan.");
            return;
        }
        for (Kunjungan k : riwayat) {
            System.out.println("  " + k);
        }
    }

    private static void menuLihatPasien(Klinik klinik) {
        ArrayList<Pasien> daftar = klinik.getDaftarPasien();
        if (daftar.isEmpty()) {
            System.out.println("Belum ada pasien.");
            return;
        }
        for (Pasien p : daftar) {
            System.out.println("  " + p + (p.isDalamAntrean() ? "  [dalam antrean]" : ""));
        }
    }

    private static void menuUbahTanggal(Klinik klinik) {
        String teks = bacaTeks("Tanggal praktik (yyyy-MM-dd)");
        klinik.setTanggalPraktik(LocalDate.parse(teks));
        System.out.println("Tanggal praktik: " + klinik.getTanggalPraktik());
    }


    private static void menuUbahData(Klinik klinik) {
        System.out.println("  1. Ubah umur pasien");
        System.out.println("  2. Ubah spesialis dokter");
        System.out.println("  3. Aktifkan / nonaktifkan dokter");
        System.out.println("  4. Ubah diagnosa kunjungan");
        System.out.println("  5. Tandai kunjungan selesai");
        String pilih = bacaTeks("Pilih");

        if (pilih.equals("1")) {
            Pasien p = klinik.cariPasien(bacaTeks("ID pasien"));
            if (p == null) {
                System.out.println("Pasien tidak ditemukan.");
                return;
            }
            p.setUmur(bacaInt("Umur baru (0-120)"));
            System.out.println("Data diubah: " + p);
        } else if (pilih.equals("2") || pilih.equals("3")) {
            Dokter d = klinik.cariDokter(bacaTeks("ID dokter"));
            if (d == null) {
                System.out.println("Dokter tidak ditemukan.");
                return;
            }
            if (pilih.equals("2")) {
                d.setSpesialis(bacaTeks("Spesialis baru"));
            } else if (d.isAktif()) {
                d.nonaktifkan();
            } else {
                d.aktifkan();
            }
            System.out.println("Data diubah: " + d);
        } else if (pilih.equals("4") || pilih.equals("5")) {
            Kunjungan k = klinik.cariKunjungan(bacaTeks("Nomor kunjungan (mis. K001)"));
            if (k == null) {
                System.out.println("Kunjungan tidak ditemukan.");
                return;
            }
            if (pilih.equals("4")) {
                k.setDiagnosa(bacaTeks("Diagnosa baru"));
            } else {
                k.tandaiSelesai();
            }
            System.out.println("Data diubah: " + k);
        } else {
            System.out.println("Pilihan tidak dikenal.");
        }
    }

    
    private static String bacaTeks(String label) {
        System.out.print(label + ": ");
        return in.nextLine().trim();
    }

    private static int bacaInt(String label) {
        String teks = bacaTeks(label);
        try {
            return Integer.parseInt(teks);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input harus berupa angka bulat");
        }
    }
}
