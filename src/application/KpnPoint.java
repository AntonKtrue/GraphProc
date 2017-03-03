package application;

public class KpnPoint  {
	private String oooName; // Наименование вершины графа в документе OASIS, описывающей объект.
	private String descName; // Описание объекта, характеризующего данную вершину.
	private int id; // Идентификатор вершины в общем графе объекта
	private int objectId; //Порядковый номер объекта определённого типа
	private int localId; //Идентификатор вершины в части графа, принадлежащего конкретной АСУТП
	private int mpsaId; //Идентификатор АСУТП, к которой принадлежит данная вершина
	private int type; //Тип данной вершины
	private int position; //Позиция вершины в глобальном списке
	
	/**
	 * Конструктор вершины
	 * @param oooName 
	 * @param descName
	 */
	public KpnPoint(String oooName, String descName) {		
		this.oooName = oooName;		
		this.descName = descName;
		localId = -1;
		mpsaId = -1;
	}
	
	/**
	 * @return the objectId
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public String getOooName() {
		return oooName;
	}

	public int getType() {
		return type;
	}

	public void setType(int i) {
		this.type = i;
	}

	/**
	 * @return the descName
	 */
	public String getDescName() {
		return descName;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the localId
	 */
	public int getLocalId() {
		return localId;
	}

	/**
	 * @param localId the localId to set
	 */
	public void setLocalId(int localId) {
		this.localId = localId;
	}

	/**
	 * @return the mpsaId
	 */
	public int getMpsaId() {
		return mpsaId;
	}

	/**
	 * @param mpsaId the mpsaId to set
	 */
	public void setMpsaId(int mpsaId) {
		this.mpsaId = mpsaId;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	
	
}
