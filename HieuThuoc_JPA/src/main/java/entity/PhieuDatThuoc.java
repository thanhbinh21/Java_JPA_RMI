package entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "PhieuDatThuoc")
public class PhieuDatThuoc {
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
    @OneToMany(mappedBy = "phieuDatThuoc", cascade = CascadeType.ALL)
    private Set<ChiTietPhieuDatThuoc> chiTietPhieuDatThuocs;
}
