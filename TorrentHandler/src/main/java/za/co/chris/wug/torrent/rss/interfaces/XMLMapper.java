package za.co.chris.wug.torrent.rss.interfaces;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface XMLMapper {

	/**
	 * Serializes assigned Object into a file with the assigned name.
	 * 
	 * @param object
	 *            - Object that should be serialized
	 * @param filename
	 *            - name of the XML-file
	 * @throws IOException
	 */
	public abstract void writeXmlObjectToOutputStream(Object object,OutputStream outputStream) throws IOException;

	/**
	 * Deserializes an object from the assigned file.
	 * 
	 * @param filename
	 *            - name of the file that should be deserialized
	 * @return deserialized object
	 * @throws IOException
	 */
	public abstract Object readXmlObjectFromInputStream(InputStream inputStream) throws IOException;

}