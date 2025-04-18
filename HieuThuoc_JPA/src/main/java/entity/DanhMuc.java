package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Setter
@Getter
@ToString
@Entity
@Table(name = "DanhMuc")
public class DanhMuc {

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
}
