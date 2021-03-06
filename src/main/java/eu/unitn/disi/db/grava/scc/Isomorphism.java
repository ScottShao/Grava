package eu.unitn.disi.db.grava.scc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.unitn.disi.db.grava.graphs.Answer;
import eu.unitn.disi.db.grava.graphs.BigMultigraph;
import eu.unitn.disi.db.grava.graphs.Edge;
import eu.unitn.disi.db.grava.graphs.MappedEdge;
import eu.unitn.disi.db.grava.graphs.MappedNode;
import eu.unitn.disi.db.grava.graphs.Multigraph;

public class Isomorphism {
	private Map<Edge, Set<MappedEdge>> queryGraphEdges;
	private int threshold;
	private Collection<Edge> queryEdges;
	private int count;
	private Answer a;
	private Multigraph query;
	private Long startingNode;
	private Map<Long, Set<MappedNode>> queryGraphMapping;
	private int queryNodesNum;
	private LinkedList<Long> prevQueryNodes;
	private LinkedList<Long> searchList;
	private LinkedList<Edge> solutionEdges;
	private int mnStartingIndex;
	
	public Isomorphism() {
		queryGraphEdges = new HashMap<>();
		count = 0;
		a = new Answer();
		startingNode = null;
		mnStartingIndex = 0;
		
	}
	
	public void findIsomorphism(){
		Collection<Long> queryNodes =  query.vertexSet();
		queryNodesNum = queryNodes.size();
		solutionEdges = new LinkedList<Edge>();
		
		LinkedList<Long> prevGraphNodes = new LinkedList<Long>();
		prevQueryNodes = new LinkedList<Long>();
//		this.combine(unvisitedEdges, answer, threshold, 0);
//		Edge e = queryEdges.iterator().next();
//		Set<MappedEdge> mappedEdges = queryGraphEdges.get(e);
//		for(MappedEdge me : mappedEdges){
//			Set<Edge> unvisitedEdges = new HashSet<Edge>();
//			unvisitedEdges.addAll(this.queryEdges);
//			unvisitedEdges.remove(e);
//			if(me.getDis() > threshold){
//				continue;
//			}else{
//				a.put(e, me.getEdge());
//				answer.add(me.getEdge());
//				this.combine(unvisitedEdges, answer, threshold - me.getDis(), 1);
//			}
//		}
//		System.out.println("Total answer number:" + count);
		if(startingNode == null){
			startingNode = queryNodes.iterator().next();
		}
		Set<Long> visitedQueryNodes = new HashSet<Long>();
		
//		searchList.add(startingNode);
		prevGraphNodes.add(-1L);
//		for(Entry<Long, Set<MappedNode>> entry : queryGraphMapping.entrySet()){
//			System.out.println("query node " + entry.getKey());
//			for(MappedNode mn : entry.getValue()){
//				
//				System.out.println(mn);
//				if(mn.getMappedEdge() == null){
//					System.out.println("nullnullnulll");
//				}
//			}
//		}
		searchList  = this.computeSearchList(startingNode);
//		System.out.println("Search List");
//		for(Long node : searchList){
//			System.out.println(node);
//		}
		this.combineMappedNodes(-1L, 0, this.threshold, 0);
		System.out.println("total answer number:" + count);
//		Long graphNode;
//		Edge graphEdge;
//		for(MappedNode mn : mappedNodes){
//			graphEdge = mn.getMappedEdge();
//			System.out.println("Edge:" +graphEdge);
//			if(graphEdge == null){
//				prevGraphNodes.add(mn.getNodeID());
//				this.combineMappedNodes(visitedQueryNodes, prevGraphNodes, searchList, solutionEdges);
//			}else{
//				
//			}
//		}
//		
		
	}
	private void combineMappedNodes(Long prevGraphNode, int index, int remainingThreshold, int mappedNodeIndex){
		Long currentQueryNode = searchList.get(index);
		Set<MappedNode> mappedNodes = queryGraphMapping.get(currentQueryNode);
//		System.out.println(currentQueryNode + " " + mappedNodes.size());
		boolean isCheckingGraphNode = false;
		boolean isPrintingSolution = false;
		if(index > 0){
			Long prevForCurrentQNode = prevQueryNodes.get(index);
			Long prevForPrevQNode = prevQueryNodes.get(index -1);
//			System.out.println(prevForCurrentQNode + " " + prevForPrevQNode);
			if(prevForCurrentQNode == prevForPrevQNode){
				isCheckingGraphNode = true;
			}else{
				mappedNodeIndex = 0;
			}
		}
		if(index == searchList.size()-1){
			isPrintingSolution = true;
		}
//		System.out.println(index + " " + searchList.size());
		int i = 0;
		int tempIndex = mappedNodeIndex;
//		System.out.println(index + " size" + mappedNodes.size() );
		for(MappedNode mn: mappedNodes){
//			System.out.println(i + " " + mnStartingIndex);
//			System.out.println("node mapping:" + currentQueryNode + " " + mn.getNodeID() + " " + isCheckingGraphNode);
			Edge mappedEdge = mn.getMappedEdge();
//			System.out.println("depth:" + index + "  " + currentQueryNode + "   mapped node  " + mn.getNodeID() + "  mapped edge   " + mappedEdge);
//			System.out.println(isCheckingGraphNode);
//			System.out.println("solution edges size  " + solutionEdges.size());
			if(mappedEdge == null){
				this.combineMappedNodes(mn.getNodeID(), index+1, this.threshold, 0);
			}else{
					
						tempIndex++;
						if(mn.isLabelDif() && remainingThreshold == 0){
							continue;
						}else{
						Long node = mn.isIncoming() ? mappedEdge.getSource() : mappedEdge.getDestination();
						if(isCheckingGraphNode){
		//					System.out.println("node matching:" + node + " " + prevGraphNode + " " + (node.equals(prevGraphNode)));
							
							if(node.equals(prevGraphNode)){
								if(solutionEdges.contains(mappedEdge)){
									continue;
								}else{
//									System.out.println("addding " + mappedEdge);
									solutionEdges.addLast(mappedEdge);
									if(!isPrintingSolution){
										mnStartingIndex = tempIndex;
										if(mn.isLabelDif())
											
											this.combineMappedNodes(node, index+1, remainingThreshold-1, mnStartingIndex);
										else
											this.combineMappedNodes(node, index+1, remainingThreshold, mnStartingIndex);
									}else{
										this.printSolution(solutionEdges);
										count ++;
									}
									solutionEdges.pollLast();
								}
							}
						}else{
							
							if(!node.equals(prevGraphNode)){
								if(solutionEdges.contains(mappedEdge)){
									continue;
								}else{
//									System.out.println("addding " + mappedEdge);
									solutionEdges.add(mappedEdge);
									if(!isPrintingSolution){
										if(mn.isLabelDif())
											this.combineMappedNodes(node, index+1, remainingThreshold-1, 0);
										else
											this.combineMappedNodes(node, index+1, remainingThreshold, 0);
									}else{
										this.printSolution(solutionEdges);
										count ++;
									}
									solutionEdges.pollLast();
								}
							}
						}
					}
				}
			
			
			i++;
		}
	}
	
	private void combineMappedNodes(Set<Long> visitedQueryNodes, LinkedList<Long> prevGraphNodes, LinkedList<Long> searchList, LinkedList<Edge> solutionEdges){
		Long currentQueryNode = searchList.pollFirst();
		Long prevGraphNode = prevGraphNodes.pollFirst();
		System.out.println("queryNode " + currentQueryNode + " search list size" + searchList.size());
		Collection<Edge> incomingEdges = query.incomingEdgesOf(currentQueryNode);
		for(Edge e : incomingEdges){
			if(!searchList.contains(e.getSource())){
				searchList.addLast(e.getSource());
			}
			
		}
		Collection<Edge> outgoingEdges = query.outgoingEdgesOf(currentQueryNode);
		for(Edge e : outgoingEdges){
			if(searchList.contains(e.getDestination())){
				searchList.addLast(e.getDestination());
			}
		}
		
		Set<MappedNode> mappedNodes = queryGraphMapping.get(currentQueryNode);
		visitedQueryNodes.add(currentQueryNode);
		Long graphNode;
		Edge graphEdge;
		for(MappedNode mn : mappedNodes){
			graphEdge = mn.getMappedEdge();
			System.out.println("Edge:" +graphEdge);
			prevGraphNodes.add(mn.getNodeID());
			if(graphEdge == null){
				
				this.combineMappedNodes(visitedQueryNodes, prevGraphNodes, searchList, solutionEdges);
			}else{
				
				graphNode = mn.isIncoming()? graphEdge.getSource() : graphEdge.getDestination();
				if(graphNode == prevGraphNode){
					solutionEdges.add(graphEdge);
					if(visitedQueryNodes.size()== queryNodesNum){
						this.printSolution(solutionEdges);
					}
				}
				this.combineMappedNodes(visitedQueryNodes, prevGraphNodes, searchList, solutionEdges);
			}
		}
		solutionEdges.removeLast();
	}
	
	private LinkedList<Long> computeSearchList(Long startingNode){
		LinkedList<Long> searchList = new LinkedList<Long>();
		LinkedList<Long> temp = new LinkedList<Long>();
		prevQueryNodes.add(startingNode);
		searchList.addLast(startingNode);
		temp.addLast(startingNode);
		Long nodeToVisit;
		while(searchList.size() < queryNodesNum){
			nodeToVisit = temp.pollFirst();
			Collection<Edge> incomingEdges = query.incomingEdgesOf(nodeToVisit);
			for(Edge e : incomingEdges){
				if(searchList.contains(e.getSource())){
					continue;
				}else{
					searchList.addLast(e.getSource());
					temp.addLast(e.getSource());
					prevQueryNodes.add(nodeToVisit);
				}
			}
			Collection<Edge> outgoingEdges = query.outgoingEdgesOf(nodeToVisit);
			for(Edge e : outgoingEdges){
				if(searchList.contains(e.getDestination())){
					continue;
				}else{
					searchList.addLast(e.getDestination());
					temp.addLast(e.getDestination());
					prevQueryNodes.add(nodeToVisit);
				}
			}
		}
		return searchList;
	}
	private void printSolution(LinkedList<Edge> edges){
//		System.out.println("One query answer");
//		for(Edge e : edges){
//			System.out.println(e);
//		}
	}
	public void combine(Set<Edge> unvisitedEdges, LinkedList<Edge> answer, int threshold, int depth){
		Edge e = unvisitedEdges.iterator().next();
		Set<MappedEdge> mappedEdges = queryGraphEdges.get(e);
		if(unvisitedEdges.size() == 1){
			for(MappedEdge me : mappedEdges){
				if(me.getDis() > threshold){
					continue;
				}else{
					a.put(e, me.getEdge());
					if(this.checkAnswer()){
						this.printAnswer(answer, me.getEdge());
						count++;
					}
				}
			}
			answer.removeLast();
		}else{
			unvisitedEdges.remove(e);
			for(MappedEdge me : mappedEdges){
				if(me.getDis() > threshold){
					continue;
				}else{
					a.put(e, me.getEdge());
					answer.add(me.getEdge());
					this.combine(unvisitedEdges, answer, threshold - me.getDis(), depth+1);
				}

			}
			if(answer.size() != 0){
				answer.removeLast();
			}
		}
	}
	
	public boolean checkAnswer(){
		Collection<Long> vertexes = query.vertexSet();
		for(Long node : vertexes){
			Collection<Edge> incomeEdges = query.incomingEdgesOf(node);
			long nodeID = -1;
			for(Edge edge : incomeEdges){
				Edge gEdge = this.a.get(edge);
				if(nodeID == -1){
					nodeID = gEdge.getDestination();
				}else{
					if(nodeID != gEdge.getDestination()){
						return false;
					}
				}
				
			}
			Collection<Edge> outcomeEdges = query.outgoingEdgesOf(node);
			nodeID = -1;
			for(Edge edge : outcomeEdges){
				Edge gEdge = this.a.get(edge);
				if(nodeID == -1){
					nodeID = gEdge.getSource();
				}else{
					if(nodeID != gEdge.getSource()){
						return false;
					}
				}
				
			}
		}
		return true;
	}
	
	private void printAnswer(LinkedList<Edge> answer, Edge edge){
		System.out.println("One query answer");
		for(Edge e : answer){
			System.out.println(e.toString());
		}
		System.out.println(edge.toString());
		
	}
	public void mappingEdges(Map<Long, Set<MappedNode>> queryGraphMapping){
		long src;
		long des;
		Set<MappedNode> srcMappedNodes;
		Set<MappedNode> desMappedNodes;
		Set<MappedEdge> mappedEgdes = null;
		MappedEdge me;
		for(Edge e : this.queryEdges){
			mappedEgdes = new HashSet<MappedEdge>();
			src = e.getSource();
			des = e.getDestination();
			srcMappedNodes = queryGraphMapping.get(src);
			desMappedNodes = queryGraphMapping.get(des);
			for(MappedNode srcMn : srcMappedNodes){
				for(MappedNode desMn : desMappedNodes){
					me = this.checkConnection(srcMn, desMn);
					if(me != null){
						mappedEgdes.add(me);
					}
				}
			}
			queryGraphEdges.put(e, mappedEgdes);
		}
	}
	
	private MappedEdge checkConnection(MappedNode srcMn, MappedNode desMn){
		long src = srcMn.getNodeID();
		long des = desMn.getNodeID();
		Edge temp = null;
		Edge result = null;
		int dis = -1;
		if(srcMn.getMappedEdge() == null && desMn.getMappedEdge() == null){
			return null;
		}else if(srcMn.getMappedEdge() == null){
			result = this.match(src, desMn);
			dis = desMn.getDist();
		}else if (desMn.getMappedEdge() == null){
			result = this.match(des, srcMn);
			dis = srcMn.getDist();
		}else{
			if ((result = this.match(src, desMn)) == null){
				result = this.match(des, srcMn);
				dis = srcMn.getDist();
			}else{
				dis = desMn.getDist();
			}
		}
		if(result != null){
			return new MappedEdge(result, dis);
		}else{
			return null;
		}
		
	}
	
	private Edge match(long node, MappedNode mn){
		Edge e = mn.getMappedEdge();
		Edge result = null;
		if(mn.isIncoming()){
			if(e.getSource() == node){
				result = mn.getMappedEdge();
			}
		}else{
			if(e.getDestination() == node){
				result = mn.getMappedEdge();
			}
		}
		return result;
	}

	public Map<Edge, Set<MappedEdge>> getQueryGraphEdges() {
		return queryGraphEdges;
	}

	public void setQueryGraphEdges(Map<Edge, Set<MappedEdge>> queryGraphEdges) {
		this.queryGraphEdges = queryGraphEdges;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public Collection<Edge> getQueryEdges() {
		return queryEdges;
	}

	public void setQueryEdges(Collection<Edge> queryEdges) {
		this.queryEdges = queryEdges;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Answer getA() {
		return a;
	}

	public void setA(Answer a) {
		this.a = a;
	}

	public Multigraph getQuery() {
		return query;
	}

	public void setQuery(Multigraph query) {
		this.query = query;
	}

	public Long getStartingNode() {
		return startingNode;
	}

	public void setStartingNode(Long startingNode) {
		this.startingNode = startingNode;
	}

	public Map<Long, Set<MappedNode>> getQueryGraphMapping() {
		return queryGraphMapping;
	}

	public void setQueryGraphMapping(Map<Long, Set<MappedNode>> queryGraphMapping) {
		this.queryGraphMapping = queryGraphMapping;
	}
	
	

}
