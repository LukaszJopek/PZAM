package refleksja;

public class Refleksja {
	public String s;
	private int value;
	public static int staticValue = 0;
	private int getValue() {
		return value;
	}
	private void setValue(int value) {
		this.value = value;
	}
	public void publicSetValue(int value) {
		this.value = value;
	}
	
	public void printValue() {
		System.out.println("value is "+value);
	}
}
