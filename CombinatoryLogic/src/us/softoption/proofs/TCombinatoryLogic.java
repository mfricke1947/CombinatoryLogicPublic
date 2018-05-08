package us.softoption.proofs;

import static us.softoption.infrastructure.Symbols.chAlpha;
import static us.softoption.infrastructure.Symbols.chDoubleArrow;
import static us.softoption.infrastructure.Symbols.chImplic;
import static us.softoption.infrastructure.Symbols.chLambda;
import static us.softoption.infrastructure.Symbols.strCR;
import static us.softoption.infrastructure.Symbols.strNull;

import java.io.StringReader;
import java.util.ArrayList;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import us.softoption.editor.TJournal;
import us.softoption.editor.TReset;
import us.softoption.infrastructure.TConstants;
import us.softoption.infrastructure.TPreferencesData;
import us.softoption.parser.TCLParser;
import us.softoption.parser.TFormula;
import us.softoption.parser.TParser;


public class TCombinatoryLogic extends TMyProofController {


static TFormula x = new TFormula(
                               TFormula.variable,
                                     "x",
                                     null,
                                     null);
static TFormula y = new TFormula(
                                     TFormula.variable,
                                     "y",
                                     null,
                                   null);




static TFormula z = new TFormula(
                             TFormula.variable,
                                   "z",
                                   null,
                                   null);
static TFormula s = new TFormula(
                                   TFormula.variable,
                                   "s",
                                   null,
                                   null);

static TFormula sz = new TFormula(
                              TFormula.application,
                               "",
                               s,
                               z);

static TFormula zero = new TFormula(
                                   TFormula.functor,
                                 "0",
                                 null,
                                 null);

static TFormula zeroLong = new TFormula(
                                 TFormula.lambda,
                                 String.valueOf(chLambda),
                                 s,
                                 new TFormula(
                                          TFormula.lambda,
                                          String.valueOf(chLambda),
                                          z,
                                          z
                                               )
                                  );

static TFormula one = new TFormula(
                                                                 TFormula.functor,
                                                               "1",
                                                               null,
                                                               null);

static TFormula oneLong = new TFormula(
                                   TFormula.lambda,
                                   String.valueOf(chLambda),
                                   s,
                                   new TFormula(
                                            TFormula.lambda,
                                            String.valueOf(chLambda),
                                            z,
                                            sz
                                                    )
                                  );



static TFormula T = new TFormula(TFormula.functor,
                                "T",
                                null,
                                null);
static TFormula TForm = new TFormula(
                                 TFormula.lambda,
                                 String.valueOf(chLambda),
                                 x,
                                 new TFormula(
                                 TFormula.lambda,
                                      String.valueOf(chLambda),
                                      y,
                                      x
                                                                                  )
                                  );

static TFormula F = new TFormula(TFormula.functor,
                                                                    "F",
                                                                                                                           null,
                                                                                                                           null);
static TFormula FForm = new TFormula(
                                                               TFormula.lambda,
                                                               String.valueOf(chLambda),
                                                               x,
                                                               new TFormula(
                                                               TFormula.lambda,
                                                                    String.valueOf(chLambda),
                                                                    y,
                                                                    y
                                                                                                                )
                                                                );


static TFormula C = new TFormula(TFormula.functor,
                                 "C",
                                 null,
                                 null);
static TFormula implic = new TFormula(TFormula.functor,
                                 ""+chImplic,
                                 null,
                                 null);

static TFormula CForm = new TFormula(
                                 TFormula.lambda,
                                 String.valueOf(chLambda),
                                 x,
                                 new TFormula(
                                          TFormula.lambda,
                                          String.valueOf(chLambda),
                                          y,
                             new TFormula(
                            TFormula.lambda,
                            String.valueOf(chLambda),
                            z,
                             new TFormula(
                            TFormula.application,
                            "",
                            new TFormula(
                            TFormula.application,
                            "",
                            x,
                             y
                             )
,
                             z
                             )

                             )

                                           ));








static String alphaJustification = " " + chAlpha;





public TCombinatoryLogic(){
    super();

// rearrangeMenus();
  }

public TCombinatoryLogic(TParser aParser, TReset aClient,TJournal itsJournal, VerticalPanel inputPanel,
		 TProofDisplayCellTable itsDisplay){
	super(aParser,aClient,itsJournal,inputPanel,itsDisplay);

 //         rearrangeMenus();
  }

@Override
public boolean load(String inputStr){
	return
			loadCL(inputStr);
	
	
}


public boolean loadCL(String inputStr){

 /*need to go careful here, the inputStr can contain spaces, as can wff lambdas */
	
/* there can be only one 'premise' and one 'conclusion' i.e. premise ⇒ conclusion */	

               TParser parser=null;;

               if (parser==null){
                 if (fParser==null)
                   initializeParser();
                 parser = fParser;
               }
               //we need to sort the above out, the document has a parser, and so too does the proof panel

               parser.initializeErrorString();

              ArrayList dummy=new ArrayList();
              boolean wellformed = true;

              fProofStr="";  //re-initialize; the old proof may still be there and if this turns out to be illformed will stay there

              if ((inputStr==null)||inputStr.equals(strNull)){
            	  createBlankStart();
            	  return
                    wellformed;
              }

       String[]premisesAndConclusion = inputStr.split(String.valueOf(chDoubleArrow),2);  /* they may input two
               therefore symbols, in which case we'll split at the first and let the parser report the second*/

          if (premisesAndConclusion[0]!=null&&(!premisesAndConclusion[0].equals(strNull))){  // there are premises

        /*The next bit is a kludge. Unfortunately the premises are separated by commas, and also subterms within
            compound terms eg Pf(a,b),Hc. We want to separate the premises but not the terms. So we will change the
         premise separators to another character*/

      //  	  premisesAndConclusion[0]=changeListSeparator(premisesAndConclusion[0]);

      //      StringTokenizer premises = new StringTokenizer(premisesAndConclusion[0],String.valueOf(chSeparator)/*String.valueOf(chComma)*/);

     //       while ((premises.hasMoreTokens())&&wellformed){
      //         inputStr=premises.nextToken();
        	  
        	inputStr=premisesAndConclusion[0];  

            if (inputStr!=null&&!inputStr.equals(strNull)){   // can be nullStr if input starts with therefore, or they put two commas togethe,should just skip
                   TFormula root = new TFormula();
                   StringReader aReader = new StringReader(inputStr);


                 //  wellformed=parser.lambdaWffCheck(root, dummy, aReader);
                   
                   wellformed=((TCLParser)parser).cLWffCheck(root, dummy, aReader);

                   if (!wellformed)
                   //  fDeriverDocument.writeToJournal(parser.fCurrCh + TConstants.fErrors12 + parser.fParserErrorMessage, TConstants.HIGHLIGHT, !TConstants.TO_MARKER);
                	   bugAlert ("Error on reading.",fParser.fCurrCh + TConstants.fErrors12 + 
                       		   (fParser.fParserErrorMessage.toString()).replaceAll(strCR, ""));
                   else
                       {addPremise(root);
                       
                       //bugAlert("Add Premise", fParser.writeFormulaToString(root));
                       
                       if (fProofStr.length()==0)
                         fProofStr=inputStr;
                       else
                         fProofStr+=chComma+inputStr;
                       }
                   }
           // }          // done with premises
          }
            if (premisesAndConclusion.length>1){  // if there is no therefore the original 'split' won't split the input
              inputStr = premisesAndConclusion[1];

              if (inputStr!=null&&!inputStr.equals(strNull)){   // can be nullStr if input starts with therefore, or they put two commas togethe,should just skip
                   TFormula root = new TFormula();
                   StringReader aReader = new StringReader(inputStr);

                  //wellformed=fParser.lambdaWffCheck(root, dummy, aReader);
                   wellformed=((TCLParser)parser).cLWffCheck(root, dummy, aReader);

                   if (!wellformed)
                //     fDeriverDocument.writeToJournal(parser.fCurrCh + TConstants.fErrors12 + parser.fParserErrorMessage, TConstants.HIGHLIGHT, !TConstants.TO_MARKER);
                	   bugAlert ("Error on reading.",fParser.fCurrCh + TConstants.fErrors12 + 
                       		   (fParser.fParserErrorMessage.toString()).replaceAll(strCR, ""));
                   else
                       {addConclusion(root);
                       
                      // bugAlert("Add Conclusion", fParser.writeFormulaToString(root));
                       
                       fProofStr+=chDoubleArrow+inputStr;
                       }
                   }

            }

            fDisplayCellTable.synchronizeViewToData();


              return
                  wellformed;

     }


/*
void rearrangeMenus(){  // we use the advanced menu

//fMenuBar.removeItem(fRules); 
//fMenuBar.removeItem(fWizard);
	
	fRules.removeFromParent();
	fWizard.removeFromParent();
	
//fMenuBar.add(rewriteMenuItem);

fAdvancedRules.setTitle("Rules");
fAdvancedRules.removeItem(theoremMenuItem);

//fAlphaMenuItem.setText(chAlpha+" Alpha");
//fAlphaMenuItem.addActionListener(new TLambda_fAlphaMenuItem_actionAdapter(this));


fAlphaMenuItem = new MenuItem("Alpha",new Command(){
		public void execute() {
			doAlpha();}}); 

fAdvancedRules.addItem(fAlphaMenuItem);

};





*/

@Override
public MenuBar createMenuBar(){
	
	// Top-level menu
	
	fMenuBar.addStyleName("menu");
	
	fMenuBar.addItem("Rules",fRules); // Creates item and adds menutwo 
	
	
	
    if (    (TPreferencesData.fAdvancedMenu)||
    		(TPreferencesData.fIdentity)||
    	       fUseIdentity||
    	       TPreferencesData.fRewriteRules||
    	       TPreferencesData.fFirstOrder||
    	       TPreferencesData.fSetTheory) {	
    	
 //   	   fMenuBar.addItem("Advanced",fAdvancedRules);
    	    }
	
	fMenuBar.addItem("Edit+",fEdit); // Creates item and adds menutwo
//	fMenuBar.addItem("Wizard",fWizard); // Creates item and adds menutwo
	
	
/*	fRules.addItem(tIMenuItem);
	fRules.addItem(negIMenuItem);
	fRules.addItem(negEMenuItem);
	fRules.addItem(andIMenuItem);
	fRules.addItem(andEMenuItem);
	fRules.addItem(orIMenuItem);
	fRules.addItem(orEMenuItem);
	fRules.addItem(implicIMenuItem);
	fRules.addItem(implicEMenuItem);
	fRules.addItem(equivIMenuItem);
	fRules.addItem(equivEMenuItem);
//	fRules.addItem(theoremMenuItem);
	fRules.addItem(absurdIMenuItem);
	
	assembleAdvancedMenu();
	
//	fUndoRedo = new MenuItem("Undo", undoRedoCommand);	*/
	
	fRules.addItem(rewriteMenuItem);
	
	boolean combinatoryLogic=true;
	
	fEdit.addItem(fUndoRedo);
	fEdit.addSeparator();
	fEdit.addItem(fCutProofline);
	//fEdit.addItem(fPrune);
	fEdit.addItem(fStartAgain);
//	fEdit.addSeparator();
//	fEdit.addItem(fNewGoal);

	

	
	return
			fMenuBar;
}	


void doSetUpRulesMenu(){     /* does advanced rules as well*/
       TFormula selectedFormula=null, secondSelectedFormula=null, thirdSelectedFormula=null;

       TProofline selection = fDisplayCellTable.oneSelected();

       boolean oneSelected;

       int totalSelected=fDisplayCellTable.totalSelected();

       oneSelected=(selection!=null);

      

       if (oneSelected){
         selectedFormula = selection.fFormula;

         



       }




}




void  doStartAgain(){              //menuItem override
    TUndoableProofEdit newEdit = new TUndoableProofEdit();   // this copies old lines

    if (fProofStr!=null)          // record of this proof as a string
      startCLProof(fProofStr);

    newEdit.doEdit();            // does not do any editing but kills last edit and allows undo
 }







@Override
public void doRewrite(){
	  Button defaultButton;

	  TGWTProofInputPanel inputPane;

	 // fSelectionRewrite="";

	  TProofline selectedLine=fDisplayCellTable.oneSelected();

	  if (selectedLine!=null){

	    String originalFormulaStr=fParser.writeFormulaToString(selectedLine.fFormula);

	    TGWTRewriteRules rules= new TCombinatoryLogicRewriteRules(selectedLine.fFormula,fParser);
	    
//mf 4/29/18	    TRewriteRules rules= new TCombinatoryLogicRewriteRules(selectedLine.fFormula,fParser);

	     boolean mustChange=true;
	     
	     defaultButton = new Button("Go");
	     defaultButton.addClickHandler(new RewriteHandler(
	             						rules,
	             						selectedLine.fLineno,
	             						selectedLine.fFormula,
	             						mustChange));

	/*     defaultButton = new JButton(new RewriteAction("Go",
	                                                   rules,
	                                                   selectedLine.fLineno,
	                                                   mustChange)); */


	     Widget[]components = {rules.getListBox(),  cancelButton(), defaultButton };  // put cancel on left

	     inputPane = new TGWTProofInputPanel("Choose rule, select (sub)formula to rewrite, click Go...",
	                                      rules.getBeforeText(),
	                                      "After rewrite, the formula will look like this:",
	                                      rules.getAfterText(),
	                                      components);


	          addInputPane(inputPane,!SELECT);

	          //TO DO	 inputPane.getRootPane().setDefaultButton(defaultButton);
	        //TO DO fInputPane.setVisible(true); // need this
	        //TO DO rules.getBeforeText().requestFocus();         // so selected text shows

	   }

	}


  public void startCLProof(String inputStr){


    dismantleProof(); //{previous one}

    initProof();

    if (loadCL(inputStr))
      startUp();

  }
  
  
  @Override	
  void initializeParser(){
  	  fParser=new TCLParser();
  	}; 

}


