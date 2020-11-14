package com.yachugak.topla.util;

public class HalfMinutesTime {
	//30분 단위의 시간인지 판별합니다.
	public static boolean isHalfMinutesTime(int minutes) {
		int residual = minutes % 30;
		
		return residual == 0;
	}
	
	//30분 단위의 시간이 아니면 반올림하여 30분 단위로 맞춥니다.
	public static int roundUpAndDown(int minutes) {
		int residual = positiveResidual(minutes % 30);
		
		if(residual == 0) {
			return minutes;
		}
		
		if(residual < 15) {
			return minutes - residual;
		}
		else {
			return minutes + (30 - residual);
		}
	}
	
	//30분 단위의 시간이 아니면 내려서 30분 단위로 맞춥니다.
	public static int roundDown(int minutes) {
		int residual = positiveResidual(minutes % 30);

		if(residual == 0) {
			return minutes;
		}
		
		return minutes - residual;
	}
	
	//30분 단위의 시간이 아니면 올려서 30분 단위로 맞춥니다.
	public static int roundUp(int minutes) {
		int residual = positiveResidual(minutes % 30);
		
		if(residual == 0) {
			return minutes;
		}
		
		return minutes + (30 - residual);
	}
	
	//나머지가 음수로 표현된 경우 양수로 바꿉니다.
	private static int positiveResidual(int residual) {
		if(residual < 0) {
			return residual + 30;
		}
		
		return residual;
	}
}
