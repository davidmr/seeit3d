package seeit3d.view.listeners;

public enum ChangeLevelOption {

	NEXT_LEVEL(true), PREVIOUS_LEVEL(false);
	
	public final boolean moreDetail;

	private ChangeLevelOption(boolean moreDetail) {
		this.moreDetail = moreDetail;
	}
	
	

}
