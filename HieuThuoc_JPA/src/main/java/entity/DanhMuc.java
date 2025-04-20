package entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DanhMuc")
public class DanhMuc implements Serializable {

    @Id
    @Column(name = "ma_danh_muc",columnDefinition = "varchar(45)")
    private String id;

    @Column(columnDefinition = "varchar(45)",name = "ten_danh_muc", nullable = false)
    private String ten;

    @Column(columnDefinition = "varchar(45)",name = "vi_tri_ke",nullable = false)
    private String viTriKe;

    @ToString.Exclude
    @OneToMany( mappedBy = "danhMuc")
    private Set<Thuoc> thuoc;

    public DanhMuc (String id, String ten, String viTriKe){
        this.id = id;
        this.ten = ten;
        this.viTriKe = viTriKe;
    }
}
