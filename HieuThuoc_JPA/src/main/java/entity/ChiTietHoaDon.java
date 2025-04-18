package entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@IdClass(ChiTietHoaDon.ChiTietHoaDonID.class)
@Table(name = "ChiTietHoaDon")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class ChiTietHoaDon {
    @Id
    @ManyToOne
    @JoinColumn(name = "ma_hoa_don", nullable = false)
    @EqualsAndHashCode.Include
    private HoaDon hoaDon;

    @Id
    @ManyToOne
    @JoinColumn(name = "ma_thuoc", nullable = false)
    @EqualsAndHashCode.Include
    private Thuoc thuoc;

    @Column(name = "so_luong", nullable = false)
    private int soLuong;

    @Column(name = "don_gia", nullable = false)
    private double donGia;

    public ChiTietHoaDon(HoaDon hoaDon, Thuoc thuoc, int soLuong, double donGia) {
        this.hoaDon = hoaDon;
        this.thuoc = thuoc;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public ChiTietHoaDon() {

    }

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietHoaDonID {
        private HoaDon hoaDon;
        private Thuoc thuoc;
    }
}
