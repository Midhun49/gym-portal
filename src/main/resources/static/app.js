// ──────────────────────────────────────────────────
//  FitForge — App.js (Frontend Logic)
// ──────────────────────────────────────────────────
const API = '';  // Spring Boot serves this on same origin

// ── STATE ──
let currentUser = null;
let weightChartInst = null;
let calorieChartInst = null;
let macroChartInst = null;
let dashChartInst = null;

// ── WORKOUT DATA ──
const WORKOUT_DATA = [
    {
        name: "Fat Burner Cardio",
        goal: "LOSE_WEIGHT", level: "BEGINNER",
        description: "Cardio-focused routine to burn fat quickly with low-impact exercises suitable for beginners.",
        schedule: "Mon: 30 min walk + abs\nWed: Cycling 30 min\nFri: Zumba / skipping 20 min\nSat: Yoga & stretching"
    },
    {
        name: "HIIT Shred Program",
        goal: "LOSE_WEIGHT", level: "INTERMEDIATE",
        description: "High-intensity interval training alternating between burpees, jump squats, and mountain climbers.",
        schedule: "Mon/Wed/Fri: 45 min HIIT\nTue/Thu: Active recovery, brisk walk\nSat: Core + flexibility"
    },
    {
        name: "Advanced Fat Torch",
        goal: "LOSE_WEIGHT", level: "ADVANCED",
        description: "Elite weight training + cardio combo with superset circuits to maximize calorie burn and retain muscle.",
        schedule: "Mon: Upper body + HIIT\nTue: Lower body + sprint intervals\nWed: Rest or yoga\nThu: Full body circuit\nFri: Cardio + abs\nSat: Sports/recreational activity"
    },
    {
        name: "Beginner Strength Build",
        goal: "GAIN_MUSCLE", level: "BEGINNER",
        description: "Learn fundamental compound lifts: squats, deadlifts, bench press, rows. Perfect form first.",
        schedule: "Mon: Chest + Triceps\nWed: Back + Biceps\nFri: Legs + Shoulders\n(Progressively add weight each week)"
    },
    {
        name: "Hypertrophy Split",
        goal: "GAIN_MUSCLE", level: "INTERMEDIATE",
        description: "5-day push/pull/legs split designed for maximum muscle hypertrophy with progressive overload.",
        schedule: "Mon: Push (Chest/Shoulder/Triceps)\nTue: Pull (Back/Biceps)\nWed: Legs\nThu: Push\nFri: Pull\nSat: Legs / Arms"
    },
    {
        name: "Elite Powerbuilding",
        goal: "GAIN_MUSCLE", level: "ADVANCED",
        description: "Mix of powerlifting (low rep, heavy) and bodybuilding (moderate rep) for strength and size.",
        schedule: "Mon: Squat focus\nTue: Bench press + accessories\nWed: Rest / light yoga\nThu: Deadlift focus\nFri: OHP + upper body\nSat: Weak point training"
    },
    {
        name: "Active Maintenance",
        goal: "MAINTAIN", level: "BEGINNER",
        description: "Keep your fitness level through enjoyable activities — walks, swimming, and light gym sessions.",
        schedule: "Mon/Wed/Fri: 30–40 min gym (full body)\nTue/Thu: Outdoor walk or swim\nWeekend: Recreational sport"
    },
    {
        name: "Functional Fitness",
        goal: "MAINTAIN", level: "INTERMEDIATE",
        description: "Functional movements that improve real-life strength — kettlebells, TRX, mobility work.",
        schedule: "Mon: Kettlebell full body\nWed: TRX + core\nFri: Dumbbell compound lifts\nSun: Mobility + stretching"
    },
    {
        name: "5K to Marathon Training",
        goal: "IMPROVE_ENDURANCE", level: "BEGINNER",
        description: "Structured running program to build cardiovascular base from 5K to longer distances.",
        schedule: "Mon: 20 min easy run\nWed: 30 min moderate run\nFri: 25 min easy\nSat: Long run (45–60 min, slow pace)"
    },
    {
        name: "Athlete Conditioning",
        goal: "IMPROVE_ENDURANCE", level: "INTERMEDIATE",
        description: "Mix of interval runs, cycling, and swimming to build all-round cardiovascular endurance.",
        schedule: "Mon: Interval run (400m x 8)\nTue: Cycling 45 min\nWed: Rest\nThu: Long run 8-10 km\nFri: Swimming 1000m\nSat: Circuit training"
    },
    {
        name: "Triathlon Prep",
        goal: "IMPROVE_ENDURANCE", level: "ADVANCED",
        description: "Swim-bike-run structured training block for competitive endurance athletes.",
        schedule: "Mon: Swim 2km\nTue: Cycling 30km\nWed: Run 12km\nThu: Brick (bike 20km + run 5km)\nFri: Rest\nSat: Long ride 50km\nSun: Long run 18km"
    }
];

const HEALTH_TIPS = [
    "Drink warm lemon water every morning before breakfast",
    "Walk 10,000 steps daily — take the stairs!",
    "Sleep 7–8 hours — crucial for muscle recovery",
    "Eat mindfully — chew slowly, no screen time while eating",
    "Cut down on maida (refined flour) — switch to atta or multigrain",
    "Include a handful of dry fruits daily (almonds, walnuts)",
    "Avoid sodas and packaged juices — drink nimbu pani instead",
    "Eat dal every day — excellent plant protein source",
    "Meditate 10 minutes daily to manage cortisol levels",
    "Batch cook rotis & sabji over the weekend to eat healthy on weekdays"
];

const WORKOUT_TIPS_BY_GOAL = {
    LOSE_WEIGHT: "💨 Tip: Start each session with 10 mins of brisk walking to warm up. Focus on compound movements — they burn more calories than isolation exercises.",
    GAIN_MUSCLE: "💪 Tip: Progressive overload is key. Add 2.5–5 kg every week. Make sure to consume protein within 30 minutes of your workout.",
    MAINTAIN: "⚖️ Tip: Consistency over intensity. Even 3 sessions a week will maintain your fitness. Mix cardio and strength for best results.",
    IMPROVE_ENDURANCE: "🏃 Tip: Train at 60–70% of your max heart rate for endurance. Never skip your cooldown — it helps flush lactic acid."
};

// ─────────── Init ───────────
window.addEventListener('load', () => {
    setTimeout(() => {
        document.getElementById('loader').style.opacity = '0';
        setTimeout(() => {
            document.getElementById('loader').classList.add('hidden');
            const saved = localStorage.getItem('gymUser');
            if (saved) {
                currentUser = JSON.parse(saved);
                startApp();
            } else {
                document.getElementById('auth-screen').classList.remove('hidden');
            }
        }, 400);
    }, 1600);
});

// ─────────── AUTH ───────────
function switchTab(tab) {
    document.getElementById('login-form').classList.toggle('hidden', tab !== 'login');
    document.getElementById('register-form').classList.toggle('hidden', tab !== 'register');
    document.getElementById('tab-login').classList.toggle('active', tab === 'login');
    document.getElementById('tab-register').classList.toggle('active', tab === 'register');

    // Reset forms and messages on switch
    document.getElementById('login-form').reset();
    document.getElementById('register-form').reset();
    document.getElementById('auth-msg').textContent = '';
    document.getElementById('auth-msg').className = 'auth-msg';
}

async function handleLogin(e) {
    e.preventDefault();
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    const msg = document.getElementById('auth-msg');
    try {
        const res = await fetch(`${API}/api/auth/login`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        const data = await res.json();
        if (data.success) {
            currentUser = data;
            localStorage.setItem('gymUser', JSON.stringify(data));
            document.getElementById('auth-screen').classList.add('hidden');
            startApp();
        } else {
            showMsg(msg, data.message, 'error');
        }
    } catch (err) {
        showMsg(msg, 'Cannot connect to server. Is Spring Boot running?', 'error');
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const username = document.getElementById('reg-username').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    const msg = document.getElementById('auth-msg');

    // Enforce Gmail-only registration
    if (!email.toLowerCase().endsWith('@gmail.com')) {
        showMsg(msg, '❌ Only Gmail addresses (@gmail.com) are accepted. Please use a Gmail account.', 'error');
        return;
    }

    try {
        const res = await fetch(`${API}/api/auth/register`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });
        const data = await res.json();
        if (data.success) {
            showMsg(msg, '✅ Registered! Please login.', 'success');
            switchTab('login');
        } else {
            showMsg(msg, data.message, 'error');
        }
    } catch (err) {
        showMsg(msg, 'Cannot connect to server. Is Spring Boot running?', 'error');
    }
}

function logout() {
    localStorage.removeItem('gymUser');
    currentUser = null;
    // Hard refresh ensure a completely clean state for the next user
    window.location.reload();
}

// ─────────── APP START ───────────
function startApp() {
    const app = document.getElementById('app');
    app.classList.remove('hidden');

    document.getElementById('sidebar-username').textContent = currentUser.username;
    document.getElementById('sidebar-role').textContent = currentUser.role;

    if (currentUser.role === 'ADMIN') {
        document.querySelectorAll('.admin-only').forEach(el => el.classList.remove('hidden'));
    }

    setupWorkouts();
    navigate('dashboard');
}

// ─────────── NAVIGATION ───────────
function navigate(page) {
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));

    const target = document.getElementById(`page-${page}`);
    if (target) target.classList.add('active');

    const navItem = document.querySelector(`[data-page="${page}"]`);
    if (navItem) navItem.classList.add('active');

    // Close sidebar on mobile
    document.getElementById('sidebar').classList.remove('open');

    // Page-specific logic
    if (page === 'dashboard') loadDashboard();
    if (page === 'profile') loadProfile();
    if (page === 'diet') loadLatestDiet();
    if (page === 'membership') loadMembership();
    if (page === 'progress') loadProgress();
    if (page === 'admin') loadAdmin();
}

function toggleSidebar() {
    document.getElementById('sidebar').classList.toggle('open');
}

// ─────────── DASHBOARD ───────────
async function loadDashboard() {
    const hour = new Date().getHours();
    const greet = hour < 12 ? 'Good morning' : hour < 17 ? 'Good afternoon' : 'Good evening';
    document.getElementById('greeting').textContent = `${greet}, ${currentUser.username}! 💪`;

    // Role-based view toggle
    const isAdmin = currentUser.role === 'ADMIN';
    document.getElementById('member-dashboard-view').classList.toggle('hidden', isAdmin);
    document.getElementById('admin-dashboard-view').classList.toggle('hidden', !isAdmin);

    if (isAdmin) {
        refreshAdminData();
        return;
    }

    // Load diet plan for dashboard (Members only)
    try {
        const res = await fetch(`${API}/api/diet/latest/${currentUser.userId}`);
        const data = await res.json();
        if (data.success) {
            document.getElementById('dash-bmi').textContent = data.bmi;
            document.getElementById('dash-bmi-cat').textContent = bmiCategory(data.bmi);
            document.getElementById('dash-calories').textContent = `${data.targetCalories} kcal`;

            // Meals highlight
            const meals = data.meals.slice(0, 2); // Show first 2 meals
            document.getElementById('dash-meals').innerHTML = meals.map(m => `
                <p><strong>${m.time}:</strong> ${m.content}</p>
            `).join('');
        }
    } catch (e) { }

    // Load profile for goal (Members only)
    try {
        const profRes = await fetch(`${API}/api/profile/${currentUser.userId}`);
        const profData = await profRes.json();
        if (profData.success && profData.fitnessGoal) {
            const goalLabel = profData.fitnessGoal.replace('_', ' ');
            document.getElementById('dash-goal').textContent = goalLabel;

            // Random workout tip based on goal
            const tipKeys = Object.keys(WORKOUT_TIPS_BY_GOAL);
            const randomTip = WORKOUT_TIPS_BY_GOAL[profData.fitnessGoal] || WORKOUT_TIPS_BY_GOAL[tipKeys[Math.floor(Math.random() * tipKeys.length)]];
            document.getElementById('dash-workout-tip').textContent = randomTip;
        } else {
            document.getElementById('dash-goal').textContent = 'NOT SET';
            document.getElementById('dash-workout-tip').textContent = 'Set your fitness goal in Profile to see tips!';
        }
    } catch (e) { }

    // Load membership for dashboard (Members only)
    try {
        const memRes = await fetch(`${API}/api/membership/${currentUser.userId}`);
        const memData = await memRes.json();
        if (memData.success) {
            document.getElementById('dash-plan').textContent = memData.plan;
            document.getElementById('dash-plan-status').textContent = memData.status;
        } else {
            document.getElementById('dash-plan').textContent = 'NO PLAN';
            document.getElementById('dash-plan-status').textContent = 'INACTIVE';
        }
    } catch (e) { }

    // Health tips
    const tips = [
        "Drink at least 3-4 liters of water daily.",
        "Ensure 7-8 hours of quality sleep for muscle recovery.",
        "Focus on protein-rich meals after your workouts.",
        "Consistency is key! Don't skip your scheduled gym days."
    ];
    document.getElementById('health-tips').innerHTML = tips.map(t => `<li>${t}</li>`).join('');

    // Random workout tip
    const tipKeys = Object.keys(WORKOUT_TIPS_BY_GOAL); // Assuming WORKOUT_TIPS_BY_GOAL is intended here
    const randomTip = WORKOUT_TIPS_BY_GOAL[tipKeys[Math.floor(Math.random() * tipKeys.length)]];
    document.getElementById('dash-workout-tip').textContent = randomTip;
}

function loadDashChart(targetCal) {
    const ctx = document.getElementById('dash-chart').getContext('2d');
    if (dashChartInst) dashChartInst.destroy();
    const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
    const data = days.map(() => Math.round(targetCal * (0.85 + Math.random() * 0.3)));
    dashChartInst = new Chart(ctx, {
        type: 'line',
        data: {
            labels: days,
            datasets: [{
                label: 'Calorie Trend',
                data, borderColor: '#f59e0b',
                backgroundColor: 'rgba(245,158,11,0.1)',
                tension: 0.4, fill: true, pointRadius: 3, pointBackgroundColor: '#f59e0b'
            }]
        },
        options: {
            responsive: true, plugins: { legend: { display: false } },
            scales: {
                x: { ticks: { color: '#64748b' }, grid: { color: 'rgba(255,255,255,0.04)' } },
                y: { ticks: { color: '#64748b' }, grid: { color: 'rgba(255,255,255,0.04)' }, beginAtZero: false }
            }
        }
    });
}

// ─────────── PROFILE ───────────
async function loadProfile() {
    try {
        const res = await fetch(`${API}/api/profile/${currentUser.userId}`);
        const data = await res.json();
        if (data.success) {
            document.getElementById('pf-name').value = data.fullName || '';
            document.getElementById('pf-age').value = data.age || '';
            document.getElementById('pf-gender').value = data.gender || '';
            document.getElementById('pf-height').value = data.heightCm || '';
            document.getElementById('pf-weight').value = data.weightKg || '';
            document.getElementById('pf-phone').value = data.phoneNumber || '';
            document.getElementById('pf-goal').value = data.fitnessGoal || '';
            document.getElementById('pf-activity').value = data.activityLevel || '';
            document.getElementById('pf-diet').value = data.dietType || 'VEGETARIAN';
            document.getElementById('pf-address').value = data.address || '';
        }
    } catch (e) { }
}

async function handleProfileSave(e) {
    e.preventDefault();
    const payload = {
        fullName: document.getElementById('pf-name').value,
        age: parseInt(document.getElementById('pf-age').value),
        gender: document.getElementById('pf-gender').value,
        heightCm: parseFloat(document.getElementById('pf-height').value),
        weightKg: parseFloat(document.getElementById('pf-weight').value),
        phoneNumber: document.getElementById('pf-phone').value,
        fitnessGoal: document.getElementById('pf-goal').value,
        activityLevel: document.getElementById('pf-activity').value,
        dietType: document.getElementById('pf-diet').value,
        address: document.getElementById('pf-address').value
    };
    const msg = document.getElementById('profile-msg');
    try {
        const res = await fetch(`${API}/api/profile/${currentUser.userId}`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        if (data.success) {
            showMsg(msg, '✅ Profile saved!', 'success');
            showToast('Profile saved successfully!', 'success');
        } else {
            showMsg(msg, data.message, 'error');
        }
    } catch (e) {
        showMsg(msg, 'Server error. Make sure the backend is running.', 'error');
    }
}

// ─────────── DIET PLAN ───────────
async function loadLatestDiet() {
    try {
        const profRes = await fetch(`${API}/api/profile/${currentUser.userId}`);
        const profData = await profRes.json();
        if (profData.success && profData.dietType) {
            const badge = document.getElementById('diet-type-badge');
            badge.textContent = profData.dietType === 'VEGETARIAN' ? '🌿 Vegetarian Plan' : '🍗 Non-Vegetarian Plan';
            badge.style.cssText = profData.dietType === 'VEGETARIAN'
                ? 'background:rgba(16,185,129,0.15);color:#10b981;border:1px solid rgba(16,185,129,0.3)'
                : 'background:rgba(239,68,68,0.15);color:#ef4444;border:1px solid rgba(239,68,68,0.3)';
        }

        const res = await fetch(`${API}/api/diet/latest/${currentUser.userId}`);
        const data = await res.json();
        if (data.success) renderDietPlan(data);
    } catch (e) { }
}

async function generateDietPlan() {
    const btn = document.getElementById('gen-diet-btn');
    const loading = document.getElementById('diet-loading');
    const container = document.getElementById('diet-plan-container');

    btn.disabled = true; btn.textContent = '⏳ Generating...';
    container.classList.add('hidden');
    loading.classList.remove('hidden');

    try {
        const res = await fetch(`${API}/api/diet/generate/${currentUser.userId}`, { method: 'POST' });
        const data = await res.json();
        loading.classList.add('hidden');
        btn.disabled = false; btn.textContent = '✨ Regenerate AI Diet Plan';

        if (data.success) {
            renderDietPlan(data);
            showToast('AI Diet Plan generated! 🥗', 'success');

            // Update diet type badge post-generation
            const profRes = await fetch(`${API}/api/profile/${currentUser.userId}`);
            const profData = await profRes.json();
            if (profData.success && profData.dietType) {
                const badge = document.getElementById('diet-type-badge');
                badge.textContent = profData.dietType === 'VEGETARIAN' ? '🌿 Vegetarian Plan' : '🍗 Non-Vegetarian Plan';
                badge.style.cssText = profData.dietType === 'VEGETARIAN'
                    ? 'background:rgba(16,185,129,0.15);color:#10b981;border:1px solid rgba(16,185,129,0.3)'
                    : 'background:rgba(239,68,68,0.15);color:#ef4444;border:1px solid rgba(239,68,68,0.3)';
            }
        } else {
            showToast(data.message || 'Error generating plan. Complete your profile first.', 'error');
        }
    } catch (e) {
        loading.classList.add('hidden');
        btn.disabled = false; btn.textContent = '✨ Generate My AI Diet Plan';
        showToast('Server error. Make sure Spring Boot is running.', 'error');
    }
}

function renderDietPlan(data) {
    document.getElementById('diet-plan-container').classList.remove('hidden');

    document.getElementById('dp-bmi').textContent = `${data.bmi} (${bmiCategory(data.bmi)})`;
    document.getElementById('dp-bmr').textContent = Math.round(data.bmr);
    document.getElementById('dp-tdee').textContent = Math.round(data.tdee);
    document.getElementById('dp-target').textContent = data.caloriesTarget;

    document.getElementById('dp-protein').textContent = `${data.proteinG}g`;
    document.getElementById('dp-carbs').textContent = `${data.carbsG}g`;
    document.getElementById('dp-fats').textContent = `${data.fatsG}g`;

    // Macro bars — calculate percentages
    const total = (data.proteinG * 4) + (data.carbsG * 4) + (data.fatsG * 9);
    const pPct = Math.round((data.proteinG * 4 / total) * 100);
    const cPct = Math.round((data.carbsG * 4 / total) * 100);
    const fPct = Math.round((data.fatsG * 9 / total) * 100);

    setTimeout(() => {
        document.getElementById('dp-protein-bar').style.width = pPct + '%';
        document.getElementById('dp-carbs-bar').style.width = cPct + '%';
        document.getElementById('dp-fats-bar').style.width = fPct + '%';
    }, 100);

    // Macro doughnut chart
    const mctx = document.getElementById('macro-chart').getContext('2d');
    if (macroChartInst) macroChartInst.destroy();
    macroChartInst = new Chart(mctx, {
        type: 'doughnut',
        data: {
            labels: ['Protein', 'Carbs', 'Fats'],
            datasets: [{
                data: [data.proteinG * 4, data.carbsG * 4, data.fatsG * 9],
                backgroundColor: ['#3b82f6', '#f59e0b', '#10b981'],
                borderWidth: 0, hoverOffset: 8
            }]
        },
        options: {
            cutout: '70%', responsive: true,
            plugins: { legend: { labels: { color: '#94a3b8', font: { size: 11 } } } }
        }
    });

    document.getElementById('dp-breakfast').textContent = data.breakfast;
    document.getElementById('dp-morning-snack').textContent = data.morningSnack;
    document.getElementById('dp-lunch').textContent = data.lunch;
    document.getElementById('dp-evening-snack').textContent = data.eveningSnack;
    document.getElementById('dp-dinner').textContent = data.dinner;
    document.getElementById('dp-post-workout').textContent = data.postWorkout;
    document.getElementById('dp-notes').textContent = data.dietNotes;
}

// ─────────── WORKOUTS ───────────
function setupWorkouts() {
    filterWorkout('all', null);
}

function filterWorkout(goalFilter, btn) {
    if (btn) {
        document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
    }

    const filtered = goalFilter === 'all'
        ? WORKOUT_DATA
        : WORKOUT_DATA.filter(w => w.goal === goalFilter);

    const grid = document.getElementById('workout-grid');
    grid.innerHTML = filtered.map(w => `
        <div class="workout-card">
            <div class="workout-header">
                <div class="workout-name">${w.name}</div>
                <span class="level-badge level-${w.level}">${w.level}</span>
            </div>
            <div class="workout-goal">${goalLabel(w.goal)}</div>
            <div class="workout-desc">${w.description}</div>
            <div class="workout-schedule"><strong>📅 Schedule:</strong><br>${w.schedule.replace(/\n/g, '<br>')}</div>
        </div>
    `).join('');
}

function goalLabel(g) {
    return { LOSE_WEIGHT: 'Weight Loss', GAIN_MUSCLE: 'Muscle Gain', MAINTAIN: 'Maintenance', IMPROVE_ENDURANCE: 'Endurance' }[g] || g;
}

// ─────────── MEMBERSHIP ───────────
async function loadMembership() {
    try {
        const res = await fetch(`${API}/api/membership/${currentUser.userId}`);
        const data = await res.json();
        if (data.success) {
            document.getElementById('mem-plan-badge').textContent = data.plan;
            document.getElementById('mem-end-date').textContent = data.endDate;
            document.getElementById('mem-status').textContent = data.status;
            document.getElementById('mem-price').textContent = `₹${data.amountPaid}`;

            // Highlight active plan
            document.querySelectorAll('.plan-card').forEach(c => c.style.boxShadow = '');
            const active = document.getElementById(`plan-${data.plan}`);
            if (active) active.style.boxShadow = '0 0 0 2px #f59e0b';
        } else {
            document.getElementById('mem-plan-badge').textContent = 'NO ACTIVE PLAN';
            document.getElementById('mem-end-date').textContent = '—';
            document.getElementById('mem-status').textContent = 'INACTIVE';
            document.getElementById('mem-price').textContent = '₹0';
            document.querySelectorAll('.plan-card').forEach(c => c.style.boxShadow = '');
        }
    } catch (e) { }
}

async function upgradePlan(plan) {
    const msg = document.getElementById('membership-msg');
    try {
        const res = await fetch(`${API}/api/membership/${currentUser.userId}/upgrade`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ plan })
        });
        const data = await res.json();
        if (data.success) {
            showMsg(msg, `✅ ${data.message}`, 'success');
            showToast(data.message, 'success');
            loadMembership();
        } else {
            showMsg(msg, data.message, 'error');
        }
    } catch (e) {
        showMsg(msg, 'Server error.', 'error');
    }
}

// ─────────── PROGRESS ───────────
async function loadProgress() {
    try {
        const res = await fetch(`${API}/api/progress/${currentUser.userId}`);
        const entries = await res.json();
        renderProgressCharts(entries);
    } catch (e) { }
}

async function handleProgressLog(e) {
    e.preventDefault();
    const payload = {
        weightKg: document.getElementById('pr-weight').value,
        caloriesConsumed: document.getElementById('pr-calories').value,
        waterIntakeMl: document.getElementById('pr-water').value,
        notes: document.getElementById('pr-notes').value
    };
    const msg = document.getElementById('progress-msg');
    try {
        const res = await fetch(`${API}/api/progress/${currentUser.userId}`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        if (data.success) {
            showMsg(msg, '✅ Progress logged!', 'success');
            showToast('Progress logged! 📈', 'success');
            document.getElementById('progress-form').reset();
            loadProgress();
        } else {
            showMsg(msg, data.message, 'error');
        }
    } catch (e) {
        showMsg(msg, 'Server error.', 'error');
    }
}

function renderProgressCharts(entries) {
    const labels = entries.map(e => e.loggedDate);
    const weights = entries.map(e => e.weightKg);
    const cals = entries.map(e => e.caloriesConsumed);

    const chartOpts = (color) => ({
        responsive: true,
        plugins: { legend: { display: false } },
        scales: {
            x: { ticks: { color: '#64748b', maxRotation: 45 }, grid: { color: 'rgba(255,255,255,0.04)' } },
            y: { ticks: { color: '#64748b' }, grid: { color: 'rgba(255,255,255,0.04)' }, beginAtZero: false }
        }
    });

    const wCtx = document.getElementById('weight-chart').getContext('2d');
    if (weightChartInst) weightChartInst.destroy();
    weightChartInst = new Chart(wCtx, {
        type: 'line',
        data: {
            labels,
            datasets: [{
                data: weights, label: 'Weight (kg)',
                borderColor: '#3b82f6', backgroundColor: 'rgba(59,130,246,0.1)',
                tension: 0.4, fill: true, pointRadius: 4, pointBackgroundColor: '#3b82f6'
            }]
        },
        options: chartOpts('#3b82f6')
    });

    const cCtx = document.getElementById('calorie-chart').getContext('2d');
    if (calorieChartInst) calorieChartInst.destroy();
    calorieChartInst = new Chart(cCtx, {
        type: 'bar',
        data: {
            labels,
            datasets: [{
                data: cals, label: 'Calories',
                backgroundColor: 'rgba(245,158,11,0.6)',
                borderColor: '#f59e0b', borderWidth: 1, borderRadius: 6
            }]
        },
        options: chartOpts('#f59e0b')
    });
}

// ─────────── ADMIN ───────────
async function refreshAdminData() {
    try {
        // Fetch stats
        const statsRes = await fetch(`${API}/api/admin/stats`);
        const stats = await statsRes.json();
        if (stats.success) {
            // Update admin panel page
            document.getElementById('admin-total').textContent = stats.totalMembers;
            document.getElementById('admin-active').textContent = stats.activeToday;
            document.getElementById('admin-revenue').textContent = `₹${stats.revenue.toLocaleString('en-IN')}`;

            // Update dashboard admin view if exists
            const dTotal = document.getElementById('dash-admin-total');
            if (dTotal) {
                dTotal.textContent = stats.totalMembers;
                document.getElementById('dash-admin-active').textContent = stats.activeToday;
                document.getElementById('dash-admin-revenue').textContent = `₹${stats.revenue.toLocaleString('en-IN')}`;
            }
        }

        // Fetch members list
        const memRes = await fetch(`${API}/api/admin/members`);
        const memData = await memRes.json();
        if (memData.success) {
            const planBadgeClass = { BASIC: 'plan-pill-basic', STANDARD: 'plan-pill-standard', PREMIUM: 'plan-pill-premium' };

            // Update main admin table
            const tbody = document.getElementById('members-tbody');
            if (tbody) {
                tbody.innerHTML = memData.members.map((m, i) => `
                    <tr>
                        <td>${i + 1}</td>
                        <td><strong>${m.username}</strong></td>
                        <td>${m.email}</td>
                        <td>${m.createdAt.split('T')[0]}</td>
                        <td><span class="plan-pill ${planBadgeClass[m.plan] || ''}">${m.plan}</span></td>
                        <td>
                            <button class="btn-view" onclick="viewMemberDetails(${m.id})">
                                👁️ View
                            </button>
                            <button class="btn btn-danger" onclick="deleteMember(${m.id}, '${m.username}')">
                                🗑 Remove
                            </button>
                        </td>
                    </tr>
                `).join('');
            }
        }
    } catch (e) { console.error('Admin data refresh failed', e); }
}

async function loadAdmin() {
    if (currentUser.role !== 'ADMIN') return;
    refreshAdminData();
}

async function handleAdminProfileUpdate(e) {
    e.preventDefault();
    const payload = {
        newUsername: document.getElementById('adm-new-username').value,
        currentPassword: document.getElementById('adm-curr-password').value,
        newPassword: document.getElementById('adm-new-password').value
    };
    const msg = document.getElementById('admin-security-msg');
    try {
        const res = await fetch(`${API}/api/admin/profile`, {
            method: 'PUT', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        if (data.success) {
            showMsg(msg, '✅ Security settings updated! Please re-login if you changed your username.', 'success');
            showToast('Security settings updated!', 'success');
            document.getElementById('admin-security-form').reset();
            if (data.newUsername && data.newUsername !== currentUser.username) {
                setTimeout(logout, 2000);
            }
        } else {
            showMsg(msg, data.message, 'error');
        }
    } catch (e) {
        showMsg(msg, 'Server error.', 'error');
    }
}

async function viewMemberDetails(id) {
    try {
        const res = await fetch(`${API}/api/admin/users/${id}/details`);
        const data = await res.json();
        if (data.success) {
            document.getElementById('md-username').textContent = data.username;
            document.getElementById('md-email').textContent = data.email;
            document.getElementById('md-password').textContent = data.password || 'N/A';
            document.getElementById('md-joined').textContent = data.createdAt ? data.createdAt.split('T')[0] : 'N/A';

            if (data.profile) {
                document.getElementById('md-age').textContent = data.profile.age || '—';
                document.getElementById('md-gender').textContent = data.profile.gender || '—';
                document.getElementById('md-height').textContent = `${data.profile.heightCm || '—'} cm`;
                document.getElementById('md-weight').textContent = `${data.profile.weightKg || '—'} kg`;
                document.getElementById('md-goal').textContent = data.profile.fitnessGoal || '—';
                document.getElementById('md-diet').textContent = data.profile.dietType || '—';

                if (data.profile.heightCm && data.profile.weightKg) {
                    const h = data.profile.heightCm / 100;
                    const bmi = (data.profile.weightKg / (h * h)).toFixed(1);
                    document.getElementById('md-bmi').textContent = `${bmi} (${bmiCategory(bmi)})`;
                } else {
                    document.getElementById('md-bmi').textContent = '—';
                }
            } else {
                ['md-age', 'md-gender', 'md-height', 'md-weight', 'md-goal', 'md-diet', 'md-bmi'].forEach(id => {
                    document.getElementById(id).textContent = '—';
                });
            }

            if (data.membership) {
                document.getElementById('md-plan').textContent = data.membership.plan;
                document.getElementById('md-status').textContent = data.membership.status;
            } else {
                document.getElementById('md-plan').textContent = 'NO PLAN';
                document.getElementById('md-status').textContent = 'INACTIVE';
            }

            document.getElementById('member-details-modal').classList.remove('hidden');
        } else {
            showToast(data.message, 'error');
        }
    } catch (e) {
        showToast('Error fetching user details', 'error');
    }
}

function closeMemberModal() {
    document.getElementById('member-details-modal').classList.add('hidden');
}

async function deleteMember(id, username) {
    if (!confirm(`Remove member "${username}"? This cannot be undone.`)) return;
    try {
        const res = await fetch(`${API}/api/admin/members/${id}`, { method: 'DELETE' });
        const data = await res.json();
        if (data.success) {
            showToast(`Member ${username} removed.`, 'success');
            loadAdmin();
        }
    } catch (e) { }
}

// ─────────── UTILITIES ───────────
function bmiCategory(bmi) {
    if (bmi < 18.5) return 'Underweight';
    if (bmi < 25) return 'Normal';
    if (bmi < 30) return 'Overweight';
    return 'Obese';
}

function showMsg(el, text, type) {
    el.textContent = text;
    el.className = `auth-msg ${type}`;
}

let toastTimer;
function showToast(text, type = 'success') {
    clearTimeout(toastTimer);
    const t = document.getElementById('toast');
    t.textContent = text;
    t.className = `toast ${type}`;
    toastTimer = setTimeout(() => t.className = 'toast hidden', 3500);
}
