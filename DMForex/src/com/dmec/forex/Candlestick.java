package com.dmec.forex;

public class Candlestick {
	public double open;
	public double close;
	public double high;
	public double low;
	private static final double SLOPE_THRESHOLD=0.0002;
	
	public Candlestick(double open, double high, double low, double close){
		this.open=open;
		this.close=close;
		this.high=high;
		this.low=low;
	}
	
	public static String getPattern(Candlestick current, Candlestick previous, double currMA,double prevMA){
		String result="";
		
		if((prevMA-currMA)>=SLOPE_THRESHOLD && current.open<=previous.close &&current.close>previous.open&&previous.close<previous.open){
			result="BULLISH_ENGULFING";
		}else if((currMA-prevMA)>=SLOPE_THRESHOLD && current.open>=previous.close && current.close<previous.open&&previous.close>previous.open){
			result="BEARISH_ENGULFING";
		}else{
			result="NONE";
		}
			
		return result;
	}
	
	
}
