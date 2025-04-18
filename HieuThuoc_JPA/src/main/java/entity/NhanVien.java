package entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "NhanVien")
public class NhanVien {
    @Id
    @Column(name = "ma_nhan_vien",columnDefinition = "nvarchar(45)",nullable = false)
    @EqualsAndHashCode.Include
    private String id;
    @Column(name = "ho_ten",columnDefinition = "nvarchar(100)",nullable = false)
    private String hoTen;
    @Column(name = "gioi_tinh",nullable = false)
    private boolean gioiTinh;
    @Column(name = "nam_sinh",nullable = false)
    private int namSinh;
    @Column(name = "ngay_vao_lam",nullable = false)
    private LocalDate ngayVaoLam;

    @Column(name = "so_dien_thoai",columnDefinition = "nvarchar(15)",nullable = false)
    private String soDienThoai;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HoaDon> hoaDons;

    @OneToMany(mappedBy = "nhanVien", fetch = FetchType.LAZY)
    private Set<PhieuNhapThuoc> phieuNhapThuocs;
}

