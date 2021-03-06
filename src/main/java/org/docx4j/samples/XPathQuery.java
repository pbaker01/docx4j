package org.docx4j.samples;

import java.util.List;

import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

public class XPathQuery {

	
	/**
	 * Example of how to find an object in document.xml
	 * via XPath.
	 * 
	 * As explained in Getting Started, there are limitations 
	 * in the JAXB reference implementation which make this
	 * approach buggy:
	 * 
	 * 1. the xpath expressions are evaluated against the XML 
	 * document as it was when first opened in docx4j.  You 
	 * can update the associated XML document once only, by
	 * passing true into getJAXBNodesViaXPath. Updating it again 
	 * (with current JAXB 2.1.x or 2.2.x) will cause an error.
	 * 
	 * 2. For some objects,JAXB can’t get parent (with getParent)
	 * 
	 * 3. For some document, JAXB can’t set up the XPath at all!
	 * 
	 * If these problems affect you, consider using TraversalUtil;
	 * see the OpenMainDocumentAndTraverse sample.
	 * 
	 */
	public static void main(String[] args) throws Exception {

		String inputfilepath = System.getProperty("user.dir") + "/sample-docs/word/sample-docx.xml";
				
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new java.io.File(inputfilepath));		
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
				
		String xpath = "//w:t[contains(text(),'scaled')]";
		//String xpath = "//w:r[w:t[contains(text(),'scaled')]]";
		
		List<Object> list = documentPart.getJAXBNodesViaXPath(xpath, false);
		
		System.out.println("got " + list.size() + " matching " + xpath );
		
		for (Object o : list) {
			
			//System.out.println(o.getClass().getName() );
			
			Object o2 = XmlUtils.unwrap(o);
			// this is ok, provided the results of the Callback
			// won't be marshalled			
			
			if (o2 instanceof org.docx4j.wml.Text) {
				
				org.docx4j.wml.Text txt = (org.docx4j.wml.Text)o2;
				
				System.out.println( txt.getValue() );
				
				// Demonstrate the getParent bug
				//Object parent = txt.getParent();			
				//System.out.println( "parent: " +  XmlUtils.unwrap(parent).getClass().getName() );
			} else {
				System.out.println( XmlUtils.marshaltoString(o, true, true));
			}

			
			
		}
						
	}
	
		
}
