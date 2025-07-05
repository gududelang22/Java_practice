import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GuessNumberGameGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow gameWindow = new GameWindow();
            gameWindow.setVisible(true);
        });
    }
}

// 单例模式实现的游戏分数管理器
class GameScoreManager {
    private static final GameScoreManager INSTANCE = new GameScoreManager();
    
    private int highScore = 0;
    private int totalGamesPlayed = 0;
    private int totalAttempts = 0;
    
    private GameScoreManager() {
        System.out.println("游戏分数管理器初始化完成");
    }
    
    public static GameScoreManager getInstance() {
        return INSTANCE;
    }
    
    public void updateHighScore(int score) {
        if (score > highScore) {
            highScore = score;
        }
    }
    
    public void addGameRecord(int attempts) {
        totalGamesPlayed++;
        totalAttempts += attempts;
    }
    
    public int getHighScore() {
        return highScore;
    }
    
    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }
    
    public double getAverageAttempts() {
        return totalGamesPlayed > 0 ? (double) totalAttempts / totalGamesPlayed : 0;
    }
    
    public String getStatistics() {
        return String.format(
            "<html><b>游戏统计：</b><br>" +
            "最高分: %d<br>" +
            "总游戏次数: %d<br>" +
            "平均尝试次数: %.1f</html>",
            highScore, totalGamesPlayed, getAverageAttempts()
        );
    }
}

// 游戏主窗口
class GameWindow extends JFrame {
    private GameScoreManager scoreManager = GameScoreManager.getInstance();
    private int targetNumber;
    private int attempts;
    private int maxAttempts = 8;
    private int maxNumber = 100;
    private int currentScore;
    
    // UI组件
    private JLabel infoLabel;
    private JTextField guessField;
    private JButton guessButton;
    private JTextArea historyArea;
    private JLabel statsLabel;
    private JButton newGameButton;
    
    public GameWindow() {
        setTitle("猜数字游戏 - 单例模式演示");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        startNewGame();
    }
    
    private void initUI() {
        // 主面板布局
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 顶部信息面板
        JPanel topPanel = new JPanel(new BorderLayout());
        infoLabel = new JLabel("欢迎来到猜数字游戏！");
        infoLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        topPanel.add(infoLabel, BorderLayout.CENTER);
        
        statsLabel = new JLabel();
        statsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statsLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        updateStats();
        topPanel.add(statsLabel, BorderLayout.EAST);
        
        // 游戏控制面板
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        guessField = new JTextField(10);
        guessField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        guessButton = new JButton("猜！");
        guessButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        guessButton.addActionListener(e -> processGuess());
        
        newGameButton = new JButton("新游戏");
        newGameButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        newGameButton.addActionListener(e -> startNewGame());
        
        controlPanel.add(new JLabel("你的猜测:"));
        controlPanel.add(guessField);
        controlPanel.add(guessButton);
        controlPanel.add(newGameButton);
        
        // 游戏历史区域
        historyArea = new JTextArea(10, 30);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(historyArea);
        
        // 添加组件到主面板
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        // 添加键盘支持
        guessField.addActionListener(e -> processGuess());
        
        add(mainPanel);
    }
    
    private void startNewGame() {
        Random random = new Random();
        targetNumber = random.nextInt(maxNumber) + 1;
        attempts = 0;
        currentScore = 0;
        
        historyArea.setText("");
        addHistory("新游戏开始！目标数字: 1-" + maxNumber);
        addHistory("你有 " + maxAttempts + " 次机会");
        infoLabel.setText("请猜测一个1到" + maxNumber + "之间的数字");
        infoLabel.setForeground(Color.BLACK);
        
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        guessField.setText("");
        guessField.requestFocus();
        
        updateStats();
    }
    
    private void processGuess() {
        String input = guessField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入一个数字！", "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int guess = Integer.parseInt(input);
            attempts++;
            
            if (guess == targetNumber) {
                gameWon();
            } else if (attempts >= maxAttempts) {
                gameLost();
            } else {
                if (guess < targetNumber) {
                    addHistory("第" + attempts + "次: " + guess + " → 太小了！");
                    infoLabel.setText("太小了！再试一次");
                    infoLabel.setForeground(Color.BLUE);
                } else {
                    addHistory("第" + attempts + "次: " + guess + " → 太大了！");
                    infoLabel.setText("太大了！再试一次");
                    infoLabel.setForeground(Color.RED);
                }
                guessField.setText("");
                guessField.requestFocus();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "请输入有效的数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
            guessField.setText("");
            guessField.requestFocus();
        }
    }
    
    private void gameWon() {
        // 计算得分(尝试次数越少得分越高)
        currentScore = (maxAttempts - attempts + 1) * 10;
        
        addHistory("第" + attempts + "次: " + targetNumber + " → 恭喜猜中！");
        addHistory("你的得分: " + currentScore);
        
        // 更新分数统计
        scoreManager.updateHighScore(currentScore);
        scoreManager.addGameRecord(attempts);
        
        infoLabel.setText("恭喜！你在第" + attempts + "次猜中了数字！得分: " + currentScore);
        infoLabel.setForeground(new Color(0, 150, 0));
        
        endGame();
    }
    
    private void gameLost() {
        addHistory("第" + attempts + "次: 未猜中！正确答案是: " + targetNumber);
        scoreManager.addGameRecord(attempts);
        
        infoLabel.setText("游戏结束！正确答案是: " + targetNumber);
        infoLabel.setForeground(Color.RED);
        
        endGame();
    }
    
    private void endGame() {
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        updateStats();
    }
    
    private void addHistory(String message) {
        historyArea.append(message + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }
    
    private void updateStats() {
        statsLabel.setText("<html>" + scoreManager.getStatistics().replace("\n", "<br>") + "</html>");
    }
    
    // 单例模式信息展示对话框
    public void showSingletonInfo() {
        String message = "<html><div style='text-align:center;'><b>单例模式演示</b><br><br>" +
                         "在这个游戏中，我们使用单例模式管理游戏分数：<br>" +
                         "1. 整个应用程序只有一个GameScoreManager实例<br>" +
                         "2. 所有游戏窗口共享同一个分数记录<br>" +
                         "3. 通过getInstance()方法获取唯一实例<br><br>" +
                         "<font color='blue'>当前实例地址: " + scoreManager + "</font></div></html>";
        
        JOptionPane.showMessageDialog(this, message, "单例模式说明", JOptionPane.INFORMATION_MESSAGE);
    }
}