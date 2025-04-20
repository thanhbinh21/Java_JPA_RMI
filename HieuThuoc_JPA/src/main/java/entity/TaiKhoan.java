package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan implements Serializable {
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
