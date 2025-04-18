package entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "KhachHang")
@Data
public class KhachHang {
    @Id
    @Column(name = "ma_khach_hang", columnDefinition = "varchar(45)")
    @EqualsAndHashCode.Include
    private String id;
    @Column(name = "ho_ten", nullable = false)
    private String hoTen;
    @Column(name = "so_dien_thoai", nullable = false)
    private String soDienThoai;
    @Column(name = "gioi_tinh", nullable = false)
    private boolean gioiTinh;
    @Column(name = "ngay_tham_gia", nullable = false)
    private LocalDate ngayThamGia;
    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HoaDon> hoaDons;
    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhieuDatThuoc> phieuDatThuocs;
}