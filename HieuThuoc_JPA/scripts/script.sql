-- Chèn dữ liệu vào bảng nhanvien
INSERT INTO nhanvien (ma_nhan_vien, ho_ten, so_dien_thoai, gioi_tinh, nam_sinh, ngay_vao_lam)
VALUES
    ('ADMIN', N'Nguyễn Thanh Hùng', '0000000000', 0, 1999, '2024-01-01'),
    ('NV001', N'Nguyễn Thanh Bình', '0906765871', 0, 2003, '2024-02-12'),
    ('NV002', N'Vũ Nương', '0931265687', 1, 2003, '2024-02-15'),
    ('NV003', N'Chí Phèo', '0967566712', 0, 2003, '2024-02-20');

-- Chèn dữ liệu vào bảng vaitro
INSERT INTO vaitro (ma_vai_tro, ten_vai_tro)
VALUES
    ('admin', N'Admin'),
    ('nvbt', N'Nhân viên Bán Thuốc');

-- Chèn dữ liệu vào bảng taikhoan
INSERT INTO taikhoan (ma_tai_khoan, password, ma_nhan_vien, vai_tro)
VALUES
    ('ADMIN', '123', 'ADMIN', 'admin'),
    ('thanhbinh', '000', 'NV001', 'nvbt');

-- Chèn dữ liệu vào bảng khachhang
INSERT INTO khachhang (ma_khach_hang, ho_ten, so_dien_thoai, gioi_tinh, ngay_tham_gia)
VALUES
    ('KH001', N'Nguyễn Văn Hùng', '0906765871', 0, '2024-02-15'),
    ('KH002', N'Nguyễn Thị Lan', '0931265687', 1, '2024-02-15'),
    ('KH003', N'Lê Đức Anh', '0967566712', 0, '2024-02-15'),
    ('KH004', N'Trần Mai Hương', '0987654321', 1, '2024-02-15'),
    ('KH005', N'Phạm Xuân Phong', '0912345678', 0, '2024-02-15'),
    ('KH006', N'Lê Thị Linh', '0956789012', 1, '2024-02-15'),
    ('KH007', N'Hồ Ngọc Minh', '0923456789', 0, '2024-02-15'),
    ('KH008', N'Võ Thị Hải Yến', '0945678901', 1, '2024-02-15'),
    ('KH009', N'Phạm Thị Anh', '0978901234', 1, '2024-02-15'),
    ('KH010', N'Hoàng Hữu Đức', '0912345678', 0, '2024-02-15');

-- Chèn dữ liệu vào bảng khuyenmai
INSERT INTO khuyenmai (ma_khuyen_mai, ten_khuyen_mai, phan_tram_giam_gia, thoi_gian_bat_dau, thoi_gian_ket_thuc)
VALUES
    ('KM001', N'cận date', 0.5, '2025-02-15', '2025-03-15'),
    ('KM002', N'Sỉ', 0.1, '2025-02-15', '2025-03-15'),
    ('NoKm', N'Không KM', 0.0, '0000-00-00', '0000-00-00');

-- Chèn dữ liệu vào bảng nhasanxuat
INSERT INTO nhasanxuat (ma_nha_san_xuat, ten_nha_san_xuat)
VALUES
    ('NSX001', N'Việt Nam'),
    ('NSX002', N'Mỹ'),
    ('NSX003', N'Pháp'),
    ('NSX004', N'Nhật Bản');

-- Chèn dữ liệu vào bảng danhmuc
INSERT INTO danhmuc (ma_danh_muc, ten_danh_muc, vi_tri_ke)
VALUES
    ('DM001', N'Hệ tim mạch & tạo máu', N'Kệ A'),
    ('DM002', N'Hệ tiêu hóa & gan mật', N'Kệ B'),
    ('DM003', N'Thuốc giảm đau', N'Kệ C');

INSERT INTO thuoc (ma_thuoc, ten_thuoc, don_vi_tinh, hinh_anh, thanh_phan, ma_khuyen_mai, ma_danh_muc, ma_nha_san_xuat, so_luong_ton, don_gia, han_su_dung)
VALUES
    ('T001', N'Hapacol 650 DHG', N'Hộp', 'hapacol_650_extra_dhg.png', N'Paracetamol', 'NoKm', 'DM003', 'NSX001', 1021, 25000, '2026-02-15 00:00:00.000000'),
    ('T002', N'Bột pha hỗn dịch Smecta', N'Gói', 'bot-pha-hon-dich-uong-smecta.jpg', N'Diosmectite', 'NoKm', 'DM002', 'NSX003', 1021, 4000, '2026-05-21 00:00:00.000000'),
    ('T003', N'Siro C.C Life', N'Chai', 'C.c-Life-100MgChai.jpg', N'Vitamin C', 'NoKm', 'DM002', 'NSX001', 1032, 30000, '2026-03-01 00:00:00.000000'),
    ('T004', N'Panadol Extra đỏ', N'Hộp', 'Panadol-Extra.png', N'Caffeine, Paracetamol', 'NoKm', 'DM003', 'NSX002', 1034, 250000, '2026-08-07 00:00:00.000000'),
    ('T005', N'Viên sủi Vitatrum C BRV', N'Hộp', 'vitatrum-c-brv.png', N'Thành phần phức tạp', 'NoKm', 'DM002', 'NSX002', 1076, 24000, '2027-12-31 00:00:00.000000'),
    ('T006', N'Bổ Gan Trường Phúc', N'Hộp', 'bo-gan-tuong-phu.jpg', N'Thảo dược', 'NoKm', 'DM002', 'NSX001', 1034, 95000, '2026-02-15 00:00:00.000000'),
    ('T007', N'Bài Thạch Trường Phúc', N'Hộp', 'bai-trang-truong-phuc.jpg', N'Xa tiền tử...', 'NoKm', 'DM002', 'NSX001', 1076, 95000, '2026-02-10 00:00:00.000000'),
    ('T008', N'Đại Tràng Trường Phúc', N'Hộp', 'dai-trang-truong-phuc.jpg', N'Hoàng liên...', 'NoKm', 'DM002', 'NSX001', 1021, 105000, '2026-09-03 00:00:00.000000'),
    ('T009', N'Ninh Tâm Vương Hồng Bàng', N'Hộp', 'ninh-tam-vuong-hong-bang.png', N'L-Carnitine...', 'KM001', 'DM001', 'NSX004', 1054, 180000, '2026-08-15 00:00:00.000000'),
    ('T010', N'Hapacol 650 DHG', N'Hộp', 'hapacol_650_extra_dhg.png', N'Paracetamol', 'NoKm', 'DM003', 'NSX001', 1021, 25000, '2024-02-15 00:00:00.000000'),
    ('T012', N'Bột pha hỗn dịch Smecta', N'Gói', 'bot-pha-hon-dich-uong-smecta.jpg', N'Diosmectite', 'NoKm', 'DM002', 'NSX003', 1021, 4000, '2023-05-21 00:00:00.000000'),
    ('T013', N'Siro C.C Life', N'Chai', 'C.c-Life-100MgChai.jpg', N'Vitamin C', 'NoKm', 'DM002', 'NSX001', 1032, 30000, '2024-03-01 00:00:00.000000');


-- Chèn dữ liệu vào bảng phieudatthuoc
INSERT INTO phieudatthuoc (ma_phieu_dat_thuoc, thoi_gian, ma_khach_hang, ma_nhan_vien, trang_thai)
VALUES
    ('PDT001', '2024-04-01 14:21:13', 'KH001', 'NV002', 1),
    ('PDT002', '2024-04-01 14:21:13', 'KH001', 'NV001', 0);

-- Chèn dữ liệu vào bảng chitietphieudatthuoc
INSERT INTO chitietphieudatthuoc (ma_phieu_dat_thuoc, ma_thuoc, so_luong, don_gia)
VALUES
    ('PDT001', 'T002', 2, 105000),
    ('PDT002', 'T003', 3, 30000);

-- Chèn dữ liệu vào bảng hoadon (đã bỏ trường thuế)
INSERT INTO hoadon (ma_hoa_don, thoi_gian, ma_nhan_vien, ma_khach_hang, trang_thai)
VALUES
    ('HD001', '2024-04-01 14:21:13', 'NV001', 'KH002', 1),
    ('HD002', '2024-04-02 16:12:51', 'ADMIN', 'KH001', 1);

-- Chèn dữ liệu vào bảng chitiethoadon
INSERT INTO chitiethoadon (ma_hoa_don, ma_thuoc, so_luong, don_gia)
VALUES
    ('HD001', 'T001', 1, 105000),
    ('HD002', 'T001', 1, 180000),
    ('HD002', 'T002', 3, 30000);

-- Chèn dữ liệu vào bảng nhacungcap
INSERT INTO nhacungcap (ma_nha_cung_cap, ten, sdt, dia_chi)
VALUES
    ('NCC001', N'Công ty Cổ phần Dược phẩm An Khang', '0283820618', N'282-284 Trần Hưng Đạo, Quận 1, TP.HCM'),
    ('NCC002', N'Công ty Cổ phần Dược phẩm Pharmacity', '0243825353', N'426 Võ Văn Ngân, Quận Thủ Đức, TP.HCM');

-- Chèn dữ liệu vào bảng phieunhapthuoc
INSERT INTO phieunhapthuoc (ma_phieu_nhap_thuoc, thoi_gian, ma_nhan_vien, ma_nha_cung_cap)
VALUES
    ('PN001', '2024-03-04 13:12:42', 'NV001', 'NCC002'),
    ('PN002', '2024-03-05 11:31:26', 'NV002', 'NCC002');

-- Chèn tiếp dữ liệu vào bảng thuoc (các thuốc hết hạn)

-- Chèn dữ liệu vào bảng phieunhapthuoc (tiếp theo)
INSERT INTO phieunhapthuoc (ma_phieu_nhap_thuoc, thoi_gian, ma_nhan_vien, ma_nha_cung_cap)
VALUES
    ('PN003', '2024-03-06 07:18:32', 'NV001', 'NCC002'),
    ('PN004', '2024-03-07 10:26:21', 'NV001', 'NCC001'),
    ('PN005', '2024-03-11 08:35:37', 'NV002', 'NCC002');

-- Chèn dữ liệu vào bảng chitietphieunhapthuoc (tiếp theo)
INSERT INTO chitietphieunhapthuoc (ma_phieu_nhap_thuoc, ma_thuoc, so_luong, don_gia)
VALUES
    ('PN003', 'T001', 200, 30000),
    ('PN004', 'T001', 500, 4000),
    ('PN004', 'T002', 300, 250000);

-- Chèn dữ liệu vào bảng nhacungcap (tiếp theo)
INSERT INTO nhacungcap (ma_nha_cung_cap, ten, sdt, dia_chi)
VALUES
    ('NCC003', N'Hệ thống nhà thuốc ECO', '0283689339', N'336 Phan Văn Trị, Quận Bình Thạnh, TP.HCM'),
    ('NCC004', N'Công ty Dược phẩm Phano', '0243574133', N'286 P. Xã Đàn, Đống Đa, Hà Nội'),
    ('NCC005', N'Công ty Dược phẩm Trung ương 2', '0243825535', '138B Đội Cấn, Ba Đình, Hà Nội'),
    ('NCC006', N'Công ty Dược phẩm VCP', '0285413833', N'780 Nguyễn Văn Linh, Quận 7, TP.HCM');


