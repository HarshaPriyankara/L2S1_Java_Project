package GUI.common;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class UITheme {
    public static final Color APP_BACKGROUND = new Color(245, 248, 252);
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_MUTED = new Color(236, 241, 247);
    public static final Color SIDEBAR = new Color(28, 44, 62);
    public static final Color PRIMARY = new Color(39, 110, 168);
    public static final Color PRIMARY_DARK = new Color(25, 79, 122);
    public static final Color ACCENT = new Color(90, 161, 196);
    public static final Color TEXT_PRIMARY = new Color(34, 48, 64);
    public static final Color TEXT_MUTED = new Color(104, 118, 136);
    public static final Color SUCCESS = new Color(58, 145, 97);
    public static final Color DANGER = new Color(188, 77, 77);

    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font SECTION_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 13);

    private UITheme() {
    }

    public static void applyPanelBackground(JComponent component) {
        component.setBackground(APP_BACKGROUND);
    }

    public static void applySurface(JComponent component) {
        component.setBackground(SURFACE);
    }

    public static void stylePrimaryButton(AbstractButton button) {
        styleButton(button, PRIMARY, Color.WHITE);
    }

    public static void styleSuccessButton(AbstractButton button) {
        styleButton(button, SUCCESS, Color.WHITE);
    }

    public static void styleDangerButton(AbstractButton button) {
        styleButton(button, DANGER, Color.WHITE);
    }

    public static void styleNeutralButton(AbstractButton button) {
        styleButton(button, SURFACE_MUTED, TEXT_PRIMARY);
    }

    public static void styleLargeMenuButton(AbstractButton button) {
        stylePrimaryButton(button);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(500, 78));
    }

    public static void setStandardButtonSize(AbstractButton button) {
        button.setPreferredSize(new Dimension(140, 38));
    }

    public static void setWideButtonSize(AbstractButton button) {
        button.setPreferredSize(new Dimension(150, 38));
    }

    public static void styleTable(JTable table) {
        table.setBackground(SURFACE);
        table.setForeground(TEXT_PRIMARY);
        table.setSelectionBackground(new Color(219, 233, 245));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setGridColor(new Color(220, 228, 236));
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
    }

    public static void styleTextField(JTextField field) {
        field.setBackground(SURFACE);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(compoundFieldBorder());
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(SURFACE);
        comboBox.setForeground(TEXT_PRIMARY);
    }

    public static void styleTextArea(JTextArea textArea) {
        textArea.setBackground(SURFACE);
        textArea.setForeground(TEXT_PRIMARY);
        textArea.setCaretColor(TEXT_PRIMARY);
        textArea.setBorder(compoundFieldBorder());
    }

    public static Border createContentBorder() {
        return BorderFactory.createEmptyBorder(20, 20, 20, 20);
    }

    public static Border createSectionBorder(String title) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 224, 234)),
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12), title)
        );
    }

    public static JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static Font sectionFont() {
        return SECTION_FONT;
    }

    public static Font buttonFont() {
        return BUTTON_FONT;
    }

    private static void styleButton(AbstractButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(BUTTON_FONT);
    }

    private static Border compoundFieldBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 214, 224)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        );
    }
}
