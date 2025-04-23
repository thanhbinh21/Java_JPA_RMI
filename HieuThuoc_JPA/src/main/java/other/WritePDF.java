package other;

import com.itextpdf.text.Chunk;
import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.html.parser.Entity;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.*;


public class WritePDF {

    private DecimalFormat formatter = new DecimalFormat("###,###,###");
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY HH:mm");
    private Document document = new Document();
    private FileOutputStream file;
    private JFrame jf = new JFrame();
    private FileDialog fd = new FileDialog(jf, "Xuất pdf", FileDialog.SAVE);
    private Font fontNormal10;
    private Font fontBold15;
    private Font fontBold25;
    private Font fontBoldItalic15;

    public WritePDF() {
        try {
            // Try multiple font loading approaches to ensure Vietnamese characters display properly
            
            // First attempt: Try to load from project structure with absolute path
            try {
                String projectRoot = new File("").getAbsolutePath();
                
                // Create fonts directory if it doesn't exist
                File fontDir = new File(projectRoot, "fonts");
                if (!fontDir.exists()) {
                    fontDir.mkdirs();
                }
                
                // Try project's lib folder
                File libFontDir = new File(projectRoot + "/lib/TimesNewRoman");
                if (libFontDir.exists()) {
                    BaseFont baseFont = BaseFont.createFont(
                        libFontDir + "/SVN-Times New Roman.ttf", 
                        BaseFont.IDENTITY_H, 
                        BaseFont.EMBEDDED);
                        
                    fontNormal10 = new Font(baseFont, 12, Font.NORMAL);
                    fontBold15 = new Font(baseFont, 15, Font.BOLD);
                    fontBold25 = new Font(baseFont, 25, Font.BOLD);
                    fontBoldItalic15 = new Font(baseFont, 15, Font.ITALIC | Font.BOLD);
                    System.out.println("Loaded fonts from lib/TimesNewRoman");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Could not load fonts from lib folder: " + e.getMessage());
            }
            
            // Second attempt: Try system fonts (Windows)
            try {
                // On Windows systems, try to use Arial Unicode which has good Vietnamese support
                String[] commonFontPaths = {
                    "C:/Windows/Fonts/Arial.ttf",
                    "C:/Windows/Fonts/arial.ttf",
                    "C:/Windows/Fonts/arialuni.ttf",
                    "C:/Windows/Fonts/tahoma.ttf",
                    "C:/Windows/Fonts/Tahoma.ttf",
                    "C:/Windows/Fonts/times.ttf",
                    "C:/Windows/Fonts/Times New Roman.ttf"
                };
                
                for (String fontPath : commonFontPaths) {
                    if (new File(fontPath).exists()) {
                        BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        fontNormal10 = new Font(baseFont, 12, Font.NORMAL);
                        fontBold15 = new Font(baseFont, 15, Font.BOLD);
                        fontBold25 = new Font(baseFont, 25, Font.BOLD);
                        fontBoldItalic15 = new Font(baseFont, 15, Font.ITALIC | Font.BOLD);
                        System.out.println("Loaded system font: " + fontPath);
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not load system fonts: " + e.getMessage());
            }
            
            // Third attempt: Use built-in iText font with CP1258 encoding (Vietnamese)
            try {
                BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
                fontNormal10 = new Font(baseFont, 12, Font.NORMAL);
                fontBold15 = new Font(baseFont, 15, Font.BOLD);
                fontBold25 = new Font(baseFont, 25, Font.BOLD);
                fontBoldItalic15 = new Font(baseFont, 15, Font.ITALIC | Font.BOLD);
                System.out.println("Using built-in iText font with CP1258 encoding");
                return;
            } catch (Exception e) {
                System.out.println("Could not use CP1258 encoding: " + e.getMessage());
            }
            
            // Last resort - use default fonts
            fontNormal10 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            fontBold15 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
            fontBold25 = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
            fontBoldItalic15 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.ITALIC | Font.BOLD);
            System.out.println("Using default iText fonts");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initializing fonts: " + e.getMessage());
            
            // Make sure we have working fonts even if there's an error
            fontNormal10 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            fontBold15 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
            fontBold25 = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
            fontBoldItalic15 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.ITALIC | Font.BOLD);
        }
    }

    public void chooseURL(String url) {
        try {
            document.close();
            document = new Document();
            file = new FileOutputStream(url);
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy đường dẫn " + url);
        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi chọn file!");
        }
    }

    public void setTitle(String title) {
        try {
            Paragraph pdfTitle = new Paragraph(new Phrase(title, fontBold25));
            pdfTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(pdfTitle);
            document.add(Chunk.NEWLINE);
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }
    }

    public String getFile(String name) {
        fd.pack();
        fd.setSize(800, 600);
        fd.validate();
        Rectangle rect = jf.getContentPane().getBounds();
        double width = fd.getBounds().getWidth();
        double height = fd.getBounds().getHeight();
        double x = rect.getCenterX() - (width / 2);
        double y = rect.getCenterY() - (height / 2);
        Point leftCorner = new Point();
        leftCorner.setLocation(x, y);
        fd.setLocation(leftCorner);
        fd.setFile(name);
        fd.setVisible(true);
        String url = fd.getDirectory() + fd.getFile();
        if (url.equals("null")) {
            return null;
        }
        return url;
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static Chunk createWhiteSpace(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(" ");
        }
        return new Chunk(builder.toString());
    }

    public void printHoaDon(HoaDon hoaDon, List<ChiTietHoaDon> listCTHD, double thue) {
        String url = "";
        try {
            fd.setTitle("In hóa đơn");
            fd.setLocationRelativeTo(null);
            url = getFile(hoaDon.getId());
            if (url == null || url.equals("nullnull") || url.equals("null")) {
                return;
            }
            
            if (!url.toLowerCase().endsWith(".pdf")) {
                url = url + ".pdf";
            }
            
            file = new FileOutputStream(url);
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            // Thông tin cửa hàng
            Paragraph company = new Paragraph("NHÀ THUỐC YÊN TÂM", fontBold15);
            company.add(new Chunk(createWhiteSpace(20)));
            company.add(new Chunk("Đại Học Công Nghiệp TP Hồ Chí Minh", fontNormal10));
            company.setAlignment(Element.ALIGN_LEFT);
            document.add(company);
            document.add(Chunk.NEWLINE);

            // Tiêu đề hóa đơn
            Paragraph header = new Paragraph("HÓA ĐƠN BÁN THUỐC", fontBold25);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(Chunk.NEWLINE);

            // Thông tin hóa đơn
            Paragraph paragraph1 = new Paragraph("Mã hóa đơn: " + hoaDon.getId(), fontNormal10);
            document.add(paragraph1);
            
            String kh = hoaDon.getKhachHang().getHoTen();
            Paragraph paragraph2 = new Paragraph("Khách hàng: " + kh, fontNormal10);
            document.add(paragraph2);
            
            if (hoaDon.getKhachHang().getSoDienThoai() != null && !hoaDon.getKhachHang().getSoDienThoai().isEmpty()) {
                Paragraph paragraphPhone = new Paragraph("SĐT: " + hoaDon.getKhachHang().getSoDienThoai(), fontNormal10);
                document.add(paragraphPhone);
            }
            
            String nv = hoaDon.getNhanVien().getHoTen();
            Paragraph paragraph3 = new Paragraph("Nhân viên: " + nv, fontNormal10);
            document.add(paragraph3);
            
            Date today = new Date(System.currentTimeMillis());
            Paragraph paragraph4 = new Paragraph("Thời gian: " + formatDate.format(hoaDon.getThoiGian()), fontNormal10);
            document.add(paragraph4);
            document.add(Chunk.NEWLINE);

            // Bảng chi tiết hóa đơn
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{40f, 15f, 15f, 10f, 20f});

            // Headers của bảng
            PdfPCell cellHeader = new PdfPCell(new Phrase("Tên thuốc", fontBold15));
            cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHeader.setPadding(5);
            table.addCell(cellHeader);
            
            cellHeader = new PdfPCell(new Phrase("Đơn vị tính", fontBold15));
            cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHeader.setPadding(5);
            table.addCell(cellHeader);
            
            cellHeader = new PdfPCell(new Phrase("Đơn giá", fontBold15));
            cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHeader.setPadding(5);
            table.addCell(cellHeader);
            
            cellHeader = new PdfPCell(new Phrase("SL", fontBold15));
            cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHeader.setPadding(5);
            table.addCell(cellHeader);
            
            cellHeader = new PdfPCell(new Phrase("Thành tiền", fontBold15));
            cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHeader.setPadding(5);
            table.addCell(cellHeader);

            // Dữ liệu của bảng
            double tongTien = 0;
            for (ChiTietHoaDon cthd : listCTHD) {
                // Tên thuốc
                PdfPCell cell = new PdfPCell(new Phrase(cthd.getThuoc().getTen(), fontNormal10));
                cell.setPadding(5);
                table.addCell(cell);
                
                // Đơn vị tính
                cell = new PdfPCell(new Phrase(cthd.getThuoc().getDonViTinh().toString(), fontNormal10));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
                
                // Đơn giá
                cell = new PdfPCell(new Phrase(formatter.format(cthd.getDonGia()) + "đ", fontNormal10));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPadding(5);
                table.addCell(cell);
                
                // Số lượng
                cell = new PdfPCell(new Phrase(String.valueOf(cthd.getSoLuong()), fontNormal10));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
                
                // Thành tiền
                double thanhTien = cthd.getDonGia() * cthd.getSoLuong();
                tongTien += thanhTien;
                cell = new PdfPCell(new Phrase(formatter.format(thanhTien) + "đ", fontNormal10));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPadding(5);
                table.addCell(cell);
            }
            document.add(table);
            document.add(Chunk.NEWLINE);

            // Tính VAT và tổng thanh toán
            double vatAmount = thue; // Nếu thue là số tiền cụ thể
            
            // Nếu thue là tỷ lệ phần trăm (ví dụ: 0.1 cho 10%)
            if (thue < 1) {
                vatAmount = tongTien * thue;
            }
            
            double tongThanhToan = tongTien + vatAmount;

            // Tổng tiền và thanh toán
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(50);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            summaryTable.setWidths(new float[]{50f, 50f});
            
            // Tổng tiền hàng
            PdfPCell labelCell = new PdfPCell(new Phrase("Tổng tiền hàng:", fontBold15));
            labelCell.setBorder(0);
            labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            labelCell.setPadding(5);
            summaryTable.addCell(labelCell);
            
            PdfPCell valueCell = new PdfPCell(new Phrase(formatter.format(tongTien) + "đ", fontBold15));
            valueCell.setBorder(0);
            valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            valueCell.setPadding(5);
            summaryTable.addCell(valueCell);
            
            // VAT
            labelCell = new PdfPCell(new Phrase("VAT:", fontBold15));
            labelCell.setBorder(0);
            labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            labelCell.setPadding(5);
            summaryTable.addCell(labelCell);
            
            valueCell = new PdfPCell(new Phrase(formatter.format(vatAmount) + "đ", fontBold15));
            valueCell.setBorder(0);
            valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            valueCell.setPadding(5);
            summaryTable.addCell(valueCell);
            
            // Tổng thanh toán
            labelCell = new PdfPCell(new Phrase("Tổng thanh toán:", fontBold15));
            labelCell.setBorder(0);
            labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            labelCell.setPadding(5);
            summaryTable.addCell(labelCell);
            
            valueCell = new PdfPCell(new Phrase(formatter.format(tongThanhToan) + "đ", fontBold15));
            valueCell.setBorder(0);
            valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            valueCell.setPadding(5);
            summaryTable.addCell(valueCell);
            
            document.add(summaryTable);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // Chữ ký
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(new Chunk("Người bán hàng", fontBoldItalic15));
            paragraph.add(new Chunk(createWhiteSpace(50)));
            paragraph.add(new Chunk("Người mua hàng", fontBoldItalic15));
            document.add(paragraph);

            Paragraph sign = new Paragraph();
            sign.setAlignment(Element.ALIGN_CENTER);
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
            sign.add(new Chunk(createWhiteSpace(45)));
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
            document.add(sign);

            document.close();
            writer.close();
            
            // Mở file PDF
            try {
                openFile(url);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Hóa đơn đã được lưu tại: " + url);
            }
            
        } catch (DocumentException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo file PDF: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void printHoaDon1(HoaDon hoaDon, List<ChiTietHoaDon> listCTHD ,double thue) {
        String url = "";
        try {
            fd.setTitle("In hóa đơn");
            fd.setLocationRelativeTo(null);
            url = getFile(hoaDon.getId());
            if (url.equals("nullnull")) {
                return;
            }
            url = url + ".pdf";
            file = new FileOutputStream(url);
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            Paragraph company = new Paragraph("Nhà thuốc YÊN TÂM", fontBold15);
            company.add(new Chunk(createWhiteSpace(20)));
            Date today = new Date(System.currentTimeMillis());
            company.add(new Chunk("Thời gian in phiếu: " + formatDate.format(today), fontNormal10));
            company.setAlignment(Element.ALIGN_LEFT);
            document.add(company);
            document.add(Chunk.NEWLINE);

            Paragraph header = new Paragraph("THÔNG TIN HÓA ĐƠN", fontBold25);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph paragraph1 = new Paragraph("Mã hóa đơn: " + hoaDon.getId(), fontNormal10);
            String kh = hoaDon.getKhachHang().getHoTen();
            Paragraph paragraph2 = new Paragraph("Khách hàng: " + kh, fontNormal10);
            paragraph2.add(new Chunk(createWhiteSpace(5)));

            String nv = hoaDon.getNhanVien().getHoTen();
            Paragraph paragraph3 = new Paragraph("Nhân viên : " + nv, fontNormal10);
            paragraph3.add(new Chunk(createWhiteSpace(5)));

            Paragraph paragraph4 = new Paragraph("Thời gian: " + formatDate.format(hoaDon.getThoiGian()), fontNormal10);
            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(paragraph4);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{40f, 20f, 20f, 20f, 20f});

            table.addCell(new PdfPCell(new Phrase("Tên thuốc", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Đơn vị tính", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Đơn giá", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Số lượng", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Thành tiền", fontBold15)));

            double tongTien = 0;

            for (ChiTietHoaDon cthd : listCTHD) {
                table.addCell(new PdfPCell(new Phrase(cthd.getThuoc().getTen(), fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(cthd.getThuoc().getDonViTinh().toString(), fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(formatter.format(cthd.getDonGia()) + "đ", fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(cthd.getSoLuong()), fontNormal10)));
                double thanhTien = cthd.getDonGia() * cthd.getSoLuong();
                table.addCell(new PdfPCell(new Phrase(formatter.format(thanhTien) + "đ", fontNormal10)));
                tongTien += thanhTien;
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            Paragraph paraTongThanhToan = new Paragraph(new Phrase("Tổng thành tiền: " + formatter.format(tongTien) + "đ", fontBold15));
            paraTongThanhToan.setIndentationLeft(300);
            Paragraph thuetxt = new Paragraph(new Phrase("Vat: " + formatter.format(thue) +"đ" , fontBold15));
            thuetxt.setIndentationLeft(300);
            Paragraph sum = new Paragraph(new Phrase("Thanh toán : " + formatter.format(tongTien+thue) +"đ" , fontBold15));
            sum.setIndentationLeft(300);

            document.add(paraTongThanhToan);
            document.add(thuetxt);
            document.add(sum);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            Paragraph paragraph = new Paragraph();
            paragraph.setIndentationLeft(22);
            paragraph.add(new Chunk("Người lập phiếu", fontBoldItalic15));
            paragraph.add(new Chunk(createWhiteSpace(30)));
            paragraph.add(new Chunk("Người giao", fontBoldItalic15));
            paragraph.add(new Chunk(createWhiteSpace(30)));
            paragraph.add(new Chunk("Khách hàng", fontBoldItalic15));

            Paragraph sign = new Paragraph();
            sign.setIndentationLeft(20);
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
            sign.add(new Chunk(createWhiteSpace(25)));
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
            sign.add(new Chunk(createWhiteSpace(23)));
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));

            document.add(paragraph);
            document.add(sign);
            document.close();
            writer.close();
            openFile(url);

        } catch (DocumentException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi ghi file " + url);
        }
    }

    public void printPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc, List<ChiTietPhieuDatThuoc> listCTPDT , double thue) {
        String url = "";
        try {
            fd.setTitle("In phiếu đặt thuốc");
            fd.setLocationRelativeTo(null);
            url = getFile(phieuDatThuoc.getId());
            if (url.equals("nullnull")) {
                return;
            }
            url = url + ".pdf";
            file = new FileOutputStream(url);
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            Paragraph company = new Paragraph("Nhà thuốc YÊN TÂM", fontBold15);
            company.add(new Chunk(createWhiteSpace(20)));
            Date today = new Date(System.currentTimeMillis());
            company.add(new Chunk("Đại Học Công Nghiệp TP Hồ Chí Minh" ));
            company.setAlignment(Element.ALIGN_LEFT);
            document.add(company);
            document.add(Chunk.NEWLINE);

            Paragraph header = new Paragraph("THÔNG TIN PHIẾU ĐẶT THUỐC", fontBold25);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph paragraph1 = new Paragraph("Mã phiếu đặt thuốc: " + phieuDatThuoc.getId(), fontNormal10);
            String kh = phieuDatThuoc.getKhachHang().getHoTen();
            Paragraph paragraph2 = new Paragraph("Khách hàng: " + kh, fontNormal10);
            paragraph2.add(new Chunk(createWhiteSpace(5)));

            String nv = phieuDatThuoc.getNhanVien().getHoTen();
            Paragraph paragraph3 = new Paragraph("Nhân viên : " + nv, fontNormal10);
            paragraph3.add(new Chunk(createWhiteSpace(5)));

            Paragraph paragraph4 = new Paragraph("Thời gian: " + formatDate.format(phieuDatThuoc.getThoiGian()), fontNormal10);
            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(paragraph4);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{40f, 20f, 20f, 20f, 20f});

            table.addCell(new PdfPCell(new Phrase("Tên thuốc", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Đơn vị tính", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Đơn giá", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Số lượng", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Thành tiền", fontBold15)));

            double tongTien = 0;

            for (ChiTietPhieuDatThuoc ctpdt : listCTPDT) {
                table.addCell(new PdfPCell(new Phrase(ctpdt.getThuoc().getTen(), fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(ctpdt.getThuoc().getDonViTinh().toString(), fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(formatter.format(ctpdt.getDonGia()) + "đ", fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ctpdt.getSoLuong()), fontNormal10)));
                double thanhTien = ctpdt.getDonGia() * ctpdt.getSoLuong();
                table.addCell(new PdfPCell(new Phrase(formatter.format(thanhTien) + "đ", fontNormal10)));
                tongTien += thanhTien;
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            Paragraph paraTongThanhToan = new Paragraph(new Phrase("Tổng thành tiền: " + formatter.format(tongTien) + "đ", fontBold15));
            paraTongThanhToan.setIndentationLeft(300);
            Paragraph thuetxt = new Paragraph(new Phrase("Vat: " + formatter.format(thue) +"đ" , fontBold15));
            thuetxt.setIndentationLeft(300);
            Paragraph sum = new Paragraph(new Phrase("Thanh toán : " + formatter.format(tongTien+thue) +"đ" , fontBold15));
            sum.setIndentationLeft(300);

            document.add(paraTongThanhToan);
            document.add(thuetxt);
            document.add(sum);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            Paragraph paragraph = new Paragraph();
            paragraph.setIndentationLeft(22);
            paragraph.add(new Chunk("Người lập phiếu", fontBoldItalic15));
            paragraph.add(new Chunk(createWhiteSpace(30)));
            paragraph.add(new Chunk("Người giao", fontBoldItalic15));
            paragraph.add(new Chunk(createWhiteSpace(30)));
            paragraph.add(new Chunk("Khách hàng", fontBoldItalic15));

            Paragraph sign = new Paragraph();
            sign.setIndentationLeft(20);
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
            sign.add(new Chunk(createWhiteSpace(25)));
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
            sign.add(new Chunk(createWhiteSpace(23)));
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));

            document.add(paragraph);
            document.add(sign);
            document.close();
            writer.close();
            openFile(url);

        } catch (DocumentException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi ghi file " + url);
        }
    }
}