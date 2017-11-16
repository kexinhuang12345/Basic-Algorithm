package dijsktra;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Solution {
	public static void main(String args[]){
		Scanner input = new Scanner(System.in);
		int v=input.nextInt();
		int e=input.nextInt();
		LinkedList<Node>[] graph=new LinkedList[2*v];
		int [][] weight=new int[2*v][2*v];
		for(int i=0;i<2*v;i++){
			for(int j=0;j<2*v;j++){
				weight[i][j]=Integer.MAX_VALUE;	
			}
		}
		Node[] node=new Node[2*v];
		int[] d=new int[2*v];
		int max=Integer.MAX_VALUE;
		//initialize adjacency list, with each array as a linkedlist 
		for(int i=0;i<2*v;i++){
			node[i]=new Node(i+1);
			LinkedList<Node> x=new LinkedList();
			graph[i]=x;
			d[i]=max;
		}
		d[0]=0;
		//initialize the edge where weight is stored in the end node 
		for(int i=0;i<e;i++){
			int start=input.nextInt();
			int end=input.nextInt();
			int weight1=input.nextInt();
			if(weight1==1){
				graph[start-1].add(node[end+v-1]);
				weight[start-1][end+v-1]=0;
			}
			graph[start-1].add(node[end-1]);
			graph[start-1+v].add(node[end+v-1]);
			weight[start-1][end-1]=1;
			weight[start-1+v][end-1+v]=weight1-1;
		}
		
        Comparator<Node> comparator = new NodeComparator();
		PriorityQueue<Node> q=new PriorityQueue<Node>(comparator);
		node[0].dvalue=0;
		q.add(node[0]);
		while(!q.isEmpty()){
			Node temp=q.poll();
			for(int i=0;i<graph[temp.name-1].size();i++){
				int w=graph[temp.name-1].get(i).name;
				if(d[temp.name-1]+weight[temp.name-1][w-1]<d[w-1]){
					if(d[w-1]==max){
						q.add(node[w-1]);
					}
					d[w-1]=d[temp.name-1]+weight[temp.name-1][w-1];
					node[w-1].dvalue=d[w-1];
					//System.out.println(d[v-1]);
					if(q.contains(node[w-1])){
						q.remove(node[w-1]);
						q.add(node[w-1]);
					}
				}
			}
		}
		if(d[2*v-1]==max){
			System.out.print(-1);
		}else{
			System.out.print(d[2*v-1]);
		}
		input.close();
	}
	
}


class NodeComparator implements Comparator<Node>{

	@Override
	public int compare(Node o1, Node o2) {
		if(o1.dvalue<o2.dvalue){
			return -1;
		}
		if(o1.dvalue>o2.dvalue){
			return 1;
		}
		return 0;
	}
	
	
	
	
}

class Node{
	int name;
	int dvalue=Integer.MAX_VALUE;
	public Node(int name1){
		name=name1;
	}
}