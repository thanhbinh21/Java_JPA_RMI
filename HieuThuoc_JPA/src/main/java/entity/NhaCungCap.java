package entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "NhaCungCap")
public class NhaCungCap {
    @Id
    @Column(name = "ma_nha_cung_cap")
    private String id;
    private String ten;
    @Column(name = "dia_chi")
    private String diaChi;
    private String sdt;
    @OneToMany(mappedBy = "nhaCungCap", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhieuNhapThuoc> phieuNhapThuocs;

}
