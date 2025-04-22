package entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@IdClass(ChiTietPhieuDatThuoc.ChiTietPhieuDatThuocID.class)
@Table(name = "ChiTietPhieuDatThuoc")
public class ChiTietPhieuDatThuoc implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "ma_phieu_dat_thuoc", nullable = false)
    @EqualsAndHashCode.Include
    private PhieuDatThuoc phieuDatThuoc;
    @Id
    @ManyToOne
    @JoinColumn(name = "ma_thuoc", nullable = false)
    @EqualsAndHashCode.Include
    private Thuoc thuoc;
    @Column(name = "so_luong", nullable = false)
    private int soLuong;
    @Column(name = "don_gia", nullable = false)
    private double donGia;

    public ChiTietPhieuDatThuoc(PhieuDatThuoc phieudat, Thuoc thuoc, int soLuong, double donGia) {
        this.phieuDatThuoc = phieuDatThuoc;
        this.thuoc = thuoc;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public ChiTietPhieuDatThuoc() {

    }

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietPhieuDatThuocID implements Serializable {
        private PhieuDatThuoc phieuDatThuoc;
        private Thuoc thuoc;
    }
}
