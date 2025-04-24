package entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"khachHang", "chiTietHoaDons"}) // Exclude problematic references
@Entity
@Table(name = "HoaDon")
public class HoaDon implements Serializable {
    @Id
    @Column(name = "ma_hoa_don",columnDefinition = "nvarchar(45)",nullable = false)
    @EqualsAndHashCode.Include
    private String id;
    @Column(name = "thoi_gian",nullable = false)
    private Timestamp thoiGian;
    @Column(name = "trang_thai",nullable = false)
    private boolean trangThai;

    @ManyToOne
    @JoinColumn(name = "ma_nhan_vien", nullable = false) // Cột khóa ngoại
    private NhanVien nhanVien;
    @ManyToOne
    @JoinColumn(name = "ma_khach_hang", nullable = false)
    private KhachHang khachHang;
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietHoaDon> chiTietHoaDons;

    public HoaDon(String id) {
        this.id = id;
    }

    public HoaDon() {

    }

    public HoaDon(String idHD, Timestamp thoiGian, NhanVien nhanVien, KhachHang khachHang) {
        this.id = idHD;
        this.thoiGian = thoiGian;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.trangThai = true;
    }


}
