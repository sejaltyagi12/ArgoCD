package com.ems.servicefinder.pdfHelper;

import java.io.IOException;
import java.net.MalformedURLException;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class GenericPdf {
	public static Float pagePaddingBase = 36f;
	public static Float headerHeight = 70f;
	public static Float footerHeight = 10f;
	public static Float pagePaddingTop = pagePaddingBase + headerHeight + 5;
	public static Float pagePaddingBottom = pagePaddingBase + footerHeight - 5;
	public static Float pagePaddingSide = pagePaddingBase;

	public static class Fonts {
		public static Integer normalSize = 10;
		public static Integer normalLeading = 12;
		public static Integer largeSize = 14;
		public static Integer extraLargeSize = 18;
		public static Integer largeLeading = 16;
		public static Integer smallSize = 8;
		public static Integer smallLeading = 10;

		public static Font normal = FontFactory.getFont("Helvetica", normalSize, Font.NORMAL);
		public static Font normalBold = FontFactory.getFont("Helvetica", normalSize, Font.BOLD);
		public static Font normalItalic = FontFactory.getFont("Helvetica", normalSize, Font.ITALIC);
		public static Font large = FontFactory.getFont("Helvetica", largeSize, Font.NORMAL);
		public static Font largeBold = FontFactory.getFont("Helvetica", largeSize, Font.BOLD);
		public static Font extraLargeBold = FontFactory.getFont("Helvetica", extraLargeSize, Font.BOLD);
		public static Font largeBoldUnderline = FontFactory.getFont("Helvetica", largeSize, Font.UNDERLINE | Font.BOLD);
		public static Font small = FontFactory.getFont("Helvetica", smallSize, Font.NORMAL);
		public static Font smallBold = FontFactory.getFont("Helvetica", smallSize, Font.BOLD);
		public static Font spacer = FontFactory.getFont("Helvetica", normalSize, Font.NORMAL);
		public static Font underline = FontFactory.getFont("Helvetica", normalSize, Font.UNDERLINE);
		public static Font linkSmall = FontFactory.getFont("Helvetica", smallSize, Font.UNDERLINE);
	}

	public static class GenericHeaderFooter extends PdfPageEventHelper {
		PdfContentByte cb;
		PdfTemplate pageCount;
		String title = "";
		String subString = "";
		String logoPath = "";

		public GenericHeaderFooter(String title, String subString, String logoPath)
		{
			this.title = title;
			this.subString = subString;
			this.logoPath = logoPath;
		}
		
		public void onOpenDocument(PdfWriter writer, Document document) {
			cb = writer.getDirectContent();
			pageCount = writer.getDirectContent().createTemplate(30, 10);
		}

		public void onEndPage(PdfWriter writer, Document document) {
			Rectangle page = document.getPageSize();

			//HEADER
			try {
				Image headerImage = Image.getInstance(logoPath);
				float percentage = 0.0f;
				percentage = headerHeight / headerImage.getHeight();
				headerImage.scalePercent(percentage * 100);

				PdfPTable head = new PdfPTable(4);
				head.setTotalWidth(page.getWidth() - GenericPdf.pagePaddingSide);
				head.setLockedWidth(true);
				head.getDefaultCell().setFixedHeight(headerHeight);

				PdfPCell c = new PdfPCell(headerImage, false);
				c.setHorizontalAlignment(Element.ALIGN_LEFT);
				c.setVerticalAlignment(Element.ALIGN_BOTTOM);
				c.setIndent(GenericPdf.pagePaddingSide);
				c.setBorder(PdfPCell.NO_BORDER);
				head.addCell(c);
				
				c = new PdfPCell(CParagraph.Get(" ", GenericPdf.Fonts.extraLargeBold, Element.ALIGN_RIGHT));
				c.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c.setVerticalAlignment(Element.ALIGN_TOP);
				c.setFollowingIndent(0);
				c.setBorder(PdfPCell.NO_BORDER);
				head.addCell(c);
				
				c = new PdfPCell(CParagraph.Get(" ", GenericPdf.Fonts.extraLargeBold, Element.ALIGN_LEFT));
				c.setHorizontalAlignment(Element.ALIGN_LEFT);
				c.setVerticalAlignment(Element.ALIGN_TOP);
				c.setFollowingIndent(0);
				c.setBorder(PdfPCell.NO_BORDER);
				head.addCell(c);
				
				c = new PdfPCell();
				c.setHorizontalAlignment(Element.ALIGN_LEFT);
				c.setIndent(GenericPdf.pagePaddingSide);
				c.setBorder(PdfPCell.NO_BORDER);
				head.addCell(c);

				// since the table header is implemented using a PdfPTable, we call WriteSelectedRows(), which requires absolute positions!
				head.writeSelectedRows(0, -1, 0, page.getHeight() - pagePaddingBase, writer.getDirectContent());
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			} catch (MalformedURLException e) {
				throw new ExceptionConverter(e);
			} catch (IOException e) {
				throw new ExceptionConverter(e);
			}

		}

		public void onCloseDocument(PdfWriter writer, Document document) {
		}
	}

}
