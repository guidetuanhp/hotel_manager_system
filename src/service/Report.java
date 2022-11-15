package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;

import entity.Customer;
import entity.DetailInvoice;
import entity.Invoice;
import entity.Room;

public class Report {

//	public static void main(String[] args) {
//		try {
//			Document document = new Document();
//
//			OutputStream outputStream = new FileOutputStream(new File("D:\\TestTableFile.pdf"));
//
//			PdfWriter.getInstance(document, outputStream);
//
//			document.open();
//
//			document.add(new Paragraph("bills id: \t\t" + 5423));
//			document.add(new Paragraph("invoice creation date: \t\t" + "2022-10-19"));
//
//			PdfPTable tableCu = new PdfPTable(4);
//			tableCu.setSpacingBefore(40);
//			tableCu.setWidthPercentage(100);
//			PdfPCell cellCus;
//			// we add a cell with colspan 3
//			Font f = new Font(FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.BLACK);
//			Phrase phrase = new Phrase("YOUR SERVICES BILL", f);
//			cellCus = new PdfPCell(phrase);
//			cellCus.setColspan(4);
//			cellCus.setBorder(Rectangle.NO_BORDER);
//			tableCu.addCell(cellCus);
//
//			// id customer
//			setTableCustomer(cellCus, tableCu, f, phrase, "your Id", "1234");
//
//			// cmnd customer
//			setTableCustomer(cellCus, tableCu, f, phrase, "identity card number: ", "9234728342");
//
//			// name of customer
//			setTableCustomer(cellCus, tableCu, f, phrase, "your name: ", "john halen");
//			
//			// qt of customer
//			setTableCustomer(cellCus, tableCu, f, phrase, "nationality", "Singapore");
//			
//			// room
//			setTableCustomer(cellCus, tableCu, f, phrase, "room", "201");
//			setTableCustomer(cellCus, tableCu, f, phrase, "price", "2000/day");
//			setTableCustomer(cellCus, tableCu, f, phrase, "booking date", "2022-10-22");
//			setTableCustomer(cellCus, tableCu, f, phrase, "return date", "2022-10-24");
//			
//			f = new Font(FontFamily.TIMES_ROMAN, 14.0f, Font.BOLD, BaseColor.BLACK);
//			phrase = new Phrase("Total Amount: " + 25000, f);
//			cellCus = new PdfPCell(phrase);
//			cellCus.setColspan(4);
//			cellCus.setBorder(Rectangle.NO_BORDER);
//			cellCus.setPaddingTop(60f);
//			tableCu.addCell(cellCus);
//
//			document.add(tableCu);
//			// Create Table object, Here 4 specify the no. of columns
//			PdfPTable table = new PdfPTable(3);
//			table.setSpacingBefore(60);
//			table.setPaddingTop(100);
//			table.setWidthPercentage(100);
//			// the cell object
//			PdfPCell cell;
//
//			// we add a cell with colspan 3
//			cell = new PdfPCell(new Phrase("Your Services"));
//			cell.setColspan(3);
//			cell.setPaddingTop(8f);
//			table.addCell(cell);
//
//			// we add the four remaining cells with addCell()
//			table.addCell("Service name");
//			table.addCell("service price");
//			table.addCell("service order date");
//
//			table.addCell("cocal cola");
//			table.addCell("3000");
//			table.addCell("2022-10-25");
//
//			// Add content to the document using Table objects.
//			document.add(table);
//
//			// Close document and outputStream.
//			document.close();
//			outputStream.close();
//
//			System.out.println("Pdf created successfully.");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void createBill(Room room, Customer customer, List<DetailInvoice> listDetail, Invoice invoice, Integer numdayOver, Double fine, Double totalAmount, String filename) {
		try {
			
			String fontname = "Fonts\\GulimChe.ttf";
			BaseFont bfont = BaseFont.createFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font font = new Font(bfont, 12);
			
			
			
			Document document = new Document();

			OutputStream outputStream = new FileOutputStream(new File("file/"+filename));

			PdfWriter.getInstance(document, outputStream);

			document.open();
			
			document.add(new Paragraph("영수증 코드: " + invoice.getId(), font));
			document.add(new Paragraph("영수증 발생 일: " + String .valueOf(new Timestamp(System.currentTimeMillis()).toString()), font));
			PdfPTable tableCu = new PdfPTable(4);
			tableCu.setSpacingBefore(40);
			tableCu.setWidthPercentage(100);
			PdfPCell cellCus;
			// we add a cell with colspan 3
			Font f = new Font(bfont, 25.0f, Font.BOLD, BaseColor.BLACK);
			Phrase phrase = new Phrase("영수증 정보", f);
			cellCus = new PdfPCell(phrase);
			cellCus.setColspan(4);
			cellCus.setBorder(Rectangle.NO_BORDER);
			tableCu.addCell(cellCus);

			
//			phrase = new Phrase(title);
//			cellCus = new PdfPCell(phrase);
//			cellCus.setBorder(Rectangle.NO_BORDER);
//			cellCus.setPaddingTop(20f);
//			tableCu.addCell(cellCus);
//			f = new Font(bfont, 14.0f, Font.BOLD, BaseColor.BLACK);
//			phrase = new Phrase(content, f);
//			cellCus = new PdfPCell(phrase);
//			cellCus.setBorder(Rectangle.NO_BORDER);
//			cellCus.setPaddingTop(20f);
//			tableCu.addCell(cellCus);
			// id customer
			setTableCustomer(cellCus, tableCu, f, phrase, "고객 번호", customer.getId().toString().toString());

			// cmnd customer
			setTableCustomer(cellCus, tableCu, f, phrase, "주민등록번호: ", customer.getCitizenIdentificationNumber());

			// name of customer
			setTableCustomer(cellCus, tableCu, f, phrase, "이름: ", customer.getName());
			
			// qt of customer
			setTableCustomer(cellCus, tableCu, f, phrase, "국적", customer.getNationality());
			
			// room
			setTableCustomer(cellCus, tableCu, f, phrase, "객실", room.getRoomName());
			setTableCustomer(cellCus, tableCu, f, phrase, "가격", room.getPrice()+"/day");
			setTableCustomer(cellCus, tableCu, f, phrase, "체크인 일", invoice.getBookingDate().toString());
			setTableCustomer(cellCus, tableCu, f, phrase, "체크아웃 일", invoice.getActualReturnDate().toString());
			setTableCustomer(cellCus, tableCu, f, phrase, "열체일수", numdayOver.toString());
			setTableCustomer(cellCus, tableCu, f, phrase, "벌금", fine.toString());
			
			f = new Font(bfont, 14.0f, Font.BOLD, BaseColor.BLACK);
			phrase = new Phrase("총: " + totalAmount, f);
			cellCus = new PdfPCell(phrase);
			cellCus.setColspan(4);
			cellCus.setBorder(Rectangle.NO_BORDER);
			cellCus.setPaddingTop(60f);
			tableCu.addCell(cellCus);

			document.add(tableCu);
			// Create Table object, Here 4 specify the no. of columns
			PdfPTable table = new PdfPTable(3);
			table.setSpacingBefore(60);
			table.setPaddingTop(100);
			table.setWidthPercentage(100);
			// the cell object
			PdfPCell cell;

			// we add a cell with colspan 3
			cell = new PdfPCell(new Phrase("리스트 서비스", font));
			cell.setColspan(3);
			cell.setPaddingTop(8f);
			
			table.addCell(cell);

			// we add the four remaining cells with addCell()
			cell = new PdfPCell(new Phrase("서비스", font));
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("가격", font));
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("주문 일", font));
			table.addCell(cell);

			for(DetailInvoice d : listDetail) {
				cell = new PdfPCell(new Phrase(d.getServices().getName(), font));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase(d.getServices().getPrice().toString(), font));
				table.addCell(d.getServices().getPrice().toString());
				cell = new PdfPCell(new Phrase(d.getCreatedDate().toString(), font));
				table.addCell(cell);
			}

			// Add content to the document using Table objects.
			document.add(table);

			// Close document and outputStream.
			document.close();
			outputStream.close();

			System.out.println("영수증 만들 성공.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setTableCustomer(PdfPCell cellCus, PdfPTable tableCu, Font f,Phrase phrase, String title, String content) {
		try {
			String fontname = "Fonts\\GulimChe.ttf";
			BaseFont bfont = BaseFont.createFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font font = new Font(bfont, 12);
			f = new Font(bfont, 14.0f);
			phrase = new Phrase(title, f);
			cellCus = new PdfPCell(phrase);
			cellCus.setBorder(Rectangle.NO_BORDER);
			cellCus.setPaddingTop(20f);
			tableCu.addCell(cellCus);
			
			phrase = new Phrase(content, f);
			cellCus = new PdfPCell(phrase);
			cellCus.setBorder(Rectangle.NO_BORDER);
			cellCus.setPaddingTop(20f);
			tableCu.addCell(cellCus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
