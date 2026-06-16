package ui;

import dao.ProductDao;
import model.Product;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductStoreUI extends JFrame {
    private final ProductDao productDao = new ProductDao();
    private final List<ProductCard> cards = new ArrayList<>();
    private ProductDetailPanel detailPanel;
    private JLabel statusLabel;

    public ProductStoreUI() {
        setTitle("Bai Thuc Hanh 4 - Product Store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1520, 820);
        setMinimumSize(new Dimension(1280, 720));
        setLocationRelativeTo(null);
        setContentPane(createRootPanel());

        loadProducts();
    }

    private JPanel createRootPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(248, 248, 248));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        root.add(createWindowBar(), BorderLayout.NORTH);

        statusLabel = new JLabel("Dang tai du lieu tu CSDL...");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        statusLabel.setForeground(new Color(120, 120, 120));
        root.add(statusLabel, BorderLayout.CENTER);
        return root;
    }

    private void loadProducts() {
        try {
            List<Product> products = productDao.findAll();
            if (products.isEmpty()) {
                showStatus("Khong co san pham trong CSDL.");
                return;
            }

            detailPanel = new ProductDetailPanel(products.get(0));
            JPanel content = createContent(products);
            getContentPane().remove(statusLabel);
            getContentPane().add(content, BorderLayout.CENTER);
            revalidate();
            repaint();
            selectProduct(products.get(0));
        } catch (SQLException e) {
            showStatus("Khong the tai du lieu tu CSDL: " + e.getMessage());
        }
    }

    private void showStatus(String message) {
        statusLabel.setText("<html><div style='text-align:center;'>" + message + "</div></html>");
    }

    private JPanel createWindowBar() {
        JPanel bar = new JPanel();
        bar.setOpaque(false);
        bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
        bar.setBorder(new EmptyBorder(0, 0, 8, 4));
        bar.add(Box.createHorizontalGlue());
        bar.add(createDot(new Color(243, 205, 36)));
        bar.add(Box.createRigidArea(new Dimension(10, 0)));
        bar.add(createDot(new Color(89, 200, 78)));
        bar.add(Box.createRigidArea(new Dimension(10, 0)));
        bar.add(createDot(new Color(244, 82, 82)));
        return bar;
    }

    private JPanel createDot(Color color) {
        JPanel dot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(16, 16));
        dot.setMaximumSize(new Dimension(16, 16));
        return dot;
    }

    private JPanel createContent(List<Product> products) {
        JPanel content = new JPanel(new BorderLayout(22, 0));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(10, 0, 0, 0));

        detailPanel.setPreferredSize(new Dimension(390, 650));
        content.add(detailPanel, BorderLayout.WEST);
        content.add(createListing(products), BorderLayout.CENTER);
        return content;
    }

    private JScrollPane createListing(List<Product> products) {
        JPanel grid = new JPanel(new GridLayout(0, 4, 14, 14));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(0, 0, 0, 8));

        for (Product product : products) {
            ProductCard card = new ProductCard(product);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectProduct(product);
                }
            });
            cards.add(card);
            grid.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private void selectProduct(Product selectedProduct) {
        detailPanel.animateTo(selectedProduct);
        for (ProductCard card : cards) {
            card.setSelected(card.product == selectedProduct);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new ProductStoreUI().setVisible(true);
        });
    }

    private static BufferedImage loadImage(String imagePath) {
        try {
            File file = resolveAssetPath(imagePath);
            if (file != null) {
                return ImageIO.read(file);
            }
        } catch (Exception ignored) {
        }

        BufferedImage fallback = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = fallback.createGraphics();
        g2.setPaint(new GradientPaint(0, 0, new Color(240, 240, 240), 600, 600, new Color(210, 210, 210)));
        g2.fillRect(0, 0, 600, 600);
        g2.setColor(new Color(150, 150, 150));
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        g2.drawString("No Image", 220, 300);
        g2.dispose();
        return fallback;
    }

    private static File resolveAssetPath(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return null;
        }

        File[] candidates = {
                new File(fileName),
                new File("BaiThucHanh4", fileName),
                new File("/home/khoa/IE303/IE303.Q22/BaiThucHanh4", fileName)
        };

        for (File candidate : candidates) {
            if (candidate.exists()) {
                return candidate;
            }
        }
        return null;
    }

    private static Image scale(BufferedImage image, int targetWidth, int targetHeight) {
        return image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
    }

    private static String shorten(String text, int limit) {
        if (text == null || text.isBlank()) {
            return "";
        }
        if (text.length() <= limit) {
            return text;
        }
        return text.substring(0, Math.max(0, limit - 3)) + "...";
    }

    static class ProductCard extends JPanel {
        private final Product product;
        private final BufferedImage image;
        private boolean selected;

        ProductCard(Product product) {
            this.product = product;
            this.image = loadImage(product.getImagePath());
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(12, 12, 12, 12));
            setPreferredSize(new Dimension(255, 320));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            add(createTitleBlock(), BorderLayout.NORTH);
            add(createImageLabel(), BorderLayout.CENTER);
            add(createFooter(), BorderLayout.SOUTH);
        }

        private Component createTitleBlock() {
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel title = new JLabel(shorten(product.getName(), 16));
            title.setFont(new Font("SansSerif", Font.BOLD, 17));
            title.setForeground(new Color(73, 73, 73));
            title.setAlignmentX(LEFT_ALIGNMENT);

            JLabel desc = new JLabel(shorten(product.getDescription(), 27));
            desc.setFont(new Font("SansSerif", Font.BOLD, 13));
            desc.setForeground(new Color(195, 195, 195));
            desc.setBorder(new EmptyBorder(6, 0, 0, 0));
            desc.setAlignmentX(LEFT_ALIGNMENT);

            panel.add(title);
            panel.add(desc);
            return panel;
        }

        private JLabel createImageLabel() {
            JLabel imageLabel = new JLabel(new ImageIcon(scale(image, 182, 120)));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setBorder(new EmptyBorder(12, 0, 12, 0));
            return imageLabel;
        }

        private Component createFooter() {
            JPanel footer = new JPanel(new BorderLayout());
            footer.setOpaque(false);

            JLabel brand = new JLabel(product.getBrand());
            brand.setFont(new Font("SansSerif", Font.PLAIN, 15));
            brand.setForeground(new Color(95, 95, 95));

            JLabel price = new JLabel(String.format(Locale.US, "$%.2f", product.getPrice()));
            price.setFont(new Font("SansSerif", Font.BOLD, 22));
            price.setForeground(new Color(73, 73, 73));

            footer.add(brand, BorderLayout.WEST);
            footer.add(price, BorderLayout.EAST);
            return footer;
        }

        void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(245, 245, 245));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            if (selected) {
                g2.setColor(new Color(86, 153, 255));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class ProductDetailPanel extends JPanel {
        private Product currentProduct;
        private Product nextProduct;
        private float progress = 1f;
        private Timer timer;

        ProductDetailPanel(Product product) {
            this.currentProduct = product;
            setOpaque(false);
            setBorder(new EmptyBorder(18, 16, 18, 16));
        }

        void animateTo(Product product) {
            if (product == currentProduct) {
                return;
            }
            nextProduct = product;
            progress = 0f;
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            timer = new Timer(16, e -> {
                progress += 0.08f;
                if (progress >= 1f) {
                    progress = 1f;
                    currentProduct = nextProduct;
                    nextProduct = null;
                    timer.stop();
                }
                repaint();
            });
            timer.start();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (nextProduct != null && progress < 1f) {
                drawProduct(g2, currentProduct, 1f - progress, (int) (-36 * progress));
                drawProduct(g2, nextProduct, progress, (int) (36 * (1f - progress)));
            } else {
                drawProduct(g2, currentProduct, 1f, 0);
            }
            g2.dispose();
        }

        private void drawProduct(Graphics2D g2, Product product, float alpha, int offsetX) {
            Graphics2D layer = (Graphics2D) g2.create();
            layer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, Math.min(1f, alpha))));
            layer.translate(offsetX, 0);

            int width = getWidth();
            int imageY = 46;
            int imageWidth = width - 70;
            int imageHeight = 250;

            Image scaled = scale(loadImage(product.getImagePath()), imageWidth, imageHeight);
            int imageX = (width - imageWidth) / 2;
            layer.drawImage(scaled, imageX, imageY, null);

            int lineY = imageY + imageHeight + 20;
            layer.setColor(new Color(186, 190, 197));
            layer.setStroke(new BasicStroke(1.3f));
            layer.drawLine(0, lineY, width - 20, lineY);

            int textY = lineY + 42;
            layer.setColor(new Color(77, 77, 77));
            layer.setFont(new Font("SansSerif", Font.BOLD, 24));
            drawWrapped(layer, product.getName(), 0, textY, width - 20, 30, 2);

            layer.setFont(new Font("SansSerif", Font.BOLD, 22));
            layer.drawString(String.format(Locale.US, "$%.2f", product.getPrice()), 0, textY + 74);

            layer.setColor(new Color(88, 88, 88));
            layer.setFont(new Font("SansSerif", Font.PLAIN, 18));
            layer.drawString(product.getBrand(), 0, textY + 106);

            layer.setColor(new Color(120, 120, 120));
            layer.setFont(new Font("SansSerif", Font.PLAIN, 17));
            layer.drawString("So luong: " + product.getQuantity(), 0, textY + 136);

            layer.setColor(new Color(174, 174, 174));
            layer.setFont(new Font("SansSerif", Font.BOLD, 16));
            drawWrapped(layer, product.getDescription(), 0, textY + 172, width - 40, 28, 3);

            layer.dispose();
        }

        private void drawWrapped(Graphics2D g2, String text, int x, int y, int maxWidth, int lineHeight, int maxLines) {
            if (text == null || text.isBlank()) {
                return;
            }

            FontMetrics metrics = g2.getFontMetrics();
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            int drawnLines = 0;

            for (String word : words) {
                String candidate = line.length() == 0 ? word : line + " " + word;
                if (metrics.stringWidth(candidate) > maxWidth && line.length() > 0) {
                    g2.drawString(line.toString(), x, y + drawnLines * lineHeight);
                    drawnLines++;
                    line = new StringBuilder(word);
                    if (drawnLines >= maxLines - 1) {
                        break;
                    }
                } else {
                    line = new StringBuilder(candidate);
                }
            }

            if (drawnLines < maxLines) {
                g2.drawString(line.toString(), x, y + drawnLines * lineHeight);
            }
        }
    }
}
