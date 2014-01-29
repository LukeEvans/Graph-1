package com.winston.utilities.comparators;

public class StringLengthComparator implements java.util.Comparator<String> {

    public StringLengthComparator() {
        super();
    }

    public int compare(String s1, String s2) {
    	
    	if (s1 == null || s2 == null) {
    		return 0;
    	}
    	
    	int len1 = s1.length();
    	int len2 = s2.length();

    	if (len1 < len2) {
    		return 1;
    	}
    	
    	if (len1 > len2) {
    		return -1;
    	}
    	
    	return 0;
    }
}