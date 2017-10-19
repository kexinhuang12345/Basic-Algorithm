package spartan;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Solution {

	public static void main(String[] args){
		
		Scanner input = new Scanner(System.in);
		int count=input.nextInt();
		ArrayList<Object> heap=new ArrayList<Object>();
		node m=new node("mei",0,0);
		heap.add(m);
		Hashtable store=new Hashtable();
		int position=1;
		for(int i=0;i<count;i++){
			String key=input.next();
			long value=input.nextLong();
			//System.out.println(value);
			node x=new node(key,value,position);
			position++;
			store.put(key, x);
			heap.add(store.get(key));
			//System.out.println(((node)store.get(key)).value);
		}
		heapify(heap,store);
		count=input.nextInt();
		for(int i=0;i<count;i++){
			int type=input.nextInt();
			if(type==1){
				String key=input.next();
				long value=input.nextLong();
				update(heap,store,key,value);
				//System.out.println("update");
				//for(int x=1;x<heap.size();x++){
					//System.out.println("key: "+((node)heap.get(x)).key+" value: "+((node)heap.get(x)).value+" position: "+((node)heap.get(x)).position);
				//}
				//need to balance the heap again
			}else{
				long standard=input.nextLong();
				int countPass=0;
				countPass=count(heap,store,1,standard,countPass);
				int size=heap.size();
				for(int j=0;j<countPass;j++){
					deleteMin(heap,store);
				}
				//System.out.println("delete");
				//for(int x=1;x<heap.size();x++){
					//System.out.println("key: "+((node)heap.get(x)).key+" value: "+((node)heap.get(x)).value+" position: "+((node)heap.get(x)).position);
				//}
				System.out.println(size-1-countPass);
			}
						
		}
		
		input.close();
	}
	

	static void update(ArrayList<Object> heap, Hashtable store,String key, long value){
		
		node temp=(node) store.get(key);
		temp.value+=value;
		//need to do balance 
		sinkDown(heap,store,key,temp.position);
		
	}
	
	static void swap(ArrayList<Object> heap, Hashtable store,String key,String key2){
		node x=(node) store.get(key);
		node x2=(node) store.get(key2);
		heap.set(x.position,store.get(key2));
		heap.set(x2.position,store.get(key));
		int temp=x.position;
		x.position=x2.position;
		x2.position=temp;		
	}
	
	static void sinkDown(ArrayList<Object> heap, Hashtable store,String key,int position){
		//recursively
		long smaller=0;
		String smallerKey="meiyou";
			if(2*position<heap.size()){
				if(((node)heap.get(position)).value>((node)heap.get(2*position)).value){
					smaller=((node)heap.get(2*position)).value;
					smallerKey=((node)heap.get(2*position)).key;
				}
			}
			if(2*position+1<heap.size()){
				if(((node)heap.get(position)).value>((node)heap.get(2*position+1)).value){
					if(smaller!=0){
						if(smaller>((node)heap.get(2*position+1)).value){
							smallerKey=((node)heap.get(2*position+1)).key;
						}
					}else{
						
						smallerKey=((node)heap.get(2*position+1)).key;
					}
					
					
				}
			}
			if(!smallerKey.equals("meiyou")){
				swap(heap,store,smallerKey,key);
				sinkDown(heap,store,key,((node)store.get(key)).position);
			}
	}
		
		
	
	
	static int count(ArrayList<Object> heap, Hashtable store, int position,long standard,int count){
		if(((node)heap.get(position)).value<standard){
			count++;
			if(2*position<heap.size()){
				count=count(heap,store,2*position,standard,count);
			}
			if(2*position+1<heap.size()){
				count=count(heap,store,2*position+1,standard,count);
			}
		}
		return count;
	}
	
	static void deleteMin(ArrayList<Object> heap, Hashtable store){
		swap(heap,store,((node)heap.get(1)).key,((node)heap.get(heap.size()-1)).key);
		heap.remove(heap.size()-1);
		sinkDown(heap,store,((node)heap.get(1)).key,1);
		
	}
	
	static void heapify(ArrayList<Object> heap, Hashtable store){
		//notice change the position of node to corresponding array
		int length=heap.size();
		int parent=(length-1)/2;
		if(length%2==0){
			//the last node has no brother
			if(((node)heap.get(length-1)).value<((node)heap.get(parent)).value){
				swap(heap,store,((node)heap.get(length-1)).key,((node)heap.get(parent)).key);
			}
			
			for(int i=length-2;i>1;i=i-2){
				int parent2=i/2;
				sinkDown(heap,store,((node)heap.get(parent2)).key,parent2);
				
			}
			
		}else{
		
			for(int i=length-1;i>1;i=i-2){
				int parent2=i/2;
				sinkDown(heap,store,((node)heap.get(parent2)).key,parent2);
			}
		}			
		
	}
	
}


class node{
	
	long value;
	int position;
	String key;
	public node(String key1,long value1, int position1){
		key=key1;
		value=value1;
		position=position1;
	}
	
	
}

