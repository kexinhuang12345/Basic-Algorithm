import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args){
		Scanner input=new Scanner(System.in);
		int stage=input.nextInt();
		//create number of stage nodes
		@SuppressWarnings("unchecked")
		LinkedList<Node>[] graph=new LinkedList[stage];
		int[] deg=new int[stage];
		ArrayList<Node> S=new ArrayList<Node>();
		ArrayList<Node> L=new ArrayList<Node>();
		for (int i=0;i<stage;i++){
			Node store=new Node(i);
			LinkedList<Node> x=new LinkedList<Node>();
			x.add(store);
			graph[i]=x;
		}		
		int count=input.nextInt();
		for (int i=0;i<count;i++){
			int before=input.nextInt();
			int after=input.nextInt();
			deg[after-1]++;
			graph[before-1].add(graph[after-1].getFirst());
			
		}
		
		//initialize S
		for(int i=0;i<stage;i++){
			if(deg[i]==0){
				S.add(graph[i].getFirst());
			}
		}
		
		while(!S.isEmpty()){
			Node v=S.remove(getMin(S,stage));
			L.add(v);
			for(int i=0;i<graph[v.name].size();i++){
				deg[graph[v.name].get(i).name]--;
				if(deg[graph[v.name].get(i).name]==0){
					S.add(graph[v.name].get(i));
				}
			}
			
		}
		
		if(L.size()<stage){
			System.out.print(-1);
		}else{
			for(int i=0;i<L.size();i++){
				System.out.print((L.get(i).name+1)+" ");				
			}
		}
		input.close();
	}
	
	static int getMin(ArrayList<Node> s,int stage){
		int min=s.get(0).name;
		int index=0;
		for(int i=1;i<s.size();i++){
			if(s.get(i).name<min){
				min=s.get(i).name;
				index=i;
			}
			
		}
		
		return index;
	}
}

class Node{
	
	Node next;
	int position;
	int value;
	int name;
	
	public Node(int name1){
		
		name=name1;
	}
	
}
