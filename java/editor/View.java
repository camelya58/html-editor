package editor;

import editor.listeners.*;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {

    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void init() {
        initGui();
        FrameListener frameListener = new FrameListener(this);
        this.addWindowListener(frameListener);
        this.setVisible(true);
    }

    public void initMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, jMenuBar);
        MenuHelper.initEditMenu(this, jMenuBar);
        MenuHelper.initStyleMenu(this, jMenuBar);
        MenuHelper.initAlignMenu(this, jMenuBar);
        MenuHelper.initColorMenu(this, jMenuBar);
        MenuHelper.initFontMenu(this, jMenuBar);
        MenuHelper.initHelpMenu(this, jMenuBar);
        this.getContentPane().add(jMenuBar, BorderLayout.NORTH);
    }

    public void initEditor() {
        htmlTextPane.setContentType("text/html");
        JScrollPane textScrollPane = new JScrollPane(htmlTextPane);
        tabbedPane.add("HTML", textScrollPane);
        JScrollPane editorScrollPane = new JScrollPane(plainTextPane);
        tabbedPane.add("Текст", editorScrollPane);
        tabbedPane.setPreferredSize(new Dimension().getSize());
        TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(tabbedPaneChangeListener);
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void initGui() {
        initMenuBar();
        initEditor();
        pack();
    }

    public void exit() {
        controller.exit();
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Новый")) {
            controller.createNewDocument();
        }
        if (command.equals("Открыть")) {
            controller.openDocument();
        }
        if (command.equals("Сохранить")) {
            controller.saveDocument();
        }
        if (command.equals("Сохранить как...")) {
            controller.saveDocumentAs();
        }
        if (command.equals("Выход")) {
            controller.exit();
        }
        if (command.equals("О программе")) {
            showAbout();
        }
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public void undo() {
        try {
            undoManager.undo();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void redo() {
        try {
            undoManager.redo();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public void resetUndo() {
        undoManager.discardAllEdits();
    }

    public boolean isHtmlTabSelected() {
        return tabbedPane.getSelectedIndex() == 0;
    }

    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }

    public void showAbout() {
        String infoMessage = "Программа представляет собой простой HTML редактор,\n" +
                "который позволяет получать и редактировать html-страницу на одной вкладке,\n" +
                "а на другой получать текст с этой страницы в обычном формате.";
        JOptionPane.showMessageDialog(tabbedPane, infoMessage, "О программе", JOptionPane.INFORMATION_MESSAGE);
    }

    public void selectedTabChanged() {
        int index = tabbedPane.getSelectedIndex();
        if (index == 0) {
            controller.setPlainText(plainTextPane.getText());
        }
        if (index == 1) {
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();
    }
}

