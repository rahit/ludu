package com.tahsinrahit.ludu;

public class LuduCourt {
	
	private int courdId;
	private String courtFileName;
	private String[] defaultPlayerNames;
	
	
	public LuduCourt() {
		// TODO Auto-generated constructor stub
	}
	
	public LuduCourt(int courdId, String courtFileurl,
			String[] defaultPlayerNames) {
		super();
		this.courdId = courdId;
		this.courtFileName = courtFileurl;
		this.defaultPlayerNames = defaultPlayerNames;
	}

	public int getCourdId() {
		return courdId;
	}

	public void setCourdId(int courdId) {
		this.courdId = courdId;
	}

	public String getCourtFileName() {
		return courtFileName;
	}

	public void setCourtFileName(String courtFileName) {
		this.courtFileName = courtFileName;
	}

	public String[] getDefaultPlayerNames() {
		return defaultPlayerNames;
	}

	public void setDefaultPlayerNames(String[] defaultPlayerNames) {
		this.defaultPlayerNames = defaultPlayerNames;
	}

	public String getPlayerName(int index) {
		return this.defaultPlayerNames[index];
	}

	
	
	
}
