package us.softoption.gwt.combinatorylogic.client;

import java.io.StringReader;
import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RichTextArea.Formatter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import us.softoption.editor.TJournal;
import us.softoption.editor.TReset;
import us.softoption.infrastructure.GWTSymbolToolbar;
import us.softoption.infrastructure.TConstants;
import us.softoption.infrastructure.TPreferencesData;
import us.softoption.infrastructure.TUtilities;
import us.softoption.parser.TBergmannParser;
import us.softoption.parser.TCLParser;
import us.softoption.parser.TCopiParser;
import us.softoption.parser.TDefaultParser;
import us.softoption.parser.TFormula;
import us.softoption.parser.TGentzenParser;
import us.softoption.parser.TGirleParser;
import us.softoption.parser.THausmanParser;
import us.softoption.parser.THerrickParser;
import us.softoption.parser.THowsonParser;
import us.softoption.parser.TParser;
import us.softoption.proofs.TCombinatoryLogic;
import us.softoption.proofs.TProofDisplayCellTable;


public class CombinatoryLogic implements EntryPoint, TJournal, TReset {


	
	VerticalPanel fInputPanel = new VerticalPanel(); // for BugAlert and TreeInputPane  
    
	
	TProofDisplayCellTable fDisplayTable = new TProofDisplayCellTable();//replaces TreePanel
	ScrollPanel fScrollPanel=null; //holds the displayed table
	GWTSymbolToolbar fSymbolToolbar;  // tend to use this in preference to the JournalPane
	final TextArea fJournalPane = new TextArea();// often not visible, if using buttons to start
	
	RichTextArea fTextForJournal = new RichTextArea();
	
	final HorizontalPanel fComponentsPanel = new HorizontalPanel(); //buttons
	
	TCombinatoryLogic fLambdaController= null;
	static TParser fParser=null;
	
	MenuBar fMenuBar = new MenuBar();  //true makes it vertical
		
//	static boolean fPropLevel=false;
	
	static final boolean HIGHLIGHT = true;
	
//	 Label  fLabel=new Label("Trees");


	 String fInputText=null;
	 
	
	boolean fDebug=false;
	
	boolean fExtraDebug=false;
	
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		TPreferencesData.readParameters();		
		setLocalParameters();  // sets Parser, Controller, and Journal		
        createGUI();  
	}
	
	
	
	
void buildMenus(){
	

if (fLambdaController!=null){
   fMenuBar=fLambdaController.createMenuBar();
}

	
/**************** */
	
if 	(RootPanel.get("menu")!=null)
	RootPanel.get("menu").add(fMenuBar);  //commented back in Jan 20 2013


}
/*******************  TReset **************************/

public void enableMenus(){  //at present GWT does not have a good way of doing this.
	

}

public void disableMenus(){


}

public void reset(){
	;
}

/******************** End of TReset **********************************/

/*
void buildMenuButtons(){
	Widget[] menuButtons=null;//={fAndButton,fExtendButton, fCloseButton, fIsClosedButton, fOpenBranchButton,
//			fIdentityIntroButton};
	
	Widget[] editButtons=null;
	
//TO DO	
	
	if (fLambdaController!=null){
		menuButtons=fLambdaController.getButtons();
		editButtons=fLambdaController.getEditButtons();
	}

	int dummy=0;
	
	
}
*/

	




void createGUI(){
	
	if (TPreferencesData.fParseOnly)
		   createParseOnlyGUI();
	   else{
	

buildMenus();

//buildMenuButtons();

	
 
Widget [] paramButtons =readParamProofs();

if (RootPanel.get("input")!=null)
	RootPanel.get("input").add(fInputPanel);

fScrollPanel=new ScrollPanel(fDisplayTable); //problem here Jan 2013

fScrollPanel.setSize("600px", "400px");  //need this


if (RootPanel.get("proof")!=null)
	RootPanel.get("proof").add(fScrollPanel);  

if ((paramButtons.length)>0)
	   finishNoPalette(paramButtons);
else
	   finishWithPalette();

fLambdaController.startProof("");   // gwt does not like no proof at all

	   }
}

void createParseOnlyGUI(){
	 finishWithPalette();
	
}


void finishNoPalette(Widget [] components){
	int depth=30;    // this is the height of the buttons
	
	 initializeComponentsPanel(components,depth);
	 
	 if (RootPanel.get("buttons")!=null)	
		 RootPanel.get("buttons").add(fComponentsPanel);	
}

//end of createGUI

void initializeComponentsPanel(Widget [] components,int depth){
	
	fComponentsPanel.setStyleName("buttons");
	
	fComponentsPanel.setHeight("50px");
	
	fComponentsPanel.setSpacing(20);

	 for (int i=0;i<components.length;i++){
				      fComponentsPanel.add(components[i]);
				    }
				}





void finishWithPalette(){

boolean lambda=true,modal=false,settheory=false;

	String symbols =  fParser.getInputPalette(lambda,modal,settheory);
		
	fTextForJournal.setWidth("100%");
	fTextForJournal.setHeight("240px");
	
	//text.setText(fInputText);
	
	fTextForJournal.setHTML(fInputText);

	fSymbolToolbar = new GWTSymbolToolbar(fTextForJournal,symbols);

	if 	(RootPanel.get("journal")!=null)
		RootPanel.get("journal").add(fSymbolToolbar);
	if 	(RootPanel.get("journal")!=null)
		RootPanel.get("journal").add(fTextForJournal);
	
	
	if (TPreferencesData.fParseOnly){
		if 	(RootPanel.get("startButton")!=null)
		RootPanel.get("startButton").add(parseButton());
		}
	else{
		if 	(RootPanel.get("startButton")!=null)
			RootPanel.get("startButton").add(startButton());
	}

/*	Button aWidget= fLambdaController.cancelButton();
	Widget [] components ={aWidget};
		
	TGWTTreeInputPanel fInputPane = new TGWTTreeInputPanel("Hello",new  TextBox(),components);
*/	
}


void setLocalParameters(){
	fInputText=TPreferencesData.fInputText;  // probably don't need input text field
	
	fJournalPane.setText(TPreferencesData.fInputText);  // not using journal, use toolbar
//	fPropLevel=TPreferencesData.fPropLevel;
	
	{ String parser =TPreferencesData.fParser;
	if (parser!=null) {
		if (parser.equals("bergmann")){
			   fParser =new TBergmannParser();
		   }
		else if (parser.equals("copi")){
			   fParser =new TCopiParser();
		   }
		else if (parser.equals("gentzen")){
			   fParser =new TGentzenParser();
		   }
		else if (parser.equals("girle")){
			   fParser =new TGirleParser();
		 	}
		else if (parser.equals("hausman")){
			   fParser =new THausmanParser();
		   }
		else if (parser.equals("herrick")){
			   fParser =new THerrickParser();
			}
		else if (parser.equals("howson")){
			   fParser =new THowsonParser();
			}
		else if (parser.equals("combinatorylogic")){
			   fParser =new TCLParser();
			}
		else{
			fParser =new TDefaultParser();
			}
		
			
	}
	else  //no parser from preferences
	{
		fParser =new TDefaultParser();
	}
	
	}
	fLambdaController=new TCombinatoryLogic (fParser,this,this,fInputPanel,fDisplayTable);

	fDisplayTable.setController(fLambdaController);	
	
}
	

String readParameterValue(String key){

Dictionary params;
	
try{
	params = Dictionary.getDictionary("Parameters");}
catch (Exception ex) {return "";}
	 		
if (params!=null){		
	try{String value= params.get(key);
	return
		value;}
	catch (Exception ex){return "";}
}
return
		"";
}  



Widget [] readParamProofs(){
	Widget[] components={};
   int i=0;

   String param= "proof"+i;	
	
   String value= readParameterValue(param);
	   while (value!=null&&!value.equals("")&&i<10){
		   i++;
		   param= "proof"+i;
		   value= readParameterValue(param);
	   }
	   
	   
	if (i>0){   
	int count =i;
	   components= new Widget[count];
	   i=0;	
	   param= "proof"+i;	   
	   String label="Proof";	   
	   if (count>6)
		   label="Pr";     // we only fit 6, but we will squeeze a few more
		
       value= readParameterValue(param);
		   while (value!=null&&!value.equals("")&&i<10){
			   components[i]=proofButton(label+(i+1),value);
			   i++;
			   param= "proof"+i;
			   value= readParameterValue(param);
		   }
	}
	   	   
	   return 
	   components;
}

/***********************  Buttons ***************************/


Button proofButton(String label, final String inputStr){
	Button button = new Button(label);	
	
	
	ProofHandler pHandler = new ProofHandler(inputStr);
	button.addClickHandler(pHandler);

	return
	   button;
}

Button startButton(){
	Button button = new Button("Start from selection");	    

	button.addClickHandler(new ClickHandler(){@Override 
		public void onClick(ClickEvent event) {
		
			String inputStr=fSymbolToolbar.getSelectionAsText();
			String filteredStr=TUtilities.combinatoryLogicFilter(inputStr);
			
			filteredStr=TUtilities.htmlEscToUnicodeFilter(filteredStr);
			// some might be &amp; etc.
		
		
				fLambdaController.startCLProof(filteredStr);
		}
			});
	

	return
	   button;
}



Button parseButton(){
	Button button = new Button("Parse selection");	    

	button.addClickHandler(new ClickHandler(){@Override 
		public void onClick(ClickEvent event) {
		parse();
		
		/*
			String inputStr=fSymbolToolbar.getSelectionAsText();
			String filteredStr=TUtilities.combinatoryLogicFilter(inputStr);
			
			filteredStr=TUtilities.htmlEscToUnicodeFilter(filteredStr);
			// some might be &amp; etc.
		
		
				fLambdaController.startProof(filteredStr); */
		} 
			});
	

	return
	   button;
}

/***********************  End of Buttons ***************************/


void parse(){
	//String inputStr=readSource(TUtilities.noFilter);
	String inputStr=fSymbolToolbar.getSelectionAsText();
	
	String filteredStr=TUtilities.combinatoryLogicFilter(inputStr);
	
	filteredStr=TUtilities.htmlEscToUnicodeFilter(filteredStr);

	fParser.initializeErrorString();
	fParser.setVerbose(true);
	ArrayList dummy=new ArrayList();
	TFormula root = new TFormula();
    StringReader aReader = new StringReader(filteredStr);

    boolean wellformed=((TCLParser)fParser).cLWffCheck(root, dummy, aReader);

/*    boolean debug=true;
    if (debug)
    	writeToJournal(" CL parser ",TConstants.HIGHLIGHT, !TConstants.TO_MARKER);
 */   
    if (!wellformed)
    		  writeToJournal(fParser.fCurrCh + 
    		  TConstants.fErrors12 + fParser.fParserErrorMessage, 
    		  TConstants.HIGHLIGHT, !TConstants.TO_MARKER);
    else
    	  writeToJournal(" Well formed " +
    			  fParser.writeFormulaToString(root), 
    			  true, 
    			  false);
	
	fParser.setVerbose(false);
	
//to do	fJournalPane.requestFocus();
}



/*****************  Commands ************************/
	

/**************************** Utilities *************************************/
/*
		static public String peculiarFilter(String inputStr){
			 String outputStr;

			 outputStr=inputStr.toLowerCase();
			 outputStr=outputStr.replaceAll("[^()a-z]"," ");   // we want just lower case, brackets, and blanks

			return
			     outputStr;
			}

		private String readSource(){
		return
		     peculiarFilter(fJournalPane.getSelectedText());
				}

		static public String defaultFilter(String inputStr){  //ie standard filter
		    String outputStr;

		    outputStr=inputStr.replaceAll("\\s",strNull); // removes ascii whitespace?
		    outputStr=outputStr.replaceAll("\u00A0",strNull); // removes html/unicode non breaking space?

		    return
		        outputStr;
		  } */

		/**************************** End of Utilities *************************************/

			
			
		
	class ProofHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on Proof.
			 */
		String fFilteredInput="";
		
		public ProofHandler(String inputStr) {
			fFilteredInput=TUtilities.combinatoryLogicFilter(inputStr);	//to do	
		}
			
			public void onClick(ClickEvent event) {
				fLambdaController.startProof(fFilteredInput);

			}
		
	}	
	
	


/************ TO DO TO IMPLEMENT TJOURNAL INTERFACE *************/
public void writeHTMLToJournal(String message,boolean append){
// haven't written it yet
	if (append)
		fTextForJournal.setHTML(fTextForJournal.getHTML()+message);
	else{
		Formatter aFormatter=fTextForJournal.getFormatter();
		if (aFormatter!=null)
			aFormatter.insertHTML(message);
		
	}
	;
	
	
	
	}

public void writeOverJournalSelection(String message){  //I think this code is right
	Formatter aFormatter=fTextForJournal.getFormatter();
	if (aFormatter!=null)
		aFormatter.insertHTML(message);

/*	   if (message.length()>0)
	     fJournalPane.replaceSelection(message); */
	}


public int getSelectionEnd(RichTextArea text){
	//This is a hack to get the selection by putting a dummy marker around it then removing it	
		
	int end=0;
	
	if (text!=null){
		Formatter aFormatter=text.getFormatter();
		if (aFormatter!=null){
	
			String fakeUrl=	"H1e2l3l4o";
			String tag= "<a href=\""+fakeUrl+"\">";

			int tagLength= tag.length();
		
			aFormatter.createLink(fakeUrl);
		
			String allText=text.getHTML();
		
		
			int startSel=allText.indexOf(tag);
			int endSel=allText.indexOf("</a>", startSel);
		
			String selStr=allText.substring(startSel+tagLength, endSel);
		
			aFormatter.removeLink();
			
			
			
		
		//There is a problem, if there was no selection, the text of the link will be
		// inserted as extra text changing it.
		
		 if (selStr.equals(fakeUrl)){  // we have a problem (and we are assuming that fakeUrl
			                           // does not actually occur in the text
			 selStr="";                //We are going to return nothing
			 
			 allText=text.getHTML(); //start again with the altered text
			 
			 String beforeStr=allText.substring(0, startSel);
			 String afterStr=allText.substring(startSel+fakeUrl.length());
			 
			 if (allText.substring(startSel, startSel+fakeUrl.length()).equals(fakeUrl))
				 allText=beforeStr+afterStr; // remove insertion
			 
			 text.setHTML(allText);
			 
			 //works, but removes focus (don't worry about it)
		
		 }
		 
		 end=startSel+selStr.length(); //it's hard to get the end but this is one way
		
	//	allText=richText.getHTML();
		
	}
	}
	return
			end;
	}



	
public void writeToJournal(String message, boolean highlight,boolean toMarker){
	
	String allText=fTextForJournal.getHTML();
	int endSel=getSelectionEnd(fTextForJournal);
	
	String before=allText.substring(0,endSel);
	String after=allText.substring(endSel);
	
	fTextForJournal.setHTML(before+message+after);   //No highlighting yet


}







/*************************/ 


/*************************************************************/

}


