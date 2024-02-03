/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleConfig.java
*
******************************************************************/

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

import sun.tools.javac.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleConfig extends Dialog {

	private CtbIDE						mCtbIDE;

	private ClassNameComponent		mClassNameComponent			= null;
	private NodeComponent			mNodeComponent					= null;
	private ScriptEditorComponent	mJavaEditorPaneComponent	= null;
	private IconComponent			mIconComponent					= null;
	
	public DialogModuleConfig(Frame parentFrame, CtbIDE ctbIDE, ModuleType modType) {
		super(parentFrame, "Module Configuration");
		setSize(300, 200);
		
		mCtbIDE = ctbIDE;
			
		mClassNameComponent	= new ClassNameComponent(ctbIDE);
		mNodeComponent			= new NodeComponent();
		mIconComponent			= new IconComponent(null);
		
		JPanel moduleSettingPanel = new JPanel();
		moduleSettingPanel.setLayout(new BoxLayout(moduleSettingPanel, BoxLayout.Y_AXIS));
		//moduleSettingPanel.setLayout(new GridLayout(3, 1, 1, 1));
		moduleSettingPanel.add(mClassNameComponent);
		moduleSettingPanel.add(mNodeComponent);
		moduleSettingPanel.add(mIconComponent);
		moduleSettingPanel.add(new JPanel());

		//moduleSettingPanel.setMaximumSize(new Dimension(300, 400));
		//moduleSettingPanel.setPreferredSize(new Dimension(300, 300));
						
		mJavaEditorPaneComponent = new ScriptEditorComponent();

		mClassNameComponent.setScriptEditorComponent(mJavaEditorPaneComponent);
		
		JPanel panel = new JPanel();
/*
		panel.setLayout(new BorderLayout(10, 10));
		panel.add(moduleSettingPanel, BorderLayout.WEST);
		panel.add(mJavaEditorPaneComponent, BorderLayout.CENTER);
*/
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(moduleSettingPanel);
		panel.add(Box.createRigidArea(new Dimension(20, 100)));
		panel.add(mJavaEditorPaneComponent);

		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = panel;
		setComponents(dialogComponent);
		
		if (modType != null) {
			mClassNameComponent.setClassName(modType.getClassName());
			mClassNameComponent.setName(modType.getName());
			mClassNameComponent.setEnabled(false);
			
			mNodeComponent.setInNodeNum(modType.getNInNodes());
			mNodeComponent.setOutNodeNum(modType.getNOutNodes());
			mNodeComponent.setExecutionNode(modType.hasExecutionNode());
			
			mIconComponent.setIcon(new ImageIcon(modType.getIcon()));
			
			String scriptFileName = ctbIDE.getModuleTypeScriptDirectory() + modType.getModuleClassName() + ".java";
			mJavaEditorPaneComponent.setScriptFile(scriptFileName);
		}
		
		setOkButtonEnabled(false);
	}

	public DialogModuleConfig(Frame parentFrame, CtbIDE ctbIDE) {
		this(parentFrame, ctbIDE, null);
	}

	public Dialog getDialog() {
		return this;
	}

	public CtbIDE getCtbIDE() {
		return mCtbIDE;
	}
	
	public ClassNameComponent getClassNameComponent() {
		return mClassNameComponent;
	}
	
	public NodeComponent getNodeComponent() {
		return mNodeComponent;
	}
	
	public ScriptEditorComponent getScriptEditorPaneComponent() {
		return mJavaEditorPaneComponent;
	}
	
	public IconComponent getIconComponent() {
		return mIconComponent;
	}
	
	public int doModal() {
		int ret = super.doModal();
		if (ret == OK_OPTION) 
			addModuleType();
		if (ret == CANCEL_OPTION) 
			doCancel();
		return ret;
	}

	private void addModuleType() {
		String	className			= getClassString();
		String	name					= getNameString();
		String	inNodeName[]		= getInNodeNames();
		String	outNodeName[]		= getOutNodeNames();
		boolean	hasExecutionNode	= hasExecutionNode();
		int		attribute			= Module.ATTRIBUTE_USER;
		File		sourceFile			= getScriptSourceFile();
		File		classFile			= getScriptClassFile();
		File		iconFile				= getIconFile();
		
		getCtbIDE().addModuleType(className, name, inNodeName, outNodeName, hasExecutionNode, attribute, sourceFile, classFile, iconFile);
	}
	
	private void doCancel() {
		File srcFile = getScriptSourceFile();
		if (srcFile != null)
			srcFile.delete();
		File classFile = getScriptClassFile();
		if (classFile != null)
			classFile.delete();
	}
	
	////////////////////////////////////////////////
	// Result
	////////////////////////////////////////////////
	
	public String getClassString() {
		return getClassNameComponent().getClassString();
	}
	
	public String getNameString() {
		return getClassNameComponent().getNameString();
	}

	public String[] getInNodeNames() {
		return getNodeComponent().getInNodeNames();
	}

	public String[] getOutNodeNames() {
		return getNodeComponent().getOutNodeNames();
	}

	public boolean hasExecutionNode() {
		return getNodeComponent().hasExecutionNode();
	}
		
	public File getScriptSourceFile() {
		return getScriptEditorPaneComponent().getCreatedScriptSourceFile();
	}

	public File getScriptClassFile() {
		return getScriptEditorPaneComponent().getCreatedScriptClassFile();
	}

	public File getIconFile() {
		return getIconComponent().getIconFile();
	}
	
	////////////////////////////////////////////////
	// ClassName Component
	////////////////////////////////////////////////
	
	public class ClassNameComponent extends JPanel {
		
		private ModuleClassComboBoxComponent	mModuleClassComboBoxComponent = null;
		private DialogTextFieldComponent 		mNameTextFieldComponent = null;
		private ScriptEditorComponent				mJavaEditorPaneComponent = null;
		
		public ClassNameComponent(CtbIDE ctbIDE) {
			mModuleClassComboBoxComponent = new ModuleClassComboBoxComponent(ctbIDE);
			mModuleClassComboBoxComponent.addActionListener(new ClassActionListener());
		
			mNameTextFieldComponent = new DialogTextFieldComponent("Name");
			mNameTextFieldComponent.addActionListener(new NameActionListener());
			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(mModuleClassComboBoxComponent);
			add(mNameTextFieldComponent);
		}
		
		private class ModuleClassComboBoxComponent extends JComboBox {
			public ModuleClassComboBoxComponent(CtbIDE ctbIDE) {
				String className[] = ctbIDE.getModuleTypeClassNames();
				addItem("User");
				for (int n=0; n<className.length; n++) {
					if (className[n].equalsIgnoreCase("SYSTEM") == true)
						continue;
					if (className[n].equalsIgnoreCase("USER") == true)
						continue;
					addItem(className[n]);
				}
				setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Class")));
			}
		}

		private class ClassActionListener implements ActionListener { 	
			public void actionPerformed(ActionEvent e) {
				Debug.message("ClassNameActionListener.actionPerformed");	
				Debug.message("\tactionCommand = " + e.getActionCommand());
				Debug.message("\tparamString   = " + e.paramString());
				getDialog().setOkButtonEnabled(false);

				ScriptEditorComponent editorComp = getScriptEditorPaneComponent();
				if (editorComp != null)
					editorComp.getEditorComponent().setClassName(getClassName());
			}
		}

		private class NameActionListener implements ActionListener { 	
			public void actionPerformed(ActionEvent e) {
				Debug.message("NameActionListener.actionPerformed");	
				Debug.message("\tactionCommand = " + e.getActionCommand());
				Debug.message("\tparamString   = " + e.paramString());
				getDialog().setOkButtonEnabled(false);
				
				ScriptEditorComponent editorComp = getScriptEditorPaneComponent();
				if (editorComp != null)
					editorComp.getEditorComponent().setClassName(getClassName());
			}
		}

		public void setClassName(String className) {
			if (className == null)
				return;
			JComboBox combo = mModuleClassComboBoxComponent;
			for (int n=0; n<combo.getItemCount(); n++) {
				String itemName = (String)combo.getItemAt(n);
				if (itemName.equals(className) == true) {
					combo.setSelectedIndex(n);
					return;
				}
			}
		}

		public void setEnabled(boolean enabled) {
			mModuleClassComboBoxComponent.setEnabled(enabled);
			mNameTextFieldComponent.setEnabled(enabled);
		}
				
		public void setName(String name) {
			mNameTextFieldComponent.setText(name);
		}
		
		public String getClassString() {
			return (String)mModuleClassComboBoxComponent.getSelectedItem();
		}
		
		public String getNameString() {
			String nameString = mNameTextFieldComponent.getText();
			
			if (nameString == null)
				return null;
			
			if (nameString.length() <= 0)
				return null;
				
			char firstCharOfNameString = nameString.charAt(0);
			if ('a' <= firstCharOfNameString && firstCharOfNameString <= 'z') {
				StringBuffer nameBuffer = new StringBuffer();
				firstCharOfNameString += (char)('A' - 'a');
				nameBuffer.append(firstCharOfNameString);
				for (int n=1; n<nameString.length(); n++)
					nameBuffer.append(nameString.charAt(n));
				nameString = nameBuffer.toString(); 
				mNameTextFieldComponent.setText(nameString);
			}
						
			return nameString;
		}
		
		public void setScriptEditorComponent(ScriptEditorComponent comp) {
			mJavaEditorPaneComponent = comp;
		}
		
		public ScriptEditorComponent getScriptEditorPaneComponent() {
			return mJavaEditorPaneComponent;
		}
		
		public String getClassName() {
			String classString= getClassString();
			String nameString = getNameString();
			if (nameString == null)
				nameString = "Name";
			return classString + nameString;
		}
	}
	
	
	////////////////////////////////////////////////
	// In/Out Node Component
	////////////////////////////////////////////////

	private class NodeComponent extends JPanel {

		private NodeComboBox	mNodeComboBox[] = new NodeComboBox[2];
		private JCheckBox		mExecutionNodeCheckBox;
		
		public NodeComponent() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			//JPanel nodePanel = new JPanel();
			//nodePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			mNodeComboBox[0] = new NodeComboBox("Input Node");
			mNodeComboBox[1] = new NodeComboBox("Output Node");
			//nodePanel.add(mNodeComboBox[0]);
			//nodePanel.add(mNodeComboBox[1]);
			//add(nodePanel);
			add(mNodeComboBox[0]);
			add(mNodeComboBox[1]);
			
			JPanel exeNodePanel = new JPanel();
			exeNodePanel.setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Execution Node")));
			mExecutionNodeCheckBox = new JCheckBox("Use Execution Node");
			exeNodePanel.add(mExecutionNodeCheckBox);
			exeNodePanel.setMaximumSize(new Dimension(300, 64));
			add(exeNodePanel);
			
			setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Node Setting")));
		}

		public void setInNodeNum(int n) {
			mNodeComboBox[0].setSelectedIndex(n);
		}

		public void setOutNodeNum(int n) {
			mNodeComboBox[1].setSelectedIndex(n);
		}
		
		public void setExecutionNode(boolean hasNode) {
			mExecutionNodeCheckBox.setSelected(hasNode);
		}
		
		public String[] getInNodeNames() {
			return mNodeComboBox[0].getNodeNames("INNODE");
		}

		public String[] getOutNodeNames() {
			return mNodeComboBox[1].getNodeNames("OUTNODE");
		}
		
		public boolean hasExecutionNode() {
			return mExecutionNodeCheckBox.isSelected();
		}
		
		private class NodeComboBox extends JComboBox {
		
			public NodeComboBox(String borderName) {
				addItem("None");
				addItem("1");
				addItem("2");
				addItem("3");
				addItem("4");
				setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), borderName)));
			}

			public String[] getNodeNames(String nodeName) {
				int nNames = getSelectedIndex();
				String names[] = new String[nNames];
				for (int n=0; n<nNames; n++) 
					names[n] = nodeName + n;
				return names;
			}
		}
		
	}

	////////////////////////////////////////////////
	// Icon Component
	////////////////////////////////////////////////
	
	private class IconComponent extends JPanel {
		
		private JButton	mIconButton;
		private File		mIconFile;
		
		public IconComponent(Image iconImage) {
			setLayout(new BorderLayout());
			if (iconImage == null) {
				mIconFile = new File(CtbIDE.getModuleTypeDefaultIconFileName());
				iconImage = readIconImage(CtbIDE.getModuleTypeDefaultIconFileName());
			}
			mIconButton = new JButton(new ImageIcon(iconImage));
			add(mIconButton);
			setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Icon")));
			mIconButton.addActionListener(new LoadAction());
			
			setMaximumSize(new Dimension(300, 64));
		}
		
		public Image readIconImage(String filename) {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			return toolkit.getImage(filename);
		}

		public void setIcon(Icon icon) {
			mIconButton.setIcon(icon);
		}
		
		public File getIconFile() {
			return mIconFile;
		}
		
		////////////////////////////////////////////////
		// Load Action
		////////////////////////////////////////////////

		private class LoadAction extends AbstractAction {
			public void actionPerformed(ActionEvent event) {
				Debug.message("IconComponent.LoadAction.actionPerformed");
				String userDir = System.getProperty("user.dir");
				Debug.message("currentDirectoryPath = " + userDir);
				JFileChooser filechooser = new JFileChooser(new File(userDir));
				FileFilter gifFilter = new GifFileFilter();
				filechooser.addChoosableFileFilter(gifFilter);
				filechooser.setFileFilter(gifFilter);
				if(filechooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					if (file != null) {
						if (file.isDirectory() == false) {
							mIconFile = file;
							setIcon(new ImageIcon(file.getAbsolutePath()));
						}
					}
				}
			}

			public class GifFileFilter extends FileFilter {
				final static String gifExt = "gif";
				public boolean accept(File f) {
					if(f.isDirectory())
						return true;
					String s = f.getName();
					int i = s.lastIndexOf('.');
					if(i > 0 &&  i < s.length() - 1) {
						String extension = s.substring(i+1).toLowerCase();
						if (gifExt.equals(extension) == true) 
							return true;
						else
							return false;
					}
					return false;
				}
				public String getDescription() {
					return "GIF Files (*.gif)";
				}
			}
		}
	}

	////////////////////////////////////////////////
	// Script Editor Component
	////////////////////////////////////////////////
		
	public class ScriptEditorComponent extends JPanel {
		
		private EditorToolBar			mEditorToolBar;
		private EditorComponent			mEditorComponent;
		private MessageComponent		mMessageComponent;
		
		public ScriptEditorComponent() {
				setLayout(new BorderLayout());
				
				mEditorComponent	= new EditorComponent(CtbIDE.getModuleTypeDefaultSouceFileName());
				mMessageComponent	= new MessageComponent();
				
				JavaEditorKit	javaEditorKit	= mEditorComponent.getJavaEditorPane().getJavaEditorKit();
				mEditorToolBar						= new EditorToolBar(javaEditorKit);
				
				add(mEditorToolBar, BorderLayout.NORTH);
				
				JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mEditorComponent, mMessageComponent);
				add(splitPane, BorderLayout.CENTER);
		}
		
		public EditorToolBar getEditorToolBar() {
			return mEditorToolBar;
		}
		
		public EditorComponent getEditorComponent() {
			return mEditorComponent;
		}

		public MessageComponent getMessageComponent() {
			return mMessageComponent;
		}

		public void setScriptFile(File file) {
			getEditorComponent().setScriptFile(file);
		}
		
		public void setScriptFile(String filename) {
			getEditorComponent().setScriptFile(filename);
		}
		
		////////////////////////////////////////////////
		// replaceClassName
		////////////////////////////////////////////////
		
		public void replaceClassName(String className) {
			mEditorComponent.setClassName(className);
		}

		////////////////////////////////////////////////
		// Script Source File
		////////////////////////////////////////////////
		
		private File	mScriptSourceFile		= null;
		
		private File createScriptSourceFile(String className) {
			try {
				mScriptSourceFile = new File(className + ".java");
				
				FileOutputStream	outputStream = new FileOutputStream(mScriptSourceFile);
				PrintStream			printStream = new PrintStream(outputStream);
				
				printStream.print(mEditorComponent.getJavaEditorPane().getText());				
				
				printStream.close();
				outputStream.close();
			}
			catch (NullPointerException npe) {
				mScriptSourceFile = null;
				return null;
			}
			catch (IOException ioe) {
				mScriptSourceFile = null;
				return null;
			}
			return mScriptSourceFile;
		}
			
		private boolean deleteCreatedScriptSourceFile() {
			if (mScriptSourceFile == null)
				return false;
			boolean ret = mScriptSourceFile.delete();
			mScriptSourceFile = null;
			return ret;
		}
			
		public File getCreatedScriptSourceFile() {
			return mScriptSourceFile;
		}
			
		////////////////////////////////////////////////
		// Script Class File
		////////////////////////////////////////////////
			
		private File	mScriptClassFile	= null;
			
		private File createScriptClassFile(String className) {
			try {
				mScriptClassFile = new File(className + ".class");
			}
			catch (NullPointerException npe) {
				mScriptClassFile = null;
				return null;
			}
			return mScriptClassFile;
		}
			
		private boolean deleteCreatedScriptClassFile() {
			if (mScriptClassFile == null)
				return false;
			boolean ret = mScriptClassFile.delete();
			mScriptClassFile = null;
			return ret;
		}

		public File getCreatedScriptClassFile() {
			return mScriptClassFile;
		}
		
		public class EditorToolBar extends ToolBar {

			public EditorToolBar (JavaEditorKit javaEditorKit) {
				String dir = CtbIDE.getImageToolbarModuleEditorDirectory();
				addToolBarButton("New", dir + "new.gif", new NewAction());
				addToolBarButton("Open", dir + "open.gif", new LoadAction());
				addSeparator();
				addToolBarButton("Copy", 	dir + "copy.gif", javaEditorKit.getCopyAction());
				addToolBarButton("Cut",		dir + "cut.gif", javaEditorKit.getCutAction()); 
				addToolBarButton("Paste",	dir + "paste.gif", javaEditorKit.getPasteAction());
				addSeparator();
				addToolBarButton("Undo",	dir + "undo.gif", javaEditorKit.getUndoAction());
				addSeparator();
				addToolBarButton("Compile",	dir + "compile.gif", new CompileAction());
			}

			////////////////////////////////////////////////
			// New Action
			////////////////////////////////////////////////
			
			private class NewAction extends AbstractAction {
				public void actionPerformed(ActionEvent e) {
					int result = Message.showConfirmDialog(getDialog(), "Are you sure you want to clear a current program ?");
					if(result == Message.YES_OPTION) 
						getEditorComponent().setScriptFile(CtbIDE.getModuleTypeDefaultSouceFileName());
				}
			}

			////////////////////////////////////////////////
			// Load Action
			////////////////////////////////////////////////

			private class LoadAction extends AbstractAction {
				public void actionPerformed(ActionEvent event) {
					String userDir = System.getProperty("user.dir");
					Debug.message("currentDirectoryPath = " + userDir);
					JFileChooser filechooser = new JFileChooser(new File(userDir));
					FileFilter javaFilter = new JavaFileFilter();
					filechooser.addChoosableFileFilter(javaFilter);
					filechooser.setFileFilter(javaFilter);
					if(filechooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						File file = filechooser.getSelectedFile();
						if (file != null) {
							if (file.isDirectory() == false) 
								getEditorComponent().setScriptFile(file);
						}
					}
				}

				public class JavaFileFilter extends FileFilter {
					final static String javaExt = "java";
					public boolean accept(File f) {
						if(f.isDirectory())
							return true;
						String s = f.getName();
						int i = s.lastIndexOf('.');
						if(i > 0 &&  i < s.length() - 1) {
							String extension = s.substring(i+1).toLowerCase();
							if (javaExt.equals(extension) == true) 
								return true;
							else
								return false;
						}
						return false;
					}
					public String getDescription() {
						return "Java Files (*.java)";
					}
				}
			}
			
			////////////////////////////////////////////////
			// Compile Action
			////////////////////////////////////////////////
			
			private class CompileAction extends AbstractAction {
				public void actionPerformed(ActionEvent e) {
					JTextArea msgArea = getMessageComponent().getScriptEditorPane();
					
					msgArea.setForeground(new Color(0.0f, 0.0f, 0.0f));
					msgArea.setText("");
					
					deleteCreatedScriptSourceFile();
					deleteCreatedScriptClassFile();
					
					String className = mClassNameComponent.getClassName();
					if (className == null) {
						Message.showWarningDialog((Component)e.getSource(), "Module has no name !!");
						getDialog().setOkButtonEnabled(false);
						return;
					}
					
					getEditorComponent().setClassName(className);

					createScriptSourceFile(className);
					
					String filename[] = {className+".java"};

					String lineSep = System.getProperty("line.separator");
					msgArea.append("Compiling " + filename[0] + " ... " + lineSep);

					ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
					Main javac = new Main(new PrintStream(outputStream), "javac");
					if (javac.compile(filename) == true) {
						msgArea.setForeground(new Color(0.0f, 0.0f, 0.0f));
						msgArea.append(filename[0] + " : 0 error");
						getDialog().setOkButtonEnabled(true);
						createScriptClassFile(className);
					}
					else {
						msgArea.setForeground(new Color(1.0f, 0.0f, 0.0f));
						msgArea.append(outputStream.toString());
						getDialog().setOkButtonEnabled(false);
					}
					Message.beep();
				}
			}
		}

		private class EditorComponent extends JPanel {
			
			private JavaEditorPane		mJavaEditorPane;  
			
			public EditorComponent(String filename) {
				setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			
				mJavaEditorPane = new JavaEditorPane();
				
				Debug.message("JavaEditorPane");
				Document doc = mJavaEditorPane.getDocument();
				Debug.message("    doc = " + doc);
				
				JScrollPane scroller = new JScrollPane();	
				scroller.getViewport().add(mJavaEditorPane);
				
				setPreferredSize(new Dimension(500, 400));
				add(scroller);
			
				Debug.message("    doc = " + doc);
				
				setScriptFile(filename);

				Debug.message("    doc = " + doc);
			}

			private void setScriptText(byte textData[]) {
				Debug.message("setScriptText");
				
				UndoableEditListener undoHandler = getJavaEditorKit().getUndoHandler();

				JavaEditorPane editor = mJavaEditorPane;

				Document oldDoc = editor.getDocument();
				Debug.message("    oldDoc = " + oldDoc);
				if(oldDoc != null)
	    			oldDoc.removeUndoableEditListener(undoHandler);

				editor.setDocument(new JavaDocument());
				editor.setText(new String(textData));
		
				Document newDoc = editor.getDocument();
				Debug.message("    newDoc = " + newDoc);
				newDoc.addUndoableEditListener(undoHandler);
			}

			private void setScriptFile(File file) {
				try {
					InputStream inputStream = new FileInputStream(file);
					long fileLength = file.length(); 
					byte textData[] = new byte[(int)fileLength];
					inputStream.read(textData);
					setScriptText(textData);
					inputStream.close();
				}
				catch (FileNotFoundException fnfe) {
				}
				catch (IOException ioe){
				}
			}
			
			private void setScriptFile(String filename) {
				File file = new File(filename);
				setScriptFile(file);
			}
		
			public JavaEditorPane getJavaEditorPane() {
				return 	mJavaEditorPane;		
			}

			public JavaEditorKit getJavaEditorKit() {
				return 	getJavaEditorPane().getJavaEditorKit();		
			}
			
			private boolean isChar(char c) {
				if (c <= ' ')
					return false;
				return true;
			}
			
			private int []findClassNamePosition() {
				String	programmText = mJavaEditorPane.getText();
				int		programmTextLength = programmText.length();
				
				String	classString = new String("class");
				int 		classIndex = programmText.indexOf(classString);
				
				int startIndex;
				for (startIndex=(classIndex+classString.length()); startIndex<programmTextLength; startIndex++) {
					char c = programmText.charAt(startIndex);
					if (isChar(c) == true)
						break; 
				}
				
				int endIndex;
				for (endIndex=startIndex; endIndex<programmTextLength; endIndex++) {
					char c = programmText.charAt(endIndex+1);
					if (isChar(c) == false)
						break; 
				}
				
				int index[] = {startIndex, endIndex+1};
				return index;
			}
			
			public void setClassName(String className) {
				int index[] = findClassNamePosition();
				JavaEditorPane editorPane = getJavaEditorPane();
				editorPane.setSelectionStart(index[0]); 
				editorPane.setSelectionEnd(index[1]); 
				editorPane.replaceSelection(className); 	
			}
		}

		private class MessageComponent extends JPanel {
		
			private JTextArea		mMessageArea;  
		
			public MessageComponent() {
				setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
				mMessageArea = new JTextArea();
				mMessageArea.setEditable(false); 
				mMessageArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
				mMessageArea.setTabSize(4);
				JScrollPane scroller = new JScrollPane();	
				scroller.getViewport().add(mMessageArea);
				//setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Build")));
				setPreferredSize(new Dimension(500, 100));
				add(scroller);
			}
			
			public void setMessage(String message) {
				mMessageArea.setText(message);
			}

			public JTextArea getScriptEditorPane() {
				return 	mMessageArea;		
			}
		}
		
	}
	
}
		
