package org.semanticweb.owlapi6.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.semanticweb.owlapi6.api.test.baseclasses.TestBase;
import org.semanticweb.owlapi6.utility.OntologyIRIShortFormProvider;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group
 * @since 3.5
 */
public class OWLOntologyIRIShortFormProviderTestCase extends TestBase {

    private static final String ONT = "ont";
    private static final String ont = "/ontologies/ont/";
    private static final String onts = "/ontologies/";
    public static final String SCHEME_DOMAIN = "http://www.semanticweb.org";
    private OntologyIRIShortFormProvider sfp = new OntologyIRIShortFormProvider();

    @Test
    public void shouldFindLastPathElement() {
        String shortForm = sfp.getShortForm(df.getIRI(SCHEME_DOMAIN + onts, ONT));
        assertThat(shortForm, is(equalTo(ONT)));
    }

    /*
     * A test to see if a meaningful short form is returned when the ontology IRI encodes version
     * information at the end. For example, the dublin core IRIs do this.
     */
    @Test
    public void shouldReturnLastNonNumericPathElement() {
        String shortForm =
            sfp.getShortForm(df.getIRI(SCHEME_DOMAIN + "/ontologies/ont/1.1.11", ""));
        assertThat(shortForm, is(equalTo(ONT)));
    }

    @Test
    public void shouldReturnLastNonVersionPathElement() {
        String shortForm =
            sfp.getShortForm(df.getIRI(SCHEME_DOMAIN + ont, "v1.1.11"));
        assertThat(shortForm, is(equalTo(ONT)));
    }

    @Test
    public void shouldReturnFullIRIForNoPath() {
        String shortForm = sfp.getShortForm(df.getIRI(SCHEME_DOMAIN, ""));
        assertThat(shortForm, is(equalTo(SCHEME_DOMAIN)));
    }

    @Test
    public void shouldStripAwayOWLExtension() {
        String shortForm = sfp.getShortForm(df.getIRI(SCHEME_DOMAIN + onts, "ont.owl"));
        assertThat(shortForm, is(equalTo(ONT)));
    }

    @Test
    public void shouldStripAwayRDFExtension() {
        String shortForm = sfp.getShortForm(df.getIRI(SCHEME_DOMAIN + onts, "ont.rdf"));
        assertThat(shortForm, is(equalTo(ONT)));
    }

    @Test
    public void shouldStripAwayXMLExtension() {
        String shortForm = sfp.getShortForm(df.getIRI(SCHEME_DOMAIN + onts, "ont.xml"));
        assertThat(shortForm, is(equalTo(ONT)));
    }

    @Test
    public void shouldStripAwayOBOExtension() {
        String shortForm = sfp.getShortForm(df.getIRI(SCHEME_DOMAIN + onts, "ont.obo"));
        assertThat(shortForm, is(equalTo(ONT)));
    }

    @Test
    public void shouldReturnSkosForSKOSNamespace() {
        String shortForm = sfp.getShortForm(df.getIRI("http://www.w3.org/2004/02/skos/", "core"));
        assertThat(shortForm, is(CoreMatchers.equalTo("skos")));
    }

    @Test
    public void shouldReturnDcForDublinCoreNamespace() {
        String shortForm = sfp.getShortForm(df.getIRI("http://purl.org/dc/elements/1.1", ""));
        assertThat(shortForm, is(CoreMatchers.equalTo("dc")));
    }

    @Test
    public void shouldReturnDcForDublinCoreNamespaceEndingWithSlash() {
        String shortForm = sfp.getShortForm(df.getIRI("http://purl.org/dc/elements/1.1/", ""));
        assertThat(shortForm, is(CoreMatchers.equalTo("dc")));
    }
}
