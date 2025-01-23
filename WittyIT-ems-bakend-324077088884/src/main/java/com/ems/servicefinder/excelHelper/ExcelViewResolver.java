package com.ems.servicefinder.excelHelper;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * Created by Avinash Tyagi on 2017-06-15.
 */
public class ExcelViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        ExcelView view = new ExcelView();
        return view;
    }
}
