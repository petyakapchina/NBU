package nbu.infm205.ui;

import nbu.infm205.service.FileService;
import nbu.infm205.service.SearchEngineService;
import nbu.infm205.service.impl.DefaultFileService;
import nbu.infm205.service.impl.DefaultSearchEngineService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MainFrame {

    private final FileService fileService = new DefaultFileService();
    private final SearchEngineService searchEngine = new DefaultSearchEngineService(fileService);
    private final int threads = fileService.countFilesInDir();

    JFrame frame = new JFrame();

    JLabel labelThreads = new JLabel();
    JLabel errorLabelThreads = new JLabel();
    JLabel labelSearchDir = new JLabel();
    JLabel searchTime = new JLabel();
    JLabel labelText = new JLabel();

    JTextField inputText = new JTextField();
    JTextField inputThreads = new JTextField();

    JButton searchBtn = new JButton("Search");
    JButton fileSelector = new JButton("Select");

    JTextPane result = new JTextPane();
    JScrollPane scroll = new JScrollPane(result);

    JFileChooser source;

    public MainFrame() {
        this.frame.setSize(800, 350);

        this.labelThreads.setBounds(10, 10, 380, 20);
        this.errorLabelThreads.setBounds(10, 30, 380, 20);
        this.labelText.setBounds(400, 10, 380, 20);

        this.inputThreads.setBounds(10, 50, 380, 40);
        this.inputText.setBounds(400, 50, 380, 40);

        this.searchBtn.setBounds(10, 110, 120, 80);
        this.fileSelector.setBounds(140, 110, 120, 80);
        this.labelSearchDir.setBounds(270, 110, 520, 40);
        this.searchTime.setBounds(270, 150, 520, 40);

        this.scroll.setBounds(10, 200, 780, 110);

        this.labelThreads.setText(String.format("Enter positive number of threads, but not bigger than %d", threads));
        this.labelThreads.setLabelFor(this.inputThreads);
        this.errorLabelThreads.setForeground(Color.red);
        this.labelText.setText("Enter text to search by");
        this.labelText.setLabelFor(this.inputText);
        this.labelSearchDir.setText("Search in: Default");

        this.result.setContentType("text/html");

        this.searchBtn.setEnabled(false);

        this.frame.setTitle("Search Engine");
        this.frame.add(this.labelThreads);
        this.frame.add(this.inputThreads);
        this.frame.add(this.searchBtn);
        this.frame.add(this.errorLabelThreads);
        this.frame.add(this.labelText);
        this.frame.add(this.inputText);
        this.frame.add(this.fileSelector);
        this.frame.add(this.labelSearchDir);
        this.frame.add(this.searchTime);
        this.frame.getContentPane().add(this.scroll);
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.fileSelector.setForeground(Color.BLUE);
        this.searchBtn.setForeground(Color.GREEN);
        this.scroll.setBackground(Color.ORANGE);

        setFrameValidations();
        setSelectFileAction();
        setSearchButtonAction();
    }

    private void setFrameValidations() {
        this.inputThreads.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent ae) {
                checkField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkField();
            }

            private void checkField() {
                errorLabelThreads.setVisible(false);
                int customerInput = 0;
                try {
                    customerInput = Integer.parseInt(inputThreads.getText());
                } catch (NumberFormatException e) {
                    System.out.println("User trying to enter disallowed number for threads count.");
                }

                if (customerInput > threads || customerInput <= 0) {
                    errorLabelThreads.setText(String.format("Enter equals or less then %d", threads));
                    errorLabelThreads.setVisible(true);
                    searchBtn.setEnabled(false);
                } else {
                    manageSearchBtnState();
                    errorLabelThreads.setVisible(false);
                }
            }
        });

        this.inputText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent ae) {
                checkField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkField();
            }

            private void checkField() {
                manageSearchBtnState();
            }
        });
    }

    private void manageSearchBtnState() {
        searchBtn.setEnabled(isInputPopulated(inputThreads) && isInputPopulated(inputText));
    }

    private boolean isInputPopulated(JTextField field) {
        return field.getText() != null && !field.getText().isEmpty();
    }

    private void setSelectFileAction() {
        this.fileSelector.addActionListener(actionEvent -> {
            this.source = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            this.source.setBounds(120, 120, 100, 40);
            this.frame.add(this.source);
            this.source.setDialogTitle("Choose a directory to save your file: ");
            this.source.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnValue = this.source.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                this.labelSearchDir.setText(this.source.getSelectedFile().getAbsolutePath());
            }
        });
    }

    private void setSearchButtonAction() {
        this.searchBtn.addActionListener(actionEvent -> {

            searchBtn.setEnabled(true);

            int runWithThreads = Integer.parseInt(inputThreads.getText());

            searchEngine.setThreadsNumber(runWithThreads);
            String searchDir = null;
            if (this.source != null) {
                searchDir = Optional.ofNullable(this.source.getSelectedFile())
                        .map(f -> f.getAbsolutePath())
                        .orElse(null);
            }
            long startTime = System.nanoTime();
            String formatted = searchEngine.search(inputText.getText(), searchDir);
            long endTime = System.nanoTime();
            long totalTime = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            System.out.println("Search executed for: " + totalTime + " ms.");

            searchTime.setText("Search executed for: " + totalTime + " ms.");

            result.setText(formatted);
        });
    }

}
