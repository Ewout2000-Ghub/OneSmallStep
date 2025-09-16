package com.example.onesmallstep.utils

import com.example.onesmallstep.data.entities.*

object PhobiaDataSeeder {

    fun getSamplePhobias(): List<Phobia> {
        return listOf(
            // Common phobias
            Phobia(1, "Spiders", "Arachnophobia", "Fear of spiders and other arachnids. One of the most common specific phobias, affecting up to 6% of the population. Often develops in childhood and can cause intense anxiety even from images.", "spider", "common", false),
            Phobia(2, "Heights", "Acrophobia", "Fear of heights or elevated places. Can range from mild uneasiness to severe panic attacks. Often includes fear of falling, though the fear may persist even when safely secured.", "height", "common", false),
            Phobia(3, "Flying", "Aviophobia", "Fear of flying or air travel. Affects approximately 6.5% of people and can severely impact travel plans and career opportunities. May involve fear of crashing, turbulence, or loss of control.", "airplane", "common", false),
            Phobia(4, "Public Speaking", "Glossophobia", "Fear of speaking in public. The most common social phobia, affecting up to 75% of people to some degree. Can cause physical symptoms like trembling, sweating, and rapid heartbeat.", "microphone", "common", false),
            Phobia(5, "Snakes", "Ophidiophobia", "Fear of snakes. An evolutionary fear that may have protective origins. Even harmless snakes or snake images can trigger intense fear responses in affected individuals.", "snake", "common", false),
            Phobia(6, "Dogs", "Cynophobia", "Fear of dogs. Often develops after a negative experience with a dog in childhood. Can range from fear of specific breeds to fear of all dogs, regardless of size or temperament.", "dog", "common", false),
            Phobia(7, "Needles", "Trypanophobia", "Fear of medical needles and injections. Can lead to avoidance of necessary medical care. Often accompanied by fainting responses and can make routine healthcare challenging.", "needle", "common", false),
            Phobia(8, "Blood", "Hemophobia", "Fear of blood. Unique among phobias as it often causes fainting due to a drop in blood pressure. Can interfere with medical procedures and emergency first aid situations.", "blood", "common", false),
            Phobia(9, "Confined Spaces", "Claustrophobia", "Fear of confined or enclosed spaces like elevators, tunnels, or small rooms. Can trigger panic attacks and avoidance behaviors that significantly impact daily life.", "box", "common", false),
            Phobia(10, "Social Situations", "Social Anxiety", "Fear of social interactions and being judged by others. Goes beyond normal shyness and can severely impact relationships, work, and educational opportunities.", "people", "common", false),

            // Additional common phobias
            Phobia(11, "Vomiting", "Emetophobia", "Fear of vomiting or seeing others vomit. Can lead to restrictive eating, avoidance of certain foods, and extreme anxiety around illness. Often develops in childhood or adolescence.", "stomach", "common", false),
            Phobia(12, "Driving", "Vehophobia", "Fear of driving or being a passenger in vehicles. Can severely limit independence and employment opportunities. May develop after car accidents or through learned behavior.", "car", "common", false),
            Phobia(13, "Germs", "Mysophobia", "Fear of germs, contamination, or dirt. Can lead to excessive hand washing, avoidance of public places, and significant distress in everyday situations involving potential contamination.", "bacteria", "common", false),
            Phobia(14, "Darkness", "Nyctophobia", "Fear of darkness or night. Common in children but can persist into adulthood. May involve fear of what might be hidden in the dark rather than darkness itself.", "moon", "common", false),
            Phobia(15, "Thunder/Lightning", "Astraphobia", "Fear of thunder, lightning, and storms. Can cause significant distress during weather events and may lead to constant weather monitoring and avoidance behaviors.", "storm", "common", false),

            // Rare/specific phobias
            Phobia(16, "Clowns", "Coulrophobia", "Fear of clowns. The unpredictable nature and exaggerated features of clowns can trigger intense fear. Often develops in childhood and can persist into adulthood.", "clown", "rare", false),
            Phobia(17, "Dolls", "Pediophobia", "Fear of dolls, puppets, or mannequins. The uncanny valley effect of human-like but not quite human figures can trigger deep unease and fear responses.", "doll", "rare", false),
            Phobia(18, "Holes", "Trypophobia", "Fear of clusters of small holes or bumps. Can cause discomfort when seeing honeycomb, lotus pods, or coral. Though not officially recognized, it affects many people.", "holes", "rare", false),
            Phobia(19, "Mirrors", "Eisoptrophobia", "Fear of mirrors or seeing one's reflection. May stem from supernatural beliefs, body image issues, or fear of seeing something unexpected in the reflection.", "mirror", "rare", false),
            Phobia(20, "Balloons", "Globophobia", "Fear of balloons. Often involves fear of the popping sound, the texture, or unpredictable movement. Can cause significant distress at parties and celebrations.", "balloon", "rare", false),
            Phobia(21, "Dentists", "Dentophobia", "Fear of dental procedures and dentists. Can lead to avoidance of necessary dental care, resulting in serious oral health problems. Often involves fear of pain or loss of control.", "dentist", "rare", false),
            Phobia(22, "Choking", "Pseudodysphagia", "Fear of choking or swallowing. Can lead to restrictive eating patterns and avoidance of certain foods. May develop after a choking incident or witnessing someone choke.", "throat", "rare", false),
            Phobia(23, "Being Touched", "Haphephobia", "Fear of being touched by others. Can severely impact relationships and social interactions. May develop from trauma or sensory processing differences.", "hand", "rare", false),
            Phobia(24, "Open Spaces", "Agoraphobia", "Fear of open or public spaces where escape might be difficult. Often develops alongside panic disorder and can lead to becoming housebound in severe cases.", "field", "rare", false),
            Phobia(25, "Death", "Thanatophobia", "Fear of death or dying. Universal concern that becomes problematic when it significantly impacts daily functioning and quality of life.", "skull", "rare", false)
        )
    }

    fun getSampleLevels(): List<ExposureLevel> {
        val levels = mutableListOf<ExposureLevel>()

        // Generate levels for all phobias (25 phobias × 4 levels each)
        for (phobiaId in 1..25) {
            val baseId = (phobiaId - 1) * 4
            levels.addAll(listOf(
                ExposureLevel(baseId + 1, phobiaId, 1, "Gentle Introduction", "Start with mild, controlled exposure in a safe environment", "1-2 weeks"),
                ExposureLevel(baseId + 2, phobiaId, 2, "Building Tolerance", "Gradually increase exposure intensity and duration", "2-3 weeks"),
                ExposureLevel(baseId + 3, phobiaId, 3, "Controlled Challenge", "Face more realistic scenarios with support systems", "3-4 weeks"),
                ExposureLevel(baseId + 4, phobiaId, 4, "Real-World Practice", "Apply skills in everyday situations with confidence", "Ongoing")
            ))
        }

        return levels
    }

    fun getSampleSteps(): List<ExposureStep> {
        val steps = mutableListOf<ExposureStep>()
        var stepId = 1

        // Detailed steps for Spider phobia (Arachnophobia)
        steps.addAll(listOf(
            // Level 1 - Gentle Introduction
            ExposureStep(stepId++, 1, 1, "Cartoon Spider", "Look at a friendly cartoon spider drawing", "Study the image for 10 seconds without looking away", "10 seconds", "3 times daily for 3 days"),
            ExposureStep(stepId++, 1, 2, "Simple Drawing", "View a simple line drawing of a spider", "Observe the drawing while taking slow, deep breaths", "30 seconds", "Daily for 4 days"),
            ExposureStep(stepId++, 1, 3, "Multiple Drawings", "Look at various spider illustrations", "Browse through 5 different friendly spider drawings", "2 minutes", "Daily for 1 week"),

            // Level 2 - Building Tolerance
            ExposureStep(stepId++, 2, 1, "Real Spider Photo", "View a clear photograph of a house spider", "Look at the photo without closing your eyes", "30 seconds", "Daily for 5 days"),
            ExposureStep(stepId++, 2, 2, "Spider Video", "Watch a short video of a spider moving", "Watch the complete video without pausing", "1 minute", "Daily for 1 week"),
            ExposureStep(stepId++, 2, 3, "Multiple Species", "View photos of different spider types", "Study 3-5 different spider species", "5 minutes", "Daily for 1 week"),

            // Level 3 - Controlled Challenge
            ExposureStep(stepId++, 3, 1, "Toy Spider", "Hold a realistic toy spider in your hands", "Pick up and examine a plastic spider", "2 minutes", "Daily for 1 week"),
            ExposureStep(stepId++, 3, 2, "Spider Behind Glass", "Observe a real spider in a terrarium", "Watch a live spider from safe distance", "5 minutes", "Every other day for 2 weeks"),
            ExposureStep(stepId++, 3, 3, "Outdoor Observation", "Look for spiders in their natural habitat", "Observe spider webs and spiders outdoors", "10 minutes", "Weekly for 1 month"),

            // Level 4 - Real-World Practice
            ExposureStep(stepId++, 4, 1, "Pet Store Visit", "Visit pet store spider section", "Walk through arachnid section calmly", "15 minutes", "Weekly for 2 weeks"),
            ExposureStep(stepId++, 4, 2, "Handle Spider Safely", "Allow supervised interaction with harmless spider", "Let spider crawl on your hand with expert present", "5 minutes", "Once with supervision"),
            ExposureStep(stepId++, 4, 3, "Independent Comfort", "Demonstrate comfort in spider presence", "Remain calm when encountering spiders naturally", "As needed", "Ongoing maintenance")
        ))

        // Vomiting phobia (Emetophobia) - Steps 13-24
        steps.addAll(listOf(
            // Level 1 - Gentle Introduction
            ExposureStep(stepId++, 41, 1, "Written Word", "Read the word 'vomit' and related terms", "Read nausea-related vocabulary without distress", "1 minute", "Daily for 3 days"),
            ExposureStep(stepId++, 41, 2, "Cartoon Images", "Look at cartoon depictions of feeling sick", "View non-graphic illustrations of nausea", "2 minutes", "Daily for 1 week"),
            ExposureStep(stepId++, 41, 3, "Discuss Topic", "Talk about the topic with a trusted person", "Have brief conversations about stomach bugs", "5 minutes", "Every other day for 1 week"),

            // Level 2 - Building Tolerance
            ExposureStep(stepId++, 42, 1, "Movie Scenes", "Watch brief movie scenes involving stomach illness", "View carefully selected film clips", "30 seconds", "Daily for 1 week"),
            ExposureStep(stepId++, 42, 2, "Sound Recordings", "Listen to audio of someone feeling unwell", "Play recordings of coughing or retching sounds", "15 seconds", "Daily for 1 week"),
            ExposureStep(stepId++, 42, 3, "Medical Information", "Read about stomach viruses and food poisoning", "Study factual information about gastroenteritis", "10 minutes", "Twice weekly for 2 weeks"),

            // Level 3 - Controlled Challenge
            ExposureStep(stepId++, 43, 1, "Fake Vomit", "Look at realistic fake vomit (Halloween prop)", "Examine artificial vomit from a safe distance", "1 minute", "Daily for 1 week"),
            ExposureStep(stepId++, 43, 2, "Hospital Visit", "Visit a hospital or clinic waiting area", "Spend time where sick people might be present", "15 minutes", "Weekly for 3 weeks"),
            ExposureStep(stepId++, 43, 3, "Care for Sick Person", "Help care for someone with a mild stomach bug", "Provide comfort to ill family member from distance", "As needed", "When opportunity arises"),

            // Level 4 - Real-World Practice
            ExposureStep(stepId++, 44, 1, "Public Transport", "Use public transportation during flu season", "Ride buses or trains when illness is common", "Normal commute", "Weekly for 1 month"),
            ExposureStep(stepId++, 44, 2, "Social Events", "Attend gatherings where food poisoning is possible", "Go to potlucks or buffets with confidence", "2+ hours", "Monthly"),
            ExposureStep(stepId++, 44, 3, "Normal Eating", "Eat a varied diet without excessive precautions", "Enjoy foods without fear of stomach upset", "Regular meals", "Daily maintenance")
        ))

        // Generate basic steps for remaining phobias (simplified pattern)
        for (phobiaId in 2..40) {
            if (phobiaId == 11) continue // Skip emetophobia as it's already detailed above

            val levelBaseId = (phobiaId - 1) * 4
            for (levelNum in 1..4) {
                val levelId = levelBaseId + levelNum
                for (stepNum in 1..3) {
                    val intensity = when (levelNum) {
                        1 -> listOf("images", "drawings", "videos")
                        2 -> listOf("sounds", "detailed images", "longer videos")
                        3 -> listOf("simulation", "controlled exposure", "practice scenarios")
                        4 -> listOf("real situations", "independent practice", "maintenance")
                        else -> listOf("general exposure")
                    }

                    steps.add(ExposureStep(
                        id = stepId++,
                        levelId = levelId,
                        stepNumber = stepNum,
                        title = "Step $stepNum - ${intensity[stepNum-1]}",
                        description = "Gradual exposure step for level $levelNum",
                        instructions = "Follow the guidance and rate your anxiety level",
                        duration = when (levelNum) {
                            1 -> listOf("30 seconds", "1 minute", "2 minutes")[stepNum-1]
                            2 -> listOf("2 minutes", "5 minutes", "10 minutes")[stepNum-1]
                            3 -> listOf("10 minutes", "20 minutes", "30 minutes")[stepNum-1]
                            4 -> listOf("As needed", "Regular practice", "Ongoing")[stepNum-1]
                            else -> "5 minutes"
                        },
                        frequency = when (levelNum) {
                            1 -> "Daily for 3-5 days"
                            2 -> "Daily for 1 week"
                            3 -> "Every other day for 2 weeks"
                            4 -> "Weekly then as needed"
                            else -> "Daily"
                        }
                    ))
                }
            }
        }

        return steps
    }
}