INSERT INTO loai_phong (ma_loai, ten_loai, gia, so_nguoi_toi_da) VALUES
                                                                     ('L', '--Tất cả--', 0),
                                                                     ('L1', 'Phòng một người', 1200000 ,1),
                                                                     ('L2', 'Phòng hai người', 1500000, 2);

INSERT INTO phong (so_phong, trang_thai, ma_loai, ghi_chu) VALUES
                                                               ('101', 'Đang Thuê', 'L1', ''),
                                                               ('102', 'Đang Thuê', 'L2', ''),
                                                               ('103', 'Trống', 'L1', ''),
                                                               ('104', 'Đang Thuê', 'L2', ''),
                                                               ('105', 'Đang Thuê', 'L2', ''),
                                                               ('106', 'Trống', 'L1', ''),
                                                               ('107', 'Trống', 'L1', ''),
                                                               ('110', 'Trống', 'L2', ''),
                                                               ('111', 'Trống', 'L1', NULL),
                                                               ('112', 'Trống', 'L2', ''),
                                                               ('114', 'Đang sửa chữa', 'L2', NULL);

INSERT INTO hop_dong_thue
(id, ngay_thue, han_thue, so_phong, gia_phong, trang_thai, ngay_tao_hop_dong)
VALUES
    ('HDT87945','2025-01-01 15:18:07','2027-01-01 15:18:07','102',1500000,'Đang Hiệu Lực','2025-01-01 15:18:07'),
    ('HDT89808','2025-01-01 15:20:46','2027-01-01 15:20:46','105',1500000,'Đang Hiệu Lực','2025-01-01 15:20:46'),
    ('HDT9139','2025-01-01 15:16:01','2027-01-01 15:16:01','101',1200000,'Đang Hiệu Lực','2025-01-01 15:16:01'),
    ('HDT93011','2025-01-01 15:18:45','2027-01-01 15:18:45','104',1500000,'Đang Hiệu Lực','2025-01-01 15:18:45');

INSERT INTO khach_thue
(ma_khach, ho_ten, ngay_sinh, cccd, so_dien_thoai, que_quan, trang_thai, email)
VALUES
    (1,'Nguyễn Minh Nhựt','2004-05-30','082204002265','0385180001','', 'Đang Thuê','klemin3005@gmail.com'),
    (2,'Nguyễn Minh Thông','2003-05-30','082204002262','0385180002','', 'Đang Thuê',''),
    (3,'Nguyễn Thị Ánh Tuyết','2003-05-01','082204002262','0385180003','', 'Đang Thuê',''),
    (4,'Nguyễn Thị Huyền Trân','2003-05-09','082204002262','0385180003','', 'Đang Thuê',''),
    (5,'Nguyễn Thị Trúc Linh','2006-01-30','0822342343','0324832433','', 'Chưa Thuê',''),
    (6,'Nguyễn Anh Tuấn','2001-03-29','3543565433','0895678832','', 'Chưa Thuê',''),
    (7,'Nguyễn Thị Thùy Trang','2003-06-29','3543565433','0895672832','', 'Chưa Thuê',''),
    (8,'Dương Hoàng Khải','2003-03-12','08220838272','0324324333','', 'Chưa Thuê',''),
    (9,'Lê Thúy Hạnh','2003-03-12','08220838272','0324324339','', 'Chưa Thuê',''),
    (10,'Nguyễn Hoàng Huy','2004-03-29','098877642224','0325637721','Hậu Giang','Đang Thuê','boedau0506@gmail.com'),
    (11,'Nguyễn Thanh Duy','2004-03-23','098877642224','0325632221','Hậu Giang','Đang Thuê','duy0303nt@gmail.com'),
    (12,'Nguyễn Ngọc Nhi','2004-03-23','098776422224','0325642701','Hậu Giang','Đang Thuê','ngocnhii18055@gmail.com');

INSERT INTO chi_tiet_hop_dong (id_hop_dong, ma_khach, vai_tro) VALUES
                                                                   ('HDT9139',1,'Khách Chính'),
                                                                   ('HDT87945',11,'Khách Chính'),
                                                                   ('HDT87945',2,'Khách Phụ'),
                                                                   ('HDT93011',12,'Khách Chính'),
                                                                   ('HDT93011',3,'Khách Phụ'),
                                                                   ('HDT89808',10,'Khách Chính'),
                                                                   ('HDT89808',4,'Khách Phụ');

INSERT INTO dich_vu_phong
(id, ma_hop_dong, so_phong, ki,
 so_dien_cu, so_dien_moi, so_nuoc_cu, so_nuoc_moi,
 gia_dien, gia_nuoc, ngay_tao, tien_mang, trang_thai)
VALUES
    (8364,'HDT9139','101','3-2025',21,31,3,6,4000,12000,'2025-03-04 00:00:00',30000,'Chờ Lập Hóa Đơn'),
    (23343,'HDT89808','105','2-2025',10,24,2,6,4000,12000,'2025-02-04 00:00:00',30000,'Đã Lập Hóa Đơn'),
    (26787,'HDT87945','102','2-2025',10,19,2,4,4000,12000,'2025-02-04 00:00:00',30000,'Đã Lập Hóa Đơn'),
    (33931,'HDT89808','105','3-2025',24,34,6,7,4000,12000,'2025-03-04 00:00:00',30000,'Chờ Lập Hóa Đơn'),
    (36541,'HDT93011','104','3-2025',24,32,5,7,4000,12000,'2025-03-04 00:00:00',30000,'Chờ Lập Hóa Đơn'),
    (44265,'HDT87945','102','3-2025',19,31,4,6,4000,12000,'2025-03-04 00:00:00',30000,'Chờ Lập Hóa Đơn');

INSERT INTO hoa_don
(id_hoa_don, id_dich_vu, so_dien, tien_dien,
 so_nuoc, tien_nuoc, phi_khac, tong_tien,
 ngay_lap_hoa_don, trang_thai, ghi_chu, gia_phong)
VALUES
    ('HD293101',47648,10,40000,2,24000,0,1294000,'2025-01-05 00:00:00','Đã Thanh Toán','',1200000),
    ('HD29310179',84023,11,44000,1,12000,0,1286000,'2025-02-28 00:00:00','Đã Thanh Toán','',1200000),
    ('HD293102',65196,10,40000,2,24000,0,1594000,'2025-02-28 00:00:00','Đã Thanh Toán','',1500000);


INSERT INTO dich_vu (ten_dich_vu, gia) VALUES
                                           ('Điện',4000),
                                           ('Nước',12000),
                                           ('Wifi',30000);

INSERT INTO config
(ten_nha_tro, so_tai_khoan, ten_tai_khoan, ten_ngan_hang, email_system, app_password)
VALUES
    ('Boarding House','039424234','NHÀ TRỌ NHỰT MINH','TECHCOMBANK',
     'vietnamboardinghouse@gmail.com','ziau vrck nvyt viem');

INSERT INTO tai_khoan (user_name, pass_word, email_user)
VALUES ('1','1','klemin3005@gmail.com');