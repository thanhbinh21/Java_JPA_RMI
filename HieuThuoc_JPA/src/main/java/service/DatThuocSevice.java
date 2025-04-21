package service;

import entity.PhieuDatThuoc;
import entity.Thuoc;

import java.util.List;

public interface DatThuocSevice  extends GenericService<PhieuDatThuoc,String>{
    boolean addPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc);

}
