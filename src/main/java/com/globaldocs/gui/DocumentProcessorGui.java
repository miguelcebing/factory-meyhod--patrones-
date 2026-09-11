package com.globaldocs.gui;

import com.globaldocs.batch.BatchDocumentProcessor;
import com.globaldocs.batch.BatchResult;
import com.globaldocs.document.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing GUI for the Document Processor.
 * Provides interactive interface to add documents and process batches.
 */
public final class DocumentProcessorGui extends JFrame {

    private final List<Document> documentQueue = new ArrayList<>();
    private final DefaultListModel<String> queueListModel = new DefaultListModel<>();
    private final JTextArea logArea = new JTextArea();

    private final JComboBox<DocumentType> typeCombo = new JComboBox<>(DocumentType.values());
    private final JComboBox<Country> countryCombo = new JComboBox<>(Country.values());
    private final JComboBox<String> formatCombo = new JComboBox<>(
            new String[]{"pdf", "doc", "docx", "md", "csv", "txt", "xlsx"});
    private final JTextField fileNameField = new JTextField(20);
    private final JTextField contentField = new JTextField(30);

    public DocumentProcessorGui() {
        super("GlobalDocs Solutions - Factory Method Document Processor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));
        getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top: input form
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Document"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; inputPanel.add(typeCombo, gbc);
        gbc.gridx = 2; inputPanel.add(new JLabel("Country:"), gbc);
        gbc.gridx = 3; inputPanel.add(countryCombo, gbc);
        gbc.gridx = 4; inputPanel.add(new JLabel("Format:"), gbc);
        gbc.gridx = 5; inputPanel.add(formatCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("File Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; inputPanel.add(fileNameField, gbc);
        gbc.gridx = 3; inputPanel.add(new JLabel("Content:"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2; inputPanel.add(contentField, gbc);
        gbc.gridwidth = 1;

        JButton addBtn = createButton("Add Document", new Color(59, 130, 246));
        JButton demoBtn = createButton("Load Demo", new Color(139, 92, 246));
        JButton batchBtn = createButton("Process Batch", new Color(16, 185, 129));
        JButton clearBtn = createButton("Clear All", new Color(239, 68, 68));

        addBtn.addActionListener(e -> addDocument());
        demoBtn.addActionListener(e -> loadDemo());
        batchBtn.addActionListener(e -> processBatch());
        clearBtn.addActionListener(e -> clearAll());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        buttonPanel.add(addBtn);
        buttonPanel.add(demoBtn);
        buttonPanel.add(batchBtn);
        buttonPanel.add(clearBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Center: split pane (queue + log)
        JList<String> queueList = new JList<>(queueListModel);
        queueList.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane queueScroll = new JScrollPane(queueList);
        queueScroll.setBorder(BorderFactory.createTitledBorder("Document Queue"));

        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBackground(new Color(248, 250, 252));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Processing Log"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, queueScroll, logScroll);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

        log("GlobalDocs Solutions - Document Processor initialized.");
        log("Factory Method pattern | Java Swing GUI");
        log("Click 'Load Demo' or add documents manually.");
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(bg.darker(), 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void addDocument() {
        try {
            DocumentType type = (DocumentType) typeCombo.getSelectedItem();
            Country country = (Country) countryCombo.getSelectedItem();
            String format = (String) formatCombo.getSelectedItem();
            String fileName = fileNameField.getText().trim();
            String content = contentField.getText().trim();

            if (fileName.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "File name and content are required.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Document doc = new Document(fileName, format, type, country, content);
            documentQueue.add(doc);
            queueListModel.addElement(String.format("%s [%s | %s | %s]",
                    doc.getFileName(), doc.getCountry(), doc.getType(), doc.getFormat()));

            log("Added: " + doc);
            fileNameField.setText("");
            contentField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDemo() {
        documentQueue.clear();
        queueListModel.clear();

        documentQueue.add(new Document("invoice_co_001.pdf", "pdf",
                DocumentType.ELECTRONIC_INVOICE, Country.COLOMBIA, "Invoice data CUFE=123456"));
        documentQueue.add(new Document("invoice_mx_002.pdf", "pdf",
                DocumentType.ELECTRONIC_INVOICE, Country.MEXICO, "Invoice data without stamp"));
        documentQueue.add(new Document("tax_ar_003.csv", "csv",
                DocumentType.TAX_DECLARATION, Country.ARGENTINA, "Declaration CUIT=30-12345678-9"));
        documentQueue.add(new Document("contract_cl_004.docx", "docx",
                DocumentType.LEGAL_CONTRACT, Country.CHILE, "Contract SIGNATURE=digital_ok"));

        for (Document doc : documentQueue) {
            queueListModel.addElement(String.format("%s [%s | %s | %s]",
                    doc.getFileName(), doc.getCountry(), doc.getType(), doc.getFormat()));
        }
        log("Loaded 4 demo documents from case study.");
    }

    private void processBatch() {
        if (documentQueue.isEmpty()) {
            log("[WARN] Queue is empty. Add documents first.");
            return;
        }

        log("========================================");
        log("STARTING BATCH PROCESSING...");
        log("========================================");

        BatchDocumentProcessor processor = new BatchDocumentProcessor();
        BatchResult result = processor.processBatch(new ArrayList<>(documentQueue));

        for (String error : result.getErrors()) {
            log("[ERROR] " + error);
        }

        log("----------------------------------------");
        log(result.getSummary());
        log("========================================");

        documentQueue.clear();
        queueListModel.clear();
    }

    private void clearAll() {
        documentQueue.clear();
        queueListModel.clear();
        logArea.setText("");
        log("Cleared.");
    }

    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new DocumentProcessorGui().setVisible(true);
        });
    }
}