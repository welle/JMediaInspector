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
 *         &lt;element name="subtype" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="selected" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "subtype",
        "operator",
        "value"
})
@SuppressWarnings("javadoc")
public class Criteria {

    @XmlElement(required = true)
    protected String subtype;
    @XmlElement(required = true)
    protected String operator;
    @XmlElement(required = true)
    protected String value;
    @XmlAttribute(name = "type", required = true)
    protected String type;
    @XmlAttribute(name = "selected", required = true)
    protected boolean selected;
    @XmlAttribute(name = "required", required = true)
    protected boolean required;

    /**
     * Obtient la valeur de la propriété subtype.
     *
     * @return
     *         possible object is
     *         {@link String }
     *
     */
    public String getSubtype() {
        return this.subtype;
    }

    /**
     * Définit la valeur de la propriété subtype.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     *
     */
    public void setSubtype(final String value) {
        this.subtype = value;
    }

    /**
     * Obtient la valeur de la propriété operator.
     *
     * @return
     *         possible object is
     *         {@link String }
     *
     */
    public String getOperator() {
        return this.operator;
    }

    /**
     * Définit la valeur de la propriété operator.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     *
     */
    public void setOperator(final String value) {
        this.operator = value;
    }

    /**
     * Obtient la valeur de la propriété value.
     *
     * @return
     *         possible object is
     *         {@link String }
     *
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Définit la valeur de la propriété value.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     *
     */
    public void setValue(final String value) {
        this.value = value;
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

    /**
     * Obtient la valeur de la propriété selected.
     *
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Définit la valeur de la propriété selected.
     *
     */
    public void setSelected(final boolean value) {
        this.selected = value;
    }

    /**
     * Obtient la valeur de la propriété required.
     *
     */
    public boolean isRequired() {
        return this.required;
    }

    /**
     * Définit la valeur de la propriété required.
     *
     */
    public void setRequired(final boolean value) {
        this.required = value;
    }

}
