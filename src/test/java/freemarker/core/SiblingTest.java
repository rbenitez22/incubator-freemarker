/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package freemarker.core;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import freemarker.ext.dom.NodeModel;
import freemarker.template.TemplateException;
import freemarker.test.TemplateTest;

public class SiblingTest extends TemplateTest {

    @Before
    public void setUp() throws SAXException, IOException, ParserConfigurationException {
        InputSource is = new InputSource(getClass().getResourceAsStream("siblingDataModel.xml"));
        addToDataModel("doc", NodeModel.parse(is));
    }

    @Test
    public void testBlankPreviousSibling() throws IOException, TemplateException {
        assertOutput("${doc.person.name?previousSibling}", "\n    ");
    }

    @Test
    public void testNonBlankPreviousSibling() throws IOException, TemplateException {
        assertOutput("${doc.person.address?previousSibling}", "12th August");
    }

    @Test
    public void testBlankNextSibling() throws IOException, TemplateException {
        assertOutput("${doc.person.name?nextSibling}", "\n    ");
    }

    @Test
    public void testNonBlankNextSibling() throws IOException, TemplateException {
        assertOutput("${doc.person.dob?nextSibling}", "Chennai, India");
    }

    @Test
    public void testNullPreviousSibling() throws IOException, TemplateException {
        assertOutput("${doc.person?previousSibling?? ?c}", "false");
    }

    @Test
    public void testSignificantPreviousSibling() throws IOException, TemplateException {
        String ftl = "${doc.person.name.@@previous_significant}";
        assertOutput(ftl, "male");
    }


    @Test
    public void testSignificantNextSibling() throws IOException, TemplateException {
        String ftl = "${doc.person.name.@@next_significant}";
        assertOutput(ftl, "12th August");
    }

    @Test
    public void testNullSignificantPreviousSibling() throws IOException, TemplateException {
        assertOutput("${doc.person.phone.@@next_significant?size}", "0");

    }

    @Test
    public void testSkippingCommentNode() throws IOException, TemplateException {
        assertOutput("${doc.person.profession.@@previous_significant}", "Chennai, India");
    }

    @Test
    public void testSkippingEmptyCDataNode() throws IOException, TemplateException {
        assertOutput("${doc.person.hobby.@@previous_significant}", "Software Engineer");
    }

    @Test
    public void testValidCDataNode() throws IOException, TemplateException {
        assertOutput("${doc.person.phone.@@previous_significant}", "\n    this is a valid cdata\n    ");
    }
}
