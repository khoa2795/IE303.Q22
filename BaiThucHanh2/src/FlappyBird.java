import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// ===== Bài 1: Create Game Window (2 points) =====
// - Create a window with size 540×960 pixels (1.5x larger)
// - Window cannot be resized
// - Set window title to "Flappy Bird"
// - Set background image from flappybirdbg.png

public class FlappyBird extends JFrame {
    private GamePanel gamePanel;

    public FlappyBird() {
        // ===== Bài 1: Window Configuration =====
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(540, 960);
        setLocationRelativeTo(null);
        
        gamePanel = new GamePanel();
        add(gamePanel);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlappyBird());
    }
}

// ===== Main Game Panel - Handles rendering and game logic =====
class GamePanel extends JPanel {
    // ===== Bài 1: Background Image Setup =====
    private BufferedImage backgroundImage;
    
    // ===== Bài 2: Bird Object and Game State =====
    private Bird bird;
    private boolean gameOver = false;
    private int score = 0;
    
    // ===== Bài 3: Pipe System =====
    private List<Pipe> pipes;
    private int pipeCounter = 0;
    private int minGapHeight = 120;
    private int currentGapHeight = 150;
    
    // ===== Bài 4: Game Over and Restart =====
    private RestartButton restartButton;
    private Timer gameTimer;
    
    public GamePanel() {
        setFocusable(true);
        
        // ===== Bài 1: Load Background Image =====
        try {
            backgroundImage = javax.imageio.ImageIO.read(
                new File("../BaiThucHanh/flappybirdbg.png")
            );
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
        
        // ===== Bài 2: Initialize Bird =====
        bird = new Bird(75, 450);
        
        // ===== Bài 3: Initialize Pipe System =====
        pipes = new ArrayList<>();
        generatePipe();
        
        // ===== Bài 4: Initialize Restart Button =====
        restartButton = new RestartButton(195, 450, 150, 75);
        
        // ===== Bài 3: Game Loop Setup =====
        gameTimer = new Timer(20, e -> {
            if (!gameOver) {
                update();
            }
            repaint();
        });
        gameTimer.start();
        
        // ===== Bài 2: Keyboard Input Setup =====
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_SPACE || 
                     e.getKeyCode() == KeyEvent.VK_ENTER) && !gameOver) {
                    bird.jump();
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // ===== Bài 4: Restart Button Click Handler =====
                if (gameOver && restartButton.contains(e.getPoint())) {
                    restart();
                }
            }
        });
    }
    
    // ===== Bài 3: Game Update Logic =====
    private void update() {
        // ===== Bài 2: Update Bird Physics =====
        bird.update();
        
        // Check if bird hit ground or flew too high
        if (bird.getY() >= 920 || bird.getY() < 0) {
            gameOver = true;
            return;
        }
        
        // ===== Bài 3: Update Pipes =====
        for (Pipe pipe : pipes) {
            pipe.update();
        }
        
        // ===== Bài 3: Generate New Pipes =====
        pipeCounter++;
        if (pipeCounter > 120) {
            generatePipe();
            pipeCounter = 0;
            // Gradually decrease gap height (max 3 pixels per new pipe)
            if (currentGapHeight > minGapHeight) {
                currentGapHeight = Math.max(minGapHeight, currentGapHeight - 1);
            }
        }
        
        // ===== Bài 3: Remove Pipes Off Screen =====
        pipes.removeIf(pipe -> pipe.getX() < -50);
        
        // ===== Bài 4: Collision Detection & Scoring =====
        for (Pipe pipe : pipes) {
            // Score when bird passes pipe
            if (!pipe.isScored() && pipe.getX() < bird.getX() && 
                pipe.getX() + pipe.getWidth() > bird.getX()) {
                score++;
                pipe.setScored(true);
            }
            
            // Collision detection
            if (pipe.collidesWith(bird)) {
                gameOver = true;
                return;
            }
        }
    }
    
    // ===== Bài 3: Generate Pipe with Random Height =====
    private void generatePipe() {
        int gap = currentGapHeight;
        int topHeight = (int)(Math.random() * (600 - gap) + 50);
        int bottomHeight = 960 - topHeight - gap;
        pipes.add(new Pipe(540, topHeight, gap, bottomHeight));
    }
    
    // ===== Bài 4: Restart Game =====
    private void restart() {
        bird = new Bird(75, 450);
        pipes.clear();
        pipeCounter = 0;
        score = 0;
        currentGapHeight = 150;
        gameOver = false;
        generatePipe();
        requestFocus();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // ===== Bài 1: Draw Background =====
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g2d.setColor(new Color(135, 206, 250));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // ===== Bài 2: Draw Bird =====
        bird.draw(g2d);
        
        // ===== Bài 3: Draw Pipes =====
        for (Pipe pipe : pipes) {
            pipe.draw(g2d);
        }
        
        // ===== Bài 4: Draw Score =====
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        g2d.drawString(String.valueOf(score), 30, 80);
        
        // ===== Bài 4: Draw Game Over and Restart Button =====
        if (gameOver) {
            // Darken the screen
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Draw "Game Over" text
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 90));
            String gameOverText = "GAME OVER";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(gameOverText)) / 2;
            g2d.drawString(gameOverText, x, 250);
            
            // Draw score
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 50));
            String scoreText = "Score: " + score;
            fm = g2d.getFontMetrics();
            x = (getWidth() - fm.stringWidth(scoreText)) / 2;
            g2d.drawString(scoreText, x, 360);
            
            // Draw restart button
            restartButton.draw(g2d);
        }
    }
}

// ===== Bài 2: Bird Class - Handles bird physics and rendering =====
class Bird {
    private static final String BIRD_IMAGE_PATH = "../BaiThucHanh/flappybird.png";
    private static final int BIRD_SIZE = 52;
    private static final float GRAVITY = 0.5f;
    private static final float JUMP_STRENGTH = -12f;
    
    private double x, y;
    private float velocityY = 0;
    private BufferedImage birdImage;
    
    public Bird(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        
        // Load bird image
        try {
            birdImage = javax.imageio.ImageIO.read(new File(BIRD_IMAGE_PATH));
        } catch (Exception e) {
            System.err.println("Error loading bird image: " + e.getMessage());
        }
    }
    
    // ===== Bài 2: Update Bird Physics - Gravity Effect =====
    public void update() {
        velocityY += GRAVITY;
        y += velocityY;
    }
    
    // ===== Bài 2: Jump Mechanic - SPACE or ENTER =====
    public void jump() {
        velocityY = JUMP_STRENGTH;
    }
    
    // ===== Bài 2: Draw Bird =====
    public void draw(Graphics2D g) {
        if (birdImage != null) {
            g.drawImage(birdImage, (int)x, (int)y, BIRD_SIZE, BIRD_SIZE, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval((int)x, (int)y, BIRD_SIZE, BIRD_SIZE);
        }
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return BIRD_SIZE; }
    public int getHeight() { return BIRD_SIZE; }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, BIRD_SIZE, BIRD_SIZE);
    }
}

// ===== Bài 3: Pipe Class - Handles pipe obstacles =====
class Pipe {
    private static final String TOP_PIPE_IMAGE = "../BaiThucHanh/toppipe.png";
    private static final String BOTTOM_PIPE_IMAGE = "../BaiThucHanh/bottompipe.png";
    private static final int PIPE_WIDTH = 90;
    private static final int PIPE_SPEED = 6;
    
    private int x;
    private int topHeight;
    private int gapHeight;
    private int bottomHeight;
    private boolean scored = false;
    private BufferedImage topPipeImage;
    private BufferedImage bottomPipeImage;
    
    public Pipe(int startX, int topHeight, int gapHeight, int bottomHeight) {
        this.x = startX;
        this.topHeight = topHeight;
        this.gapHeight = gapHeight;
        this.bottomHeight = bottomHeight;
        
        // Load pipe images
        try {
            topPipeImage = javax.imageio.ImageIO.read(new File(TOP_PIPE_IMAGE));
            bottomPipeImage = javax.imageio.ImageIO.read(new File(BOTTOM_PIPE_IMAGE));
        } catch (Exception e) {
            System.err.println("Error loading pipe images: " + e.getMessage());
        }
    }
    
    // ===== Bài 3: Update Pipe Position - Move Left =====
    public void update() {
        x -= PIPE_SPEED;
    }
    
    // ===== Bài 3: Draw Pipes =====
    public void draw(Graphics2D g) {
        // Draw top pipe
        if (topPipeImage != null) {
            g.drawImage(topPipeImage, x, 0, PIPE_WIDTH, topHeight, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillRect(x, 0, PIPE_WIDTH, topHeight);
        }
        
        // Draw bottom pipe
        int bottomY = topHeight + gapHeight;
        if (bottomPipeImage != null) {
            g.drawImage(bottomPipeImage, x, bottomY, PIPE_WIDTH, bottomHeight, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillRect(x, bottomY, PIPE_WIDTH, bottomHeight);
        }
    }
    
    // ===== Bài 4: Collision Detection =====
    public boolean collidesWith(Bird bird) {
        Rectangle birdBounds = bird.getBounds();
        
        // Collision with top pipe
        if (birdBounds.intersects(new Rectangle(x, 0, PIPE_WIDTH, topHeight))) {
            return true;
        }
        
        // Collision with bottom pipe
        int bottomY = topHeight + gapHeight;
        if (birdBounds.intersects(new Rectangle(x, bottomY, PIPE_WIDTH, bottomHeight))) {
            return true;
        }
        
        return false;
    }
    
    public int getX() { return x; }
    public int getWidth() { return PIPE_WIDTH; }
    public boolean isScored() { return scored; }
    public void setScored(boolean scored) { this.scored = scored; }
}

// ===== Bài 4: Restart Button Class =====
class RestartButton {
    private Rectangle bounds;
    
    public RestartButton(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);
    }
    
    public boolean contains(Point p) {
        return bounds.contains(p);
    }
    
    public void draw(Graphics2D g) {
        // Draw button background
        g.setColor(new Color(100, 200, 100));
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Draw button border
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Draw button text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String text = "RESTART";
        FontMetrics fm = g.getFontMetrics();
        int x = bounds.x + (bounds.width - fm.stringWidth(text)) / 2;
        int y = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(text, x, y);
    }
}
