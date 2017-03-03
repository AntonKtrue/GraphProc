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

	//Создание глобального списка
	private void makeGlobalList()
	{
		globalList = new LinkedHashMap<>();
		//Сгенерируем структуру глобального списка в соответсвии с указанным порядком
		for(Integer type : globalOrder)
		{
			LinkedList<KpnPoint> typeElements = new LinkedList<>();
			globalList.put(type, typeElements);	
		}		
		//Заполним глобальный список
		
		//for(Map.Entry<String, KpnPoint> pair : kpnGraph.getKpnPoints().entrySet())	
		for(KpnPoint point : kpnGraph.getCleanKpnPoints())
		{
			
			if(globalOrder.contains(point.getType())) 
			{
				globalList.get(point.getType()).add(point);
			}
		}		
		//посмотрим что получилось, пронумеруем позиции
		int pos=0;
		for(Map.Entry<Integer, LinkedList<KpnPoint>> pair : globalList.entrySet())
		{
			for(KpnPoint point : pair.getValue())
			{
				point.setPosition(++pos);
				//System.out.println("point type:" + pair.getKey() + "; point " + point.getOooName() + "; позиция: " + point.getPosition());				
			}
		}
		
	}
	
	/**
	 * Нахождение стека досягаемости для АСУТП
	 * @param startPoint - начальная вершина
	 * @param devide - тип граничных вершин
	 * @param matrix - матрица смежности общего графа
	 * @return
	 */
	private List<KpnPoint> findStack(KpnPoint startPoint, int devide, int[][] matrix)
	{
		//s - указатель стека, ActivePoint - вершина из стека, обрабатываемая в данный момент
		int  s=0, activePoint;
		//Переменные прохождения матрицы смежности.
		int i, j;
		// Общее количество вершин.
		int pointsCount = kpnGraph.getCleanKpnPoints().size();
		//Стэк досягаемости
		List<KpnPoint> stack = new ArrayList<>(pointsCount/2);
		//Информация о занесенных в стек вершинах
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
			if(point.getLocalId() == -1 && point.getType() != 99) //Если вершина не распределена и не делитель :)
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
	
	//Создание матриц смежности данного графа. Орграфной и соединений
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
