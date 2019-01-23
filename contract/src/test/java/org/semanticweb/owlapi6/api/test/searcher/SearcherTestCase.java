/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi6.api.test.searcher;

import static org.junit.Assert.assertTrue;
import static org.semanticweb.owlapi6.api.test.TestEntities.C;
import static org.semanticweb.owlapi6.api.test.TestEntities.D;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.Boolean;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.DataProperty;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.DataPropertyDomain;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.DataPropertyRange;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.EquivalentDataProperties;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.EquivalentObjectProperties;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.IRI;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.ObjectProperty;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.ObjectPropertyDomain;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.ObjectPropertyRange;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.SubDataPropertyOf;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.SubObjectPropertyOf;
import static org.semanticweb.owlapi6.model.parameters.Imports.INCLUDED;
import static org.semanticweb.owlapi6.search.Searcher.domain;
import static org.semanticweb.owlapi6.search.Searcher.equivalent;
import static org.semanticweb.owlapi6.search.Searcher.range;
import static org.semanticweb.owlapi6.search.Searcher.sub;
import static org.semanticweb.owlapi6.search.Searcher.sup;
import static org.semanticweb.owlapi6.utilities.OWLAPIStreamUtils.asUnorderedSet;
import static org.semanticweb.owlapi6.utilities.OWLAPIStreamUtils.contains;

import java.util.Collection;

import org.junit.Test;
import org.semanticweb.owlapi6.api.test.baseclasses.TestBase;
import org.semanticweb.owlapi6.model.AxiomType;
import org.semanticweb.owlapi6.model.OWLAxiom;
import org.semanticweb.owlapi6.model.OWLClass;
import org.semanticweb.owlapi6.model.OWLDataProperty;
import org.semanticweb.owlapi6.model.OWLObjectProperty;
import org.semanticweb.owlapi6.model.OWLOntology;
import org.semanticweb.owlapi6.search.Filters;

public class SearcherTestCase extends TestBase {

    @Test
    public void shouldSearch() {
        // given
        OWLOntology o = getOWLOntology();
        OWLAxiom ax = SubClassOf(C, D);
        o.addAxiom(ax);
        assertTrue(contains(o.axioms(AxiomType.SUBCLASS_OF), ax));
        assertTrue(contains(o.axioms(C), ax));
    }

    @Test
    public void shouldSearchObjectProperties() {
        // given
        OWLOntology o = getOWLOntology();
        OWLObjectProperty c = ObjectProperty(IRI("urn:test#", "c"));
        OWLObjectProperty d = ObjectProperty(IRI("urn:test#", "d"));
        OWLObjectProperty e = ObjectProperty(IRI("urn:test#", "e"));
        OWLClass x = Class(IRI("urn:test#", "x"));
        OWLClass y = Class(IRI("urn:test#", "Y"));
        OWLAxiom ax = SubObjectPropertyOf(c, d);
        OWLAxiom ax2 = ObjectPropertyDomain(c, x);
        OWLAxiom ax3 = ObjectPropertyRange(c, y);
        OWLAxiom ax4 = EquivalentObjectProperties(c, e);
        o.addAxiom(ax);
        o.addAxiom(ax2);
        o.addAxiom(ax3);
        o.addAxiom(ax4);
        assertTrue(contains(o.axioms(AxiomType.SUB_OBJECT_PROPERTY), ax));
        Collection<OWLAxiom> axioms = asUnorderedSet(o.axioms(Filters.subObjectPropertyWithSuper, d, INCLUDED));
        assertTrue(contains(sub(axioms.stream()), c));
        axioms = asUnorderedSet(o.axioms(Filters.subObjectPropertyWithSub, c, INCLUDED));
        assertTrue(contains(sup(axioms.stream()), d));
        assertTrue(contains(domain(o.objectPropertyDomainAxioms(c)), x));
        assertTrue(contains(equivalent(o.equivalentObjectPropertiesAxioms(c)), e));
    }

    @Test
    public void shouldSearchDataProperties() {
        // given
        OWLOntology o = getOWLOntology();
        OWLDataProperty c = DataProperty(IRI("urn:test#", "c"));
        OWLDataProperty d = DataProperty(IRI("urn:test#", "d"));
        OWLDataProperty e = DataProperty(IRI("urn:test#", "e"));
        OWLAxiom ax = SubDataPropertyOf(c, d);
        OWLClass x = Class(IRI("urn:test#", "x"));
        OWLAxiom ax2 = DataPropertyDomain(c, x);
        OWLAxiom ax3 = DataPropertyRange(c, Boolean());
        OWLAxiom ax4 = EquivalentDataProperties(c, e);
        o.addAxiom(ax);
        o.addAxiom(ax2);
        o.addAxiom(ax3);
        o.addAxiom(ax4);
        assertTrue(contains(o.axioms(AxiomType.SUB_DATA_PROPERTY), ax));
        assertTrue(contains(sub(o.axioms(Filters.subDataPropertyWithSuper, d, INCLUDED)), c));
        Collection<OWLAxiom> axioms = asUnorderedSet(o.axioms(Filters.subDataPropertyWithSub, c, INCLUDED));
        assertTrue(contains(sup(axioms.stream()), d));
        assertTrue(contains(domain(o.dataPropertyDomainAxioms(c)), x));
        assertTrue(contains(range(o.dataPropertyRangeAxioms(c)), Boolean()));
        assertTrue(contains(equivalent(o.equivalentDataPropertiesAxioms(c)), e));
    }
}
