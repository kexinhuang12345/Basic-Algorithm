package full_dfs;

import java.util.ArrayList;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args){
		Scanner input=new Scanner(System.in);
		int v=input.nextInt();
		int e=input.nextInt();
		ArrayList<Node>[] graph=new ArrayList[v];
		Node[] nodes=new Node[v];
		for(int i=0;i<v;i++){
			Node x=new Node(0,i);
			nodes[i]=x;
			ArrayList<Node> x1=new ArrayList<Node>();
			graph[i]=x1;
		}
		for(int i=0;i<e;i++){
			int start=input.nextInt();
			int end=input.nextInt();
			graph[start-1].add(nodes[end-1]);
		}
		
		Node[] parent=new Node[v];
		int[] backedge=new int[2];
		DFS(nodes[0],parent,backedge,graph);
		//System.out.println(backedge[0]+" "+backedge[1]);
		if(backedge[0]==0&&backedge[1]==0){
			System.out.print(0);
		}else{
			int count=backedge[0];
			ArrayList<Integer> cycle=new ArrayList<Integer>();
			while(parent[count]!=nodes[backedge[1]]){
				cycle.add(count+1);
				count=parent[count].value;
			}
			cycle.add(count+1);
			cycle.add(backedge[1]+1);
			//System.out.println(cycle.size());
			System.out.println(1);
			for(int i=cycle.size()-1;i>=0;i--){
				if(i!=0){
					System.out.print(cycle.get(i)+" ");
				}else{
					System.out.print(cycle.get(i));
				}
				
			}
			
		}
		
	}
	
	static void DFS(Node n,Node[] parent,int[] backedge, ArrayList<Node>[] graph){
		n.color=1;
		for(int i=0; i<graph[n.value].size();i++){
			//nbh of n
			if(graph[n.value].get(i).color==1){
				backedge[0]=n.value;
				backedge[1]=graph[n.value].get(i).value;
				//parent[graph[n.value].get(i).value]=n;
				//System.out.println(graph[n.value].get(i).value);
			}else if(graph[n.value].get(i).color==0){
				parent[graph[n.value].get(i).value]=n;
				//System.out.println(graph[n.value].get(i).value);
				DFS(graph[n.value].get(i),parent,backedge,graph);
			}
		}
		n.color=2;

	}
	
	
}


class Node{
	int color;
	//White 0, grey 1, black 2
	int value;
	public Node(int color1,int value1){
		color=color1;
		value=value1;
	}
	
}
