/*
Copyright (C) 2015 Martin Frické (mfricke@u.arizona.edu http://softoption.us mfricke@softoption.us)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation 
files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, 
modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the 
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package us.softoption.parser;

//8/9/06


import static us.softoption.infrastructure.Symbols.chAnd;

import java.io.Reader;


public class TBergmannParser extends TParser{

/*We want several different parsers of similar structure; we define generic procedures
which are called in the normal way.  Most documents and windows have fParser fields to
ensure the correct reading and writing.*/


/*Nov 8th 2003 Old versions of this used to have the filtering input mechanism written into the code
but Javas regular expression powers allow this to be done to the input before it ever gets to skip
or to the parser*/

/*constants*/

 public TBergmannParser(){
 }

 public TBergmannParser(Reader aReader,boolean firstOrder)
        {
        super(aReader,firstOrder);
        }



//static final String gCopiConstants="abcdefghijklmnopqrst";

//static final String gBergmannConstants="abcdefghijklmnopqrstuv";  //actually I prefer to stick with Gentzen constants


//static final String gFunctors="abcdefghijklmnopqrstuvwxyz012";
                                         /*zero-order functors a..l are constants, m..z are variables
                                         the binary predefineds like + are treated as special cases
                                         and there are the numerals 0,1,2; conceptually gFunctors is a
                                         set of char*/

 //public static final String gCopiVariables="uvwxyz";   //{take care if you change these as some procedures use}
                                           //   their indices, e.g. TFormula.firstfreevar}
 //public static final String gPredicates="ABCDEFGHIJKLMNOPQRSTUVWXYZ";

 //public static final String gBergmannVariables="wxyz"; //actually I prefer to stick with Gentzen variables n for number


 public static final char chBergmannAnd = '&';

// public static final String gBergmannConnectives=""+chBergmannAnd+chNeg+chOr+chExiquant+chImplic+chEquiv;



public String renderAnd(){
   return
       String.valueOf(chBergmannAnd);
         }


public String translateConnective(String connective){

  if (connective.equals(String.valueOf(chAnd)))
    return
        String.valueOf(chBergmannAnd);

 return
     connective;
}

/*
public /*static boolean isConnective (char ch){
    return
        ((gBergmannConnectives.indexOf((int)ch)!=-1));
 } */

/* we'll go with Gentzen variables
public static boolean isVariable (char ch)
{
return  ((gBergmannVariables.indexOf((int)ch)!=-1));
}
*/





/* Use to have
 * 
 * 
 * 
 * 
 * /*
public static boolean isBergmannConnective (char ch){
           return
               ((gBergmannConnectives.indexOf((int)ch)!=-1));
        }
*/



/******** need to revise these overrides *****/

 

}


