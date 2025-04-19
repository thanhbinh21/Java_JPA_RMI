package entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper=false, onlyExplicitlyIncluded=true)
@Entity
@Table(name = "PhieuNhapThuoc")
public class PhieuNhapThuoc implements Serializable {
    @Id
    @Column(name = "ma_phieu_nhap_thuoc", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String id;
    @Column(name = "thoi_gian")
    private LocalDateTime thoiGian;

    @ManyToOne
    @JoinColumn(name = "ma_nhan_vien")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "ma_nha_cung_cap")
    private NhaCungCap nhaCungCap;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "phieuNhapThuoc")
    private Set<ChiTietPhieuNhapThuoc> chiTietPhieuNhapThuocs;



}
