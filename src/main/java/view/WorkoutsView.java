package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import entity.Exercise;
import entity.WorkoutPlan;
import interface_adapter.recommendation.RecommendationController;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;

/**
 * Modern Workouts View with clean Cal unit labels and image-free burn banners.
 */
public class WorkoutsView extends JPanel implements PropertyChangeListener {

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color BG_DARK = new Color(245, 247, 250);
    private static final Color REST_COLOR = new Color(149, 165, 166);
    private static final Color CARD_BORDER = new Color(220, 224, 230);
    private static final Color BURN_BG = new Color(245, 247, 250);

    private final String viewName = "workouts";
    private final WorkoutsViewModel workoutsViewModel;

    private final JLabel focusLabel = new JLabel();
    private final JPanel week1Container = new JPanel();
    private final JPanel week2Container = new JPanel();
    private final JButton refreshButton = new JButton("Refresh 2-Week Schedule");

    private RecommendationController recommendationController;

    public WorkoutsView(final WorkoutsViewModel workoutsViewModel) {
        this.workoutsViewModel = workoutsViewModel;
        this.workoutsViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(BG_DARK);

        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        final JLabel title = new JLabel("Personal Workout Schedule");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        this.focusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        this.focusLabel.setForeground(new Color(236, 240, 241));

        topPanel.add(title);
        topPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topPanel.add(this.focusLabel);

        this.week1Container.setLayout(new BoxLayout(this.week1Container, BoxLayout.Y_AXIS));
        this.week1Container.setBackground(BG_DARK);

        this.week2Container.setLayout(new BoxLayout(this.week2Container, BoxLayout.Y_AXIS));
        this.week2Container.setBackground(BG_DARK);

        final JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));
        schedulePanel.setBackground(BG_DARK);
        schedulePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        schedulePanel.add(createWeekHeader("Week 1 Routine"));
        schedulePanel.add(this.week1Container);
        schedulePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        schedulePanel.add(createWeekHeader("Week 2 Routine"));
        schedulePanel.add(this.week2Container);

        final JScrollPane scrollPane = new JScrollPane(schedulePanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        this.refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        this.refreshButton.setBackground(ACCENT_COLOR);
        this.refreshButton.setForeground(Color.WHITE);
        this.refreshButton.setFocusPainted(false);
        this.refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.refreshButton.setPreferredSize(new Dimension(800, 40));

        this.refreshButton.addActionListener(evt -> {
            if (this.recommendationController != null) {
                this.recommendationController.execute();
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(this.refreshButton, BorderLayout.SOUTH);

        displayState(this.workoutsViewModel.getState());
    }

    private JLabel createWeekHeader(final String text) {
        final JLabel header = new JLabel(text);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setForeground(PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return header;
    }

    private void displayState(final WorkoutsState state) {
        this.focusLabel.setText("Active Focus Target: " + state.getWorkoutFocus());
        this.week1Container.removeAll();
        this.week2Container.removeAll();

        final List<WorkoutPlan> plans = state.getWorkoutPlans();

        if (plans.isEmpty()) {
            final JLabel emptyLabel = new JLabel("No schedule loaded. Update your profile and click refresh!");
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            this.week1Container.add(emptyLabel);
        }
        else {
            for (int i = 0; i < plans.size(); i++) {
                final WorkoutPlan plan = plans.get(i);
                final JPanel targetWeekContainer = (i < 7) ? this.week1Container : this.week2Container;

                final JPanel planCard = new JPanel();
                planCard.setLayout(new BoxLayout(planCard, BoxLayout.Y_AXIS));
                planCard.setBackground(Color.WHITE);
                planCard.setAlignmentX(Component.LEFT_ALIGNMENT);

                final boolean isRestDay = plan.getExercises().isEmpty();
                final Color headerColor = isRestDay ? REST_COLOR : PRIMARY_COLOR;

                planCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 5, 0, 0, headerColor),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(CARD_BORDER, 1),
                                BorderFactory.createEmptyBorder(10, 12, 10, 12)
                        )
                ));

                final JLabel dateTitleLabel = new JLabel(String.format("%s — %s", plan.getDate(), plan.getTitle()));
                dateTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                dateTitleLabel.setForeground(headerColor);

                final JLabel descLabel = new JLabel(plan.getDescription());
                descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                descLabel.setForeground(new Color(100, 110, 120));

                planCard.add(dateTitleLabel);
                planCard.add(Box.createRigidArea(new Dimension(0, 3)));
                planCard.add(descLabel);

                if (!isRestDay) {
                    final JLabel burnLabel = new JLabel(String.format("Est. Burn: %d Cal | %dg Fat | %dg Carbs",
                            plan.getEstimatedCaloriesBurned(),
                            plan.getEstimatedFatBurnedGrams(),
                            plan.getEstimatedCarbsBurnedGrams()));
                    burnLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
                    burnLabel.setForeground(PRIMARY_COLOR);
                    burnLabel.setOpaque(true);
                    burnLabel.setBackground(BURN_BG);
                    burnLabel.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

                    planCard.add(Box.createRigidArea(new Dimension(0, 5)));
                    planCard.add(burnLabel);

                    planCard.add(Box.createRigidArea(new Dimension(0, 8)));
                    final JPanel exercisesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
                    exercisesPanel.setBackground(Color.WHITE);

                    for (Exercise exercise : plan.getExercises()) {
                        final JButton exButton = new JButton(exercise.getName() + " [" + exercise.getSetsAndReps() + "]");
                        exButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
                        exButton.setBackground(new Color(235, 243, 250));
                        exButton.setForeground(PRIMARY_COLOR);
                        exButton.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
                        exButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        exButton.setFocusPainted(false);

                        exButton.addActionListener(evt -> showExerciseGuideModal(exercise));
                        exercisesPanel.add(exButton);
                    }
                    planCard.add(exercisesPanel);
                }

                targetWeekContainer.add(planCard);
                targetWeekContainer.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        this.week1Container.revalidate();
        this.week2Container.revalidate();
        revalidate();
        repaint();
    }

    private void showExerciseGuideModal(final Exercise exercise) {
        final JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Exercise Guide: " + exercise.getName());
        dialog.setModal(true);
        dialog.setSize(450, 320);
        dialog.setLocationRelativeTo(this);

        final JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);

        final JLabel nameLabel = new JLabel(exercise.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setForeground(PRIMARY_COLOR);

        final JLabel setsLabel = new JLabel("Target: " + exercise.getSetsAndReps(), SwingConstants.CENTER);
        setsLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        setsLabel.setForeground(ACCENT_COLOR);

        final JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setBackground(Color.WHITE);
        topBox.add(nameLabel);
        topBox.add(setsLabel);

        final JLabel instLabel = new JLabel("<html><body style='width: 320px; text-align: center; color: #333333;'>"
                + exercise.getInstructions() + "</body></html>", SwingConstants.CENTER);
        instLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        final JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(Color.WHITE);

        final JButton videoBtn = new JButton("Open Video / GIF Guide in Browser");
        videoBtn.setBackground(PRIMARY_COLOR);
        videoBtn.setForeground(Color.WHITE);
        videoBtn.setFocusPainted(false);
        videoBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        videoBtn.addActionListener(evt -> {
            if (exercise.getVideoUrl() != null && !exercise.getVideoUrl().isEmpty()) {
                try {
                    Desktop.getDesktop().browse(new URI(exercise.getVideoUrl()));
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Could not open URL: " + exercise.getVideoUrl());
                }
            }
        });

        btnPanel.add(videoBtn);

        contentPanel.add(topBox, BorderLayout.NORTH);
        contentPanel.add(instLabel, BorderLayout.CENTER);
        contentPanel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        displayState((WorkoutsState) evt.getNewValue());
    }

    public String getViewName() {
        return this.viewName;
    }

    public void setRecommendationController(final RecommendationController recommendationController) {
        this.recommendationController = recommendationController;
    }
}
