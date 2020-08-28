package events;

public final class MidEventBankSelect extends MidEvent {

	final int bankNumber;
	
	final int channel;
	
	private MidEventBankSelect() {
		throw new AssertionError("Default MidEventBankSelect constructor is unsupported");
	}
	
	public MidEventBankSelect(int channel, int bankNumber) {
		this.channel = channel;
		this.bankNumber = bankNumber;
	}

}