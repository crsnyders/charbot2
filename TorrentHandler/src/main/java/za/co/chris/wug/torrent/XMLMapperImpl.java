package za.co.chris.wug.torrent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Component;

import za.co.chris.wug.torrent.rss.interfaces.XMLMapper;

@Component
public class XMLMapperImpl implements XMLMapper {

	private final Logger logger = LoggerFactory.getLogger(XMLMapperImpl.class);

	@Autowired
	@Qualifier("jaxbMarshaller")
	private Marshaller marshaller;

	@Autowired
	@Qualifier("jaxbMarshaller")
	private Unmarshaller unmarshaller;

	@Override
	public void writeXmlObjectToOutputStream(Object object, OutputStream filename) throws IOException {
		try {
			this.marshaller.marshal(object, new StreamResult(filename));
		} catch (XmlMappingException e) {
			this.logger.error("Xml-Serialization failed due to an XmlMappingException.", e);
		} catch (IOException e) {
			this.logger.error("Xml-Serialization failed due to an IOException.", e);
		}
	}



	@Override
	public Object readXmlObjectFromInputStream(InputStream inputStream) throws IOException {
		try {
			return this.unmarshaller.unmarshal(new StreamSource(inputStream));
		} catch (IOException e) {
			this.logger.error("Xml-Deserialization failed due to an IOException.", e);
		}
		return null;
	}

}
