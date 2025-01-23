package com.ems.servicefinder.utils;

import java.util.Comparator;

import com.ems.domain.PaySlip;


public class PaySlipComparator implements Comparator<PaySlip> {

	@Override
	public int compare(PaySlip o1, PaySlip o2) 
	{
		if(o1.getYear() > o2.getYear())
			return -1;
		else if (o1.getYear() < o2.getYear())
			return 1;
	
		
		if(o1.getMonth() > o2.getMonth())
			return -1;
		else if (o1.getMonth() < o2.getMonth())
			return 1;
		
		return 0;
	}
	
	

}
