package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@ToString
@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {

    @Id
    @Column(name = "ma_khuyen_mai",columnDefinition = "varchar(45)")
    private String id;

    @Column(columnDefinition = "varchar(45)", name = "ten_khuyen_mai",nullable = false)
    private String ten;

    @Column(columnDefinition = "DECIMAL(10,2)",name = "phan_tram_giam_gia", nullable = false)
    private double phanTramGiamGia;

    @Column(columnDefinition = "DATE",name = "thoi_gian_bat_dau", nullable = false)
    private LocalDate thoiGianBatDau;

    @Column(columnDefinition = "DATE",name = "thoi_gian_ket_thuc", nullable = false)
    private LocalDate thoiGianKetThuc;


    @ToString.Exclude
    @OneToMany( mappedBy = "khuyenMai")
    private Set<Thuoc> thuoc;


}
