package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class KpnGlobalList {
	private KpnGraph kpnGraph; // graph data of rays and points
	private List<Integer> globalOrder; //order of objects for processing into mpsa
	private Map<Integer,LinkedList<KpnPoint>> globalList; // GlobalList
	private int[][] matrix; // for MPSA distribute
	private int[][] orgraphMatrix; 
	/**
	 * @return the globalList
	 */
	public Map<Integer, LinkedList<KpnPoint>> getGlobalList() {
		return globalList;
	}
	/**
	 * @param globalOrder the globalOrder to set
	 */
	public void setGlobalOrder(LinkedList<Integer> globalOrder) {
		this.globalOrder = globalOrder;
	}
	public KpnGlobalList(KpnGraph kpnGraph, List<Integer> order) {		
		this.kpnGraph = kpnGraph;
		this.globalOrder = order;
		makeGlobalList();
		makeMatrix();
		devideByMpsa();
	}

	//�������� ����������� ������
	private void makeGlobalList()
	{
		globalList = new LinkedHashMap<>();
		//����������� ��������� ����������� ������ � ����������� � ��������� ��������
		for(Integer type : globalOrder)
		{
			LinkedList<KpnPoint> typeElements = new LinkedList<>();
			globalList.put(type, typeElements);	
		}		
		//�������� ���������� ������
		
		//for(Map.Entry<String, KpnPoint> pair : kpnGraph.getKpnPoints().entrySet())	
		for(KpnPoint point : kpnGraph.getCleanKpnPoints())
		{
			
			if(globalOrder.contains(point.getType())) 
			{
				globalList.get(point.getType()).add(point);
			}
		}		
		//��������� ��� ����������, ����������� �������
		int pos=0;
		for(Map.Entry<Integer, LinkedList<KpnPoint>> pair : globalList.entrySet())
		{
			for(KpnPoint point : pair.getValue())
			{
				point.setPosition(++pos);
				//System.out.println("point type:" + pair.getKey() + "; point " + point.getOooName() + "; �������: " + point.getPosition());				
			}
		}
		
	}
	
	/**
	 * ���������� ����� ������������ ��� �����
	 * @param startPoint - ��������� �������
	 * @param devide - ��� ��������� ������
	 * @param matrix - ������� ��������� ������ �����
	 * @return
	 */
	private List<KpnPoint> findStack(KpnPoint startPoint, int devide, int[][] matrix)
	{
		//s - ��������� �����, ActivePoint - ������� �� �����, �������������� � ������ ������
		int  s=0, activePoint;
		//���������� ����������� ������� ���������.
		int i, j;
		// ����� ���������� ������.
		int pointsCount = kpnGraph.getCleanKpnPoints().size();
		//���� ������������
		List<KpnPoint> stack = new ArrayList<>(pointsCount/2);
		//���������� � ���������� � ���� ��������
		boolean[] pointTest = new boolean[pointsCount];
		stack.add(startPoint);
		i = startPoint.getId();
		activePoint = 0;
		pointTest[i] = true;	
		while(i < pointsCount)
		  {
			  for(j = 0; j < pointsCount; j++)
			  {
				  if( (matrix[i][j] != 0) && 
						   !pointTest[j])
				  {
					  pointTest[j] = true;
					  s++;
					  stack.add(kpnGraph.getCleanKpnPoints().get(j));	
					  System.out.println(stack.contains((KpnPoint) kpnGraph.getCleanKpnPoints().get(j)));
				  }
			  }
			  do
			  {
				  activePoint++;
				  if(activePoint > s)
				  {
					  return stack;
				  }
			  }			
			  while(stack.get(activePoint).getType() == devide);
			  i = stack.get(activePoint).getId();
		  }
		  return stack;
	}
	

	private void devideByMpsa() {
		HashMap<Integer, Integer> typeAcc = new HashMap<>();
		int mpsaId = 0;
		for(KpnPoint point : kpnGraph.getCleanKpnPoints())
		{
			int localId = 1;
			if(point.getType() > 0)
			{
				int objectsId = typeAcc.containsKey(point.getType()) ? 
						typeAcc.get(point.getType()) : 0;
				objectsId++;	
				typeAcc.put(point.getType(), objectsId);
				point.setObjectId(objectsId);
			}
			if(point.getLocalId() == -1 && point.getType() != 99) //���� ������� �� ������������ � �� �������� :)
			{
				List<KpnPoint> sStack = findStack(point, 99, matrix);
				for(KpnPoint procPoint : sStack)
				{
					procPoint.setLocalId(localId++);
					procPoint.setMpsaId(mpsaId);
				}
				mpsaId++;
			}			
			System.out.println("Point: " + point.getId() +" " + point.getOooName() + 
								"; localId: " + point.getLocalId() + 
								"; mpsaId: " + point.getMpsaId());
		}
		System.out.println("Division by mpsa done :)");
	}
	
	//�������� ������ ��������� ������� �����. ��������� � ����������
	private void makeMatrix() {
		matrix = new int[kpnGraph.getCleanKpnPoints().size()][kpnGraph.getCleanKpnPoints().size()];
		orgraphMatrix = new int[kpnGraph.getCleanKpnPoints().size()][kpnGraph.getCleanKpnPoints().size()];
		for(KpnRay kpnRay : kpnGraph.getCleanKpnRays())
		{
			orgraphMatrix[kpnRay.getStartId().getId()][kpnRay.getEndId().getId()]=1;
			matrix[kpnRay.getStartId().getId()][kpnRay.getEndId().getId()]=1;
			matrix[kpnRay.getEndId().getId()][kpnRay.getStartId().getId()]=1;
		}		
	}

	
}
