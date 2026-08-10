package data_access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Exercise;
import entity.FitnessGoal;
import entity.User;

/**
 * Static reference data and pure lookup logic for generating workout/exercise content: which
 * exercises belong to which workout "type" (running, biking, upper, lower, hiit, ...), how a
 * workout title or exercise name maps onto a type/category/intensity/equipment, and which
 * workout types/titles/descriptions suit each FitnessGoal.
 */
public final class ExerciseKnowledgeBase {

    private static final int DEFAULT_SETS = 3;
    private static final int DEFAULT_REPS = 12;
    private static final int DEFAULT_EX_DURATION = 10;
    private static final int MAX_EXERCISES_PER_WORKOUT = 3;

    private static final Map<String, String[]> WORKOUT_EXERCISE_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_INSTRUCTIONS_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_SEARCH_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_SUBCATEGORY_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_INTENSITY_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_EQUIPMENT_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_CATEGORY_MAP = new HashMap<>();

    private static final Map<FitnessGoal, String[]> GOAL_WORKOUT_TYPES = new HashMap<>();
    private static final Map<FitnessGoal, String[]> GOAL_WORKOUT_TITLES = new HashMap<>();
    private static final Map<FitnessGoal, String[]> GOAL_WORKOUT_DESCS = new HashMap<>();

    static {
        WORKOUT_EXERCISE_MAP.put("running", new String[]{"Running", "Sprints", "Jumping Jacks"});
        WORKOUT_INSTRUCTIONS_MAP.put("running", new String[]{
                "Jog or run at a steady pace with proper form. Keep your back straight and arms pumping.",
                "Sprint at maximum effort for 30 seconds, then walk back to recover. Repeat 8-10 times.",
                "Jump with arms and legs out, then back together. Keep a steady rhythm."
        });
        WORKOUT_SEARCH_MAP.put("running", new String[]{"running+form", "sprint+workout", "jumping+jacks"});
        WORKOUT_SUBCATEGORY_MAP.put("running", new String[]{"RUNNING", "RUNNING", "RUNNING"});
        WORKOUT_INTENSITY_MAP.put("running", new String[]{"HIGH", "HIGH", "MEDIUM"});
        WORKOUT_EQUIPMENT_MAP.put("running", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_CATEGORY_MAP.put("running", new String[]{"CARDIO", "CARDIO", "CARDIO"});

        WORKOUT_EXERCISE_MAP.put("biking", new String[]{"Stationary Bike", "Hill Climbs", "Sprint Intervals"});
        WORKOUT_INSTRUCTIONS_MAP.put("biking", new String[]{
                "Pedal at moderate to high intensity. Maintain a steady cadence of 80-100 RPM.",
                "Increase resistance and pedal standing up. Simulate climbing a steep hill.",
                "Sprint at max effort for 30 seconds, then pedal easy for 60 seconds. Repeat 10 times."
        });
        WORKOUT_SEARCH_MAP.put("biking", new String[]{
                "stationary+bike+workout", "hill+climbs+bike", "bike+sprint+intervals"
        });
        WORKOUT_SUBCATEGORY_MAP.put("biking", new String[]{"BIKING", "BIKING", "BIKING"});
        WORKOUT_INTENSITY_MAP.put("biking", new String[]{"MEDIUM", "HIGH", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("biking", new String[]{"STATIONARY_BIKE", "STATIONARY_BIKE", "STATIONARY_BIKE"});
        WORKOUT_CATEGORY_MAP.put("biking", new String[]{"CARDIO", "CARDIO", "CARDIO"});

        WORKOUT_EXERCISE_MAP.put("upper", new String[]{"Push-Ups", "Dumbbell Press", "Pull-Ups"});
        WORKOUT_INSTRUCTIONS_MAP.put("upper", new String[]{
                "Lower your chest to the floor, push up explosively. Keep your body in a straight line.",
                "Press dumbbells vertically over your chest with controlled cadence.",
                "Pull your chin above the bar, lower with control. Use a band if you can't do full pull-ups."
        });
        WORKOUT_SEARCH_MAP.put("upper", new String[]{"pushups", "dumbbell+press", "pull+ups"});
        WORKOUT_SUBCATEGORY_MAP.put("upper", new String[]{"UPPER_BODY", "UPPER_BODY", "UPPER_BODY"});
        WORKOUT_INTENSITY_MAP.put("upper", new String[]{"MEDIUM", "HIGH", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("upper", new String[]{"BODYWEIGHT", "DUMBBELLS", "PULL_UP_BAR"});
        WORKOUT_CATEGORY_MAP.put("upper", new String[]{"STRENGTH", "STRENGTH", "STRENGTH"});

        WORKOUT_EXERCISE_MAP.put("lower", new String[]{"Squats", "Lunges", "Goblet Squats"});
        WORKOUT_INSTRUCTIONS_MAP.put("lower", new String[]{
                "Keep your chest up, lower your hips back and down. Go to at least parallel.",
                "Step forward, lower your hips until both knees are bent at 90 degrees. Alternate legs.",
                "Hold weight at chest height, squat deeply while keeping back flat."
        });
        WORKOUT_SEARCH_MAP.put("lower", new String[]{"squats", "lunges", "goblet+squat"});
        WORKOUT_SUBCATEGORY_MAP.put("lower", new String[]{"LOWER_BODY", "LOWER_BODY", "LOWER_BODY"});
        WORKOUT_INTENSITY_MAP.put("lower", new String[]{"MEDIUM", "MEDIUM", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("lower", new String[]{"BODYWEIGHT", "BODYWEIGHT", "DUMBBELLS"});
        WORKOUT_CATEGORY_MAP.put("lower", new String[]{"STRENGTH", "STRENGTH", "STRENGTH"});

        WORKOUT_EXERCISE_MAP.put("leg", new String[]{"Squats", "Lunges", "Calf Raises"});
        WORKOUT_INSTRUCTIONS_MAP.put("leg", new String[]{
                "Keep your chest up, lower your hips back and down. Go to at least parallel.",
                "Step forward, lower your hips until both knees are bent at 90 degrees. Alternate legs.",
                "Rise up on your toes, hold for 2 seconds, lower slowly. Repeat 20-25 times."
        });
        WORKOUT_SEARCH_MAP.put("leg", new String[]{"squats", "lunges", "calf+raises"});
        WORKOUT_SUBCATEGORY_MAP.put("leg", new String[]{"LOWER_BODY", "LOWER_BODY", "LOWER_BODY"});
        WORKOUT_INTENSITY_MAP.put("leg", new String[]{"MEDIUM", "MEDIUM", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("leg", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_CATEGORY_MAP.put("leg", new String[]{"STRENGTH", "STRENGTH", "STRENGTH"});

        WORKOUT_EXERCISE_MAP.put("hiit", new String[]{"Burpees", "Mountain Climbers", "Jump Squats"});
        WORKOUT_INSTRUCTIONS_MAP.put("hiit", new String[]{
                "Drop to a squat, kick your feet back, do a push-up, jump up. That's one rep.",
                "In plank position, alternate driving your knees to your chest. Keep your hips low.",
                "Squat down, then explode up into a jump. Land softly and go right into the next rep."
        });
        WORKOUT_SEARCH_MAP.put("hiit", new String[]{"burpees", "mountain+climbers", "jump+squats"});
        WORKOUT_SUBCATEGORY_MAP.put("hiit", new String[]{"FULL_BODY", "FULL_BODY", "FULL_BODY"});
        WORKOUT_INTENSITY_MAP.put("hiit", new String[]{"HIGH", "HIGH", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("hiit", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_CATEGORY_MAP.put("hiit", new String[]{"HIIT", "HIIT", "HIIT"});

        WORKOUT_EXERCISE_MAP.put("core", new String[]{"Planks", "Bicycle Crunches", "Leg Raises"});
        WORKOUT_INSTRUCTIONS_MAP.put("core", new String[]{
                "Hold a straight body line on your forearms. Keep your core tight and don't let your hips sag.",
                "Alternate bringing opposite elbow to knee. Move slowly and control the movement.",
                "Lie flat on your back, raise your legs to 90 degrees, lower with control."
        });
        WORKOUT_SEARCH_MAP.put("core", new String[]{"plank", "bicycle+crunches", "leg+raises"});
        WORKOUT_SUBCATEGORY_MAP.put("core", new String[]{"CORE", "CORE", "CORE"});
        WORKOUT_INTENSITY_MAP.put("core", new String[]{"MEDIUM", "MEDIUM", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("core", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_CATEGORY_MAP.put("core", new String[]{"CORE", "CORE", "CORE"});

        WORKOUT_EXERCISE_MAP.put("strength", new String[]{"Push-Ups", "Squats", "Planks"});
        WORKOUT_INSTRUCTIONS_MAP.put("strength", new String[]{
                "Lower your chest to the floor, push up explosively. Keep your body in a straight line.",
                "Keep your chest up, lower your hips back and down. Go to at least parallel.",
                "Hold a straight body line on your forearms. Keep your core tight and don't let your hips sag."
        });
        WORKOUT_SEARCH_MAP.put("strength", new String[]{"pushups", "squats", "plank"});
        WORKOUT_SUBCATEGORY_MAP.put("strength", new String[]{"FULL_BODY", "FULL_BODY", "FULL_BODY"});
        WORKOUT_INTENSITY_MAP.put("strength", new String[]{"MEDIUM", "MEDIUM", "MEDIUM"});
        WORKOUT_EQUIPMENT_MAP.put("strength", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_CATEGORY_MAP.put("strength", new String[]{"STRENGTH", "STRENGTH", "STRENGTH"});

        WORKOUT_EXERCISE_MAP.put("yoga", new String[]{"Downward Dog", "Warrior Pose", "Tree Pose"});
        WORKOUT_INSTRUCTIONS_MAP.put("yoga", new String[]{
                "Start on all fours, lift your hips up into an inverted V. Stretch your back and legs.",
                "Step into a lunge position, extend your arms. Keep your front knee at 90 degrees.",
                "Balance on one foot, place the other foot on your inner thigh."
        });
        WORKOUT_SEARCH_MAP.put("yoga", new String[]{"downward+dog", "warrior+pose", "tree+pose"});
        WORKOUT_SUBCATEGORY_MAP.put("yoga", new String[]{"YOGA", "YOGA", "YOGA"});
        WORKOUT_INTENSITY_MAP.put("yoga", new String[]{"LOW", "LOW", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("yoga", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_CATEGORY_MAP.put("yoga", new String[]{"FLEXIBILITY", "FLEXIBILITY", "FLEXIBILITY"});

        WORKOUT_EXERCISE_MAP.put("flexibility", new String[]{"Downward Dog", "Warrior Pose", "Tree Pose"});
        WORKOUT_INSTRUCTIONS_MAP.put("flexibility", new String[]{
                "Start on all fours, lift your hips up into an inverted V. Stretch your back and legs.",
                "Step into a lunge position, extend your arms. Keep your front knee at 90 degrees.",
                "Balance on one foot, place the other foot on your inner thigh."
        });
        WORKOUT_SEARCH_MAP.put("flexibility", new String[]{"downward+dog", "warrior+pose", "tree+pose"});
        WORKOUT_SUBCATEGORY_MAP.put("flexibility", new String[]{"YOGA", "YOGA", "YOGA"});
        WORKOUT_INTENSITY_MAP.put("flexibility", new String[]{"LOW", "LOW", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("flexibility", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_CATEGORY_MAP.put("flexibility", new String[]{"FLEXIBILITY", "FLEXIBILITY", "FLEXIBILITY"});

        GOAL_WORKOUT_TYPES.put(FitnessGoal.LOSE_WEIGHT,
                new String[]{"hiit", "running", "hiit", "biking", "hiit", "running", "strength"});
        GOAL_WORKOUT_TITLES.put(FitnessGoal.LOSE_WEIGHT, new String[]{
                "HIIT Cardio Burn", "Cardio Running Interval", "Full Body Calorie Burner",
                "Biking Fat Burner", "High Intensity Circuit", "Endurance Run Session", "Full Body Sculpt"
        });
        GOAL_WORKOUT_DESCS.put(FitnessGoal.LOSE_WEIGHT, new String[]{
                "High intensity interval training designed to maximize calorie burn and elevate metabolism.",
                "Paced cardio running intervals focused on sustained endurance and fat oxidation.",
                "Full body high-energy workout targeting major muscle groups for calorie expenditure.",
                "Stationary biking interval session targeting lower body power and aerobic capacity.",
                "Circuit-style high intensity exercise block designed for minimal rest and max effort.",
                "Interval running session alternating sprints with active recovery periods.",
                "Full body resistance session aimed at lean muscle activation and metabolic rate support."
        });

        GOAL_WORKOUT_TYPES.put(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN,
                new String[]{"upper", "lower", "strength", "upper", "lower", "strength", "hiit"});
        GOAL_WORKOUT_TITLES.put(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, new String[]{
                "Upper Body Hypertrophy", "Lower Body Power & Strength", "Full Body Heavy Compound",
                "Upper Body Push/Pull Split", "Lower Body Quad & Hamstring Focus",
                "Full Body Progressive Strength", "Conditioning & Core"
        });
        GOAL_WORKOUT_DESCS.put(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, new String[]{
                "Targeted upper body volume session focusing on chest, back, and arm muscle development.",
                "Heavy lower body strength session building power across squats, lunges, and glute movements.",
                "Compound strength training designed for progressive overload and total body development.",
                "Upper body push and pull split maximizing muscular endurance and strength gains.",
                "Lower body focus targeting quads, hamstrings, and calves through targeted sets.",
                "Full body strength routine prioritizing resistance overload and form excellence.",
                "High effort metabolic conditioning routine supporting cardiovascular health and lean tissue."
        });

        GOAL_WORKOUT_TYPES.put(FitnessGoal.INCREASE_ENDURANCE,
                new String[]{"running", "biking", "hiit", "running", "biking", "hiit", "running"});
        GOAL_WORKOUT_TITLES.put(FitnessGoal.INCREASE_ENDURANCE, new String[]{
                "Long Distance Aerobic Run", "Cardio Endurance Cycling", "High Intensity Interval Cardio",
                "Lactate Threshold Run", "Hill Climb Cycle Focus", "Speed & Stamina Circuit",
                "Aerobic Capacity Distance Run"
        });
        GOAL_WORKOUT_DESCS.put(FitnessGoal.INCREASE_ENDURANCE, new String[]{
                "Sustained pace distance run aimed at improving aerobic capacity and stamina.",
                "Steady state endurance cycling session promoting cardiovascular efficiency.",
                "HIIT cardio interval session strengthening recovery speed and cardiac output.",
                "Tempo run structured to push lactate threshold and overall running efficiency.",
                "High-resistance cycling hill climbs strengthening leg stamina and lung output.",
                "Fast-paced interval circuit structured to build mental and physical endurance.",
                "Paced distance aerobic run focused on form control and breathing stamina."
        });

        GOAL_WORKOUT_TYPES.put(FitnessGoal.FLEXIBILITY_AND_MOBILITY,
                new String[]{"yoga", "flexibility", "yoga", "flexibility", "yoga", "flexibility", "yoga"});
        GOAL_WORKOUT_TITLES.put(FitnessGoal.FLEXIBILITY_AND_MOBILITY, new String[]{
                "Full Body Yoga Flow", "Deep Muscle Stretching & Mobility", "Core & Balance Yoga Flow",
                "Joint Mobility & Spine Lengthening", "Restorative Yoga & Recovery",
                "Dynamic Range of Motion Stretch", "Mindful Mobility Flow"
        });
        GOAL_WORKOUT_DESCS.put(FitnessGoal.FLEXIBILITY_AND_MOBILITY, new String[]{
                "Fluid full body yoga sequence targeting tight joints and core stability.",
                "Targeted stretching sequence aimed at widening range of motion and tissue flexibility.",
                "Balance-focused yoga flow building stabilization, core control, and breath alignment.",
                "Joint mobility routine working on hips, shoulders, and spinal decompression.",
                "Gentle restorative yoga focusing on deep breathing, relaxation, and hamstrings.",
                "Active dynamic stretching session preparing tendons and joint tissues for movement.",
                "Decompressing mobility flow designed to relieve tightness and posture strain."
        });

        GOAL_WORKOUT_TYPES.put(FitnessGoal.MAINTAIN_GENERAL_FITNESS,
                new String[]{"strength", "running", "upper", "biking", "hiit", "lower", "strength"});
        GOAL_WORKOUT_TITLES.put(FitnessGoal.MAINTAIN_GENERAL_FITNESS, new String[]{
                "Full Body Functional Strength", "Cardio Health Run", "Upper Body Conditioning",
                "Steady Cycling Session", "Full Body HIIT Refresh", "Lower Body Stability & Tone",
                "Total Fitness Compound Split"
        });
        GOAL_WORKOUT_DESCS.put(FitnessGoal.MAINTAIN_GENERAL_FITNESS, new String[]{
                "Balanced full body functional resistance workout for overall fitness and health.",
                "Moderate pace aerobic run boosting heart health, circulation, and energy.",
                "Upper body conditioning block maintaining upper core strength and posture.",
                "Steady cadence cycling session preserving cardiovascular stamina and leg endurance.",
                "Energizing high intensity interval routine supporting metabolism and movement quality.",
                "Lower body stability workout maintaining hip, knee, and ankle joint strength.",
                "Complete body strength session keeping foundational muscle groups active."
        });
    }

    private ExerciseKnowledgeBase() {
        // Utility class.
    }

    /**
     * Returns the workout types (in generation order) that suit a fitness goal.
     *
     * @param goal the user's fitness goal, possibly {@code null}
     * @param defaultTypes fallback used when {@code goal} is unmapped
     * @return the matching workout type keys
     */
    public static String[] getWorkoutTypesForGoal(final FitnessGoal goal, final String[] defaultTypes) {
        return GOAL_WORKOUT_TYPES.getOrDefault(goal, defaultTypes);
    }

    /**
     * Returns the workout titles (in generation order) that suit a fitness goal.
     *
     * @param goal the user's fitness goal, possibly {@code null}
     * @param defaultTitles fallback used when {@code goal} is unmapped
     * @return the matching workout titles
     */
    public static String[] getWorkoutTitlesForGoal(final FitnessGoal goal, final String[] defaultTitles) {
        return GOAL_WORKOUT_TITLES.getOrDefault(goal, defaultTitles);
    }

    /**
     * Returns the workout descriptions (in generation order) that suit a fitness goal.
     *
     * @param goal the user's fitness goal, possibly {@code null}
     * @param defaultDescriptions fallback used when {@code goal} is unmapped
     * @return the matching workout descriptions
     */
    public static String[] getWorkoutDescriptionsForGoal(final FitnessGoal goal, final String[] defaultDescriptions) {
        return GOAL_WORKOUT_DESCS.getOrDefault(goal, defaultDescriptions);
    }

    /**
     * Builds up to {@value #MAX_EXERCISES_PER_WORKOUT} exercises for a workout type (e.g.
     * {@code "running"}, {@code "upper"}, {@code "hiit"}), substituting a bodyweight-only
     * placeholder for any exercise whose equipment the user doesn't have, and falling back to a
     * generic Push-Ups/Squats/Planks trio for an unrecognized type.
     *
     * @param type the workout type key (see {@link #determineWorkoutType(String)})
     * @param user the user the plan is for, used to check equipment availability
     * @return up to {@value #MAX_EXERCISES_PER_WORKOUT} exercises for the type
     */
    public static List<Exercise> exercisesForType(final String type, final User user) {
        final List<Exercise> exercises = new ArrayList<>();

        if (WORKOUT_EXERCISE_MAP.containsKey(type)) {
            final String[] exerciseNames = WORKOUT_EXERCISE_MAP.get(type);
            final String[] instructions = WORKOUT_INSTRUCTIONS_MAP.get(type);
            final String[] searchQueries = WORKOUT_SEARCH_MAP.get(type);
            final String[] categories = WORKOUT_CATEGORY_MAP.get(type);
            final String[] subCategories = WORKOUT_SUBCATEGORY_MAP.get(type);
            final String[] intensities = WORKOUT_INTENSITY_MAP.get(type);
            final String[] equipmentTypes = WORKOUT_EQUIPMENT_MAP.get(type);

            for (int i = 0; i < Math.min(MAX_EXERCISES_PER_WORKOUT, exerciseNames.length); i++) {
                final String name = exerciseNames[i];
                final String inst;
                if (instructions != null && i < instructions.length) {
                    inst = instructions[i];
                }
                else {
                    inst = "Perform " + name + " with proper form.";
                }
                final String searchQuery;
                if (searchQueries != null && i < searchQueries.length) {
                    searchQuery = searchQueries[i];
                }
                else {
                    searchQuery = name.replace(" ", "+");
                }
                final String videoUrl = "https://www.youtube.com/results?search_query="
                        + searchQuery + "+exercise+tutorial";

                String equipmentType;
                if (equipmentTypes != null && i < equipmentTypes.length) {
                    equipmentType = equipmentTypes[i];
                }
                else {
                    equipmentType = "BODYWEIGHT";
                }

                if (!userHasEquipment(user, equipmentType)) {
                    equipmentType = "BODYWEIGHT";
                }

                final String category;
                if (categories != null && i < categories.length) {
                    category = categories[i];
                }
                else {
                    category = "GENERAL";
                }
                final String subCategory;
                if (subCategories != null && i < subCategories.length) {
                    subCategory = subCategories[i];
                }
                else {
                    subCategory = "GENERAL";
                }
                final String intensity;
                if (intensities != null && i < intensities.length) {
                    intensity = intensities[i];
                }
                else {
                    intensity = "MEDIUM";
                }

                exercises.add(new Exercise(name, DEFAULT_SETS, DEFAULT_REPS,
                        DEFAULT_EX_DURATION, "Various", equipmentType,
                        inst, videoUrl, category, subCategory, intensity, equipmentType));
            }
        }

        if (exercises.isEmpty()) {
            exercises.add(createExercise("Push-Ups"));
            exercises.add(createExercise("Squats"));
            exercises.add(createExercise("Planks"));
        }

        return Collections.unmodifiableList(exercises);
    }

    /**
     * Builds a single generic exercise by name, deriving its category, intensity, equipment,
     * and instructions purely from the name.
     *
     * @param name the exercise name
     * @return a fully-populated exercise
     */
    public static Exercise createExercise(final String name) {
        final String category = determineCategory(name);
        final String subCategory = determineSubCategory(name);
        final String intensity = determineIntensity(name);
        final String equipment = determineEquipment(name);
        final String instruction = getInstructionForExercise(name);
        final String videoUrl = "https://www.youtube.com/results?search_query="
                + name.replace(" ", "+") + "+exercise+tutorial";

        return new Exercise(name, DEFAULT_SETS, DEFAULT_REPS,
                DEFAULT_EX_DURATION, "Various", equipment,
                instruction, videoUrl, category, subCategory, intensity, equipment);
    }

    /**
     * Reports whether the user has (or doesn't need) a given equipment type.
     *
     * @param user the user to check, possibly {@code null}
     * @param equipmentType the required equipment type, possibly {@code null}
     * @return true if the requirement is bodyweight-only or the user's equipment matches it
     */
    public static boolean userHasEquipment(final User user, final String equipmentType) {
        boolean result;
        if (equipmentType == null || "BODYWEIGHT".equalsIgnoreCase(equipmentType)
                || "BODYWEIGHT_ONLY".equalsIgnoreCase(equipmentType)) {
            result = true;
        }
        else if (user == null || user.getEquipment() == null || user.getEquipment().isEmpty()) {
            result = false;
        }
        else {
            result = false;
            for (final Object eq : user.getEquipment()) {
                if (eq != null) {
                    final String eqStr = eq.toString().toUpperCase();
                    final String reqType = equipmentType.toUpperCase();
                    if (eqStr.contains(reqType) || reqType.contains(eqStr)) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Maps a lower-cased workout title to one of the workout type keys used by
     * {@link #exercisesForType(String, User)}.
     *
     * @param titleLower the workout title, already lower-cased
     * @return the matching workout type key
     */
    public static String determineWorkoutType(final String titleLower) {
        final String result;
        if (titleLower.contains("yoga") || titleLower.contains("stretch") || titleLower.contains("flex")) {
            result = "yoga";
        }
        else if (titleLower.contains("hiit") || titleLower.contains("interval")) {
            result = "hiit";
        }
        else if (titleLower.contains("cardio") || titleLower.contains("run") || titleLower.contains("running")) {
            result = "running";
        }
        else if (titleLower.contains("bike") || titleLower.contains("biking") || titleLower.contains("cycle")) {
            result = "biking";
        }
        else if (titleLower.contains("core") || titleLower.contains("abs")) {
            result = "core";
        }
        else if (titleLower.contains("upper") || titleLower.contains("push") || titleLower.contains("pull")) {
            result = "upper";
        }
        else if (titleLower.contains("lower") || titleLower.contains("leg") || titleLower.contains("squat")) {
            result = "lower";
        }
        else {
            result = "strength";
        }
        return result;
    }

    private static String determineCategory(final String exerciseName) {
        final String lower = exerciseName.toLowerCase();
        final String result;
        if (lower.contains("run") || lower.contains("sprint") || lower.contains("jog")
                || lower.contains("bike") || lower.contains("cycle") || lower.contains("pedal")) {
            result = "CARDIO";
        }
        else if (lower.contains("push") || lower.contains("pull") || lower.contains("press")
                || lower.contains("squat") || lower.contains("lunge") || lower.contains("deadlift")) {
            result = "STRENGTH";
        }
        else if (lower.contains("burpee") || lower.contains("mountain") || lower.contains("jump")) {
            result = "HIIT";
        }
        else if (lower.contains("plank") || lower.contains("crunch") || lower.contains("raise")) {
            result = "CORE";
        }
        else if (lower.contains("yoga") || lower.contains("downward") || lower.contains("warrior")
                || lower.contains("tree") || lower.contains("stretch")) {
            result = "FLEXIBILITY";
        }
        else {
            result = "GENERAL";
        }
        return result;
    }

    private static String determineSubCategory(final String exerciseName) {
        final String lower = exerciseName.toLowerCase();
        final String result;
        if (lower.contains("run") || lower.contains("sprint") || lower.contains("jog")) {
            result = "RUNNING";
        }
        else if (lower.contains("bike") || lower.contains("cycle") || lower.contains("pedal")) {
            result = "BIKING";
        }
        else if (lower.contains("push") || lower.contains("pull") || lower.contains("press")) {
            result = "UPPER_BODY";
        }
        else if (lower.contains("squat") || lower.contains("lunge") || lower.contains("deadlift")
                || lower.contains("calf")) {
            result = "LOWER_BODY";
        }
        else if (lower.contains("burpee") || lower.contains("mountain") || lower.contains("jump")) {
            result = "FULL_BODY";
        }
        else if (lower.contains("plank") || lower.contains("crunch") || lower.contains("raise")) {
            result = "CORE";
        }
        else if (lower.contains("yoga") || lower.contains("downward") || lower.contains("warrior")
                || lower.contains("tree")) {
            result = "YOGA";
        }
        else {
            result = "GENERAL";
        }
        return result;
    }

    private static String determineIntensity(final String exerciseName) {
        final String lower = exerciseName.toLowerCase();
        final String result;
        if (lower.contains("sprint") || lower.contains("burpee") || lower.contains("hiit")) {
            result = "HIGH";
        }
        else if (lower.contains("yoga") || lower.contains("stretch") || lower.contains("plank")) {
            result = "LOW";
        }
        else {
            result = "MEDIUM";
        }
        return result;
    }

    private static String determineEquipment(final String exerciseName) {
        final String lower = exerciseName.toLowerCase();
        final String result;
        if (lower.contains("bike") || lower.contains("cycle")) {
            result = "STATIONARY_BIKE";
        }
        else if (lower.contains("pull") || lower.contains("chin")) {
            result = "PULL_UP_BAR";
        }
        else if (lower.contains("press") && lower.contains("dumb")) {
            result = "DUMBBELLS";
        }
        else if (lower.contains("deadlift") || lower.contains("barbell")) {
            result = "BARBELL";
        }
        else {
            result = "BODYWEIGHT";
        }
        return result;
    }

    /**
     * Derives generic performance instructions for an exercise purely from its name, used when
     * neither the AI response nor a knowledge-base entry supplies instructions.
     *
     * @param name the exercise name
     * @return instructions for performing the exercise
     */
    public static String getInstructionForExercise(final String name) {
        final String lower = name.toLowerCase();
        final String result;
        if (lower.contains("push")) {
            result = "Lower your chest to the floor, push up explosively. Keep your body in a straight line.";
        }
        else if (lower.contains("squat")) {
            result = "Keep your chest up, lower your hips back and down. Go to at least parallel.";
        }
        else if (lower.contains("plank")) {
            result = "Hold a straight body line on your forearms. Keep your core tight.";
        }
        else if (lower.contains("lunge")) {
            result = "Step forward, lower your hips until both knees are bent at 90 degrees.";
        }
        else if (lower.contains("run") || lower.contains("jog")) {
            result = "Jog or run at a steady pace with proper form. Keep your back straight.";
        }
        else if (lower.contains("bike") || lower.contains("cycle")) {
            result = "Pedal at moderate to high intensity. Maintain a steady cadence.";
        }
        else if (lower.contains("burpee")) {
            result = "Drop to squat, kick feet back, do push-up, jump up. That's one rep.";
        }
        else if (lower.contains("yoga") || lower.contains("downward") || lower.contains("warrior")) {
            result = "Perform the yoga pose with proper form and controlled breathing.";
        }
        else {
            result = "Perform " + name + " with proper form. Maintain controlled movement throughout.";
        }
        return result;
    }
}


