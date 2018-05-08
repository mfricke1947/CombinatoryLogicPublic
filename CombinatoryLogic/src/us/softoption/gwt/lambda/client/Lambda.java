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

package us.softoption.gwt.lambda.client;

import static us.softoption.infrastructure.Symbols.strNull;

import java.io.StringReader;
import java.util.ArrayList;

import us.softoption.editor.TJournal;
import us.softoption.editor.TReset;
import us.softoption.infrastructure.GWTSymbolToolbar;
import us.softoption.infrastructure.TConstants;
import us.softoption.infrastructure.TPreferencesData;
import us.softoption.infrastructure.TUtilities;


import us.softoption.parser.TBergmannParser;
import us.softoption.parser.TCopiParser;
import us.softoption.parser.TDefaultParser;
import us.softoption.parser.TFormula;
import us.softoption.parser.TGentzenParser;
import us.softoption.parser.TGirleParser;
import us.softoption.parser.THausmanParser;
import us.softoption.parser.THerrickParser;
import us.softoption.parser.THowsonParser;



import us.softoption.parser.TParser;
import us.softoption.proofs.TLambda;
import us.softoption.proofs.TProofDisplayCellTable;
// mf ?? import us.softoption.tree.TGWTTreeInputPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RichTextArea.Formatter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class Lambda implements EntryPoint, TJournal, TReset {


	
	VerticalPanel fInputPanel = new VerticalPanel(); // for BugAlert and TreeInputPane  
    
	
	TProofDisplayCellTable fDisplayTable = new TProofDisplayCellTable();//replaces TreePanel
	ScrollPanel fScrollPanel=null; //holds the displayed table
	GWTSymbolToolbar fSymbolToolbar;  // tend to use this in preference to the JournalPane
	final TextArea fJournalPane = new TextArea();// often not visible, if using buttons to start
	
	RichTextArea fTextForJournal = new RichTextArea();
	
	final HorizontalPanel fComponentsPanel = new HorizontalPanel(); //buttons
	
	TLambda fLambdaController= null;
	static TParser fParser=null;//new TDefaultParser();
	
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
		else{
			fParser =new TDefaultParser();
			}
		
			
	}
	else  //no parser from preferences
	{
		fParser =new TDefaultParser();
	}
	
	}
	fLambdaController=new TLambda (fParser,this,this,fInputPanel,fDisplayTable);

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
			String filteredStr=TUtilities.lambdaFilter(inputStr);
			
			filteredStr=TUtilities.htmlEscToUnicodeFilter(filteredStr);
			// some might be &amp; etc.
		
		
				fLambdaController.startLambdaProof(filteredStr);
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
			String filteredStr=TUtilities.lambdaFilter(inputStr);
			
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
	
	String filteredStr=TUtilities.lambdaFilter(inputStr);
	
	filteredStr=TUtilities.htmlEscToUnicodeFilter(filteredStr);

	fParser.initializeErrorString();
	fParser.setVerbose(true);
	ArrayList dummy=new ArrayList();
	TFormula root = new TFormula();
    StringReader aReader = new StringReader(filteredStr);

    boolean wellformed=fParser.lambdaWffCheck(root, dummy, aReader);

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
			fFilteredInput=TUtilities.lambdaFilter(inputStr);	//to do	
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

/*    old one from host applets
 * 
 
 
 
 
 
 
 
 
 
 
 

public class Lambda extends JApplet implements TJournal{

	TDeriverDocument fDeriverDocument= new TDeriverDocument(this);
	
	
	public boolean fRewriteRules=true; // extra menu item
	
	//THIS IS A LITTLE INELEGANT-- USING A GLOBAL (should read and pass it)
	
	
	public boolean fRemoveAdvanced=false; // remove that menu
	
	 TParser fParser;
	// TProofPanel fProofPanel;     // TMy or TMyCopi etc.
	 TLambda fLambdaPanel; 
	 JTextPane fJournalPane;      // often not visible, if using buttons to start
	 HTMLEditorKit fEditorKit;
	 JLabel  fLabel=new JLabel("Lambda Reductions");
	 JPanel fComponentsPanel;    //usually buttons

	 String fInputText=null;
	 
	 //boolean fNoCommands=true;
	 Dimension fPreferredSize=new Dimension(600,400);
	 Dimension fMinimumSize=new Dimension( 540,300);
	 Dimension fJournalPreferredSize=new Dimension(600,300);
	 
	 boolean fParseOnly=false;

	 
	 public void writeHTMLToJournal(String message,boolean append){
			// haven't written it yet
		}

	 
	 
	 /************************
	 
	 
	 
	
	public void init(){
		Container contentPane=this.getContentPane();
		
		//contentPane.setBackground(Color.lightGray);  poor
		
		 Calendar cal= Calendar.getInstance();
	//	 long time=cal.getTimeInMillis();
	//	 long expiry=TConstants.expiry;
		
		 int year=cal.get(Calendar.YEAR);
			
		 if (year>TConstants.APPLET_EXPIRY){
	          JLabel label = new JLabel("The code for this applet expired in " +TConstants.APPLET_EXPIRY +" .");
           contentPane.add(label);
		}
		else{
			TPreferences.resetToDefaults();
			   
			   createGUI(contentPane);
			   
			   this.validate();  // new June 08
			   
			   this.setVisible(true);
			   this.setPreferredSize(fPreferredSize);
			   
			
		}		
	}
	
public void paint(Graphics g) {   // can see background properly in new Firefox

    	
    	super.paint(g);
 
    	g.drawRect(0, 0, 
     		   getSize().width - 1,
     		   getSize().height - 1);  	
    	

        }
	
	
	
	
	
void readParameters(){
   fInputText= getParameter("inputText");

   String title= getParameter("title"); 		
   if (title!=null)
      fLabel = new JLabel(title);
   
   String parser= getParameter("parser");
   if (parser!=null&&parser.equals("bergmann"))
	   TPreferences.fParser="bergmann [copi gentzen hausman]";
   else if (parser!=null&&parser.equals("copi"))
	   TPreferences.fParser="copi [bergmann gentzen hausman]";
   else if (parser!=null&&parser.equals("hausman"))
	   TPreferences.fParser="hausman [bergmann copi gentzen]";
   
 /*  String commands= getParameter("commands");
   if (commands!=null&&commands.equals("noCommands"))
	   fNoCommands=true;  
   String rewrites= getParameter("rewrites"); //by default true but needs advancedMenu
   if (rewrites!=null&&rewrites.equals("false"))
	   fRewriteRules=false;     //TProofPanel will not put up the menu
	
   String noAdvanced= getParameter("advancedMenu");  //true by default
   if (noAdvanced!=null&&noAdvanced.equals("false"))
	   fRemoveAdvanced=true;     //TProofPanel will put up the menu   
   
   String firstOrder= getParameter("firstOrder");
   if (firstOrder!=null&&firstOrder.equals("true"))
	   TPreferences.fFirstOrder=true;     //parse first Order + induction  
   
   String blind= getParameter("blind");
   if (blind!=null&&blind.equals("true"))
	   TPreferences.fBlind=true;        //hide justification on auto proof 

   {String derive= getParameter("derive"); 
   if ((derive!=null)&&derive.equals("false"))
	   TPreferences.fDerive= false;
   }
   
   {String derive= getParameter("useAbsurd"); // default true except Bergmann
   if ((derive!=null)&&derive.equals("true"))
	   TPreferences.fUseAbsurd= true;
   
   }
   
   {String parserOnly= getParameter("parseOnly"); // default true except Bergmann
   if ((parserOnly!=null)&&parserOnly.equals("true"))
	   fParseOnly= true;
   
   }
   
   TPreferences.fRightMargin=360;  // not reading, using default

}
	
void createGUI(Container contentPane){
		   fJournalPane = new JTextPane();
		   fEditorKit = new HTMLEditorKit();
		   fComponentsPanel = new JPanel();  //usually buttons
		 			
   readParameters();
   
   if (fParseOnly)
	   createParseOnlyGUI(contentPane);
   else{
   
   fLambdaPanel =new TLambda(fDeriverDocument);

   fEditorKit = new HTMLEditorKit();

		fJournalPane.setEditorKit(fEditorKit);
		fJournalPane.setDragEnabled(true);
		fJournalPane.setEditable(true);
		fJournalPane.setPreferredSize(fJournalPreferredSize);
		fJournalPane.setMinimumSize(new Dimension(300,200));

		if (fInputText!=null)
			fJournalPane.setText(fInputText);
			    
		contentPane.setPreferredSize(fPreferredSize);
		contentPane.setMinimumSize(fMinimumSize);  

		contentPane.setLayout(new GridBagLayout());

		contentPane.add(fLabel,new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
		       ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));

		//JScrollPane aScroller=new JScrollPane(fProofPanel);
		JScrollPane aScroller=new JScrollPane(fLambdaPanel);
		
        
		fLambdaPanel.removePruneMenuItem();
		fLambdaPanel.removeNewGoalMenuItem();
		fLambdaPanel.removeConfCodeWriter();
        fLambdaPanel.removeMarginMenuItem();
        fLambdaPanel.removeWriteProofMenuItem();
	
		
		aScroller.setPreferredSize(fPreferredSize);
		aScroller.setMinimumSize(fMinimumSize);

		contentPane.add(aScroller,new GridBagConstraints(0, 1, 2, 2, 0.0, 0.0
		       ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));

		JScrollPane anotherScroller=new JScrollPane(fJournalPane);
		
		anotherScroller.setPreferredSize(fJournalPreferredSize);
		anotherScroller.setMinimumSize(new Dimension(300,200));

 JComponent[] components= {anotherScroller,startButton()};
 JComponent[] paramComponents =readParamProofs();
 
 int depth =fJournalPane.getPreferredSize().height;
 
 if ((paramComponents.length)>0){     // don't use default journal, load buttons from javascript
    	   components= paramComponents;
    	   depth=30;
 }
			
 initializeComponentsPanel(components,depth);
		
 contentPane.add(fComponentsPanel,new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			       ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
		}
}


void createParseOnlyGUI(Container contentPane){

	fParser =new TParser();
fEditorKit = new HTMLEditorKit();

	fJournalPane.setEditorKit(fEditorKit);
	fJournalPane.setDragEnabled(true);
	fJournalPane.setEditable(true);
	
	fJournalPreferredSize=new Dimension(500,300);   // we'll make it deeper for this
	
	fJournalPane.setPreferredSize(fJournalPreferredSize);
	fJournalPane.setMinimumSize(new Dimension(300,200));

	if (fInputText!=null)
		fJournalPane.setText(fInputText);
		    
	contentPane.setPreferredSize(fPreferredSize);
	contentPane.setMinimumSize(fMinimumSize);  

	contentPane.setLayout(new GridBagLayout());
	
	fLabel=new JLabel("Lambda Verbose Parse");

	contentPane.add(fLabel,new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
	       ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));

	//JScrollPane aScroller=new JScrollPane(fProofPanel);
//	JScrollPane aScroller=new JScrollPane(fLambdaPanel);
		
//	aScroller.setPreferredSize(fPreferredSize);
//	aScroller.setMinimumSize(fMinimumSize);

	//contentPane.add(aScroller,new GridBagConstraints(0, 1, 2, 2, 0.0, 0.0
	//       ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));

	JScrollPane anotherScroller=new JScrollPane(fJournalPane);
	
	
	
	anotherScroller.setPreferredSize(fJournalPreferredSize);
	anotherScroller.setMinimumSize(new Dimension(300,200));
	
	contentPane.add(anotherScroller,new GridBagConstraints(0, 1, 2, 2, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));

	

//JComponent[] components= {anotherScroller,parseButton()};

//int depth =fJournalPane.getPreferredSize().height;
		
//initializeComponentsPanel(components,depth);
	
//contentPane.add(fComponentsPanel,new GridBagConstraints(0,/*3 1, 1, 1, 0.0, 0.0
//		       ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
	
contentPane.add(parseButton(),new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));



}





void initializeComponentsPanel(JComponent [] components,int depth){
  fComponentsPanel.setPreferredSize(new Dimension(fPreferredSize.width,depth));

  fComponentsPanel.setLayout(new GridBagLayout());               // the inner grid is a row of n buttons


 for (int i=0;i<components.length;i++){
			      fComponentsPanel.add(components[i],   new GridBagConstraints(i, 0, 1, 1, 0.0, 0.0
			         ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
			    }
			}



JComponent [] readParamProofs(){
	JComponent[] components={};
   int i=0;

   String param= "proof"+i;	
	
   String value= getParameter(param);
	   while (value!=null&&i<10){
		   i++;
		   param= "proof"+i;
		   value= getParameter(param);
	   }
	   
	   
	if (i>0){   
	int count =i;
	   components= new JComponent[count];
	
	   i=0;
	
	   param= "proof"+i;
	   
	   String label="Proof";
	   
	   if (count>6)
		   label="Pf";     // we only fit 6, but we will squeeze a few more
		
       value= getParameter(param);
		   while (value!=null&&i<10){
			   components[i]=proofButton(label+(i+1),value);
			   i++;
			   param= "proof"+i;
			   value= getParameter(param);
		   }
	}
	   	   
	   return 
	   components;
}
	
/***********************  Buttons **************************
	
	JButton proofButton(String label, final String inputStr){
		JButton button = new JButton(label);	    
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String filteredStr=TUtilities.noFilter(inputStr);
				//fProofPanel.startProof(filteredStr);
				fLambdaPanel.startLambdaProof(filteredStr);
			}});
		return
		   button;
	}
	
	JButton startButton(){
		JButton button = new JButton("Start from selection");	    
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				readAndStart();
			}});
		return
		   button;
	}
	
	JButton parseButton(){
		JButton button = new JButton("Parse selection");	    
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parse();
			}});
		return
		   button;
	}
	
/***********************  End of Buttons **************************
	
void readAndStart(){
	String inputStr=readSource(TUtilities.noFilter);
	
//	fProofPanel.startProof(inputStr);
	
	fLambdaPanel.startLambdaProof(inputStr);
}
	
/*
  String inputStr=readDualSource(TUtilities.logicFilter);//TUtilities.readSelectionToString(fJournalPane,TUtilities.logicFilter);

    //May 04 DO WE WANT LISP FILTER HERE?



    ((TMyProofPanel)(fDeriverDocument.fProofPanel)).startProof(inputStr);
 * 

void parse(){
	String inputStr=readSource(TUtilities.noFilter);

	fParser.initializeErrorString();
	fParser.setVerbose(true);
	ArrayList dummy=new ArrayList();
	TFormula root = new TFormula();
    StringReader aReader = new StringReader(inputStr);

    boolean wellformed=fParser.lambdaWffCheck(root, dummy, aReader);

    if (!wellformed)
      fDeriverDocument.writeToJournal(fParser.fCurrCh + 
    		  TConstants.fErrors12 + fParser.fParserErrorMessage, 
    		  TConstants.HIGHLIGHT, !TConstants.TO_MARKER);
    else
    	  writeToJournal(" Well formed " +
    			  fParser.writeFormulaToString(root), 
    			  true, 
    			  false);
	
	fParser.setVerbose(false);
	
	fJournalPane.requestFocus();
}



 
 /**************************** Utilities ************************************

 String readSource(int filter){

 	return
 	   TSwingUtilities.readSelectionToString(fJournalPane,filter);
 		}

 public void writeOverJournalSelection(String message){

 	   if (message.length()>0)
 	     fJournalPane.replaceSelection(message);
 	}


 	
 public void writeToJournal(String message, boolean highlight,boolean toMarker){

         int newCaretPosition = fJournalPane.getSelectionEnd(); //if there isn't one it's dot which is the old one

         int messageLength = message.length();

         if (messageLength>0) {

           fJournalPane.setSelectionStart(newCaretPosition);
           fJournalPane.setCaretPosition(newCaretPosition);    //leave existing selection and do everything after

           fJournalPane.replaceSelection(message);

           if (highlight) {
             fJournalPane.setSelectionStart(newCaretPosition);
             fJournalPane.setSelectionEnd(newCaretPosition+messageLength);

           }

         }
      }	
	

}



/*
   JFrame aFrame=new JFrame("Show whether the formula is satisfiable ie make it true");
   TSatisfiable game =new TSatisfiable(aFrame,fDeriverDocument.getParser());

   aFrame.getContentPane().add(game);
     aFrame.setSize(500,230);
      aFrame.setLocation((TDeriverApplication.fScreenSize.width-500)/2, (TDeriverApplication.fScreenSize.height-230)/2);
      aFrame.setResizable(false);

      aFrame.setVisible(true);   // used to be commented out but the Proguard obfuscator won't let the sub-panel set this
      game.run();
   }
 * 

 
 
 
 * 
 * 
 */

