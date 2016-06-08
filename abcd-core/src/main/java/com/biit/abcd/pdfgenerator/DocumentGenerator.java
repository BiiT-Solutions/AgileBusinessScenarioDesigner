package com.biit.abcd.pdfgenerator;

import java.io.ByteArrayOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This class serves to generate a pdf document. Stores the default
 * configuration of the pdf page.
 *
 */
public abstract class DocumentGenerator {
	private final static float MARGIN_LEFT = 50;
	private final static float MARGIN_RIGHT = 50;
	private final static float MARGIN_TOP = 75;
	private final static float MARGIN_BOTTON = 50;

	private final static String DEFAULT_DOCUMENT_NAME = "Form document PDF";
	private final static String DEFAULT_DOCUMENT_SUBJECT = "Form document PDF";
	private final static String DOCUMENT_AUTHOR = "BiiT";
	private final static String DOCUMENT_CREATOR = "BiiT";

	private PdfPageEvent pageEvent;
	private PdfWriter writer;

	protected static void addMetaData(Document document) {
		document.addTitle(DEFAULT_DOCUMENT_NAME);
		document.addSubject(DEFAULT_DOCUMENT_SUBJECT);
		document.addAuthor(DOCUMENT_AUTHOR);
		document.addCreator(DOCUMENT_CREATOR);
	}

	protected static Document generateDocument() {
		return new Document(PageSize.A4, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, MARGIN_BOTTON);
	}

	protected abstract void generateDocumentContent(Document document) throws DocumentException;

	public void setPageEvent(PdfPageEvent event) {
		this.pageEvent = event;
	}

	public final byte[] generate() throws DocumentException {
		Document document = generateDocument();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writer = PdfWriter.getInstance(document, baos);

		if (writer == null) {
			return null;
		}

		if (pageEvent != null) {
			writer.setPageEvent(pageEvent);
		}

		document.open();
		addMetaData(document);

		generateDocumentContent(document);

		document.close();
		writer = null;
		return baos.toByteArray();
	}

	public PdfWriter getWriter() {
		return writer;
	}
}
