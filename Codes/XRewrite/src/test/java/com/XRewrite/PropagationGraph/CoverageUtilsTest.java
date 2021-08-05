package com.XRewrite.PropagationGraph;

import com.XRewrite.Parser.impl.Parser;
import junit.framework.TestCase;

public class CoverageUtilsTest extends TestCase {

    public void testIsPropagatable() {
        Parser parser = new Parser();
        //boolean propagatable = CoverageUtils.isPropagatable(parser.parseLiteral("a(x,x,y)"), parser.parseLiteral("a(w,e,e)"));
        //System.out.println(propagatable);
    }
}