package de.luma.breakout.data.objects;

public interface IDecodable {

	/**
	 * Decodes the properties of this object from the given string.
	 * 
	 * @param line Line must be formatted like "value1,value2,value3,0.123,true"
	 */
	public void decode(String line);
	
	/**
	 * Encodes the properties of this object to a string.
	 * @return String formatted like "value1,value2,value3"
	 */
	public String encode();
	
}
