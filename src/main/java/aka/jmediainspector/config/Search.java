//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.11
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2017.06.25 à 03:52:16 PM CEST
//

package aka.jmediainspector.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour anonymous complex type.
 *
 * <p>
 * Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="criterias"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="criteria" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="subtype" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                           &lt;/sequence&gt;
 *                           &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="selected" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                           &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "name",
        "criterias"
})
@SuppressWarnings("javadoc")
public class Search {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected Criterias criterias;
    @XmlAttribute(name = "type", required = true)
    protected String type;

    /**
     * Obtient la valeur de la propriété name.
     *
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getName() {
        return this.name;
    }

    /**
     * Définit la valeur de la propriété name.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     * 
     */
    public void setName(final String value) {
        this.name = value;
    }

    /**
     * Obtient la valeur de la propriété criterias.
     *
     * @return
     *         possible object is
     *         {@link Criterias }
     * 
     */
    public Criterias getCriterias() {
        return this.criterias;
    }

    /**
     * Définit la valeur de la propriété criterias.
     *
     * @param value
     *            allowed object is
     *            {@link Criterias }
     * 
     */
    public void setCriterias(final Criterias value) {
        this.criterias = value;
    }

    /**
     * Obtient la valeur de la propriété type.
     *
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getType() {
        return this.type;
    }

    /**
     * Définit la valeur de la propriété type.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     * 
     */
    public void setType(final String value) {
        this.type = value;
    }

}
