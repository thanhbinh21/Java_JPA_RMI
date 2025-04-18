package entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
    @Id
    @Column(name ="ma_tai_khoan", unique = true,nullable = false)
    private String id;
    private String password;
    @OneToOne
    @JoinColumn(name = "ma_nhan_vien")
    private NhanVien nhanVien;
    @ManyToOne
    @JoinColumn(name = "vai_tro" )
    private VaiTro vaiTro;

}
