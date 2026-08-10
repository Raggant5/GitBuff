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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.swing.SwingWorker;

import interface_adapter.recommendation.RecommendWorkoutPlanController;
import interface_adapter.workouts.RecommendedExerciseDisplayData;
import interface_adapter.workouts.WorkoutPlanDisplayData;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;

/**
 * View presenting personal workout plans and exercise guides in Swing UI.
 */
public class WorkoutsView extends JPanel implements PropertyChangeListener {

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color BG_DARK = new Color(245, 247, 250);
    private static final Color REST_COLOR = new Color(149, 165, 166);
    private static final Color CARD_BORDER = new Color(220, 224, 230);
    private static final Color BURN_BG = new Color(245, 247, 250);
    private static final Color DURATION_BG = new Color(230, 240, 250);

    private static final int TITLE_FONT_SIZE = 20;
    private static final int HEADER_FONT_SIZE = 16;
    private static final int CARD_TITLE_FONT_SIZE = 14;
    private static final int BODY_FONT_SIZE = 12;
    private static final int SMALL_FONT_SIZE = 11;

    private static final int MODAL_WIDTH = 500;
    private static final int MODAL_HEIGHT = 400;
    private static final int DAYS_PER_WEEK = 7;
    private static final int SCROLL_UNIT_INCREMENT = 16;
    private static final int REFRESH_BTN_HEIGHT = 40;
    private static final int REFRESH_BTN_WIDTH = 800;
    private static final int DEFAULT_PREFERRED_DURATION = 45;

    private final String viewName = "workouts";
    private final WorkoutsViewModel workoutsViewModel;

    private final JLabel focusLabel = new JLabel();
    private final JPanel scheduleContainer = new JPanel();
    private final JButton refreshButton = new JButton("Refresh 1-Week Schedule");

    private RecommendWorkoutPlanController recommendWorkoutPlanController;
    private int userPreferredDuration = DEFAULT_PREFERRED_DURATION;

    /**
     * Constructs a WorkoutsView panel bound to the view model.
     *
     * @param workoutsViewModel view model for workout state
     */
    public WorkoutsView(final WorkoutsViewModel workoutsViewModel) {
        this.workoutsViewModel = workoutsViewModel;
        this.workoutsViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBackground(BG_DARK);

        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        final JLabel title = new JLabel("Personal Workout Schedule");
        title.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        title.setForeground(Color.WHITE);

        this.focusLabel.setFont(new Font("SansSerif", Font.PLAIN, BODY_FONT_SIZE + 1));
        this.focusLabel.setForeground(new Color(236, 240, 241));

        topPanel.add(title);
        topPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topPanel.add(this.focusLabel);

        this.scheduleContainer.setLayout(new BoxLayout(this.scheduleContainer, BoxLayout.Y_AXIS));
        this.scheduleContainer.setBackground(BG_DARK);
        this.scheduleContainer.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        final JScrollPane scrollPane = new JScrollPane(this.scheduleContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        this.refreshButton.setFont(new Font("SansSerif", Font.BOLD, CARD_TITLE_FONT_SIZE));
        this.refreshButton.setBackground(ACCENT_COLOR);
        this.refreshButton.setForeground(Color.WHITE);
        this.refreshButton.setFocusPainted(false);
        this.refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.refreshButton.setPreferredSize(new Dimension(REFRESH_BTN_WIDTH, REFRESH_BTN_HEIGHT));

        this.refreshButton.addActionListener(evt -> {
            if (this.recommendWorkoutPlanController != null) {
                final WorkoutsState currentState = this.workoutsViewModel.getState();
                currentState.setLoading(true);
                this.workoutsViewModel.firePropertyChanged();

                final SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {
                        recommendWorkoutPlanController.execute();
                        return null;
                    }
                };
                worker.execute();
            }
        });

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(this.refreshButton, BorderLayout.SOUTH);

        displayState(this.workoutsViewModel.getState());
    }

    private void displayState(final WorkoutsState state) {
        this.focusLabel.setText("Active Focus Target: " + state.getWorkoutFocus());
        this.scheduleContainer.removeAll();
        this.setUserPreferredDuration(state.getPreferredDurationMinutes());

        if (state.isLoading()) {
            this.refreshButton.setEnabled(false);
            this.refreshButton.setText("Generating Personalized Schedule...");

            final JLabel loadingLabel = new JLabel("Loading workout schedule...");
            loadingLabel.setFont(new Font("SansSerif", Font.BOLD, CARD_TITLE_FONT_SIZE));
            loadingLabel.setForeground(PRIMARY_COLOR);
            loadingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.scheduleContainer.add(loadingLabel);
        }
        else {
            this.refreshButton.setEnabled(true);
            this.refreshButton.setText("Refresh 1-Week Schedule");

            final List<WorkoutPlanDisplayData> plans = state.getWorkoutPlans();

            if (plans == null || plans.isEmpty()) {
                final JLabel emptyLabel = new JLabel("No schedule loaded. Update your profile and click refresh!");
                emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, CARD_TITLE_FONT_SIZE));
                emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                this.scheduleContainer.add(emptyLabel);
            }
            else {
                final JLabel weekHeader = new JLabel("Week 1 Routine");
                weekHeader.setFont(new Font("SansSerif", Font.BOLD, HEADER_FONT_SIZE));
                weekHeader.setForeground(PRIMARY_COLOR);
                weekHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
                weekHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                this.scheduleContainer.add(weekHeader);

                final int totalDays = Math.min(plans.size(), DAYS_PER_WEEK);
                for (int i = 0; i < totalDays; i++) {
                    final WorkoutPlanDisplayData plan = plans.get(i);
                    final JPanel planCard = createPlanCard(plan, i + 1);
                    this.scheduleContainer.add(planCard);
                    this.scheduleContainer.add(Box.createRigidArea(new Dimension(0, 8)));
                }
            }
        }

        this.scheduleContainer.revalidate();
        this.scheduleContainer.repaint();
        this.revalidate();
        this.repaint();
    }

    private JPanel createPlanCard(final WorkoutPlanDisplayData plan, final int dayNumber) {
        final JPanel planCard = new JPanel();
        planCard.setLayout(new BoxLayout(planCard, BoxLayout.Y_AXIS));
        planCard.setBackground(Color.WHITE);
        planCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        final boolean isRestDay = plan.getExercises() == null || plan.getExercises().isEmpty();
        final Color headerColor;
        if (isRestDay) {
            headerColor = REST_COLOR;
        }
        else {
            headerColor = PRIMARY_COLOR;
        }

        planCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, headerColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(CARD_BORDER, 1),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)
                )
        ));

        final String dateStr;
        if (plan.getDate() != null && !plan.getDate().trim().isEmpty()) {
            dateStr = plan.getDate();
        }
        else {
            dateStr = "Day " + dayNumber;
        }

        int duration = this.userPreferredDuration;
        if (plan.getEstimatedDurationMinutes() > 0) {
            duration = plan.getEstimatedDurationMinutes();
        }

        final JLabel dateTitleLabel = new JLabel(String.format("%s — %s", dateStr, plan.getTitle()));
        dateTitleLabel.setFont(new Font("SansSerif", Font.BOLD, CARD_TITLE_FONT_SIZE));
        dateTitleLabel.setForeground(headerColor);
        dateTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel descLabel = new JLabel(plan.getDescription());
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, BODY_FONT_SIZE));
        descLabel.setForeground(new Color(100, 110, 120));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        planCard.add(dateTitleLabel);
        planCard.add(Box.createRigidArea(new Dimension(0, 3)));
        planCard.add(descLabel);

        if (!isRestDay) {
            final JLabel durationLabel = new JLabel(String.format("Duration: %d minutes", duration));
            durationLabel.setFont(new Font("SansSerif", Font.BOLD, SMALL_FONT_SIZE));
            durationLabel.setForeground(new Color(41, 98, 150));
            durationLabel.setOpaque(true);
            durationLabel.setBackground(DURATION_BG);
            durationLabel.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
            durationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            final JLabel burnLabel = new JLabel(String.format("Est. Burn: %d Cal | %dg Fat | %dg Carbs",
                    plan.getEstimatedCaloriesBurned(),
                    plan.getEstimatedFatBurnedGrams(),
                    plan.getEstimatedCarbsBurnedGrams()));
            burnLabel.setFont(new Font("SansSerif", Font.BOLD, SMALL_FONT_SIZE));
            burnLabel.setForeground(PRIMARY_COLOR);
            burnLabel.setOpaque(true);
            burnLabel.setBackground(BURN_BG);
            burnLabel.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
            burnLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            planCard.add(Box.createRigidArea(new Dimension(0, 5)));
            planCard.add(durationLabel);
            planCard.add(Box.createRigidArea(new Dimension(0, 3)));
            planCard.add(burnLabel);

            planCard.add(Box.createRigidArea(new Dimension(0, 8)));
            final JPanel exercisesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
            exercisesPanel.setBackground(Color.WHITE);
            exercisesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            for (final RecommendedExerciseDisplayData exercise : plan.getExercises()) {
                final String btnText = String.format("%s [%ds x %dr]",
                        exercise.getName(), exercise.getSets(), exercise.getReps());
                final JButton exButton = new JButton(btnText);
                exButton.setFont(new Font("SansSerif", Font.PLAIN, BODY_FONT_SIZE));
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

        return planCard;
    }

    private void showExerciseGuideModal(final RecommendedExerciseDisplayData exercise) {
        final JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Exercise Guide: " + exercise.getName());
        dialog.setModal(true);
        dialog.setSize(MODAL_WIDTH, MODAL_HEIGHT);
        dialog.setLocationRelativeTo(this);

        final JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);

        final JLabel nameLabel = new JLabel(exercise.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, HEADER_FONT_SIZE));
        nameLabel.setForeground(PRIMARY_COLOR);

        final String targetMuscleGroup;
        if (exercise.getTargetMuscleGroup() != null) {
            targetMuscleGroup = exercise.getTargetMuscleGroup();
        }
        else {
            targetMuscleGroup = "N/A";
        }
        final String equipmentRequired;
        if (exercise.getEquipmentRequired() != null) {
            equipmentRequired = exercise.getEquipmentRequired();
        }
        else {
            equipmentRequired = "Bodyweight";
        }
        final String targetSummary = String.format("%d sets x %d reps (%d mins) | Target: %s | Equip: %s",
                exercise.getSets(), exercise.getReps(), exercise.getDurationMinutes(),
                targetMuscleGroup,
                equipmentRequired);
        final JLabel setsLabel = new JLabel("<html><center>" + targetSummary + "</center></html>",
                SwingConstants.CENTER);
        setsLabel.setFont(new Font("SansSerif", Font.BOLD, BODY_FONT_SIZE));
        setsLabel.setForeground(ACCENT_COLOR);

        final JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setBackground(Color.WHITE);
        topBox.add(nameLabel);
        topBox.add(Box.createRigidArea(new Dimension(0, 4)));
        topBox.add(setsLabel);

        final String instructions;
        if (exercise.getInstructions() != null && !exercise.getInstructions().trim().isEmpty()) {
            instructions = exercise.getInstructions();
        }
        else {
            instructions = "Perform this movement with proper posture, controlled cadence, and full range of motion.";
        }

        final JLabel instLabel = new JLabel("<html><body style='width: 360px; text-align: left; color: #333333;'>"
                + instructions + "</body></html>");
        instLabel.setFont(new Font("SansSerif", Font.PLAIN, BODY_FONT_SIZE + 1));

        final JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(Color.WHITE);

        final JButton videoBtn = new JButton("Open Video Guide on YouTube");
        videoBtn.setBackground(PRIMARY_COLOR);
        videoBtn.setForeground(Color.WHITE);
        videoBtn.setFocusPainted(false);
        videoBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        videoBtn.addActionListener(evt -> {
            String targetUrl = exercise.getVideoUrl();
            if (targetUrl == null || targetUrl.trim().isEmpty()) {
                targetUrl = "https://www.youtube.com/results?search_query="
                        + exercise.getName().replace(" ", "+") + "+exercise+tutorial";
            }
            try {
                Desktop.getDesktop().browse(new URI(targetUrl));
            }
            catch (final IOException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(dialog, "Could not open URL: " + targetUrl);
            }
        });

        btnPanel.add(videoBtn);

        final JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        final JScrollPane instScroll = new JScrollPane(instLabel);
        instScroll.setBorder(null);
        instScroll.setBackground(Color.WHITE);
        centerPanel.add(instScroll, BorderLayout.CENTER);

        contentPanel.add(topBox, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        displayState((WorkoutsState) evt.getNewValue());
    }

    public String getViewName() {
        return viewName;
    }

    public void setRecommendWorkoutPlanController(
            final RecommendWorkoutPlanController recommendWorkoutPlanController) {
        this.recommendWorkoutPlanController = recommendWorkoutPlanController;
    }

    /**
     * Sets the user's preferred workout duration, used for plans that don't specify their own.
     *
     * @param minutes the preferred duration in minutes; falls back to the default if not positive
     */
    public void setUserPreferredDuration(final int minutes) {
        if (minutes > 0) {
            this.userPreferredDuration = minutes;
        }
        else {
            this.userPreferredDuration = DEFAULT_PREFERRED_DURATION;
        }
    }
}



