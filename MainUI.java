import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainUI extends JFrame {
    private user currentUser; // uses the provided `user` class

    private JTextField nameField;
    private JComboBox<String> moodCombo;
    private DefaultListModel<String> historyModel;
    private JList<String> historyList;
    private JLabel quoteLabel;
    private JPanel centerPanel;

    // built-in quotes fallback
    private static final String[] DEFAULT_QUOTES = {
        "This too shall pass.",
        "Small steps every day.",
        "Breathe. You are doing your best.",
        "It's okay to ask for help.",
        "Be kind to yourself — you're learning to heal."
    };

    public MainUI() {
        setTitle("Mental Health Simulator — UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout(12, 12));

        // Top: header / user registration
        getContentPane().add(createTopPanel(), BorderLayout.NORTH);

        // Center: main interactive area
        centerPanel = new JPanel(new BorderLayout(10,10));
        centerPanel.setBorder(new EmptyBorder(10,10,10,10));
        centerPanel.add(createMoodPanel(), BorderLayout.WEST);
        centerPanel.add(createHistoryPanel(), BorderLayout.CENTER);
        centerPanel.add(createRightPanel(), BorderLayout.EAST);

        getContentPane().add(centerPanel, BorderLayout.CENTER);

        // Footer: status
        JLabel footer = new JLabel("Built with Java Swing — integrates with user, mood tracker and optional providers if present.");
        footer.setBorder(new EmptyBorder(6,10,6,10));
        getContentPane().add(footer, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel(){
        JPanel top = new JPanel(new BorderLayout(8,8));
        top.setBorder(new EmptyBorder(10,10,0,10));

        JLabel title = new JLabel("Mental Health Simulator");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nameField = new JTextField(14);
        JButton createBtn = new JButton("Create / Load User");
        createBtn.addActionListener(e -> onCreateUser());

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(createBtn);

        top.add(title, BorderLayout.WEST);
        top.add(form, BorderLayout.EAST);
        return top;
    }

    private JPanel createMoodPanel(){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setPreferredSize(new Dimension(260, 0));

        JLabel lbl = new JLabel("Record your mood");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        moodCombo = new JComboBox<>(new String[]{"Happy","Calm","Neutral","Anxious","Sad","Angry","Stressed","Excited"});
        moodCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        moodCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton addMoodBtn = new JButton("Add Mood");
        addMoodBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addMoodBtn.addActionListener(e -> onAddMood());

        JButton quickCheckBtn = new JButton("Quick Mood Check (emoji)");
        quickCheckBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        quickCheckBtn.addActionListener(e -> onQuickMoodCheck());

        JButton calmBtn = new JButton("Start Calm Activity");
        calmBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        calmBtn.addActionListener(e -> onStartCalmActivity());

        p.add(lbl);
        p.add(Box.createRigidArea(new Dimension(0,8)));
        p.add(moodCombo);
        p.add(Box.createRigidArea(new Dimension(0,8)));
        p.add(addMoodBtn);
        p.add(Box.createRigidArea(new Dimension(0,6)));
        p.add(quickCheckBtn);
        p.add(Box.createRigidArea(new Dimension(0,6)));
        p.add(calmBtn);

        return p;
    }

    private JPanel createHistoryPanel(){
        JPanel p = new JPanel(new BorderLayout(6,6));
        p.setBorder(BorderFactory.createTitledBorder("Mood History"));

        historyModel = new DefaultListModel<>();
        historyList = new JList<>(historyModel);
        JScrollPane sp = new JScrollPane(historyList);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton clearBtn = new JButton("Clear History");
        clearBtn.addActionListener(e -> {
            if(currentUser!=null) {
                currentUser.getMoodHistory().clear();
                refreshHistory();
            }
        });
        JButton exportBtn = new JButton("Export (simple)");
        exportBtn.addActionListener(e -> exportHistory());

        controls.add(clearBtn);
        controls.add(exportBtn);

        p.add(sp, BorderLayout.CENTER);
        p.add(controls, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createRightPanel(){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setPreferredSize(new Dimension(320, 0));
        p.setBorder(BorderFactory.createTitledBorder("Support & Quotes"));

        quoteLabel = new JLabel("\u201C" + getQuote() + "\u201D");
        quoteLabel.setFont(new Font("Serif", Font.ITALIC, 14));
        quoteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quoteLabel.setBorder(new EmptyBorder(8,8,8,8));

        JButton newQuote = new JButton("New Quote");
        newQuote.setAlignmentX(Component.LEFT_ALIGNMENT);
        newQuote.addActionListener(e -> quoteLabel.setText("\u201C" + getQuote() + "\u201D"));

        JTextArea quickHelp = new JTextArea();
        quickHelp.setEditable(false);
        quickHelp.setLineWrap(true);
        quickHelp.setWrapStyleWord(true);
        quickHelp.setText("If you are feeling overwhelmed: take 3 deep breaths, step outside for a minute, or reach out to a trusted person. This app is not a substitute for professional help.");
        quickHelp.setBorder(new EmptyBorder(6,6,6,6));
        quickHelp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        p.add(quoteLabel);
        p.add(newQuote);
        p.add(Box.createRigidArea(new Dimension(0,8)));
        p.add(quickHelp);

        return p;
    }

    // --- Actions ---
    private void onCreateUser(){
        String name = nameField.getText().trim();
        if(name.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter a name.", "Missing name", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{
            // Try to instantiate provided `user` class via constructor that accepts String
            Class<?> userClass = Class.forName("user");
            Constructor<?> ctor = userClass.getConstructor(String.class);
            Object u = ctor.newInstance(name);
            this.currentUser = (user) u;
            refreshHistory();
            JOptionPane.showMessageDialog(this, "User created/loaded: " + currentUser.getName());
        } catch (ClassNotFoundException cnf){
            // fallback: create a local simple user-like wrapper
            JOptionPane.showMessageDialog(this, "Provided 'user' class not found on classpath. Using local fallback.\nMake sure your user.java is compiled and in same folder.", "Warning", JOptionPane.WARNING_MESSAGE);
            this.currentUser = new user(name); // still works if the provided class exists; otherwise your local user file will be used
            refreshHistory();
        } catch (Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not create user via reflection. Falling back to local user.\nError: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.currentUser = new user(name);
            refreshHistory();
        }
    }

    private void onAddMood(){
        if(this.currentUser == null){
            JOptionPane.showMessageDialog(this, "Create a user first.", "No user", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String mood = (String) moodCombo.getSelectedItem();
        currentUser.addMoodHistory(mood + " — " + java.time.LocalDateTime.now().withNano(0));
        refreshHistory();
    }

    private void onQuickMoodCheck(){
        String[] emojis = {"😊","🙂","😐","😟","😢","😡","😰","🤩"};
        int idx = moodCombo.getSelectedIndex();
        String mood = (String) moodCombo.getItemAt(idx);
        String em = emojis[Math.min(idx, emojis.length-1)];
        JOptionPane.showMessageDialog(this, em + "  You selected: " + mood, "Quick Mood Check", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onStartCalmActivity(){
        // Try to call external CalmActivity class if present via reflection
        try{
            Class<?> calm = Class.forName("CalmActivity");
            // try static method 'start' or 'showActivity'
            try{
                Method m = calm.getMethod("start");
                m.invoke(null);
                return;
            } catch (NoSuchMethodException ns){
                // try constructor and instance method 'start' or 'show'
                try{
                    Object inst = calm.getDeclaredConstructor().newInstance();
                    try{
                        Method m2 = calm.getMethod("start");
                        m2.invoke(inst);
                        return;
                    } catch (NoSuchMethodException ns2){
                        try{
                            Method m3 = calm.getMethod("show");
                            m3.invoke(inst);
                            return;
                        } catch (NoSuchMethodException ns3){
                            // fall through to fallback
                        }
                    }
                } catch(Exception e){
                    // fall through to fallback
                }
            }
        } catch (ClassNotFoundException cnf){
            // not present — show fallback
        } catch (Exception ex){
            ex.printStackTrace();
        }

        // fallback: open internal breathing activity
        showBreathingDialog();
    }

    private void showBreathingDialog(){
        JDialog d = new JDialog(this, "Breathing Activity", true);
        d.setSize(420,220);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout(6,6));

        JLabel instruction = new JLabel("Follow the animation: Breathe in — Hold — Breathe out");
        instruction.setBorder(new EmptyBorder(8,8,8,8));

        JPanel animPanel = new JPanel(){
            int radius = 20;
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                int cx = w/2;
                int cy = h/2;
                int r = radius;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.drawOval(cx-r, cy-r, r*2, r*2);
            }
        };
        animPanel.setPreferredSize(new Dimension(380,100));

        JButton close = new JButton("Close");
        close.addActionListener(ev -> d.dispose());

        d.add(instruction, BorderLayout.NORTH);
        d.add(animPanel, BorderLayout.CENTER);
        d.add(close, BorderLayout.SOUTH);

        // simple animation using Timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            int step = 0;
            @Override
            public void run(){
                SwingUtilities.invokeLater(() -> {
                    Math.sin(step * Math.PI / 20);
                    // reflect radius into animPanel
                    animPanel.setPreferredSize(new Dimension(animPanel.getWidth(), animPanel.getHeight()));
                    animPanel.repaint();
                    step = (step+1) % 40;
                });
            }
        }, 0, 300);

        d.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosed(WindowEvent e){
                timer.cancel();
            }
            @Override
            public void windowClosing(WindowEvent e){
                timer.cancel();
            }
        });

        d.setVisible(true);
    }

    private void refreshHistory(){
        historyModel.clear();
        if(currentUser!=null){
            ArrayList<String> list = currentUser.getMoodHistory();
            for(String s : list){
                historyModel.addElement(s);
            }
        }
    }

    private void exportHistory(){
        if(currentUser==null){
            JOptionPane.showMessageDialog(this, "No user to export.");
            return;
        }
        List<String> list = currentUser.getMoodHistory();
        if(list.isEmpty()){
            JOptionPane.showMessageDialog(this, "History is empty.");
            return;
        }
        // simple export show in dialog. You can redirect to file as needed.
        StringBuilder sb = new StringBuilder();
        for(String s : list) sb.append(s).append("\n");
        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(480,320));
        JOptionPane.showMessageDialog(this, sp, "Exported History", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getQuote(){
       
        try{
            Class<?> qp = Class.forName("quoteprovider");
            // try static method getQuote()
            try{
                Method m = qp.getMethod("getQuote");
                Object q = m.invoke(null);
                if(q!=null) return q.toString();
            } catch (NoSuchMethodException ns){
                // try instance method
                Object inst = qp.getDeclaredConstructor().newInstance();
                try{
                    Method m2 = qp.getMethod("getQuote");
                    Object q2 = m2.invoke(inst);
                    if(q2!=null) return q2.toString();
                } catch(Exception ex){
                    // ignore
                }
            }
        } catch (ClassNotFoundException cnf){
            // ignore — use default
        } catch (Exception ex){
            ex.printStackTrace();
        }
        // fallback
        int i = (int)(Math.random() * DEFAULT_QUOTES.length);
        return DEFAULT_QUOTES[i];
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            try{
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(Exception ignored){}
            MainUI ui = new MainUI();
            ui.setVisible(true);
        });
    }
}
