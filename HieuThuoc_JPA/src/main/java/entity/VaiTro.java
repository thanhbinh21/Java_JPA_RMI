package entity;

import jakarta.persistence.*;
import lombok.Data;

import javax.xml.namespace.QName;
import java.util.Set;

@Data
@Entity
@Table(name = "VaiTro")
public class VaiTro {
    @Id
    @Column(name = "ma_vai_tro",columnDefinition = "varchar(45)", unique = true,nullable = false)
    private String id;
    @Column(name = "ten_vai_tro",columnDefinition = "varchar(45)")
    private String tenVaiTro;
    @Column(name = "tai_khoan")
    @OneToMany(mappedBy = "vaiTro")
    private Set<TaiKhoan> taiKhoan;



}
