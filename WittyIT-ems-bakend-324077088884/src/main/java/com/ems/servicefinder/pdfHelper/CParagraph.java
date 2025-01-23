package com.ems.servicefinder.pdfHelper;

import java.util.ArrayList;


import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

//short for CInsight Paragraph Part
public class CParagraph {
	// HELPER CALLS TO MAKE WRITING QUICKER 
	public static Paragraph Get(String text, Font font, float leading) {
		return Get(new CParaPart(text, font, leading));
	}

	public static Paragraph Get(String text, Font font, float leading, int horizontalAlignment, float indent) {
		return Get(new CParaPart(text, font, leading, horizontalAlignment, indent));
	}

	//THE ACTUAL CALLS
	public static Paragraph Get(CParaPart para) {
		ArrayList<CParaPart> l = new ArrayList<CParaPart>();
		l.add(para);
		return Get(l);
	}

	public static Paragraph Get(ArrayList<CParaPart> list) {
		Paragraph p = new Paragraph();
		if (list != null && list.size() > 0) {
			p.setIndentationLeft(list.get(0).Indent);
			p.setAlignment(list.get(0).HorizontalAlign);
			p.setLeading(list.get(0).Leading);
			for (CParaPart ps : list) {
				Phrase phrase = new Phrase();
				phrase.add(new Chunk(ps.Text, ps.Font));
				p.add(phrase);
			}
		}
		return p;
	}

	public static Paragraph SpacerParagraph() {
		return Get(" ", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading);
	}
}
