package com.zelaux.hjson.lexer;

import com.intellij.psi.TokenType;
import com.zelaux.hjson.HJsonElementTypes;
import org.junit.Assert;

import static com.zelaux.hjson.lexer.HJsonLexerTest.TestCase.test;
import static com.zelaux.hjson.lexer.LexerResultEntry.entry;

public class HJsonLexerTest {
    static final TestCase[] cases = {
            test("[123]",
                    entry(HJsonElementTypes.L_BRACKET, "[", 0, 1),
                    entry(HJsonElementTypes.NUMBER_TOKEN, "123", 1, 4),
                    entry(HJsonElementTypes.R_BRACKET, "]", 4, 5)
            ),
            test("true", entry(HJsonElementTypes.TRUE, "true", 0, 4)),
            test("null", entry(HJsonElementTypes.NULL, "null", 0, 4)),
            test("false", entry(HJsonElementTypes.FALSE, "false", 0, 5)),
            test("4 ",
                    entry(HJsonElementTypes.NUMBER_TOKEN, "4", 0, 1),
                    entry(TokenType.WHITE_SPACE, " ", 1, 2)
            ),
            test("0 ,",
//                    entry(HJsonElementTypes.QUOTELESS_STRING_TOKEN, "0 ,", 0, 3)
                    entry(HJsonElementTypes.MEMBER_NAME,"0",0,1),
                    entry(TokenType.WHITE_SPACE," ",1,2),
                    entry(HJsonElementTypes.COMMA,",",2,3)
            ),
            test("123",
//                    entry(HJsonElementTypes.QUOTELESS_STRING_TOKEN, "0 ,", 0, 3)
                    entry(HJsonElementTypes.NUMBER_TOKEN,"123",0,3)
            ),
    };

    public HJsonLexerTest() {
        super();
    }

    @org.junit.Test
    public void testHjsonParsing() {

        HJsonLexer lexer = new HJsonLexer();
        for (TestCase testCase : cases) {
            lexer.start(testCase.data);
            System.out.println("begin");
            LexerResultEntry[] entries = testCase.entries;
            int i = 0;
            while (lexer.getTokenType() != null) {


                LexerResultEntry entry = new LexerResultEntry(
                        lexer.getTokenType(),
                        lexer.getTokenText(),
                        lexer.getTokenStart(),
                        lexer.getTokenEnd()

                );
                Assert.assertTrue("Unexpected token "+entry, i < entries.length);
                LexerResultEntry expectedEntry = entries[i];
                Assert.assertNotEquals("String lexer returns empty token(" + lexer.getTokenStart() + "," + lexer.getTokenEnd() + ")", "", entry.text);
                Assert.assertEquals(expectedEntry, entry);
                System.out.println("\t" + entry);
                i++;
                lexer.advance();
            }
            if (i < entries.length) Assert.fail("Missing tokens " + (entries.length - i - 1)+"("+testCase.data+")");
            System.out.println("end");
        }


    }

    static class TestCase {
        public final String data;
        public final LexerResultEntry[] entries;

        TestCase(String data, LexerResultEntry... entries) {
            this.data = data;
            this.entries = entries;
        }

        static TestCase test(String data, LexerResultEntry... entries) {
            return new TestCase(data, entries);
        }
    }
}