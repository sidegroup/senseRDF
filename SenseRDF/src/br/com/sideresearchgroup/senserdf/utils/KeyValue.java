/**
 * 
 */
package br.com.sideresearchgroup.senserdf.utils;

/**
 * @author ayrton
 *
 */
public class KeyValue implements Comparable<KeyValue>{
	private int key;
	private String value;
	
	public KeyValue(int key, String value){
		this.key  = key;
		this.value = value;
	}
	
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public int compareTo(KeyValue outroKey) {       
		if (this.key == outroKey.key)
			return 1;
		
		return 0;
    }
}
