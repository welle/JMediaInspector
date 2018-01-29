package aka.jmediainspector.config.helpers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;

/**
 * JAXBHelper.
 *
 * @author charlottew
 */
public class JAXBHelper {

    /**
     * Copy in deep an object.
     *
     * @param object
     * @param clazz
     * @return deep copied object.
     */
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

    /**
     * Copy in deep an object.
     *
     * @param object
     * @return deep copied object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCopyJAXB(final T object) {
        if (object == null) {
            throw new RuntimeException("Can't guess at class");
        }
        return deepCopyJAXB(object, (Class<T>) object.getClass());
    }
}
