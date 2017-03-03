package application;

public class KpnRay {
	//��������� ������� ����
	private KpnPoint startId;
	//�������� ������� ����
	private KpnPoint endId;
	/**
	 * ����������� ����
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
