package application;

public class KpnRay {
	//Начальная вершина дуги
	private KpnPoint startId;
	//Конечная вершина дуги
	private KpnPoint endId;
	/**
	 * Конструктор дуги
	 * @param startId
	 * @param endId
	 */
	public KpnRay(KpnPoint startId, KpnPoint endId) {		
		this.startId = startId;
		this.endId = endId;
	}

	public KpnPoint getStartId() {
		return startId;
	}

	public KpnPoint getEndId() {
		return endId;
	}
	

}
