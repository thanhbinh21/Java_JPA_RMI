package entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"chiTietPhieuDatThuocs"})
@Entity
@Table(name = "PhieuDatThuoc")
public class PhieuDatThuoc implements Serializable {
    @Id
    @Column(name = "ma_phieu_dat_thuoc", columnDefinition = "varchar(45)")
    @EqualsAndHashCode.Include
    private String id;
    @Column(name = "thoi_gian", nullable = false)
    private Timestamp thoiGian;
    @ManyToOne
    @JoinColumn(name = "ma_khach_hang", nullable = false)
    private KhachHang khachHang;
    @ManyToOne
    @JoinColumn(name = "ma_nhan_vien", nullable = false)
    private NhanVien nhanVien;
    @Column(name = "trang_Thai", nullable = false)
    private boolean trangThai;

    public PhieuDatThuoc(String idHD, Timestamp thoiGian, NhanVien nhanVien, KhachHang khachHang, boolean b) {
        this.id = idHD;
        this.thoiGian = thoiGian;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.trangThai = b;
    }
    public PhieuDatThuoc() {

    }
    @OneToMany(mappedBy = "phieuDatThuoc", cascade = CascadeType.ALL)
    private Set<ChiTietPhieuDatThuoc> chiTietPhieuDatThuocs;



}
