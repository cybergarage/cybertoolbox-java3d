import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.undo.*;
import javax.swing.text.*;
import javax.swing.event.*;

public class JavaEditorKit extends DefaultEditorKit {
	
	JavaContext preferences;

	public JavaEditorKit() {
		super();
	}

	public JavaContext getStylePreferences() {
		if (preferences == null) {
			preferences = new JavaContext();
		}
		return preferences;
	}

	public void setStylePreferences(JavaContext prefs) {
		preferences = prefs;
	}

	// --- EditorKit methods -------------------------

    /**
     * Get the MIME type of the data that this
     * kit represents support for.  This kit supports
     * the type <code>text/java</code>.
     */
	public String getContentType() {
		return "text/java";
	}

    /**
     * Create a copy of the editor kit.  This
     * allows an implementation to serve as a prototype
     * for others, so that they can be quickly created.
     */
	public Object clone() {
		JavaEditorKit kit = new JavaEditorKit();
		kit.preferences = preferences;
		return kit;
	}

    /**
     * Creates an uninitialized text storage model
     * that is appropriate for this type of editor.
     *
     * @return the model
     */
	public Document createDefaultDocument() {
		return new JavaDocument();
	}

    /**
     * Fetches a factory that is suitable for producing 
     * views of any models that are produced by this
     * kit.  The default is to have the UI produce the
     * factory, so this method has no implementation.
     *
     * @return the view factory
     */
	public final ViewFactory getViewFactory() {
		return getStylePreferences();
	}

	//////////////////////////////////////////////////
	// Basic Edirot Action
	//////////////////////////////////////////////////
	
	private Action editorActions[] = null;
	
	public Action getAction(String key) {
		if (editorActions == null)
			editorActions = getActions();
		for (int n=0; n<editorActions.length; n++) {
			Action a = editorActions[n];
			if (key.equals(a.getValue(Action.NAME)))
				return a;
		} 
		return null;
	}
	
	public Action getCopyAction() {
		return getAction(copyAction);
	}

	public Action getCutAction() {
		return getAction(cutAction);
	}

	public Action getPasteAction() {
		return getAction(pasteAction);
	}

	//////////////////////////////////////////////////
	// Undo / Redo Edirot Action
	//////////////////////////////////////////////////

	private UndoableEditListener undoHandler = new UndoHandler();
	private UndoManager undo = new UndoManager();
	
	private UndoAction undoAction = new UndoAction();
	private RedoAction redoAction = new RedoAction();

	private class UndoHandler implements UndoableEditListener {

		/**
		 * Messaged when the Document has created an edit, the edit is
		 * added to <code>undo</code>, an instance of UndoManager.
		*/
		public void undoableEditHappened(UndoableEditEvent e) {
			Debug.message("addEdit = " + e);
			undo.addEdit(e.getEdit());
			undoAction.update();
			redoAction.update();
		}
	}
	
	private class UndoAction extends AbstractAction {
		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.undo();
			} catch (CannotUndoException ex) {
				System.out.println("Unable to undo: " + ex);
				ex.printStackTrace();
			}
			update();
			redoAction.update();
		}

		protected void update() {
			if(undo.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getUndoPresentationName());
			}
			else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}

	private class RedoAction extends AbstractAction {
		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.redo();
			} catch (CannotRedoException ex) {
				System.out.println("Unable to redo: " + ex);
				ex.printStackTrace();
			}
			update();
			undoAction.update();
		}

		protected void update() {
			if(undo.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getRedoPresentationName());
			}
			else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}

	public UndoableEditListener getUndoHandler() {
		return (UndoableEditListener)undoHandler;
	}

	public Action getUndoAction() {
		return undoAction;
	}

	public Action getRedoAction() {
		return redoAction;
	}

}







