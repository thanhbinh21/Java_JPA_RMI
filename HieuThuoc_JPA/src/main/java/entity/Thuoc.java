package entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Thuoc")
public class Thuoc implements Serializable {
    @Id
    @Column(name = "ma_thuoc",columnDefinition = "varchar(45)")
    private String id;

    @Column(columnDefinition = "varchar(45)",name = "ten_thuoc",  nullable = false)
    private String ten;

    @Column(columnDefinition = "varchar(45)",name = "don_vi_tinh",nullable = false)
    private String donViTinh;

    @Column(columnDefinition = "varchar(45)",name = "thanh_phan",nullable = false)
    private String thanhPhan;

    @Column(columnDefinition = "INT",name = "so_luong_ton", nullable = false)
    private int soLuongTon;



    @Column(columnDefinition = "double",name = "don_gia", nullable = false)
    private double donGia;

    @Column(columnDefinition = "varchar(145)",name = "hinh_anh")
    private String hinhAnh;
    @Column(name = "han_su_dung")
    private Date hanSuDung;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ma_nha_san_xuat")
    private NhaSanXuat nhaSanXuat;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ma_danh_muc")
    private DanhMuc danhMuc;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ma_khuyen_mai")
    private KhuyenMai khuyenMai;

    @OneToMany(mappedBy = "thuoc", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietHoaDon> chiTietHoaDons;

//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "phieuDatThuoc_id", nullable = false)
//    private Set<PhieuDatThuoc> phieuDatThuoc;
//
//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "hoaDon_id", nullable = false)
//    private Set<HoaDon> hoaDon;
//
//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "phieuNhap_id", nullable = false)
//    private Set<PhieuNhap> phieuNhap;
}
