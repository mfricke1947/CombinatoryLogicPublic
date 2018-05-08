package us.softoption.proofs;

import static us.softoption.infrastructure.Symbols.*;
import static us.softoption.parser.TFormula.application;

//import java.awt.Toolkit;
import java.io.StringReader;
import java.util.ArrayList;

import us.softoption.infrastructure.TUtilities;
import us.softoption.parser.TCLParser;
import us.softoption.parser.TFormula;
import us.softoption.parser.TParser;


public class TCombinatoryLogicRewriteRules extends TGWTRewriteRules {

// 4/29/18 public class TCombinatoryLogicRewriteRules extends TRewriteRules {

public TCombinatoryLogicRewriteRules(TFormula selectedFormula,
                        TParser aParser){
 super(selectedFormula, aParser);
 
 String rewriteString=fParser.writeFormulaToString(selectedFormula);

 
 //rewriteString=TUtilities.addSpaceToInnerParantheses(rewriteString);  //commented out 5/6/18
 
 /* 5/6/18 mf puzzled
  * This adds space to inner Parantheses e.g. from a (b c) d to a ( b c ) d
  * 
  * We needed it elsewhere for some reason, but I don't think we need it here
  * and it causes trouble a (b c) d is well-formed in CL but a ( b c ) d is not
  * because space is application
  * 
  * I'm just going to try without it and see what happense
  */
 
 fBeforeText.setText(rewriteString);
 fBeforeTextReference.setText(rewriteString);

   }


/********** overrides to use lambda parser ****************/
@Override
   public TFormula getAfterRoot(){
  // we need to find the entire after formula

  TFormula afterRoot = new TFormula();
  StringReader aReader = new StringReader(fAfterText.getText());
  ArrayList dummy = new ArrayList();

  boolean wellFormed = //fParser.lambdaWffCheck(afterRoot, dummy, aReader);
  
  ((TCLParser)fParser).cLWffCheck(afterRoot, dummy, aReader);

  if (wellFormed)
    return afterRoot;
  else
    return
        null;
}
@Override
boolean testOldFormula(){
	  boolean wellFormed=false;
	  
	  String entry=fBeforeText.getText();
	  int selStart=fBeforeText.getCursorPos();
	  fSelection=fBeforeText.getSelectedText();


	  fPreSelection=entry.substring(0, selStart);
	  fPostSelection=entry.substring(selStart+fSelection.length(),
			  entry.length());
	
	//They must not have a trailing blank because parser will parse but before will be an
	 // application and after cannot be because that blank goes missing

	  if (fSelection==null||
			     fSelection.length()==0||
			     fSelection.charAt(0)==chBlank||
			     fSelection.charAt(fSelection.length()-1)==chBlank){
//			   Toolkit.getDefaultToolkit().beep(); //give error message?
			   return
			    false;
			}
	 
	  fSelectionRoot = new TFormula();
	  StringReader aReader = new StringReader(fSelection);
	  ArrayList dummy = new ArrayList();

	  wellFormed =// fParser.lambdaWffCheck(fSelectionRoot, dummy,aReader);
			  ((TCLParser)fParser).cLWffCheck(fSelectionRoot, dummy, aReader);

	/*The subformula check is for this with AvBcd they could select AvBc which is WFF but not a subformula*/

	if (!wellFormed||!(fSelectedFormula).subFormulaOccursInFormula(fSelectionRoot, fSelectedFormula)){
	//Toolkit.getDefaultToolkit().beep(); //give error message?
	//fBeforeText.setSelectionRange(0,0);            // we'll clear the selection if it is not well formed

	return
			false;  //not occurs
	}
	  

	  return
	     wellFormed;
	}
/*
boolean getOldFormula(){
  boolean wellFormed=false;

  String entry=fBeforeText.getText();
  int selStart=fBeforeText.getSelectionStart();
  int selEnd=fBeforeText.getSelectionEnd();
  //int entryEnd=entry.length();

  try
     {fPreSelection=fBeforeText.getText(0,selStart);}  //NB getText uses offset, len
  catch (BadLocationException e)
     {fPreSelection="";
     System.out.print("Rewrite catch Pre");}
  fSelection=fBeforeText.getSelectedText();
  try
     {fPostSelection=fBeforeText.getText(selEnd,entry.length()-selEnd);}
  catch (BadLocationException ex)
     {fPostSelection="";
     System.out.print("Rewrite catch Post");
     }

//They must not have a trailing blank because parser will parse but before will be an
     // application and after cannot be because that blank goes missing

if (fSelection==null||
     fSelection.length()==0||
     fSelection.charAt(0)==chBlank||
     fSelection.charAt(fSelection.length()-1)==chBlank){
   Toolkit.getDefaultToolkit().beep(); //give error message?
   return
    !wellFormed;
}


   fSelectionRoot = new TFormula();
   StringReader aReader = new StringReader(fSelection);
   ArrayList dummy = new ArrayList();

   wellFormed = fParser.lambdaWffCheck(fSelectionRoot, dummy, aReader);


   /*The subformula check is for this with AvBcd they could select AvBc which is WFF but not a subformula

    if (!wellFormed||!(fSelectedFormula).subFormulaOccursInFormula(fSelectionRoot, fSelectedFormula)){
     Toolkit.getDefaultToolkit().beep(); //give error message?
     fBeforeText.select(0,0);            // we'll clear the selection if it is not well formed
   }

  return
     wellFormed;
}
*/

@Override
void putNewFormula(){


  // change of syntax means don't need following

    // with lambda's we want to put the reduction in brackets to prevent incorrect association
  // eg (lambda x. a)<expr> where expr reduces to b lambda x. ab


   if (!(fNewRoot==null||fParser.writeFormulaToString(fNewRoot).equals("")))   // we'll only do this if there is change
   {if ((fPreSelection.length()==0)&&
       (fSelection.length()>0)&&
       (fPostSelection.length()==0)

         ){

       if ((fSelection.charAt(0)!= '('))
        fSelectionRewrite = fParser.writeFormulaToString(fNewRoot);   //we'll omit brackets (doesn't with lambda)
      else
        fSelectionRewrite = fParser.writeInner(fNewRoot);


       }
       else
         fSelectionRewrite = fParser.writeInner(fNewRoot);


       //{June 1990 but then you get the problem of p:-pVp and then the pVp associating incorrectly!}

     fAfterText.setText(fPreSelection +
                        fSelectionRewrite +
                        fPostSelection);
     }
   }







  /**********************/

@Override
void initializeRulesList(){
   fRulesList=new ArrayList();
 
  
   fRulesList.add(new DoCompose());
   fRulesList.add(new DoPermute());
   fRulesList.add(new DoIdentity());
   fRulesList.add(new DoCancel());
   fRulesList.add(new DoDistribute());
   fRulesList.add(new DoDuplicate());
   fRulesList.add(new DoFixpoint());
   
   
   
   /*
   
   fRulesList.add(new DoReduce());
   fRulesList.add(new DoTrue());
   fRulesList.add(new DoFalse());
   fRulesList.add(new DoCond());
   fRulesList.add(new DoZero());
   fRulesList.add(new DoOne());

*/

}

 /*  JComboBox initializeRules(){
    JComboBox rules;

   // String [] ruleStrings={"p^p :: q^p","two"};

    rules=new JComboBox();
    rules.setMaximumRowCount(6);

    rules.addItem(new DoReduce());
    rules.addItem(new DoTrue());
    rules.addItem(new DoFalse());
    rules.addItem(new DoCond());
    rules.addItem(new DoZero());
    rules.addItem(new DoOne());


  return
     rules;
  }
*/

 /********************* Combinator Rules *****************/

class DoIdentity extends AbstractRule{

//This needs to go from I a to a
	
    boolean doRule() {
	if (fParser.isApplication(fSelectionRoot)&&
          ((TCLParser)fParser).isIdentificator(fSelectionRoot.fLLink)) {  // Lambda x.A b::(Ex m)~p)

          fNewRoot =fSelectionRoot.fRLink.copyFormula();
          fLastRewrite = " Identity";
          return
              true;
        }

      return
              false;
    }

    public String toHTMLString() {
      return
          "<html>"+
          "<em>Identity &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
          "<strong>"+
          "("+"I x"+" :- "+"x"+
          "</strong>"+
          "</html>";
    }
    
    public String toString() {
        return
            "Identity      "+
            "(I x) :- x";
      }
}

class DoCancel extends AbstractRule{

	//K x y -> x
		
	    boolean doRule() {
		if (fParser.isApplication(fSelectionRoot)&&
			fParser.isApplication(fSelectionRoot.fLLink)&&
	          ((TCLParser)fParser).isCancellator(fSelectionRoot.fLLink.fLLink)) {  // Lambda x.A b::(Ex m)~p)

	          fNewRoot =fSelectionRoot.fLLink.fRLink.copyFormula();
	          fLastRewrite = " Cancel";
	          return
	              true;
	        }

	      return
	              false;
	    }

	    public String toHTMLString() {
	      return
	          "<html>"+
	          "<em>Cancel &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
	          "<strong>"+
	          "("+"K x y"+" :- "+"x"+
	          "</strong>"+
	          "</html>";
	    }
	    
	    public String toString() {
	        return
	            "Cancel      "+
	            "(K x y) :- x";
	      }
	}





class DoCompose extends AbstractRule{

	//B x y z -> x (y z) (* compositor *)
		
	    boolean doRule() {
	    	
	   TFormula B,x,y,z;
	   
	   if ((fSelectionRoot!=null) &&
	   	  (fSelectionRoot.fLLink!=null) &&
	   	  (fSelectionRoot.fRLink!=null) &&
	   	  (fSelectionRoot.fLLink.fLLink!=null) &&	
	   	  (fSelectionRoot.fLLink.fRLink!=null) &&
	   	  (fSelectionRoot.fLLink.fLLink.fLLink!=null) &&
	   	  (fSelectionRoot.fLLink.fLLink.fRLink!=null)){
		   
		   B=fSelectionRoot.fLLink.fLLink.fLLink;
		   x=fSelectionRoot.fLLink.fLLink.fRLink;
		   y=fSelectionRoot.fLLink.fRLink;
		   z=fSelectionRoot.fRLink;
		   
		  	    	
		if (fParser.isApplication(fSelectionRoot)&&
			fParser.isApplication(fSelectionRoot.fLLink)&&
			fParser.isApplication(fSelectionRoot.fLLink.fLLink)&&
	          ((TCLParser)fParser).isCompositor(B)) {
			
			  fNewRoot= new TFormula(
					  application,
					  "",
					  x.copyFormula(),
					  new TFormula(
							  application,
							  "",
							  y.copyFormula(),
							  z.copyFormula()));


	          fLastRewrite = " Compose";
	          return
	              true;
	        }
	   
	      return
	              false;
	   }
	   else
		   return
		              false;
	    }

	    public String toHTMLString() {
	      return
	          "<html>"+
	          "<em>Compose &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
	          "<strong>"+
	          "("+"B x y z"+" :- "+"x (y z)"+
	          "</strong>"+
	          "</html>";
	    }
	    
	    public String toString() {
	        return
	            "Compose      "+
	            "(B x y z) :- x (y z)";
	      }
	}


class DoPermute extends AbstractRule{

	// C x y z -> x z y (* permutator*)
		
	    boolean doRule() {
	    	
	   TFormula C,x,y,z;
	   
	   if ((fSelectionRoot!=null) &&
	   	  (fSelectionRoot.fLLink!=null) &&
	   	  (fSelectionRoot.fRLink!=null) &&
	   	  (fSelectionRoot.fLLink.fLLink!=null) &&	
	   	  (fSelectionRoot.fLLink.fRLink!=null) &&
	   	  (fSelectionRoot.fLLink.fLLink.fLLink!=null) &&
	   	  (fSelectionRoot.fLLink.fLLink.fRLink!=null)){
		   
		   C=fSelectionRoot.fLLink.fLLink.fLLink;
		   x=fSelectionRoot.fLLink.fLLink.fRLink;
		   y=fSelectionRoot.fLLink.fRLink;
		   z=fSelectionRoot.fRLink;
		   
		  	    	
		if (fParser.isApplication(fSelectionRoot)&&
			fParser.isApplication(fSelectionRoot.fLLink)&&
			fParser.isApplication(fSelectionRoot.fLLink.fLLink)&&
	          ((TCLParser)fParser).isPermutator(C)) {
			
			  fNewRoot= new TFormula(
					  application,
					  "",
					  new TFormula(
							  application,
							  "",
							  x.copyFormula(),
							  z.copyFormula()),
					  
					  y.copyFormula());


	          fLastRewrite = " Permute";
	          return
	              true;
	        }
	   
	      return
	              false;
	   }
	   else
		   return
		              false;
	    }

	    public String toHTMLString() {
	      return
	          "<html>"+
	          "<em>Permute &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
	          "<strong>"+
	          "("+"C x y z"+" :- "+"x z y)"+
	          "</strong>"+
	          "</html>";
	    }
	    
	    public String toString() {
	        return
	            "Permute      "+
	            "(C x y z) :- x z y";
	      }
	}



class DoDistribute extends AbstractRule{

	// S x y z -> x z (y z) (* distributor *)
		
	    boolean doRule() {
	    	
	   TFormula S,x,y,z;
	   
	   if ((fSelectionRoot!=null) &&
	   	  (fSelectionRoot.fLLink!=null) &&
	   	  (fSelectionRoot.fRLink!=null) &&
	   	  (fSelectionRoot.fLLink.fLLink!=null) &&	
	   	  (fSelectionRoot.fLLink.fRLink!=null) &&
	   	  (fSelectionRoot.fLLink.fLLink.fLLink!=null) &&
	   	  (fSelectionRoot.fLLink.fLLink.fRLink!=null)){
		   
		   S=fSelectionRoot.fLLink.fLLink.fLLink;
		   x=fSelectionRoot.fLLink.fLLink.fRLink;
		   y=fSelectionRoot.fLLink.fRLink;
		   z=fSelectionRoot.fRLink;
		   
		  	    	
		if (fParser.isApplication(fSelectionRoot)&&
			fParser.isApplication(fSelectionRoot.fLLink)&&
			fParser.isApplication(fSelectionRoot.fLLink.fLLink)&&
	          ((TCLParser)fParser).isDistributor(S)) {
			
			  fNewRoot= new TFormula(
					  application,
					  "",
					  new TFormula(
							  application,
							  "",
							  x.copyFormula(),
							  z.copyFormula()),
					  
					  new TFormula(
							  application,
							  "",
							  y.copyFormula(),
							  z.copyFormula()));


	          fLastRewrite = " Distribute";
	          return
	              true;
	        }
	   
	      return
	              false;
	   }
	   else
		   return
		              false;
	    }

	    public String toHTMLString() {
	      return
	          "<html>"+
	          "<em>Distribute &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
	          "<strong>"+
	          "("+"S x y z"+" :- "+"x z (y z))"+
	          "</strong>"+
	          "</html>";
	    }
	    
	    public String toString() {
	        return
	            "Distribute      "+
	            "(S x y z) :- x z (y z)";
	      }
	}

class DoDuplicate extends AbstractRule{

	// W x y -> x y y (* duplicator*)
		
	    boolean doRule() {
	    	
	   TFormula W,x,y;
	   
	   if ((fSelectionRoot!=null) &&
	   	  (fSelectionRoot.fLLink!=null) &&
	   	  (fSelectionRoot.fRLink!=null) &&
	   	  (fSelectionRoot.fLLink.fLLink!=null) &&	
	   	  (fSelectionRoot.fLLink.fRLink!=null) ){
		   
		  W=fSelectionRoot.fLLink.fLLink;
		   x=fSelectionRoot.fLLink.fRLink;
		   y=fSelectionRoot.fRLink;

		   
		  	    	
		if (fParser.isApplication(fSelectionRoot)&&
			fParser.isApplication(fSelectionRoot.fLLink)&&
	          ((TCLParser)fParser).isDuplicator(W)) {
			
			fNewRoot= new TFormula(
					  application,
					  "",
					  new TFormula(
							  application,
							  "",
							  x.copyFormula(),
							  y.copyFormula()),
					  
					  y.copyFormula());



	          fLastRewrite = " Duplicate";
	          return
	              true;
	        }
	   
	      return
	              false;
	   }
	   else
		   return
		              false;
	    }

	    public String toHTMLString() {
	      return
	          "<html>"+
	          "<em>Duplicate &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
	          "<strong>"+
	          "("+"W x y"+" :- "+"x y y)"+
	          "</strong>"+
	          "</html>";
	    }
	    
	    public String toString() {
	        return
	            "Duplicate      "+
	            "(W x y) :- x y y";
	      }
	}



class DoFixpoint extends AbstractRule{

	// Y f -> f (Y f) 
		
	    boolean doRule() {
	    	
	   TFormula Y,f;
	   
	   if ((fSelectionRoot!=null) &&
	   	  (fSelectionRoot.fLLink!=null) &&
	   	  (fSelectionRoot.fRLink!=null)  ){
		   
		  Y=fSelectionRoot.fLLink;
		  f=fSelectionRoot.fRLink;


		   
		  	    	
		if (fParser.isApplication(fSelectionRoot)&&
	          ((TCLParser)fParser).isFixpoint(Y)) {
			
			fNewRoot= new TFormula(
					  application,
					  "",
					  f.copyFormula(),
					  new TFormula(
							  application,
							  "",
							  Y.copyFormula(),
							  f.copyFormula())
					  );



	          fLastRewrite = " Fixpoint";
	          return
	              true;
	        }
	   
	      return
	              false;
	   }
	   else
		   return
		              false;
	    }

	    public String toHTMLString() {
	      return
	          "<html>"+
	          "<em>Fixpoint &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
	          "<strong>"+
	          "("+"Y f"+" :- "+"f (Y f))"+
	          "</strong>"+
	          "</html>";
	    }
	    
	    public String toString() {
	        return
	            "Fixpoint      "+
	            "(Y f) :- f (Y f)";
	      }
	}

/********************* End of Combinator Rules *****************/

class DoReduce extends AbstractRule{


    boolean doRule() {
      TFormula p,m,term,var,scope;


      if (fParser.isApplication(fSelectionRoot)&&
          fParser.isLambda(fSelectionRoot.fLLink)) {  // Lambda x.A b::(Ex m)~p)

        //This is like Universal Instantiation

        term = fSelectionRoot.fRLink;
        scope = fSelectionRoot.fLLink.scope();
        var = fSelectionRoot.fLLink.lambdaVarForm();

        if ( (term != null) &&
            (scope != null) &&
            (var != null)) {

         if (!scope.freeForTest(term, var)) {

    //       Toolkit.getDefaultToolkit().beep(); // need to change variable here

          if (fParser.lambdaChangeVariable(fSelectionRoot)){
            scope = fSelectionRoot.fLLink.scope();
            var = fSelectionRoot.fLLink.lambdaVarForm();
          }
          else
            return
                false;   // run out of terms
         }


          term=term.copyFormula();
          scope=scope.copyFormula();
          var=var.copyFormula();

          scope.subTermVar(scope, term, var);

          fNewRoot =scope;

          /*
                    String message = aString + " for " +
                                       fFirstline.fFormula.quantVar()+
                                       " in " +
                                       fParser.writeFormulaToString(scope) +
                                       " leads to capture. " +
                                       "Use another term or Cancel";


           */



  /*        p = fSelectionRoot.fRLink.fRLink.copyFormula();
          m = fSelectionRoot.fRLink.quantVarForm().copyFormula();

          fNewRoot = new TFormula(TFormula.quantifier,
                                  String.valueOf(chExiquant),
                                  m,
                                  new TFormula(TFormula.unary,
                                               String.valueOf(chNeg),
                                               null,
                                               p)
              ); */

          fLastRewrite = " " + chBeta + "Reduce";
          return
              true;
        }
      }
      return
              false;
    }

    public String toHTMLString() {
      return
          "<html>"+
          "<em>Reduce &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
          "<strong>"+
          "("+chLambda+"x.A b) :- "+"A[b/x]"+
          "</strong>"+
          "</html>";
    }
    
    public String toString() {
        return
            "Reduce      "+
            "("+chLambda+"x.A b) :- "+"A[b/x]";
      }
}

  class DoCond extends AbstractRule{

     boolean doRule() {

       fLastRewrite = " Cond";

       if (fSelectionRoot.equalFormulas(fSelectionRoot,TCombinatoryLogic.C)||
           fSelectionRoot.equalFormulas(fSelectionRoot,TCombinatoryLogic.implic)){

           fNewRoot =TCombinatoryLogic.CForm;

           return
               true;
         }

         if (fParser.alphaEqualFormulas(fSelectionRoot,TCombinatoryLogic.CForm)){

             fNewRoot =TCombinatoryLogic.implic;

             return
                 true;
         }
       return
               false;
     }

     public String toHTMLString() {
       return
           "<html>"+
           "<em>Cond &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
           "<strong>"+
            chImplic+" :: "+chLambda+"x."+
                      chLambda+"y."+
                      chLambda+"z."+"((x y) z)"+
           "</strong>"+
           "</html>";
     }
     
     public String toString() {
         return
             "Cond      "+
               chImplic+" :: "+chLambda+"x."+
                        chLambda+"y."+
                        chLambda+"z."+"((x y) z)";
       }
}



  class DoFalse extends AbstractRule{

     boolean doRule() {

       fLastRewrite = " False";

       if (fSelectionRoot.equalFormulas(fSelectionRoot,TCombinatoryLogic.F)){

           fNewRoot =TCombinatoryLogic.FForm;

           return
               true;
         }

         if (fParser.alphaEqualFormulas(fSelectionRoot,TCombinatoryLogic.FForm)){

             fNewRoot =TCombinatoryLogic.F;

             return
                 true;
         }
       return
               false;
     }

     public String toHTMLString() {
       return
           "<html>"+
           "<em>False &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
           "<strong>"+
            "F"+" :: "+chLambda+"x."+chLambda+"y."+"y"+
           "</strong>"+
           "</html>";
     }
     
     public String toString() {
         return
             "False      "+
              "F"+" :: "+chLambda+"x."+chLambda+"y."+"y";
       }
}
   class DoTrue extends AbstractRule{

      boolean doRule() {

        fLastRewrite = " True";

        if (fSelectionRoot.equalFormulas(fSelectionRoot,TCombinatoryLogic.T)){

            fNewRoot =TCombinatoryLogic.TForm;

            return
                true;
          }

          if (fParser.alphaEqualFormulas(fSelectionRoot,TCombinatoryLogic.TForm)){

              fNewRoot =TCombinatoryLogic.T;

              return
                  true;
          }
        return
                false;
      }

      public String toHTMLString() {
        return
            "<html>"+
            "<em>True &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
            "<strong>"+
             "T"+" :: "+chLambda+"x."+chLambda+"y."+"x"+
            "</strong>"+
            "</html>";
      }
      
      public String toString() {
          return
              "True      "+

               "T"+" :: "+chLambda+"x."+chLambda+"y."+"x";
        }
}

  class DoZero extends AbstractRule{


     boolean doRule() {

       fLastRewrite = " Zero";

       if (fSelectionRoot.equalFormulas(fSelectionRoot,TCombinatoryLogic.zero)){

           fNewRoot =TCombinatoryLogic.zeroLong;

      //     fLastRewrite = " " + chBeta + "Zero";
           return
               true;
         }

         if (fParser.alphaEqualFormulas(fSelectionRoot,TCombinatoryLogic.zeroLong)){

             fNewRoot =TCombinatoryLogic.zero;

  //           fLastRewrite = " Zero";
             return
                 true;
         }
       return
               false;
     }

     public String toHTMLString() {
       return
           "<html>"+
           "<em>Zero &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
           "<strong>"+
            "0"+" :: "+chLambda+"s."+"("+chLambda+"z."+"z)"+
           "</strong>"+
           "</html>";
     }
     
     public String toString() {
         return
             "Zero      "+
              "0"+" :: "+chLambda+"s."+"("+chLambda+"z."+"z)";
       }
}

   class DoOne extends AbstractRule{


       boolean doRule() {

         fLastRewrite = " One";

         if (fSelectionRoot.equalFormulas(fSelectionRoot,TCombinatoryLogic.one)){

             fNewRoot =TCombinatoryLogic.oneLong;

             return
                 true;
           }

           if (fParser.alphaEqualFormulas(fSelectionRoot,TCombinatoryLogic.oneLong)){

               fNewRoot =TCombinatoryLogic.one;

               return
                   true;
           }
         return
                 false;
       }

       public String toHTMLString() {
         return
             "<html>"+
             "<em>One &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</em>"+
             "<strong>"+
              "1"+" :: "+chLambda+"s."+"("+chLambda+"z."+"s(z))"+
             "</strong>"+
             "</html>";
       }
       
       public String toString() {
           return
               "One       "+
                "1"+" :: "+chLambda+"s."+"("+chLambda+"z."+"s(z))";
         }
}



}
