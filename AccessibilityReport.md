# RainCheck Accessibility Report

## Introduction

RainCheck is a scheduling and weather-tracking application designed to help users plan tasks efficiently around changing weather conditions. This report evaluates RainCheck’s design against the **Seven Principles of Universal Design**, identifies the **target audience**, highlights **potentially excluded users** using **E3I terminology** (tangible harm, relational harm, medical/social model), and outlines **above-and-beyond commitments** for accessibility compliance and inclusion.

---

## 1. Application of the Seven Principles of Universal Design

### **1. Equitable Use**

**Definition:** The design is useful and marketable to people with diverse abilities.

**Current alignment:**

- Cloud-based data (Supabase) ensures consistent access across sessions for all users.
- Weather and task information are accessible in one view for convenience.

**Opportunities for improvement:**

- Add non-color indicators (e.g. text labels, shape variations) for priority and status.
- Provide high-contrast themes for low-vision users.

**Model alignment:** Social model — removes visual-only barriers.

---

### **2. Flexibility in Use**

**Definition:** Accommodates a wide range of individual preferences and abilities.

**Current alignment:**

- Customizable tags with text and emoji for personal organization.
- Adjustable reminders for varied planning needs.

**Opportunities for improvement:**

- Support multiple input modes (e.g. keyboard shortcuts, speech-to-text, drag-and-drop).
- Support additional output modes (e.g. audible readouts and notifications for low/no-vision users).
- Allow customization of UI layout (e.g. reordering, hiding panels).

**Model alignment:** Social model — empowers diverse interaction styles.

---

### **3. Simple and Intuitive Use**

**Definition:** Easy to understand regardless of experience, knowledge, or concentration level.

**Current alignment:**

- Persistent display of both weather and schedule for context.
- Clear binary status indicators (Complete/Incomplete).
- Universally recognized icons and/or text labels for actions (e.g. "Add Task", pencil icon for edit, etc.)

**Opportunities for improvement:**

- Add an onboarding tutorial with both visual and text instructions.
- Provide tooltips for all icons and controls.
- Offer a simplified view for reduced cognitive load.
- **Model alignment:** Social model — lowers unnecessary complexity.

---

### **4. Perceptible Information**

**Definition:** Communicates information effectively regardless of sensory abilities.

**Current alignment:**

- Uses visual weather icons and task labels.
- Uses visual action icons for edit, delete, and complete task actions.

**Opportunities for improvement:**

- Integrate screen reader compatibility for all interface elements.
- Add optional sound or haptic alerts for reminders.
- Include plain-language weather descriptions alongside icons.
- **Model alignment:** Mixed — medical model (assistive tech support) and social model (redundant cues).

---

### **5. Tolerance for Error**

**Definition:** Minimizes hazards and unintended actions.

**Current alignment:**

- Tasks can be edited or deleted.

**Opportunities for improvement:**

- Add an “undo” option for deletions.
- Require confirmation before deleting high-priority tasks.
- Auto-save task edits in draft mode.
- **Model alignment:** Social model — prevents irreversible mistakes for all.

---

### **6. Low Physical Effort**

**Definition:** Can be used efficiently and comfortably with minimal fatigue.

**Current alignment:**

- Task creation and editing occur in a single interface.

**Opportunities for improvement:**

- Keyboard shortcuts for major functions.
- Recurring task options to reduce repetitive entry.
- Adjustable UI scaling to reduce eye strain.

**Model alignment:** Social model — reduces repetitive or strenuous input.

---

### **7. Size and Space for Approach and Use**

**Definition:** Appropriate space for approach, reach, manipulation, and use regardless of mobility.

**Current alignment:**

- Fits on standard laptop displays.

**Opportunities for improvement:**

- Ensure interactive elements meet [WCAG 2.1 AA target size requirements](https://www.w3.org/WAI/WCAG21/quickref/#target-size).
- Provide zoom and font-size adjustments.
- Explore touch-friendly layouts for future mobile/tablet versions.

**Model alignment:** Social model — ensures usability across devices.

---

## 2. Target Audience

RainCheck is designed primarily for students, professionals, and other individuals who need to align their schedules with changing weather conditions. These users often balance multiple commitments and benefit from having both task management and weather forecasting integrated into a single platform. The application’s clear task labeling, customizable reminders, and combined hourly weather and schedule view support fast, informed decision-making. By enabling users to anticipate weather changes and plan accordingly, RainCheck helps them avoid poorly timed outdoor activities, minimize disruptions, and optimize their daily productivity.

---

## 3. Users Who May Struggle (E3I Analysis)

While RainCheck serves a broad audience, certain user groups may encounter barriers to effective use. Users with visual impairments, such as color blindness, low vision, or blindness, may have difficulty distinguishing task priorities or understanding status indicators when they rely solely on color cues. This can cause tangible harm by limiting access to critical information and relational harm by conveying, intentionally or not, that their needs were not considered in the design. Users with motor impairments may find it challenging to interact with small click targets or drag-and-drop features, leading to frustration and decreased usability. Neurodivergent users, such as those with ADHD, may struggle with a dense interface that increases cognitive load, reducing efficiency and overall satisfaction. Many of these barriers arise from design choices, aligning with the social model of disability, while certain accommodations, such as screen reader compatibility, align with the medical model by directly addressing specific impairments.

---

## 4. Above-and-Beyond Accessibility Commitments

RainCheck will:

- Conduct **beta testing with diverse user groups** (including those with disabilities).
- Ensure compliance with [WCAG 2.1 AA](https://www.w3.org/WAI/WCAG21/quickref/) per [AODA](https://www.ontario.ca/page/how-make-websites-accessible) standards.
- Integrate automated accessibility checks (e.g. Axe, Lighthouse) into development.
- Maintain **open feedback channels** for ongoing accessibility improvements.
- Provide an **accessibility statement** in documentation and GitHub README.

---

## Appendix: Accessibility Summary Table


| Principle               | Current Features                        | Planned Enhancements                           |
| ----------------------- | --------------------------------------- | ---------------------------------------------- |
| Equitable Use           | Cloud storage, integrated weather/tasks | Non-color indicators, high-contrast mode       |
| Flexibility in Use      | Custom tags, adjustable reminders       | Multiple input methods, customizable UI        |
| Simple & Intuitive      | Persistent weather/calendar             | Onboarding tutorial, tooltips, simplified mode |
| Perceptible Information | Visual icons/labels                     | Screen reader support, haptic/sound alerts     |
| Tolerance for Error     | Editable/deletable tasks                | Undo, confirmation prompts, auto-save          |
| Low Physical Effort     | Single-interface editing                | Keyboard shortcuts, recurring tasks, scaling   |
| Size & Space            | Laptop-friendly layout                  | Larger click targets, zoom, touch support      |
