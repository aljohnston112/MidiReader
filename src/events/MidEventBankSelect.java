package events;

public final class MidEventBankSelect extends MidEvent {

	private final int bankNumber;
	
	private final int channel;
	
	private MidEventBankSelect() {
		throw new AssertionError("Default MidEventBankSelect constructor is unsupported");
	}
	
	public MidEventBankSelect(int channel, int bankNumber) {
		this.channel = channel;
		this.bankNumber = bankNumber;
	}

	public int getBankNumber() {
		return bankNumber;
	}

	public int getChannel() {
		return channel;
	}

}