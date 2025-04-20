package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VaiTro", uniqueConstraints = @UniqueConstraint(columnNames = "ten_vai_tro"))
public class VaiTro implements Serializable {
    @Id
    @Column(name = "ma_vai_tro",columnDefinition = "varchar(45)", unique = true,nullable = false)
    private String id;
    @Column(name = "ten_vai_tro",columnDefinition = "varchar(45)")
    private String tenVaiTro;
    @Column(name = "tai_khoan")
    @OneToMany(mappedBy = "vaiTro")
    @ToString.Exclude
    private Set<TaiKhoan> taiKhoan;

    public VaiTro(String id, String tenVaiTro) {
        this.id = id;
        this.tenVaiTro = tenVaiTro;
    }

}
