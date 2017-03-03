package application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class KpnGraph {	
	//Исходные данные о вершинах и дугах из документа OASIS
	private List<KpnPoint> kpnPoints;
	private List<KpnRay> kpnRays;
	//Подготовленные для загрузки в АСУТП данные о вершинах и дугах
	private List<KpnPoint> cleanKpnPoints;
	private List<KpnRay> cleanKpnRays;	
	
	/**
	 * Конструктор - список вершин и список дуг
	 * @param kpnPoints
	 * @param kpnRays
	 */
	public KpnGraph(List<KpnPoint> kpnPoints, List<KpnRay> kpnRays) {		
		this.kpnPoints = kpnPoints;
		this.kpnRays = kpnRays;
	}
	//Этапы подготовки к загрузке в мпса
	public void prepareMpsa() throws InterruptedException 
	{
		makeMpsaGraph();
		bindPoints();		
		List<Integer> order = new LinkedList<>();
		order.add(5);
		order.add(2); 
		order.add(4);
		order.add(11);
		order.add(12);
		order.add(6);
		KpnGlobalList gList = new KpnGlobalList(this, order);				
		for(KpnPoint kpnPoint : cleanKpnPoints)
		{
			System.out.println( 
							"Point name: " + kpnPoint.getOooName() + "\t" +
							"Point ID: " + kpnPoint.getId() + "\t" +
							"Point OBJID: " + kpnPoint.getObjectId() + "\t" +
							"Point LID: " + kpnPoint.getLocalId() + "\t" +
							"Point MPSA: " + kpnPoint.getMpsaId() + "\t" +
							"Point POSITION: " + kpnPoint.getPosition() + "\t" +
							"Point TYPE: " + kpnPoint.getType() + "\t"
							);
		
		}
		for(Map.Entry<Integer, LinkedList<KpnPoint>> pair : gList.getGlobalList().entrySet())
		{
			for(KpnPoint point : pair.getValue())
			{				
				System.out.println("point type:" + pair.getKey() + "; point " + point.getOooName() + "; позиция: " + point.getPosition());				
			}
		}
	}	
	//Назначение типов вершин в зависимости от наименования вершины
	private void bindPoints() {
		for(KpnPoint point : cleanKpnPoints)
		{
			String oooName = point.getOooName();
			 if(oooName.matches("^Z.*")) {				 
				 point.setType(1);
			 } else if (oooName.matches("^R.*")) {
				  point.setType(2);
			 } else if (oooName.matches("^MNA.*")) {
				  point.setType(3);
			 } else if (oooName.matches("^PNA.*")) {
				 point.setType(4);
			 } else if (oooName.matches("^Ki.*")) {
				 point.setType(5);
			 } else if (oooName.matches("^Ko.*")) {
				 point.setType(6);
			 } else if (oooName.matches("^S-.*")) {
				 point.setType(7);
			 } else if (oooName.matches("^KPP.*")) {
				 point.setType(8);
			 } else if (oooName.matches("^KIP.*")) {
				 point.setType(9);
			 } else if (oooName.matches("^SAR.*")) {
				 point.setType(10);
			 } else if (oooName.matches("^NSI-MNS.*")) {
				 point.setType(11);
			 } else if (oooName.matches("^NSO.*")) {
				 point.setType(12);
			 } else if (oooName.matches("^NSI-PNS.*")) {
				 point.setType(13);
			 } else if (oooName.matches("^BN-.*")) {
				 point.setType(14);
			 } else if (oooName.matches("^MPSA.*")) {
				 point.setType(99);
			 } else if (oooName.matches("^AR.*")) {
				 point.setType(2);
			 }
		}		
	}
	
	/**
	 * Сокращение незначимых вершин (вершины соединители для удобной разработки
	 * графа в визуальном редакторе). В мпса эти вершины затрудняют обработку.
	 * @throws InterruptedException 
	 */
	private void makeMpsaGraph() throws InterruptedException {
		cleanKpnPoints = new ArrayList<>();		
		cleanKpnPoints.addAll(kpnPoints);
		cleanKpnRays = new LinkedList<>(kpnRays);
		System.out.println("Count before: " + cleanKpnPoints.size() + " : " + cleanKpnRays.size());
		for(KpnPoint point : kpnPoints)
		{
			if(point.getOooName().matches("^U.*"))
			{
				//Удалим вершину из списка
				System.out.println("Union point: id: " + point.getId() + ", name: " 
						+ point.getOooName() + " remove: " + cleanKpnPoints.remove(point));
				//Найдем все дуги, которые начинаются или заканчиваются на данную вершину:
				ArrayList<KpnPoint> starts = new ArrayList<>();
				ArrayList<KpnPoint> ends = new ArrayList<>();
				List<KpnRay> tmpKpnRays = new LinkedList<>(cleanKpnRays);
				for(int i = 0; i < cleanKpnRays.size(); i++)
					{					
						if(cleanKpnRays.get(i).getStartId() == point)
						{
							ends.add(cleanKpnRays.get(i).getEndId());
							System.out.println("Remove ray: " + cleanKpnRays.get(i).getStartId().getOooName() + " " +
									cleanKpnRays.get(i).getEndId().getOooName() + ": " + tmpKpnRays.remove(cleanKpnRays.get(i)));
						
						} else if (cleanKpnRays.get(i).getEndId() == point)
						{
							starts.add(cleanKpnRays.get(i).getStartId());
							System.out.println("Remove ray: " + cleanKpnRays.get(i).getStartId().getOooName() + " " +
									cleanKpnRays.get(i).getEndId().getOooName() + ": " + tmpKpnRays.remove(cleanKpnRays.get(i)));
						}
					}
				for(int i = 0; i < starts.size(); i++) 
				{
					for(int j = 0; j < ends.size(); j++)
					{
						if(starts.get(i) != ends.get(j))
							tmpKpnRays.add(new KpnRay(starts.get(i), ends.get(j)));
					}
				}
				cleanKpnRays = new LinkedList<>(tmpKpnRays);
			}
		}		
		System.out.println("Count after: " + cleanKpnPoints.size() + " : " + cleanKpnRays.size());	
		for(int i = 0; i<cleanKpnPoints.size(); i++)
		{
			cleanKpnPoints.get(i).setId(i);
		}		
	}

	
	public KpnGraphUploadData getKpnGraphUploadData() {
		return new KpnGraphUploadData();
	}
	/**
	 * @return the cleanKpnPoints
	 */
	public List<KpnPoint> getCleanKpnPoints() {
		return cleanKpnPoints;
	}
	/**
	 * @return the cleanKpnRays
	 */
	public List<KpnRay> getCleanKpnRays() {
		return cleanKpnRays;
	}
	//Геттер карты вершин
	public List<KpnPoint> getKpnPoints() {
		return kpnPoints;
	}
	//Геттер списка дуг
	public List<KpnRay> getKpnRays() {
		return kpnRays;
	}
	

}
