package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false, onlyExplicitlyIncluded=true)
@Entity
@IdClass(ChiTietPhieuNhapThuoc.ChiTietPhieuNhapThuocID.class)
@Table(name = "ChiTietPhieuNhapThuoc")
public class ChiTietPhieuNhapThuoc {
    @Id
    @ManyToOne
    @JoinColumn(name = "ma_phieu_nhap_thuoc", nullable = false)
    @EqualsAndHashCode.Include
    private PhieuNhapThuoc phieuNhapThuoc;
    @Id
    @ManyToOne
    @JoinColumn(name = "ma_thuoc", nullable = false)
    @EqualsAndHashCode.Include
    private Thuoc thuoc;
    @Column(name = "so_luong", nullable = false)
    private int soLuong;
    @Column(name = "don_gia", nullable = false)
    private double donGia;

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietPhieuNhapThuocID implements Serializable {
        private PhieuNhapThuoc phieuNhapThuoc;
        private Thuoc thuoc;
    }
}
