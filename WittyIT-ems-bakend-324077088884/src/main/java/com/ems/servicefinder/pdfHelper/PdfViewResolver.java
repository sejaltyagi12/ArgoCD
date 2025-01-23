package com.ems.servicefinder.pdfHelper;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * Created by Avinash Tyagi on 2017-06-15.
 */
public class PdfViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        PdfView view = new PdfView();
        return view;
    }
}
