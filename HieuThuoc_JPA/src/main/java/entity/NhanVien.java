package entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Check;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "NhanVien")
public class NhanVien implements Serializable {
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

    public NhanVien(String id, String hoTen, String soDienThoai, boolean gioiTinh, int namSinh, LocalDate ngayVaoLam) {
        this.id = id;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
        this.namSinh = namSinh;
        this.ngayVaoLam = ngayVaoLam;
    }

    @OneToMany(mappedBy = "nhanVien")
    @ToString.Exclude
    private Set<HoaDon> hoaDons;

    @OneToOne(mappedBy = "nhanVien")
    @ToString.Exclude
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy = "nhanVien", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<PhieuNhapThuoc> phieuNhapThuocs;
}

