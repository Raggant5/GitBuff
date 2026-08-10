1. Principles of Universal Design

For each Principle of Universal Design, we describe how GitBuff adheres to the principle, plan to implement features, and/or explain why it does not apply.

Principle 1: Equitable Use

Program adherence: Program adherence: GitBuff provides identical interface capabilities to all users regardless of their baseline fitness level, body metrics, or demographic profile (Guideline 1a). By using dynamic profile calculations to adapt caloric and exercise targets, the core feature set—viewing schedules, recommendations, and analytics—remains non-segregated, non-stigmatizing, and equally available to all registered users (Guidelines 1b, 1c).

Future Features: We will implement high-contrast color palette alternatives (adhering to legal guidelines mentioned in class materials) for all charts and text to ensure equitable data visualization for visually impaired users (colorblindness). We also plan to add options and features for users that have certain physical disabilities, such as individuals in a wheelchair. For example, we will have a section in the profile with checkboxes related to certain disabilities, and then if the user selects relevant disabilities (eg. a wheelchair related disability), the program will only suggest exercises that work for users in wheelchairs.

Principle 2: Flexibility in Use
    
Program adherence: The core personalization feature allows GitBuff to tailor recommendation methods and complexity based on individual user profiles (Guideline 2a). By using personal goals and ability details to generate structured 1-week workout routines and customized meal suggestions, the program facilitates user accuracy and adaptability to their unique pace. GitBuff also provides distinct specialized views (such as separate Nutrition, Workouts, and Dashboard panes) rather than a monolithic display. This allows diverse users to focus purely on the features relevant to their specific health and ability needs, avoiding segregating users with specific conditions (e.g., users who don’t want to do workouts and only want to use the app for nutritional purposes don't need to engage with features outside of the dashboard and the nutrition view).

Principle 3: Simple and Intuitive Use

Program adherence: GitBuff utilizes a standard, consistent desktop application shell navigation structure, meeting the explicit user expectation of Major section icons at the top-level (Guideline 3b). The separation of the app into modular views accessed by the easily spotted navbar on the dashboard eliminates unnecessary complexity and arranges information clearly and intuitively within focused areas (e.g., dashboards only show aggregates, the nutrition tab only shows information about food and meals, the workouts tab only shows information about scheduled workouts and exercises, etc).

Principle 4: Perceptible Information
    
Program adherence: Exercise guides redundancy is currently maximized by presenting essential textual instructions alongside potential video link integrations. This redundancy helps communicate necessary information effectively using different modes (pictorial, verbal, digital text). This would be especially helpful for users who can only understand one or two modes (for example, users who can’t read or understand English can still watch and understand the exercise videos and perform the exercises).
    
Future Features: To improve on this idea even further, GitBuff needs to have the ability to be viewed in multiple languages in all of its windows. For the example mentioned above, while it is true that a user who can see, but can’t read or understand English can understand the exercises to be performed, they won’t be able to understand anything else in the program and might not be able to navigate to the workout suggestions screen and find the exercises. To go even further, we could implement multiple languages with a text-to-speech feature for each language, for users who are visually impaired.

Principle 5: Tolerance for Error
    
Future Features: As a safety-critical application dealing with exercise, we must implement "Undo" features for data deletions. Users will probably *very frequently* delete things that they don’t mean to accidentally. Or they will delete something that they did truly mean to delete but then realize that they shouldn’t have deleted it or actually didn’t want to delete it. At the moment if a user of GitBuff did this, they would not be able to get their data back. It would be permanently deleted. This should change.

Principle 6: Low Physical Effort
    
Program adherence: By implementing a centralized profile input (weight, height, fitness goal, duration, activity level, dietary restriction, equipment, etc) all at once, GitBuff eliminates repetitive input reducing user entry effort because users won’t have to look for multiple places to input this data, and they won’t ever have to input it again. Additionally, the dashboard summarizes important data in one place, preventing users from needing repetitive physical navigation to view health information if all they want is a summary of nutritional information and their meal/workout schedule.

Principle 7: Size and Space for Approach and Use
    
Future Features: Technically if we take the way this principle is stated literally go by the, this is software with virtual controls rather than a physical artifact, guidelines regarding grasp, manipulation, reach boundaries, and physical assistant space (e.g., Guidance 7b, 7c, 7d examples like wide gates) do not apply to the virtual design. However, if we wanted to stretch the concept a bit and address the virtual accessibility equivalent to these physical constraints, we will implement scalable views where users can adjust sizes of textboxes and diagrams to further accommodate users that have vision impairments (they can make text and diagrams bigger to see it easier), and also to allow all users to adjust the UI/visuals to their liking and preferences, making the app pleasant and easier to use for everyone.

2. Program Marketing

GitBuff is marketed towards busy individuals, most likely young adults and middle-aged adults, who require personalized, data-driven recommendations analysis of/for their physical health. This is because the customized recommendation logic specifically targets those with limited time who are balancing academic or professional pressure and don’t have time to give themselves fitness recommendations or do research on workouts and nutrition themselves. Because the recommendations automatically adapt to varying fitness and nutrition targets from profile details, the program is meant for those seeking detailed tracking and analysis metrics on their fitness, which would normally be younger individuals. While elderly individuals also care about health, they would go to other sources for advice, not a fitness app, because they have a lot more time on their hands. Furthermore, the aspects of their health that they care about would also often be different than what our app is tracking. 

3. Demographics and Ethical Use

A program of this type is significantly less likely to be useful to and adopted by elderly demographics. This is common; as we learned in the readings, software designers frequently fail to keep elderly users in mind, assuming a user like themselves (middle majority), which leads to designs that create hidden hierarchies and relational harm. Elderly users are often more vulnerable, possessing diverse abilities and sensory limitations (e.g., limited dexterity or visual acuity) that would turn standard and functional features of our app into barriers without assistive devices. Until our app implements all the accessibility features described above in section 1 and makes large feature additions to appeal more to elderly peoples’ health needs rather than just young peoples’ health needs, it would not appeal to the average elderly user very much. Designing without keeping elderly demographics in mind would implicitly communicate a lower status for these people, violating equality principles by increasing the social hierarchy between demographics with differing ability levels. To address this ethics risk, future GitBuff development will consult directly with elderly individuals to identify specific interaction limitations or necessary design and feature additions to ensure the inclusion of this demographic.
