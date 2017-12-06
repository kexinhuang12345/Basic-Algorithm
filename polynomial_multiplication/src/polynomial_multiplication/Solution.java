package polynomial_multiplication;

import java.util.Scanner;

public class Solution {
	public static void main(String []args){
		Scanner input=new Scanner(System.in);
		int degree=input.nextInt();
		int a[]=new int[degree+1];
		int b[]=new int[degree+1];
		for(int i=degree;i>=0;i--){
			a[i]=input.nextInt();
		}
		for(int i=degree;i>=0;i--){
			b[i]=input.nextInt();
		}
		
		int[] result=karat(a,b);
		for(int i = 0; i < result.length / 2; i++)
		{
			//reverse the coefficients to make it start from 0 ... n
		    int temp = result[i];
		    result[i] = result[result.length - i - 1];
		    result[result.length - i - 1] = temp;
		}		
		for(int i=0;i<result.length;i++){
			System.out.print(result[i]+" ");
		}
		
		input.close();
		
	}
	
	static int[] karat(int[] a,int[] b){
		//only when a and b has the same length and a,b are even
		int result_len=a.length+b.length-1;
		int[] result=new int[result_len];
		if(a.length<=4){
			if(a.length==1){
				result[0]=a[0]*b[0];
			}else if(a.length==2){
				result[0]=a[0]*b[0];
				result[1]=a[0]*b[1]+a[1]*b[0];
				result[2]=a[1]*b[1];
			}else if(a.length==3){
				result[0]=a[0]*b[0];
				result[1]=a[0]*b[1]+a[1]*b[0];
				result[2]=a[0]*b[2]+a[1]*b[1]+a[2]*b[0];
				result[3]=a[1]*b[2]+a[2]*b[1];
				result[4]=a[2]*b[2];
			}else{
				result[0]=a[0]*b[0];
				result[1]=a[0]*b[1]+a[1]*b[0];
				result[2]=a[0]*b[2]+a[1]*b[1]+a[2]*b[0];
				result[3]=a[0]*b[3]+a[1]*b[2]+a[2]*b[1]+a[3]*b[0];
				result[4]=a[1]*b[3]+a[2]*b[2]+a[3]*b[1];
				result[5]=a[2]*b[3]+a[3]*b[2];
				result[6]=a[3]*b[3];
			}
		}else{
			int mid=a.length/2;
			int length=a.length;
			int[] a_high=new int[mid];
			int[] a_low=new int[mid];
			int[] b_high=new int[mid];
			int[] b_low=new int[mid];
			for(int i=0;i<length;i++){
				if(i<mid){
					a_high[i]=a[i];
					b_high[i]=b[i];
				}else{
					a_low[i-mid]=a[i];
					b_low[i-mid]=b[i];
				}
			}
			int[] x1=karat(a_high,b_high);
			int[] x2=karat(a_low,b_low);
			int[] sum_a=new int[mid];
			int[] sum_b=new int[mid];
			for(int i=0;i<mid;i++){
				sum_a[i]=a_high[i]+a_low[i];
				sum_b[i]=b_high[i]+b_low[i];
			}
			int[] x3=karat(sum_a,sum_b);
			int[] x4=new int[length-1];
			for(int i=0;i<length-1;i++){
				x4[i]=x3[i]-x2[i]-x1[i];
			}
			for(int i=0;i<x1.length;i++){
				result[i]=x1[i];
			}
			int count=0;
			for(int i=result_len-mid-x4.length;i<result_len-mid;i++){
				result[i]+=x4[count];
				count++;
			}
			int count1=0;
			for(int i=result_len-x2.length;i<result_len;i++){
				result[i]+=x2[count1];
				count1++;
			}	
		}
		return result;
	}
	
	static void print(int[] x){
		for(int i=0;i<x.length;i++){
			System.out.print(x[i]+" ");
		}
		System.out.println(" ");
	}
}
