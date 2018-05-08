/*
Copyright (C) 2017 Martin Frické (mfricke@u.arizona.edu http://softoption.us mfricke@softoption.us)

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

import static us.softoption.infrastructure.Symbols.chAt;
import static us.softoption.infrastructure.Symbols.chBlank;
import static us.softoption.infrastructure.Symbols.chLSqBracket;
import static us.softoption.infrastructure.Symbols.chSmallLeftBracket;
import static us.softoption.infrastructure.Symbols.chSmallRightBracket;
import static us.softoption.infrastructure.Symbols.strNotMemberOf;
import static us.softoption.infrastructure.Symbols.strNull;
import static us.softoption.parser.TFormula.application;
import static us.softoption.parser.TFormula.binary;
import static us.softoption.parser.TFormula.comprehension;
import static us.softoption.parser.TFormula.equality;
import static us.softoption.parser.TFormula.functor;
import static us.softoption.parser.TFormula.lambda;
import static us.softoption.parser.TFormula.modalKappa;
import static us.softoption.parser.TFormula.modalRho;
import static us.softoption.parser.TFormula.pair;
import static us.softoption.parser.TFormula.predicator;
import static us.softoption.parser.TFormula.quantifier;
import static us.softoption.parser.TFormula.typedQuantifier;
import static us.softoption.parser.TFormula.unary;
import static us.softoption.parser.TFormula.variable;

import java.io.Reader;
import java.util.ArrayList;

/*
 * 	<combex> ::= <atom> | <application> | (<combex>)
	<atom> ::= <variable>|<constant>|<combinator>
	<application> ::= <combex> <combex>
 * 
 * 
 */


public class TCLParser extends TParser{
	
public static final String gCombinator="BCIKSWY"; // NOTE C is conditional also
	
	public boolean cLWffCheck(TFormula root, ArrayList newValuation,Reader aReader){

        initializeErrorString();   //new Dec07
        fInput=aReader;

        initializeInputBuffer();
        //skip(1);  // I think you need this to initialize lookaheads

        return
            cLWffCheck (root,newValuation);
   }
	
	private boolean cLWffCheck (TFormula root, ArrayList newValuation){


		/* {RECURSIVE DESCENT WITH PRECEDENCE AND LEFT ASSOCIATION}
		 * 
		 * 
		 * /*
 * 	<combex> ::= <atom> | <application> | (<combex>)
	<atom> ::= <variable>|<constant>|<combinator>
	<application> ::= <combex> <combex>
 * 
 * 


		    <expression> := <name> | <function> | <application> | <defined constant>
		    <function> :=  lambda  <name>.<scope>
		    <application> := (<function expression> <argument expression>)
		    <scope>:= <expression>
		    <function expression>:= <expression>
		    <argument expression>:= <expression>
		    <name>:= any string of characters starting with a lower case letter
		    <defined constant>:= any string of characters starting with an upper case letter


		*/

		    skipSpace();  //leading


		    if (newCombex(root))
		       {

		       skipSpace(); // This added Feb 2013 should be no junk on end  ,no 'cos want blanks for applications

		       if (fCurrCh == chBlank)
		          return
		              WELLFORMED;
		       else
		           {
		           if (fCurrCh == chLSqBracket)  // {valuation}
		               {
		               if (getValuation(newValuation))
		                 return
		                     WELLFORMED;
		               else
		                 return
		                     ILLFORMED;
		               }
		           else{
		               writeError(CR+"(*The extra character '"+
		                                    fCurrCh+
		                                    "' should be a blank.*)" );
		               return
		                   ILLFORMED;
		               }
		           }
		           }
		       else
		        return
		            ILLFORMED;
		   }
	
private boolean newCombex (TFormula root)
    {

/*	< combex > ::= < element >
		| < combex > < element >        //application Left Associate
		| < combex >@< element >		//application
	< element > ::= <variable>|<constant>|<combinator>|(< combex >)
*/	
	
	//<combex> ::= <atom> | <application> | (<combex>)
	//<atom> ::= <variable>|<constant>|<combinator>
	//<application> ::= <combex> <combex> | <combex>@<combex>
	
	//<expression> := <name> | <function> | <application> | (expression)


TFormula elem= new TFormula();

if (!element(elem))
	return
			ILLFORMED;


return
		collectLeftAssociatedApplications (root,elem);


// now looking for application or series of applications
// it can only be an application if a blank or an @ comes next
// but we want to go careful if the whole input ends in a blank, which
// should not be read as an error.

// they may input an application as A B or A  B or A@   B or A    @B
/*
if ((fCurrCh == chBlank)|| (fCurrCh == chAt))  // might be application	
	{
	skipSpace(); // move to next real item
	if (fCurrCh == chAt){
		skip(1); // move past @
		skipSpace(); // move to next real item
	}
	
	if (fCurrCh == chBlank) {    // means, after skipSpace(), met EOF
								// i.e. trailing blanks
		root.assignFieldsToMe(elem); // give local fields to calling parameter
		return
				WELLFORMED;	
	}
	
	// ok, what we have parsed so far is the 'function'
	// now we want the 'argument'
	// because of the left association the argument can only be an element
	// it cannot itself be a plain application without brackets around it
	
	TFormula argument= new TFormula();

	if (!element(argument))
	return
	 ILLFORMED;
	
	TFormula newRoot = new TFormula(
	           application,
	           "",
	           elem, //for root
	           argument);

	root.assignFieldsToMe(newRoot);
	
	return
			WELLFORMED;
	}

return
	WELLFORMED;  //element no following blanks

*/
/*

//if (fCurrCh == chSmallLeftBracket) {

	//<application> := <function> <argument> 

	//skip(1); /*looking at next item
	skipSpace();

	TFormula function= new TFormula();

	if (!newExpression(function))
	return
	ILLFORMED;

	if (!((fCurrCh == chBlank)|| (fCurrCh == chAt)) )
	return
	 ILLFORMED;

	skipSpace();

	TFormula argument= new TFormula();

	if (!newExpression(argument))
	return
	 ILLFORMED;


	TFormula newRoot = new TFormula(
	           application,
	           "",
	           function, //for root
	           argument);

	root.assignFieldsToMe(newRoot);

	//  skip(1); /*looking at next item
	skipSpace();

	/*if (fCurrCh!=chSmallRightBracket){
	writeError("(*The character '" + fCurrCh + "' should be ')'.*)");
	return
	 ILLFORMED;
	} 

	//skip(1); /*the small right-bracket

	return
	 WELLFORMED; // application
	//}
*/


/* TO DO





//<expression> := lambda <name> . <expr>

if (fCurrCh == chLambda) {

skip(1); /*looking at next item
skipSpace();

if (isLambdaName(fCurrCh)){ /* tests whether functor

String identifier = readName(fCurrCh);

if (!identifier.equals("")) {


{
 TFormula nameRoot = new TFormula();
 TFormula scope = new TFormula();

 nameRoot.fInfo = identifier;
 nameRoot.fKind = variable; /*treat lambda names as variables

 skip(1); /*looking at next item, the period
 skipSpace();

 if (fCurrCh != '.')
   return
       ILLFORMED;

 skip(1); /*looking at next item, the scope expression
 skipSpace();

 if (!newExpression(scope))
   return
       ILLFORMED;

 // skip(1); /*looking at next item

 root.fKind = lambda; // alter caller
 root.fInfo = String.valueOf(chLambda);
 root.fLLink = nameRoot;
 root.fRLink = scope;

 return
     WELLFORMED; // atom
}
}
}
}

TO DO */
    //    return ILLFORMED;
 }

private boolean collectLeftAssociatedApplications (TFormula root, TFormula elem){
	
	// now looking for application or series of applications
	// it can only be an application if a blank or an @ comes next
	// but we want to go careful if the whole input ends in a blank, which
	// should not be read as an error.

	// they may input an application as A B or A  B or A@   B or A    @B
	
	// we need to parse A B C D etc. as (((A@B)@C)@D)
	
	
TFormula argument=null;
TFormula newRoot;
Boolean finished=false;
	
while (!finished){
	
	if (!((fCurrCh == chBlank)|| (fCurrCh == chAt))){  // not an application 
		root.assignFieldsToMe(elem); 	// give local fields to calling parameter
		finished=true; 	//not needed but makes clear
		return
				WELLFORMED;		
	}

	if ((fCurrCh == chBlank)|| (fCurrCh == chAt)){  // might be application	
		skipSpace(); // move to next real item
		if (fCurrCh == chAt){
			skip(1); // move past @
			skipSpace(); // move to next real item
			}
		
		if (fCurrCh == chBlank) {    // means, after skipSpace(), met EOF
									// i.e. trailing blanks
			root.assignFieldsToMe(elem); // give local fields to calling parameter
			finished=true;
			return
					WELLFORMED;	
			}
		
		// ok, what we have parsed so far is the 'function' as element
		// now we want the 'argument'
		// because of the left association the argument can only be an element
		// it cannot itself be a plain application without brackets around it
		
		argument= new TFormula();

		if (!element(argument)){
			finished=true; 	//not needed but makes clear
			return
					ILLFORMED;
			}
		
		newRoot = new TFormula(
		           application,
		           "",
		           elem, //for root
		           argument);

		//root.assignFieldsToMe(newRoot);
		
		//at this point everything is good, but there may be more arguments
		// so we set elem to the new root and do it again seeing if there
		// is another application
		
		elem=newRoot;
		
		//return
		//		WELLFORMED;
		}
	}
return
		WELLFORMED;  //element no following blanks		
}


private boolean element(TFormula root) {
	if (fCurrCh == chSmallLeftBracket) {

		//<combex> := (<combex>) There can be any number of brackets but they need to match

	skip(1); /*looking at next item*/
	skipSpace();
	if (!newCombex(root))
        return
        		ILLFORMED;
		
	skipSpace();

	if (fCurrCh!=chSmallRightBracket){
		writeError("(*The character '" + fCurrCh + "' should be ')'.*)");
		return
				ILLFORMED;
		}

	skip(1); /*the small right-bracket*/

		return
				WELLFORMED; // (<combex>)
	}
	
	
if (isLambdaName(fCurrCh)) {/* tests whether name   <atom but variable>
string starting lowercase. We allow all lambda names here.
This means that it might be an atom or an application*/
            
	String identifier = readName(fCurrCh);

	if (!identifier.equals("")) {

		root.fInfo = String.valueOf(identifier);
		root.fKind = variable; /*treat lambda/CL names as variables*/

		skip(1); /*looking at next item*/

		return
				WELLFORMED; // atom
              }
            }

if (isLambdaConstant(fCurrCh)) /* tests whether functor ie defined constant*/
                            {
                          root.fInfo = String.valueOf(fCurrCh);
                          root.fKind = functor; /*treat lambda names as variables*/

                          skip(1); /*looking at next item*/


                            return
                                WELLFORMED; // atom


        }

if (isCombinator(fCurrCh)) /* tests whether functor ie defined constant*/
	{
	root.fInfo = String.valueOf(fCurrCh);
	root.fKind = functor; /*treat lambda names as variables*/

	skip(1); /*looking at next item*/


	return
			WELLFORMED; // atom
	}	
	
return
		ILLFORMED;
}





public static boolean isCombinator (char ch)
{
                return  ((gCombinator.indexOf((int)ch)!=-1));
}
 
public static boolean isCancellator (TFormula root){
	
	return
			root.fKind == functor &&
			root.fInfo.equals("K");
}

public static boolean isCompositor (TFormula root){
	
	return
			root.fKind == functor &&
			root.fInfo.equals("B");
}

public static boolean isDistributor (TFormula root){
	
	return
			root.fKind == functor &&
			root.fInfo.equals("S");
}

public static boolean isDuplicator (TFormula root){
	
	return
			root.fKind == functor &&
			root.fInfo.equals("W");
}

public static boolean isFixpoint (TFormula root){
	
	return
			root.fKind == functor &&
			root.fInfo.equals("Y");
}


public static boolean isIdentificator (TFormula root){
	
	return
			root.fKind == functor &&
			root.fInfo.equals("I");
}

public static boolean isPermutator (TFormula root){
	
	return
			root.fKind == functor &&
			root.fInfo.equals("C");
}


@Override
public String writeFormulaToString (TFormula root){
	boolean rightBranch=false;
	return
			writeFormulaToString (root, rightBranch);
}


public String writeFormulaToString (TFormula root, boolean immediateRightBranch){
	
/*What is a problem here is that it is usual
 * for applications not to have brackets around them
 * and to left associate.
 * This means that a 'right associated application' must have brackets
 * i.e. I a a a as opposed to I a (a a)
 * Now, any	'right associated application' must be in a right branch
 * so we will mark that
 */
	
/*
 * 
 * 5/28/17

Parantheses omitting

(((a b) c) d)

((a b) c) d

guess, any application to the left of parentheses means that the parentheses is needed

(((a b) c) d)  none needed

 ((a b) (c d))  gives a b (c d)
 * 
 * 
 */
		
		
		
	
if (root==null)
  return
      strNull;
else{
String leftString = new String();
String rightString = new String();
String termString = new String();
String compString = new String();

      switch (root.fKind) {

        case predicator:
          return
              writePredicateToString(root);
        case functor:
        case variable:{
        	termString=writeTermToString(root);
        	if (termString.length() > fWrapBreak)   // terms can be arbitrarily long
        		termString = fTermTruncate;
        	
          return
             termString;
        }

        case equality: {
          leftString = writeTermToString(root.firstTerm());
          if (leftString.length() > fWrapBreak)
            leftString = fLeftTruncate;
          rightString = writeTermToString(root.secondTerm());
          if (rightString.length() > fWrapBreak)
            rightString = fRightTruncate;
          
          if (leftString.length()+rightString.length() > fWrapBreak)
              rightString = fRightTruncate;

          return
              (leftString +
               root.fInfo +
               rightString);
        }

        case unary: {
      
        if (isNotMemberOf(root)){             //special case from set theory
           // System.out.print("write " + writeFormulaToString(term.fLLink));
        	leftString = writeInner(root.fRLink.firstTerm());
        	rightString = writeInner(root.fRLink.secondTerm());
            if (leftString.length() > fWrapBreak)
                leftString = fLeftTruncate;
            if (rightString.length() > fWrapBreak)
                rightString = fRightTruncate;

            if (leftString.length()+rightString.length() > fWrapBreak)
                rightString = fRightTruncate;
        	
        	
        	return
        	   leftString + strNotMemberOf + rightString;
        	
        }
        	
        
        rightString = writeInner(root.fRLink);
          if (rightString.length() > fWrapBreak)
            rightString = fRightTruncate;
          return
              (translateConnective(root.fInfo) +
               rightString);
        }

        case binary: {
          leftString = writeInner(root.fLLink);
          if (leftString.length() > fWrapBreak)
            leftString = fLeftTruncate;
          rightString = writeInner(root.fRLink);
          if (rightString.length() > fWrapBreak)
            rightString = fRightTruncate;
          
          if (leftString.length()+rightString.length() > fWrapBreak)
              rightString = fRightTruncate;
          
          return
              (leftString +
               translateConnective(root.fInfo) +
               rightString);

        }
        
        
        case modalKappa: 
        case modalRho:{                           // like prefix binary
            leftString = writeInner(root.fLLink);
            if (leftString.length() > fWrapBreak)
              leftString = fLeftTruncate;
            rightString = writeInner(root.fRLink);
            if (rightString.length() > fWrapBreak)
              rightString = fRightTruncate;
            
            if (leftString.length()+rightString.length() > fWrapBreak)
                rightString = fRightTruncate;
            
            return
                (translateConnective(root.fInfo) +
                 leftString +              
                 rightString);

          }
        
        
        case quantifier: {

          return
              writeQuantifierToString(root);
        }




        case typedQuantifier:
          return
              writeTypedQuantifierToString(root);

        case application: {
          leftString = writeFormulaToString(root.fLLink);
          if (leftString.length() > fWrapBreak)
            leftString = fLeftTruncate;
          
          Boolean localImmediateRightBranch=true;
          rightString = writeFormulaToString(root.fRLink, localImmediateRightBranch);
          if (rightString.length() > fWrapBreak)
            rightString = fRightTruncate;
          
          if (leftString.length()+rightString.length() > fWrapBreak)
              rightString = fRightTruncate;       
        		  
        	if (fVerbose)                      //give kitchen sink
        		return
        				("("+leftString +
        				(fVerbose?"@":" ")+    // must write a blank in
                        rightString +")");
        	else if (immediateRightBranch){		// need parentheses
        		return
        				("("+leftString +
        				(fVerbose?"@":" ")+    // must write a blank in
                        rightString +")");
        	}
        	else
        		return
        				(leftString + " " + rightString);
        	}
        	


        case lambda: {  //lambdas do not call inner at all
          String prefix = new String();
          String scope = new String();

          prefix = (translateConnective(root.fInfo) + root.lambdaVar() + ".");

          scope = writeFormulaToString(root.scope());

          if (scope.length() > fWrapBreak)
            scope = fScopeTruncate;
          return
         //     "("+
              (prefix + scope);
      //        +")";
        }
        
        case comprehension: { 
           	compString=writeComprehensionToString(root);
        	if (compString.length() > fWrapBreak)   // terms can be arbitrarily long
        		compString = fCompTruncate;

            return
            compString;

          }
        
        case pair:{

        		return
        		   writePairToString(root);}


        default:
          return strNull;
      }

}
}

}





