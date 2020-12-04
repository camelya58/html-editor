package editor.listeners;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 * Class UndoListener
 *
 * @author Kamila Meshcheryakova
 * created by 04.12.2020
 */
public class UndoListener implements UndoableEditListener {

    private UndoManager undoManager;

    public UndoListener(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent undoableEditEvent) {
        undoManager.addEdit(undoableEditEvent.getEdit());
    }
}
