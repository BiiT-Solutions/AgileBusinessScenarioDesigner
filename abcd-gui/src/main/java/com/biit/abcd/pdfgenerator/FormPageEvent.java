package com.biit.abcd.pdfgenerator;

import com.biit.abcd.pdfgenerator.utils.PdfFont;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * iText page event class. This formats the page to have a header and a footer
 * with page count and titles.
 *
 */
public class FormPageEvent extends PdfPageEventHelper {
	private static final int HEADER_HEIGHT = 10;
	private String header;
	private PdfTemplate total;

	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(30, HEADER_HEIGHT);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		PdfPTable table = new PdfPTable(3);

		int pageNumber = writer.getPageNumber();

		if (pageNumber > 1) {
			try {
				table.setWidths(new int[] { 24, 24, 2 });
				table.setTotalWidth(527);
				table.setLockedWidth(true);
				table.getDefaultCell().setFixedHeight(20);
				table.getDefaultCell().setBorder(Rectangle.BOTTOM);
				PdfPCell headerCell = PdfPCellGenerator.generateHeaderTitle(header);
				headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				headerCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				headerCell.setFixedHeight(HEADER_HEIGHT);
				headerCell.setBorder(Rectangle.BOTTOM);
				table.addCell(headerCell);
				headerCell = PdfPCellGenerator.generateHeaderTitle(String.format("Page %d of", writer.getPageNumber()));
				headerCell.setFixedHeight(HEADER_HEIGHT);
				headerCell.setBorder(Rectangle.BOTTOM);
				headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				headerCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				table.addCell(headerCell);
				PdfPCell cell = new PdfPCell(Image.getInstance(total));
				cell.setBorder(Rectangle.BOTTOM);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				table.addCell(cell);
				table.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			}
		}
	}

	@Override
	public void onCloseDocument(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(total, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber() - 1), PdfFont.HEADER_FONT.getFont()), 2, 2, 0);
	}

}
