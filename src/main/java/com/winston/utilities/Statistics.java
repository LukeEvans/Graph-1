package com.winston.utilities;

import java.util.ArrayList;
import java.util.Collections;

public class Statistics 
{
	ArrayList<Long> data;
	long size;    

	public Statistics(ArrayList<Long> data) 
	{
		this.data = data;
		size = data.size();
	}   

	public long getMean()
	{
		if (size == 0) {
			return 0;
		}
		
		long sum = 0;
		for(long a : data)
			sum += a;
		return sum/size;
	}

	public long getVariance()
	{
		if (size == 0) {
			return 0;
		}
		
		long mean = getMean();
		long temp = 0;
		for(long a :data)
			temp += (mean-a)*(mean-a);
		return temp/size;
	}

	public long getStdDev()
	{
		if (size == 0) {
			return 0;
		}
		
		return (long) Math.sqrt(getVariance());
	}

	public long getMax() {
		if (size == 0) {
			return 0;
		}
		
		long max = data.get(0);
		
		for (Long l : data) {
			if (l > max) {
				max = l;
			}
		}
		
		return max;
	}
	
	public long getMedian() 
	{
		if (size == 0) {
			return 0;
		}
		
		Collections.sort(data);

		if (data.size() % 2 == 0) 
		{
			int indexA = (data.size() / 2) - 1;
			int indexB = (data.size() / 2) / 2;
			
			return data.get(indexA) + data.get(indexB);
		} 
		else 
		{
			return data.get(data.size() / 2);
		}
	}
}