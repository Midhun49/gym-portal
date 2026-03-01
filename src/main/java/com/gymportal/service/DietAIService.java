package com.gymportal.service;

import com.gymportal.entity.DietPlan;
import com.gymportal.entity.MemberProfile;
import com.gymportal.entity.User;
import com.gymportal.repository.DietPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DietAIService {

    @Autowired
    private DietPlanRepository dietPlanRepository;

    // ─────────────────── INDIAN FOOD DATABASES ───────────────────

    // VEGETARIAN
    private static final List<String> VEG_BREAKFAST = Arrays.asList(
            "Oats Upma with vegetables (1 cup) + 1 glass low-fat milk",
            "Poha (flattened rice) with peas & peanuts (1 bowl) + 1 banana",
            "Moong Dal Chilla (2 nos) + green chutney + 1 glass buttermilk",
            "Idli (3 nos) + Sambar (1 bowl) + coconut chutney",
            "Besan Cheela (2 nos) + 1 glass low-fat milk",
            "Methi Paratha (2 nos) + 1 cup low-fat curd",
            "Ragi Dosa (2 nos) + Tomato Chutney + 1 glass milk",
            "Sprouts Chaat (1 bowl) + 1 glass of green tea",
            "Dalia (broken wheat) Khichdi (1 bowl) + low-fat curd",
            "Vegetable Upma (1 cup) + 1 cup green tea + 1 apple",
            "Masala Oats (1 bowl) + 2 boiled eggs substitute: tofu scramble",
            "Aloo Paratha (1) + 1 cup low-fat curd + pickle",
            "Sabudana Khichdi (1 small bowl) + peanuts + coconut chutney",
            "Vermicelli Upma with vegetables (1 bowl) + 1 glass milk");

    private static final List<String> VEG_MORNING_SNACK = Arrays.asList(
            "1 apple + 10 almonds",
            "1 banana + 1 cup low-fat curd",
            "Roasted chana (1 handful) + 1 cup green tea",
            "Sprouts salad (1 small bowl)",
            "Makhana (foxnuts) roasted – 1 cup + green tea",
            "1 orange + 5 walnuts",
            "Peanut butter (1 tsp) on whole wheat bread (1 slice)",
            "Coconut water (1 glass) + 5 almonds",
            "1 cup low-fat buttermilk + roasted seeds mix",
            "1 small bowl of cut fruits (papaya + guava)");

    private static final List<String> VEG_LUNCH = Arrays.asList(
            "2 Whole wheat roti + Palak Paneer (1 cup) + Cucumber Raita",
            "Brown rice (1 cup) + Dal Tadka (1 cup) + Mixed veg sabji + Salad",
            "3 Chapati + Chana Masala (1 cup) + Onion salad + Curd",
            "Rajma (kidney beans) Chawal (1 cup rice + 1 cup rajma) + Salad",
            "2 Jowar Roti + Lauki (bottle gourd) Sabji + Dal + Buttermilk",
            "Vegetable Biryani (1 plate) + Cucumber Raita + Papad",
            "2 Bajra Roti + Mixed Dal + Tomato Onion Salad + Curd",
            "Sambar Rice (1 bowl) + Stir-fried vegetables + Buttermilk",
            "2 Multigrain Roti + Tofu Bhurji + Salad + Curd",
            "Brown rice Pulao with vegetables + Moong Dal + Raita",
            "3 Wheat Roti + Subji (Bhindi/Baingan) + Lentil soup + Salad",
            "Khichdi (Dal + Rice 1 bowl) + Ghee (1/2 tsp) + Papad + Salad");

    private static final List<String> VEG_EVENING_SNACK = Arrays.asList(
            "1 cup Masala Chai (low sugar) + 2 Marie biscuits",
            "Roasted Makhana (1 cup) + Green tea",
            "Fruit chaat (1 bowl) with lemon + chaat masala",
            "Murmura (puffed rice) chaat (1 cup) + 1 cup green tea",
            "Low-fat Paneer cubes (50g) + cucumber sticks",
            "Dhokla (2 pieces) + Green chutney",
            "Steamed corn kernels (1 cup) with butter + lemon",
            "Oat cookies (2 nos) + 1 cup low-fat milk",
            "Roasted chana with jaggery (1 small bowl)",
            "1 cup of low-fat curd with pomegranate seeds");

    private static final List<String> VEG_DINNER = Arrays.asList(
            "2 Wheat Roti + Palak Dal + Salad",
            "Vegetable Khichdi (1 bowl) + Curd + Papad",
            "2 Roti + Paneer Bhurji (1/2 cup) + Tomato soup",
            "Mixed Vegetable Soup (1 bowl) + 2 Multigrain Toast",
            "Dal Soup (1 bowl) + 2 Roti + Salad",
            "1 cup Brown Rice + Moong Dal (1 cup) + Sabji",
            "Dalia Khichdi (1 bowl) + Curd + Roasted Papad",
            "2 Bajra Roti + Methi Sabji + Lentil Dal",
            "Masoor Dal (1 cup) + 2 Wheat Roti + Cucumber Salad",
            "Vegetable Oats Soup + 2 Whole Grain Bread slices",
            "Paneer Tikka (2-3 pieces) + 2 Roti + Dal + Salad",
            "Sambar (1 bowl) + 2 Idli + Coconut Chutney");

    // NON-VEGETARIAN
    private static final List<String> NONVEG_BREAKFAST = Arrays.asList(
            "3 Egg Omelette (veggies) + 2 Whole Wheat Toast + 1 glass milk",
            "Boiled Eggs (2) + Poha (1 cup) + 1 banana",
            "Egg Bhurji (2 eggs) + 2 Wheat Paratha + Curd",
            "Idli (3 nos) + Chicken Sambar (1 bowl) + Chutney",
            "Scrambled Eggs (3) + Multigrain Toast + Green Tea",
            "Chicken Omelette Wrap (1) + 1 glass low-fat milk",
            "Moong Dal Chilla (2) + 2 Boiled Eggs + Green Chutney",
            "Egg Fried Rice (1 cup) + cucumber salad + 1 glass milk",
            "Besan Cheela (2) + Eggs (1 boiled) + 1 glass buttermilk",
            "Ragi Dosa (2) + Egg Curry (1 bowl) + Green Tea",
            "Oats porridge with eggs (1 cup cooked) + 1 glass milk",
            "Whole wheat bread (2 slices) + Egg whites (4) + 1 apple");

    private static final List<String> NONVEG_MORNING_SNACK = Arrays.asList(
            "Hard boiled eggs (2) + 1 apple",
            "Chicken tikka (grilled, 3-4 pieces) – no sauce",
            "Roasted chana (1 handful) + 1 cup green tea",
            "Tuna salad (½ tin) with cucumber sticks",
            "1 banana + 2 boiled egg whites",
            "Sprouts + boiled egg (1) salad",
            "Greek yogurt (1 cup) + 5 almonds + 1 orange",
            "Coconut water (1 glass) + boiled eggs (2)",
            "Grilled Chicken strip (100g) + lemon juice",
            "Low-fat curd (1 cup) + chia seeds + 1 fruit");

    private static final List<String> NONVEG_LUNCH = Arrays.asList(
            "2 Wheat Roti + Chicken Curry (100g) + Salad + Curd",
            "Brown Rice (1 cup) + Egg Curry (2 eggs) + Dal + Salad",
            "Chicken Biryani (1 plate) + Raita + Papad",
            "Grilled Fish (100g) + 2 Roti + Dal + Salad",
            "Mutton Keema (½ cup) + 2 Roti + Onion Salad",
            "Prawn Masala (100g) + 1 cup Brown Rice + Salad",
            "3 Roti + Egg Bhurji (3 eggs) + Dal + Curd",
            "Chicken Tikka Masala (100g) + Naan (1) + Salad",
            "Fish Curry (100g) + 1 cup Rice + Sambhar + Salad",
            "Egg Masala (2 eggs) + 2 Bajra Roti + Buttermilk",
            "Grilled Chicken (100g) + Vegetable Pulao (1 cup) + Raita",
            "Chicken Dal (100g chicken + 1 cup lentils) + 2 Roti + Salad");

    private static final List<String> NONVEG_EVENING_SNACK = Arrays.asList(
            "Grilled Chicken strips (80g) + cucumber slices",
            "2 Boiled Eggs + 1 cup Green Tea",
            "Egg White Omelette (3 whites) + 1 slice whole wheat bread",
            "Roasted Makhana (1 cup) + 1 cup low-fat milk",
            "Tuna on whole wheat crackers (2-3 nos)",
            "Dhokla (2 pieces) + 2 boiled egg whites",
            "Fruit chaat (1 bowl) + 2 boiled eggs",
            "Paneer or Chicken Tikka skewers (grilled, 2-3 nos)",
            "Murmura chaat (1 cup) + 1 glass buttermilk",
            "Roasted chana (1 small bowl) + 1 boiled egg");

    private static final List<String> NONVEG_DINNER = Arrays.asList(
            "2 Wheat Roti + Chicken Stew (100g) + Salad",
            "Grilled Fish (100g) + 2 Roti + Tomato-Onion Salad",
            "Egg Drop Soup (2 eggs) + 2 Multigrain Toast + Salad",
            "Chicken Soup (1 bowl) + 2 Whole Wheat Bread slices",
            "Fish Curry (100g) + 1 cup Brown Rice + Salad",
            "Scrambled Eggs (3) + Vegetable Sabji + 2 Roti",
            "Prawn Masala (100g) + 2 Roti + Cucumber Raita",
            "Chicken Dal (100g + 1 cup lentils) + 2 Roti + Salad",
            "Egg Omelette (2 eggs) + Vegetable Upma (1 cup) + Green Tea",
            "Grilled Chicken (100g) + Steamed Vegetables + Tomato Soup",
            "Mutton Curry (small portion 80g) + 2 Roti + Salad",
            "Tandoori Chicken (2 pieces) + Green Salad + 1 Roti");

    private static final List<String> POST_WORKOUT = Arrays.asList(
            "1 banana + 1 glass low-fat milk / protein shake",
            "Paneer (50g) or Boiled Eggs (2) + 1 banana",
            "1 glass buttermilk + 5 almonds + 1 fruit",
            "Brown Bread sandwich with Paneer/Chicken + 1 glass milk",
            "Dahi (curd) with banana (1 bowl)",
            "Roasted chana (1 cup) + 1 glass coconut water",
            "Sprouts Chaat (1 bowl) + 1 glass water",
            "Idli (2) + Sambar (1 cup) — great carb-protein combo",
            "Whey protein (if available) + 1 banana + water",
            "Mango lassi (low-fat, 1 glass) + handful of nuts");

    // ─────────────────── CORE AI METHOD ───────────────────

    public DietPlan generateDietPlan(MemberProfile profile, User user) {
        double heightM = profile.getHeightCm() / 100.0;
        double weight = profile.getWeightKg();
        int age = profile.getAge();

        // 1. BMI
        double bmi = weight / (heightM * heightM);
        bmi = Math.round(bmi * 10.0) / 10.0;

        // 2. BMR — Mifflin-St Jeor Equation
        double bmr;
        if (profile.getGender() == MemberProfile.Gender.MALE) {
            bmr = (10 * weight) + (6.25 * profile.getHeightCm()) - (5 * age) + 5;
        } else {
            bmr = (10 * weight) + (6.25 * profile.getHeightCm()) - (5 * age) - 161;
        }

        // 3. TDEE — Total Daily Energy Expenditure
        double activityMultiplier = switch (profile.getActivityLevel()) {
            case SEDENTARY -> 1.2;
            case LIGHT -> 1.375;
            case MODERATE -> 1.55;
            case ACTIVE -> 1.725;
            case VERY_ACTIVE -> 1.9;
        };
        double tdee = bmr * activityMultiplier;

        // 4. Calorie target based on goal
        int calorieTarget = switch (profile.getFitnessGoal()) {
            case LOSE_WEIGHT -> (int) (tdee - 500);
            case GAIN_MUSCLE -> (int) (tdee + 400);
            case MAINTAIN -> (int) tdee;
            case IMPROVE_ENDURANCE -> (int) (tdee + 100);
        };
        // Safety floor
        calorieTarget = Math.max(calorieTarget, profile.getGender() == MemberProfile.Gender.MALE ? 1400 : 1200);

        // 5. Macro calculation
        double proteinPct, carbsPct, fatsPct;
        switch (profile.getFitnessGoal()) {
            case LOSE_WEIGHT -> {
                proteinPct = 0.40;
                carbsPct = 0.30;
                fatsPct = 0.30;
            }
            case GAIN_MUSCLE -> {
                proteinPct = 0.30;
                carbsPct = 0.45;
                fatsPct = 0.25;
            }
            case IMPROVE_ENDURANCE -> {
                proteinPct = 0.25;
                carbsPct = 0.55;
                fatsPct = 0.20;
            }
            default -> {
                proteinPct = 0.30;
                carbsPct = 0.40;
                fatsPct = 0.30;
            }
        }

        int proteinG = (int) ((calorieTarget * proteinPct) / 4);
        int carbsG = (int) ((calorieTarget * carbsPct) / 4);
        int fatsG = (int) ((calorieTarget * fatsPct) / 9);

        // 6. Pick Indian meals
        boolean isVeg = profile.getDietType() == MemberProfile.DietType.VEGETARIAN;
        Random rand = new Random();

        List<String> bfList = isVeg ? VEG_BREAKFAST : NONVEG_BREAKFAST;
        List<String> mssnList = isVeg ? VEG_MORNING_SNACK : NONVEG_MORNING_SNACK;
        List<String> lunchList = isVeg ? VEG_LUNCH : NONVEG_LUNCH;
        List<String> esnList = isVeg ? VEG_EVENING_SNACK : NONVEG_EVENING_SNACK;
        List<String> dinnerList = isVeg ? VEG_DINNER : NONVEG_DINNER;

        String breakfast = bfList.get(rand.nextInt(bfList.size()));
        String morningSnack = mssnList.get(rand.nextInt(mssnList.size()));
        String lunch = lunchList.get(rand.nextInt(lunchList.size()));
        String eveningSnack = esnList.get(rand.nextInt(esnList.size()));
        String dinner = dinnerList.get(rand.nextInt(dinnerList.size()));
        String postWorkout = POST_WORKOUT.get(rand.nextInt(POST_WORKOUT.size()));

        // 7. Generate health notes
        String notes = buildDietNotes(bmi, profile.getFitnessGoal(), isVeg);

        // 8. Build and save plan
        DietPlan plan = new DietPlan();
        plan.setUser(user);
        plan.setBmi(bmi);
        plan.setBmr(Math.round(bmr * 10.0) / 10.0);
        plan.setTdee(Math.round(tdee * 10.0) / 10.0);
        plan.setCaloriesTarget(calorieTarget);
        plan.setProteinG(proteinG);
        plan.setCarbsG(carbsG);
        plan.setFatsG(fatsG);
        plan.setBreakfast(breakfast);
        plan.setMorningSnack(morningSnack);
        plan.setLunch(lunch);
        plan.setEveningSnack(eveningSnack);
        plan.setDinner(dinner);
        plan.setPostWorkout(postWorkout);
        plan.setDietNotes(notes);
        plan.setGeneratedAt(LocalDateTime.now());

        return dietPlanRepository.save(plan);
    }

    private String buildDietNotes(double bmi, MemberProfile.FitnessGoal goal, boolean isVeg) {
        StringBuilder sb = new StringBuilder();

        // BMI category note
        if (bmi < 18.5) {
            sb.append("⚠️ Your BMI is Underweight (").append(bmi)
                    .append("). Focus on calorie-dense Indian foods like dals, paneer, ghee, and dry fruits. ");
        } else if (bmi < 25.0) {
            sb.append("✅ Your BMI is Normal (").append(bmi)
                    .append("). Maintain your current habits with balanced Indian home-cooked meals. ");
        } else if (bmi < 30.0) {
            sb.append("⚠️ Your BMI is Overweight (").append(bmi)
                    .append("). Reduce fried foods, maida, and sugary chai. Prefer steamed, baked preparations. ");
        } else {
            sb.append("🚨 Your BMI is Obese (").append(bmi).append(
                    "). Strongly recommended: consult a doctor alongside this plan. Avoid ghee, full-fat dairy, and white rice. ");
        }

        // Goal-specific notes
        sb.append(switch (goal) {
            case LOSE_WEIGHT ->
                "💡 Weight Loss Tips: Eat 5 small meals instead of 3 large ones. Drink 8-10 glasses of water daily. Avoid packaged foods, biscuits, bhujia, and mithai. Prefer dal, sabji, salads. Replace white rice with brown rice or millets. ";
            case GAIN_MUSCLE ->
                "💡 Muscle Gain Tips: Eat within 30 minutes after workout. Prioritise protein-rich foods like dal, " +
                        (isVeg ? "paneer, soya chunks, rajma, chole. " : "eggs, chicken, fish, lentils. ") +
                        "Have 5-6 meals throughout the day. Include whole milk and dry fruits. ";
            case IMPROVE_ENDURANCE ->
                "💡 Endurance Tips: Focus on complex carbs — brown rice, oats, whole wheat roti, sweet potato. Eat a light meal 1.5 hours before training. Stay well hydrated with coconut water and buttermilk. ";
            case MAINTAIN ->
                "💡 Maintenance Tips: Follow 80/20 rule — eat healthy 80% of the time. Enjoy Indian sweets and fried foods occasionally but in moderation. Keep meal timings consistent. ";
        });

        // General Indian diet tips
        sb.append(
                "🥗 General: Include seasonal vegetables in every meal. Prefer homemade food. Use minimal oil — preferably mustard, coconut, or groundnut oil. Have a glass of warm water or green tea first thing in the morning.");

        return sb.toString();
    }

    public Optional<DietPlan> getLatestDietPlan(long userId) {
        return dietPlanRepository.findFirstByUserIdOrderByGeneratedAtDesc(userId);
    }

    public List<DietPlan> getAllDietPlans(long userId) {
        return dietPlanRepository.findByUserIdOrderByGeneratedAtDesc(userId);
    }
}
