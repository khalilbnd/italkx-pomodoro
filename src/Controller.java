import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Controller extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel TimeLabel = new JLabel("Time (minutes)");
    private JTextField TimeField = new JTextField();
    private JLabel MessageLabel = new JLabel("Message");
    public static JTextField MessageField = new JTextField("Welcome to the 2nd Edition  of iTalkX !");
    private JButton TimerButton = new JButton("Send Timer");
    private JButton MessageButton = new JButton("Send Message");
    private JButton PauseTimerButton = new JButton("Pause");
    private JButton ResumeTimerButton = new JButton("Resume");
    private JButton reset = new JButton("Reset");


    private int secondsRemaining;

    private Timer timer;

    private JButton SecondScreenbutton = new JButton("Launch Second Screen");

    private JButton StartTimerButton = new JButton("Start Timer");
    private JButton StopTimerButton = new JButton("Stop Timer");

    private SoundPlayer soundPlayer;
    private boolean soundEffectEnabled = false;

    private FullscreenFrame fullscreenFrame = new FullscreenFrame();
    private boolean isPaused = false;

    public Controller() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        super("ItalkX");

        setLayout(null);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateTimer();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        TimeLabel.setBounds(10, 10, 100, 20);
        TimeField.setBounds(120, 10, 180, 20);

        PauseTimerButton.setBounds(10, 170, 140, 20);
        PauseTimerButton.addActionListener(e -> pauseTimer());

        ResumeTimerButton.setBounds(160, 170, 140, 20);
        ResumeTimerButton.addActionListener(e -> resumeTimer());

        MessageLabel.setBounds(10, 40, 100, 20);
        MessageField.setBounds(120, 40, 180, 20);

        TimerButton.setBounds(10, 70, 290, 20);
        TimerButton.setEnabled(this.fullscreenFrame.isVisible());
        TimerButton.addActionListener(e -> {
            this.fullscreenFrame.label.setText(formatTime(Integer.parseInt(TimeField.getText()) * 60));
        });

        MessageButton.setBounds(10, 100, 290, 20);
        MessageButton.setEnabled(this.fullscreenFrame.isVisible());

        MessageButton.addActionListener(e -> {
            this.fullscreenFrame.label.setText(MessageField.getText());
        });

        SecondScreenbutton.setBounds(10, 130, 290, 20);

        SecondScreenbutton.addActionListener(e -> {

            if (this.fullscreenFrame.isVisible()) {
                this.fullscreenFrame.setVisible(false);
                TimerButton.setEnabled(false);
                MessageButton.setEnabled(false);
                SecondScreenbutton.setText("Launch Second Screen");

            } else {
                this.fullscreenFrame.setVisible(true);
                TimerButton.setEnabled(true);
                MessageButton.setEnabled(true);
                SecondScreenbutton.setText("Close Second Screen");
                new FullscreenFrame();
            }
        });

        StartTimerButton.setBounds(10, 200, 290, 20);
        StartTimerButton.addActionListener(e -> {
            try {
                secondsRemaining = Integer.parseInt(TimeField.getText()) * 60;
                startTimer();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number of minutes", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (UnsupportedAudioFileException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (LineUnavailableException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        StopTimerButton.setBounds(10, 230, 290, 20);
        StopTimerButton.addActionListener(e -> stopTimer());

        reset.setBounds(10, 260, 290, 20);
        reset.addActionListener(e -> {
            fullscreenFrame.reset();
        });

        add(TimeLabel);
        add(TimeField);
        add(MessageLabel);
        add(MessageField);
        add(TimerButton);
        add(MessageButton);
        add(SecondScreenbutton);
        add(ResumeTimerButton);
        add(PauseTimerButton);
        add(StartTimerButton);
        add(StopTimerButton);
        add(reset);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(320, 340);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private void startTimer() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (!timer.isRunning()) {
            timer.start();
            this.soundPlayer = new SoundPlayer();

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    while (secondsRemaining > 0 && !isCancelled()) {

                        Thread.sleep(1000);
                        if (!isPaused) { // Check if the timer is not paused

                            secondsRemaining--;
                            publish(); // Trigger process() to update the UI
                        }

                    }
                    return null;
                }

                @Override
                protected void process(java.util.List<Void> chunks) {
                    fullscreenFrame.label.setText(formatTime(secondsRemaining));
                }

                @Override
                protected void done() {
                    fullscreenFrame.timesUp();
                    
                    timer.stop();

                    soundPlayer = null;

                    soundEffectEnabled = false;

                    

                }
            };

            worker.execute();
        }
    }

    private void stopTimer() {
        timer.stop();

        secondsRemaining = 1;
        fullscreenFrame.label.setText(formatTime(secondsRemaining));
        soundEffectEnabled = false;
        soundPlayer.stopSound();
    }

    private void updateTimer() throws InterruptedException {
        if (secondsRemaining <= 60 && !soundEffectEnabled && !isPaused) {
            soundEffectEnabled = true;
            soundPlayer.playSound();
        }
        fullscreenFrame.label.setText(formatTime(secondsRemaining));
        if(secondsRemaining == 0) {
            fullscreenFrame.timesUp();
        }
    }

    private void pauseTimer() {
        if (timer.isRunning()) {
            isPaused = true; // Set the flag to true to pause the SwingWorker
            soundEffectEnabled = false;
            soundPlayer.stopSound();
            JOptionPane.showMessageDialog(this, "Timer paused");
        } else {
            JOptionPane.showMessageDialog(this, "Timer is not running");
        }
    }

    private void resumeTimer() {
        if (isPaused) {
            isPaused = false; // Set the flag to false to resume the SwingWorker
            timer.start();

            JOptionPane.showMessageDialog(this, "Timer resumed");
        } else {
            JOptionPane.showMessageDialog(this, "Timer is not paused");
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public static void main(String[] args) throws Exception {
        new Controller();
    }
}
