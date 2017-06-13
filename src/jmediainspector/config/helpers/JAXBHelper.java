package jmediainspector.config.helpers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;

public class JAXBHelper {
    public static <T> T deepCopyJAXB(final T object, final Class<T> clazz) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            final JAXBElement<T> contentObject = new JAXBElement<>(new QName(clazz.getSimpleName()), clazz, object);
            final JAXBSource source = new JAXBSource(jaxbContext, contentObject);
            return jaxbContext.createUnmarshaller().unmarshal(source, clazz).getValue();
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deepCopyJAXB(final T object) {
        if (object == null) {
            throw new RuntimeException("Can't guess at class");
        }
        return deepCopyJAXB(object, (Class<T>) object.getClass());
    }
}
