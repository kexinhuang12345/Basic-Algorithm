import java.io.*;
import java.util.*;

public class Solution {
	public static void main(String args[]){
		Scanner input = new Scanner(System.in);
		TwoThreeTree tree23 = new TwoThreeTree();
		int count=input.nextInt();

		while(count>0){
			int type=input.nextInt();

			if (type==1){
				//query 1
				String key=input.next();
				int value=input.nextInt();
				twothree.insert(key,value,tree23);
			}else if(type==2){
				//query 2
				String key=input.next();
				System.out.println(twothree.search(key, (InternalNode)tree23.root,tree23.height));
			}
			count--;
		}
		input.close();
	}
	
	
	
}
